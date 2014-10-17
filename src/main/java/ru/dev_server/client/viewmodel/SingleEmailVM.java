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
import ru.dev_server.client.dao.SingleEmailDAO;
import ru.dev_server.client.model.Client;
import ru.dev_server.client.model.SingleEmail;

import java.util.Date;

/**.*/
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class SingleEmailVM {
    private static final Logger LOG= LoggerFactory.getLogger(SingleEmailVM.class);
    private String text;
    private String subject;
    private Client client;

    @WireVariable
    private AuthService authService;

    @WireVariable
    private SingleEmailDAO singleEmailDAO;


    private Window newSingleEmailWin;


    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Window view,@ExecutionArgParam("client") Client client){
        newSingleEmailWin =view;
        if(client==null){
            throw new NullPointerException("client can't be null");
        }
        this.client=client;
    }
    @Command
    public void send(){
        SingleEmail email= new SingleEmail();
        email.setSubject(subject);
        email.setText(text);
        email.setTo(client);
        email.setFrom(authService.getEmployee());
        email.setStartDate(new Date());
        singleEmailDAO.saveOrUpdate(email);

        newSingleEmailWin.detach();
    }
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
