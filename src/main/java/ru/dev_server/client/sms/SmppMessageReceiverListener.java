package ru.dev_server.client.sms;

import org.jsmpp.bean.AlertNotification;
import org.jsmpp.bean.DataSm;
import org.jsmpp.bean.DeliverSm;
import org.jsmpp.bean.DeliveryReceipt;
import org.jsmpp.bean.MessageType;
import org.jsmpp.extra.ProcessRequestException;
import org.jsmpp.session.DataSmResult;
import org.jsmpp.session.MessageReceiverListener;
import org.jsmpp.session.Session;
import org.jsmpp.util.DeliveryReceiptState;
import org.jsmpp.util.InvalidDeliveryReceiptException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import ru.dev_server.client.dao.SmppStatusDAO;
import ru.dev_server.client.dao.SmsNotificationDAO;
import ru.dev_server.client.model.NotificationStatus;
import ru.dev_server.client.model.SmppStatus;
import ru.dev_server.client.model.SmsNotification;

import javax.annotation.Resource;

/**.*/
public class SmppMessageReceiverListener implements MessageReceiverListener {
    private static final Logger LOG = LoggerFactory.getLogger(SmppMessageReceiverListener.class);

    @Resource
    private SmsNotificationDAO smsNotificationDAO;
    @Resource
    private SmppStatusDAO smppStatusDAO;

    public SmppMessageReceiverListener() {
    }

    @Transactional
    public void onAcceptDeliverSm(DeliverSm deliverSm) throws ProcessRequestException {
        if (MessageType.SMSC_DEL_RECEIPT.containedIn(deliverSm.getEsmClass())) {
            // this message is delivery receipt
            try {
                DeliveryReceipt delReceipt = deliverSm.getShortMessageAsDeliveryReceipt();
                long id = Long.parseLong(delReceipt.getId()) & 0xffffffff;

                SmppStatus smppStatus = smppStatusDAO.findBySmppId(id, deliverSm.getSourceAddr(), deliverSm.getDestAddress());
                if(smppStatus==null){
                    LOG.error("receive smppStatus for unknown sms");
                    return;
                }
                if(smppStatus.getSmppFinalStatus()==null){
                    smppStatus.setSmppFinalStatus(delReceipt.getFinalStatus());
                }else{
                    LOG.error("can't change SmppFinalStatus from "+smppStatus.getSmppFinalStatus().name()+" to " + delReceipt.getFinalStatus().name());
                }
                smppStatusDAO.saveOrUpdate(smppStatus);
                SmsNotification notification = smppStatus.getNotification();
                setNotificationStatus(notification);
                smsNotificationDAO.saveOrUpdate(notification);

            } catch (InvalidDeliveryReceiptException e) {
                LOG.error("Failed getting delivery receipt",e);
            }
        }



    }

    /**
     * if a
     * @param notification
     */
    private void setNotificationStatus(SmsNotification notification) {
        if (isAllSame(notification, DeliveryReceiptState.DELIVRD)) {
            notification.setNotificationStatus(NotificationStatus.RECEIVED);
            return;
        };
        if(hasState(notification,DeliveryReceiptState.UNDELIV) || hasState(notification,DeliveryReceiptState.REJECTD )){
            notification.setNotificationStatus(NotificationStatus.ERROR);
            return;
        }
        if(hasState(notification,DeliveryReceiptState.EXPIRED)){
            notification.setNotificationStatus(NotificationStatus.EXPIRED);
            return;
        }
        notification.setNotificationStatus(NotificationStatus.DIFFERENT);
    }

    private boolean isAllSame(SmsNotification notification, DeliveryReceiptState status) {
        for(SmppStatus s:notification.getSmppStatusList()){
            if(s.getSmppFinalStatus()!= status){
                return false;
            }
        }
        return true;
    }

    private boolean hasState(SmsNotification notification, DeliveryReceiptState status) {
        for(SmppStatus s:notification.getSmppStatusList()){
            if(s.getSmppFinalStatus()== status){
                return true;
            }
        }
        return false;
    }

    @Override
    public void onAcceptAlertNotification(AlertNotification alertNotification) {
        LOG.info("onAcceptAlertNotification");
    }

    @Override
    public DataSmResult onAcceptDataSm(DataSm dataSm, Session source) throws ProcessRequestException {
        LOG.info("onAcceptDataSm");
        return null;
    }
}
