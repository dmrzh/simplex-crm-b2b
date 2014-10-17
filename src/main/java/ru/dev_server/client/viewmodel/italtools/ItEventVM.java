package ru.dev_server.client.viewmodel.italtools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Window;
import ru.dev_server.client.I18nConverter;
import ru.dev_server.client.dao.itevents.EventDAO;
import ru.dev_server.client.model.ItEvent;
import ru.dev_server.client.model.JuridicalPerson;

import java.util.Date;

/**
 * .
 */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ItEventVM {
    private static final Logger LOG= LoggerFactory.getLogger( ItEventVM.class);

    @WireVariable
    private EventDAO itEventDAO;


    private Date time;
    private ItEvent currntItEvent;
    private Window itEventWin;
    private I18nConverter typeConverter=new I18nConverter(ItEvent.EventType.class);

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Window view,@ExecutionArgParam("itEvent") ItEvent itEvent){
        this.currntItEvent=itEvent;
        this.itEventWin=view;
        if(itEvent.getEventTime()!=null){
            time=(Date)itEvent.getEventTime().clone();
        }
    }

    @Command
    public void save(){
        Date eventTime = currntItEvent.getEventTime();
        eventTime.setHours(time.getHours());
        eventTime.setMinutes(time.getMinutes());
        itEventDAO.saveOrUpdate(currntItEvent);
        itEventWin.detach();
        BindUtils.postGlobalCommand(null, null, "refreshItEvents", null);
        BindUtils.postGlobalCommand(null, null, "refreshMeetings", null);
    }

    public String getTitle(){
        JuridicalPerson client = (JuridicalPerson)currntItEvent.getClient();
        return typeConverter.coerceToUi(currntItEvent.getEventType(),null, null)+". Клиент: "+ client.getName();
    }

    @Command
    public void cancel(){
        itEventWin.detach();
    }

    public String concat(String s1,String s2){
        return s1+s2;
    }


    public ItEvent getCurrntItEvent() {
        return currntItEvent;
    }

    public void setCurrntItEvent(ItEvent currntItEvent) {
        this.currntItEvent = currntItEvent;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public I18nConverter getTypeConverter() {
        return typeConverter;
    }
}
