package ru.dev_server.client.controllers;

/**.*/

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.BindUtils;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.OpenEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Menuitem;
import ru.dev_server.client.model.Client;
import ru.dev_server.client.utils.ClientUtils;

import java.util.HashMap;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ClientContextMenuController extends SelectorComposer {
    private static final Logger LOG= LoggerFactory.getLogger(ClientContextMenuController.class);
    private Client  client;

    @Listen("onOpen=menupopup#clientMenuCtx")
    public void onOpen(OpenEvent openEvt) throws Exception {
        if(openEvt.isOpen()){
            Listitem reference = (Listitem) openEvt.getReference();
            client= reference.getValue();

            Menuitem emailItm = (Menuitem)getSelf().getFellowIfAny("email",true);
            emailItm.setDisabled(! ClientUtils.hasEmail(client));

            Menuitem smsItm = (Menuitem)getSelf().getFellowIfAny("sms",true);
            smsItm.setDisabled(!ClientUtils.hasMobile(client));

        }
    }
    @Listen("onClick = menuitem#edit")
    public void onEdit(){
        if(client==null) {
            LOG.warn("client is null");
            return;
        }
        HashMap<String, Object> args = new HashMap<String, Object>();
        args.put("client",client);
        BindUtils.postGlobalCommand(null, null, "editClient", args);
    }

    @Listen("onClick = menuitem#sms")
    public void onSms(){
        if(client==null) {
            LOG.warn("client is null");
            return;
        }
        HashMap params = new HashMap();
        params.put("client",client);
        Executions. createComponents("/WEB-INF/zul/newSingleSms.zul", null, params);
    }
    @Listen("onClick = menuitem#email")
    public void onEmail(){
        if(client==null) {
            LOG.warn("client is null");
            return;
        }
        HashMap params = new HashMap();
        params.put("client",client);
        Executions. createComponents("/WEB-INF/zul/newSingleEmail.zul", null, params);
    }
}
