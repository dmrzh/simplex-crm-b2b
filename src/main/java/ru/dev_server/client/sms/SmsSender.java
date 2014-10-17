package ru.dev_server.client.sms;

import org.apache.commons.lang.StringEscapeUtils;
import org.jsmpp.InvalidResponseException;
import org.jsmpp.PDUException;
import org.jsmpp.bean.BindType;
import org.jsmpp.bean.GeneralDataCoding;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.bean.SMSCDeliveryReceipt;
import org.jsmpp.bean.TypeOfNumber;
import org.jsmpp.extra.NegativeResponseException;
import org.jsmpp.extra.ResponseTimeoutException;
import org.jsmpp.session.BindParameter;
import org.jsmpp.session.MessageReceiverListener;
import org.jsmpp.session.SMPPSession;
import org.jsmpp.util.AbsoluteTimeFormatter;
import org.jsmpp.util.TimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MarkerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.dev_server.client.dao.SmppSettingsDAO;
import ru.dev_server.client.dao.SmppStatusDAO;
import ru.dev_server.client.dao.SmsNotificationDAO;
import ru.dev_server.client.model.Company;
import ru.dev_server.client.model.Contact;
import ru.dev_server.client.model.ContactType;
import ru.dev_server.client.model.NotificationStatus;
import ru.dev_server.client.model.SmppSettings;
import ru.dev_server.client.model.SmppStatus;
import ru.dev_server.client.model.SmsNotification;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**. */
public class SmsSender {
    private static final Logger LOG= LoggerFactory.getLogger(SmsSender.class);

    @Resource
    private SmsNotificationDAO smsNotificationDAO;
    @Resource
    private SmppSettingsDAO smppSettingsDAO;

    @Resource
    private SmppStatusDAO smppStatusDAO;


    @Resource
    private MessageReceiverListener smppMessageReceiverListener;

    @Resource SmsSender smsSender;

    public static final int SMS_SELECT_COUNT_BATCH = 10;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void remindToClients(){
        List<SmsNotification> notifications = smsNotificationDAO.findForNotification(new Date());
        LOG.debug("notifications found {} ",notifications.size());
        for(SmsNotification n:notifications){
            smsSender.processSmsNotification(n);
        }

    }

    private String normolizePhone(String s) {
        StringBuilder sb=new StringBuilder();
        for(char c:s.toCharArray()){
            if(c>='0' && c<='9'){
                sb.append(c);
            }
        }
        return sb.toString();
    }



