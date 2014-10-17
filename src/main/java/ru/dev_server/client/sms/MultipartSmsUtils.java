package ru.dev_server.client.sms;

import org.jsmpp.bean.Alphabet;
import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.GSMSpecificFeature;
import org.jsmpp.bean.MessageMode;
import org.jsmpp.bean.MessageType;

import java.io.UnsupportedEncodingException;
import java.util.Random;

/**.*/
public class MultipartSmsUtils {

    public static final int MAX_MULTIPART_MSG_SEGMENT_SIZE_UCS2 = 134;
    public static final int MAX_SINGLE_MSG_SEGMENT_SIZE_UCS2 = 70;
    public static final int MAX_MULTIPART_MSG_SEGMENT_SIZE_7BIT = 154;
    public static final int MAX_SINGLE_MSG_SEGMENT_SIZE_7BIT = 160;


    public static byte[][] splitUnicodeMessage(byte[] aMessage, Integer maximumMultipartMessageSegmentSize) {

        final byte UDHIE_HEADER_LENGTH = 0x05;
        final byte UDHIE_IDENTIFIER_SAR = 0x00;
        final byte UDHIE_SAR_LENGTH = 0x03;

        // determine how many messages have to be sent
        int numberOfSegments = aMessage.length / maximumMultipartMessageSegmentSize;
        int messageLength = aMessage.length;
        if (numberOfSegments > 255) {
            numberOfSegments = 255;
            messageLength = numberOfSegments * maximumMultipartMessageSegmentSize;
        }
        if ((messageLength % maximumMultipartMessageSegmentSize) > 0) {
            numberOfSegments++;
        }

        // prepare array for all of the msg segments
        byte[][] segments = new byte[numberOfSegments][];

        int lengthOfData;

        // generate new reference number
        byte[] referenceNumber = new byte[1];
        new Random().nextBytes(referenceNumber);

        // split the message adding required headers
        for (int i = 0; i < numberOfSegments; i++) {
            if (numberOfSegments - i == 1) {
                lengthOfData = messageLength - i * maximumMultipartMessageSegmentSize;
            } else {
                lengthOfData = maximumMultipartMessageSegmentSize;
            }

            // new array to store the header
            segments[i] = new byte[6 + lengthOfData];

            // UDH header
            // doesn't include itself, its header length
            segments[i][0] = UDHIE_HEADER_LENGTH;
            // SAR identifier
            segments[i][1] = UDHIE_IDENTIFIER_SAR;
            // SAR length
            segments[i][2] = UDHIE_SAR_LENGTH;
            // reference number (same for all messages)
            segments[i][3] = referenceNumber[0];
            // total number of segments
            segments[i][4] = (byte) numberOfSegments;
            // segment number
            segments[i][5] = (byte) (i + 1);

            // copy the data into the array
            System.arraycopy(aMessage, (i * maximumMultipartMessageSegmentSize), segments[i], 6, lengthOfData);

        }
        return segments;
    }



    public static MessageParts divideMessage(String text) throws UnsupportedEncodingException{
        MessageParts messageParts =new MessageParts();
        int maximumSingleMessageSize = 0;
        int maximumMultipartMessageSegmentSize = 0;
        byte[] byteSingleMessage = null;
        if (Gsm0338.isEncodeableInGsm0338(text)) {
            byteSingleMessage = text.getBytes();
            messageParts.alphabet = Alphabet.ALPHA_DEFAULT;
            maximumSingleMessageSize = MultipartSmsUtils.MAX_SINGLE_MSG_SEGMENT_SIZE_7BIT;
            maximumMultipartMessageSegmentSize =MultipartSmsUtils. MAX_MULTIPART_MSG_SEGMENT_SIZE_7BIT;
        } else {
            byteSingleMessage = text.getBytes("UTF-16BE");
            messageParts.alphabet = Alphabet.ALPHA_UCS2;
            maximumSingleMessageSize = MultipartSmsUtils.MAX_SINGLE_MSG_SEGMENT_SIZE_UCS2;
            maximumMultipartMessageSegmentSize = MultipartSmsUtils.MAX_MULTIPART_MSG_SEGMENT_SIZE_UCS2;
        }

        // check if message needs splitting and set required sending parameters

        messageParts.byteMessagesArray = null;

        if (text.length() > maximumSingleMessageSize) {
            // split message according to the maximum length of a segment
            messageParts.byteMessagesArray = MultipartSmsUtils.splitUnicodeMessage(byteSingleMessage, maximumMultipartMessageSegmentSize);
            // set UDHI so PDU will decode the header
            messageParts.esmClass = new ESMClass(MessageMode.DEFAULT, MessageType.DEFAULT, GSMSpecificFeature.UDHI);
        } else {
            messageParts.byteMessagesArray = new byte[][] { byteSingleMessage };
            messageParts.esmClass = new ESMClass();
        }
        return messageParts;
    }

    static public class MessageParts {
        public ESMClass esmClass;
        public Alphabet alphabet;
        public  byte[][] byteMessagesArray;
        public int getMessagesCount(){
            return byteMessagesArray.length;
        }
    }

}
