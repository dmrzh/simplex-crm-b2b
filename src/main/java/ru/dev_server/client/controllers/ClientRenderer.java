package ru.dev_server.client.controllers;

import org.zkoss.bind.BindUtils;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import ru.dev_server.client.AuthService;
import ru.dev_server.client.I18nConverter;
import ru.dev_server.client.dao.DynamicColumnDAO;
import ru.dev_server.client.model.Client;
import ru.dev_server.client.model.ClientDynamicValue;
import ru.dev_server.client.model.Contact;
import ru.dev_server.client.model.ContactType;
import ru.dev_server.client.model.DynamicColumn;
import ru.dev_server.client.model.ExstendedTables;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**. */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ClientRenderer implements ListitemRenderer<Client> {
    private static final ClientEditEventEventListener DOUBLE_CLICK_LISTENER = new ClientEditEventEventListener();

    private DynamicColumnDAO dynamicColumnDAO;


    private AuthService authService;
    private I18nConverter typeConverter=new I18nConverter(ContactType.class);

    public ClientRenderer(AuthService authService, DynamicColumnDAO dynamicColumnDAO) {
        this.authService = authService;
        this.dynamicColumnDAO = dynamicColumnDAO;
    }

    @Override
    public void render(Listitem item, final Client client, int index) throws Exception {


        item.addEventListener(Events.ON_DOUBLE_CLICK, DOUBLE_CLICK_LISTENER) ;
        item.setContext("clientMenuCtx");
        item.setValue(client);


        Listcell cell = new Listcell();
        cell.setLabel(client.getFio());
        item.appendChild(cell);

        cell = new Listcell();
        if(client.getCategory()!=null){
            cell.setLabel(client.getCategory().getName());
        }
        item.appendChild(cell);


        cell = new Listcell();
        if(client.getContacts().size()>0){
            Contact contact = client.getContacts().get(0);
            ContactType contactType = contact.getContactType();
            String type = typeConverter.coerceToUi(contactType, null, null);
            cell.setLabel(type +": "+contact.getValue());
        }
        item.appendChild(cell);


        cell = new Listcell();
        cell.setLabel(client.getAddress());
        item.appendChild(cell);

        cell = new Listcell();
        cell.setLabel(client.getNote());
        item.appendChild(cell);


        List<DynamicColumn> all = dynamicColumnDAO.findToShow(authService.getCompany(), ExstendedTables.CLIENT);
        Map<DynamicColumn,ClientDynamicValue> dynamicValueList = client.getDynamicValueList();
        for(DynamicColumn dc:all){
            cell = new Listcell();
            cell.setLabel(dynamicValueList.get(dc).toString());
            item.appendChild(cell);
        }

    }

    private static class ClientEditEventEventListener implements EventListener<Event> {

        public ClientEditEventEventListener() {

        }

        @Override
        public void onEvent(Event event) throws Exception {
            Listitem target = (Listitem) event.getTarget();
            Client client= target.getValue();
            HashMap<String, Object> args = new HashMap<String, Object>();
            args.put("client", client);
            BindUtils.postGlobalCommand(null, null, "editClient", args);
        }
    }
}
