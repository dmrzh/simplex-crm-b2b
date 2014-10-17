package ru.dev_server.client.compoment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.GlobalCommandEvent;
import org.zkoss.bind.sys.BinderCtrl;
import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import ru.dev_server.client.AuthService;
import ru.dev_server.client.FioConverter;
import ru.dev_server.client.dao.ClientDAO;
import ru.dev_server.client.model.Client;
import ru.dev_server.client.viewmodel.ClientListModel;
import ru.dev_server.client.viewmodel.ClientUtils;

import java.util.HashMap;

/**
 * Display and choose client.
 */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ClientEditor extends HtmlMacroComponent {
    private static final Logger LOG= LoggerFactory.getLogger(ClientEditor.class);
    @Wire("listbox#lb")
    private Listbox listbox;

    @Wire("bandbox#bd")
    private Bandbox bandbox;

    @WireVariable
    private ClientDAO clientDAO;
    @WireVariable("authService")
    private AuthService authService;

    Client client;
    ClientListModel clientListModel;

    private String filter="";

    private FioConverter fioConverter= new FioConverter();


    public ClientEditor() {
        setMacroURI("/macro/client-editor.zul");
    }

    @Override
    public void afterCompose() {
        super.afterCompose();
        clientListModel= new ClientListModel(clientDAO,authService);

        clientListModel.setMultiple(false);

        EventQueues.lookup(BinderCtrl.DEFAULT_QUEUE_NAME, EventQueues.DESKTOP, true).subscribe(
                new EventListener<Event>() {
                    public void onEvent(Event evt) {
                        if(evt instanceof GlobalCommandEvent){
                            GlobalCommandEvent globalCommandEvent = (GlobalCommandEvent) evt;
                            if("refreshClients".equals(globalCommandEvent.getCommand())){
                                searchByFilter(filter);
                            }
                        }
                    }
                });
    }

    @Listen("onChanging=textbox#searchTb")
    public void search(InputEvent event){
        String value = event.getValue();
        searchByFilter(value);
    }

    private void searchByFilter(String value) {
        try{

            String s = new ClientUtils().checkFilterAndReturnQuery(value);
            clientListModel.setFilter(s);
        }catch (Exception e) {
            LOG.debug("@Command(\"filterClients\")  rejected"+value);
            Messagebox.show("только буквы и цифры");
        }
    }

    @Listen("onClick=#openClient")
    public void openClient(){
        if(client==null) {
            return;
        }
        HashMap<String, Object> args = new HashMap<String, Object>();
        args.put("client",client);
        BindUtils.postGlobalCommand(null, null, "editClient", args);
    }


    @Listen("onSelect=listbox")
    public void onSelect(){
        Client client = (Client)listbox.getSelectedItem().getValue();
        setClient(client);
        bandbox.setValue(client.getFio());
        bandbox.close();
        Events.postEvent("onEdited", this, client);
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
        if(client!=null){
            bandbox.setValue(client.getFio());
        }
    }


    public FioConverter getFioConverter() {
        return fioConverter;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public ClientListModel getClientListModel() {
        return clientListModel;
    }
}
