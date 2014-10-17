package ru.dev_server.client.viewmodel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import ru.dev_server.client.I18nConverter;
import ru.dev_server.client.dao.SmsNotificationDAO;
import ru.dev_server.client.model.Client;
import ru.dev_server.client.model.NotificationStatus;
import ru.dev_server.client.model.SmsNotification;

import java.util.Collections;
import java.util.List;

/**.*/
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class SmsHistoryVM {
    private static final Logger LOG= LoggerFactory.getLogger(SmsHistoryVM.class);
    I18nConverter stateConverter =new I18nConverter(NotificationStatus.class);
    @WireVariable
    SmsNotificationDAO smsNotificationDAO;
    private Client client;
    @AfterCompose
    public void afterCompose(@ExecutionArgParam("client") Client client){
        if(client==null){
            throw new NullPointerException("client can't be null");
        }
        this.client=client;
    }
    public List<SmsNotification> getSmsNotifications(){
        if(client.getId()==null){
            return Collections.emptyList();
        }
        return smsNotificationDAO.findByClient(client,1000);
    }

    public I18nConverter getStateConverter() {
        return stateConverter;
    }
}