    public void sendSingleSms(){
        while (smsSender.sendNSingleSms());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean sendNSingleSms() {
        List<SmsNotification> notifications = smsNotificationDAO.findForSingleSmsNotification(new Date(), SMS_SELECT_COUNT_BATCH);
        LOG.debug("sendNSingleSms: notifications found {} ",notifications.size());
        for(SmsNotification n:notifications){
            smsSender.processSmsNotification(n);
        }
        return notifications.size()==SMS_SELECT_COUNT_BATCH;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void processSmsNotification(SmsNotification n) {
        n=smsNotificationDAO.get(n.getId());
        if(n.getNotificationStatus()!=NotificationStatus.WAITING){
            LOG.warn("sms already sent");
            return;
        }
        long balance = n.getClient().getCompany().getBalance();
        if(balance<0){
            n.setNotificationStatus(NotificationStatus.ERROR);
            n.setSendError("low balance: "+balance);
            smsNotificationDAO.saveOrUpdate(n);
           return;
        }
        List<Contact> contacts = n.getClient().getContacts();
        boolean mobileFound=false;
        for(Contact c:contacts){
            if(c.getContactType()== ContactType.MOBILE){
                mobileFound=true;
                try {
                    LOG.info(MarkerFactory.getMarker("BEEP"),"");
                    String to = normolizePhone(c.getValue());
                    String from = n.getClient().getCompany().getCompanyPhone();
                    String text = n.getTextTemlate();
                    List<SmppStatus> smppStatuses = notifyClient(from, to, text);

                    n.setSmppStatusList(smppStatuses );
                    n.setNotificationStatus(NotificationStatus.SENT);
                    for(SmppStatus s:smppStatuses){
                        s.setNotification(n);
                    }
                    n.setSendTo(to);
                    n.setSendFrom(from);

                    for(SmppStatus s:smppStatuses){
                        if(s.getSendError()==null){
                            Company company = n.getClient().getCompany();
                            int constantCost = company.getCurrentTariff().getConstantCost();
                            long balance1 = company.getBalance();
                            company.setBalance(balance1 - constantCost);
                        }else {
                            n.setNotificationStatus(NotificationStatus.ERROR);
                        }
                    }
                }catch (IOException e) {
                    n.setSendError(e.getClass().getName() + ":" + StringEscapeUtils.escapeJava(e.getMessage()));
                    n.setNotificationStatus(NotificationStatus.ERROR);
                    LOG.error(e.getMessage(), e);
                }catch (Exception e) {
                    n.setSendError(e.getClass().getName() + ":" + StringEscapeUtils.escapeJava(e.getMessage()) );
                    n.setNotificationStatus(NotificationStatus.ERROR);
                    LOG.error(e.getMessage(), e);
                }
                smsNotificationDAO.saveOrUpdate(n);
                break;
            }
        }
        if(!mobileFound){
            n.setSendError("mobile phone not found");
            n.setNotificationStatus(NotificationStatus.ERROR);
            smsNotificationDAO.saveOrUpdate(n);
        }
    }

    public void sendMassSms(){
        while (smsSender.sendNSms());
    }

    /**
     *
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean sendNSms() {

        List<SmsNotification> notifications = smsNotificationDAO.findForMassSmsNotification(new Date(), SMS_SELECT_COUNT_BATCH);
        LOG.debug("notifications found {} ",notifications.size());
        for(SmsNotification n:notifications){
            processSmsNotification(n);
        }
        return notifications.size()== SMS_SELECT_COUNT_BATCH;
    }

    private static TimeFormatter timeFormatter = new AbsoluteTimeFormatter();

    private SmppSettings smppSettings;
    private synchronized SmppSettings getSmppSettings(){
        if(smppSettings==null){
            smppSettings=smppSettingsDAO.findAll().get(0);
        }
        return smppSettings;
    };


    /**
     * send sms (possible long up to 8 parts.
     * @param smsFrom
     * @param to
     * @param text
     * @return  statuses of al sms parts
     * @throws IOException if can't bind session to server.
     */
    public List<SmppStatus> notifyClient(String smsFrom, String to, String text)  throws  IOException {
        SmppSettings smppSettings = getSmppSettings();

        SMPPSession session = new SMPPSession();
        session.setMessageReceiverListener(smppMessageReceiverListener);
        session.connectAndBind(smppSettings.getHost(),  smppSettings.getPort(),
                new BindParameter(
                        BindType.BIND_TRX,smppSettings.getLogin(),
                        smppSettings.getPassword(),
                        "cp", TypeOfNumber.UNKNOWN, NumberingPlanIndicator.UNKNOWN, null
                ) ,60000
        );

        MultipartSmsUtils.MessageParts messageParts = MultipartSmsUtils.divideMessage(text);

        List<SmppStatus> result= new ArrayList<SmppStatus>(messageParts.getMessagesCount());
        GeneralDataCoding generalDataCiding = new GeneralDataCoding(messageParts.alphabet, smppSettings.getMessageClass(), false);
        byte priorityFlag =  (byte)smppSettings.getPriorityFlag();


        for (int i = 0; i < messageParts.getMessagesCount(); i++) {
            String format = null;//timeFormatter.format(new Date()); todo
            String messageId = null;
            SmppStatus element = new SmppStatus();
            result.add(element);
            try {
                messageId = session.submitShortMessage(smppSettings.getServiceType(), smppSettings.getSourceAddrTon(), smppSettings.getSourceAddrNpi(),
                        smsFrom, smppSettings.getDestAddrTon(), smppSettings.getDestAddrNpi(),to , messageParts.esmClass,
                        (byte)0, priorityFlag, format, null,
                        new RegisteredDelivery(SMSCDeliveryReceipt.SUCCESS_FAILURE), (byte)0,
                        generalDataCiding,
                        (byte)0, messageParts.byteMessagesArray[i]);
                long smppId = Long.parseLong(messageId);
                element.setSmppId(smppId);
            } catch (PDUException e) {
                element.setSendError(e.getClass().getName() + ":" + StringEscapeUtils.escapeJava(e.getMessage()));
                LOG.error(e.getMessage(), e);
                break;
            } catch (ResponseTimeoutException e) {
                element.setSendError(e.getClass().getName() + ":" + StringEscapeUtils.escapeJava(e.getMessage()));
                LOG.error(e.getMessage(), e);
                break;
            } catch (InvalidResponseException e) {
                element.setSendError(e.getClass().getName() + ":" + StringEscapeUtils.escapeJava(e.getMessage()));
                LOG.error(e.getMessage(), e);
                break;
            } catch (NegativeResponseException e) {
                element.setSendError(e.getClass().getName() + ":" + StringEscapeUtils.escapeJava(e.getMessage()));
                element.setCommandStatus(e.getCommandStatus());
                LOG.error(e.getMessage(), e);
                break;
            } catch (IOException e) {
                element.setSendError(e.getClass().getName() + ":" + StringEscapeUtils.escapeJava(e.getMessage()));
                LOG.error(e.getMessage(), e);
                break;
            }
        }

        return result;
    }
}
