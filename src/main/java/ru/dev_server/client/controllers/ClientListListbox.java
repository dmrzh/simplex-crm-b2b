package ru.dev_server.client.controllers;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listheader;
import ru.dev_server.client.AuthService;
import ru.dev_server.client.dao.DynamicColumnDAO;
import ru.dev_server.client.model.DynamicColumn;
import ru.dev_server.client.model.ExstendedTables;

import java.util.List;

/**.*/
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ClientListListbox extends SelectorComposer {

    @WireVariable
    private DynamicColumnDAO dynamicColumnDAO;

    @WireVariable
    private AuthService authService;



    @Override
    public void doAfterCompose(Component comp) throws Exception {

        Listbox  clientListbox=(Listbox)comp;

        List<DynamicColumn> all = dynamicColumnDAO.findToShow(authService.getCompany(), ExstendedTables.CLIENT);
        for(DynamicColumn dc:all){
            Listbox lb=(Listbox)comp;
            Listheader listheader = new Listheader();
            listheader.setLabel(dc.getName());
            lb.getListhead().appendChild(listheader);
        }
        clientListbox.setItemRenderer(new ClientRenderer(authService,dynamicColumnDAO));
        super.doAfterCompose(comp);
    }

}
