package ru.dev_server.client.controllers;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Listbox;
import ru.dev_server.client.AuthService;
import ru.dev_server.client.dao.JuridicalPersonDAO;

/**.*/
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class JuridicalPersonListListbox extends SelectorComposer {

    @WireVariable
    private JuridicalPersonDAO juridicalPersonDAO;

    @WireVariable
    private AuthService authService;

    public JuridicalPersonListListbox() {
    }

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        Listbox  listbox=(Listbox)comp;
        listbox.setItemRenderer(new JuridicalPersonRenderer(juridicalPersonDAO, authService));
        super.doAfterCompose(comp);
    }

}
