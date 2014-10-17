package ru.dev_server.client.viewmodel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Window;
import ru.dev_server.client.AuthService;
import ru.dev_server.client.dao.SingleSmsDAO;
import ru.dev_server.client.dao.SmsNotificationDAO;
import ru.dev_server.client.model.Client;
import ru.dev_server.client.model.SingleSms;
import ru.dev_server.client.model.SmsNotification;

import java.util.Date;

/**.*/
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class SingleSmsVM {
    private static final Logger LOG= LoggerFactory.getLogger(SingleSmsVM.class);
    private String text;
    private Client client;

    @WireVariable
    private AuthService authService;

    @WireVariable
    private SmsNotificationDAO smsNotificationDAO;

    @WireVariable
    private SingleSmsDAO singleSmsDAO;

    private Window newSingleSmsWin;


    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Window view,@ExecutionArgParam("client") Client client){
        newSingleSmsWin=view;
        if(client==null){
            throw new NullPointerException("client can't be null");
        }
        this.client=client;
    }
    @Command
    public void send(){
        SingleSms singleSms = new SingleSms();
        singleSms.setText(text);
        singleSms.setStartDate(new Date());
        singleSmsDAO.saveOrUpdate(singleSms);


        SmsNotification smsNotification = new SmsNotification();
        smsNotification.setSingleSms(singleSms);
        smsNotification.setClient(client);
        singleSms.setSmsNotification(smsNotification);
        smsNotificationDAO.saveOrUpdate(smsNotification);

        newSingleSmsWin.detach();

    }
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
