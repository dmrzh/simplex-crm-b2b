package ru.dev_server.client.controllers;

/**.*/

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.BindUtils;
import org.zkoss.zk.ui.event.OpenEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zul.Listitem;
import ru.dev_server.client.model.JuridicalPerson;

import java.util.HashMap;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class JuridicalPersonContextMenuController extends SelectorComposer {
    private static final Logger LOG= LoggerFactory.getLogger(JuridicalPersonContextMenuController.class);
    private JuridicalPerson juridicalPerson;

    @Listen("onOpen=menupopup#clientMenuCtx")
    public void onOpen(OpenEvent openEvt) throws Exception {
        if(openEvt.isOpen()){
            Listitem reference = (Listitem) openEvt.getReference();
            juridicalPerson= reference.getValue();

//            Menuitem emailItm = (Menuitem)getSelf().getFellowIfAny("email",true);
//            emailItm.setDisabled(! ClientUtils.hasEmail(juridicalPerson));
//
//            Menuitem smsItm = (Menuitem)getSelf().getFellowIfAny("sms",true);
//            smsItm.setDisabled(!ClientUtils.hasMobile(juridicalPerson));

        }
    }
    @Listen("onClick = menuitem#edit")
    public void onEdit(){
        if(juridicalPerson==null) {
            LOG.warn("client is null");
            return;
        }
        HashMap<String, Object> args = new HashMap<String, Object>();
        args.put("juridicalPerson",juridicalPerson);
        BindUtils.postGlobalCommand(null, null, "editJuridicalPerson", args);
    }

//    @Listen("onClick = menuitem#sms")
//    public void onSms(){
//        if(client==null) {
//            LOG.warn("client is null");
//            return;
//        }
//        HashMap params = new HashMap();
//        params.put("client",client);
//        Executions. createComponents("/WEB-INF/zul/newSingleSms.zul", null, params);
//    }
//    @Listen("onClick = menuitem#email")
//    public void onEmail(){
//        if(client==null) {
//            LOG.warn("client is null");
//            return;
//        }
//        HashMap params = new HashMap();
//        params.put("client",client);
//        Executions. createComponents("/WEB-INF/zul/newSingleEmail.zul", null, params);
//    }
}
