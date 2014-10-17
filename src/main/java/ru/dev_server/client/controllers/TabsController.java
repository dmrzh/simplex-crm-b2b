package ru.dev_server.client.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.GlobalCommandEvent;
import org.zkoss.bind.sys.BinderCtrl;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**.*/
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class TabsController extends SelectorComposer{
    private static final Logger LOG= LoggerFactory.getLogger(TabsController.class);
    public static final String MESSAGE_ATTRIBUTE = "ru.simplex-crm.index.message";
    Tabbox tabbox;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        tabbox=(Tabbox)comp;
        HttpServletRequest nativeRequest = (HttpServletRequest)Executions.getCurrent().getNativeRequest();
        String message = (String)nativeRequest.getSession(true).getAttribute(MESSAGE_ATTRIBUTE);
        if(message!=null){
            Messagebox.show(message);

            nativeRequest.getSession().setAttribute(MESSAGE_ATTRIBUTE,null);
        }

        EventQueues.lookup(BinderCtrl.DEFAULT_QUEUE_NAME, EventQueues.DESKTOP, true).subscribe(
                new EventListener<Event>() {
                    public void onEvent(Event evt) {
                        back(evt);
                    }
                });


        super.doAfterCompose(comp);
    }

   // @Subscribe(BinderCtrl.DEFAULT_QUEUE_NAME)  //need zkoss EE
    public void back(Event evt){
        if(evt instanceof GlobalCommandEvent){
            GlobalCommandEvent GlobalCmdEv = (GlobalCommandEvent) evt;
            if("newTab".equals(GlobalCmdEv.getCommand())){
                Map<String,Object> args = GlobalCmdEv.getArgs();
                String zul = (String) args.get("zul");
                String title = (String) args.get("title");
                Tab tab = new Tab(title);
                tab.setClosable(true);
                Tabpanel tabpanel = new Tabpanel();
                tabbox.getTabs().appendChild(tab);
                tabbox.getTabpanels().appendChild(tabpanel);
                Executions. createComponents(zul, tabpanel, null);
                tab.setSelected(true);
            }
        }
    }
}
