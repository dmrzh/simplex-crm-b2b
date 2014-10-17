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
import ru.dev_server.client.dao.JuridicalPersonDAO;
import ru.dev_server.client.model.ContactType;
import ru.dev_server.client.model.ItGroup;
import ru.dev_server.client.model.JuridicalPerson;
import ru.dev_server.client.utils.Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

/**. */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class JuridicalPersonRenderer implements ListitemRenderer<JuridicalPerson> {
    public static final JuridicalPersonEditEventEventListener DOUBLE_CLICK_LISTENER = new JuridicalPersonEditEventEventListener();
    public static final String LIGHT_BEE = "#f2eac1";
    public static final String DARK_BEE = "#ede0a7";

    private JuridicalPersonDAO juridicalPersonDAO;
    private AuthService authService;
    private I18nConverter typeConverter=new I18nConverter(ContactType.class);
    private static final DateFormat DATE_FORMAT=SimpleDateFormat.getDateInstance() ;

    public JuridicalPersonRenderer(JuridicalPersonDAO juridicalPersonDAO,AuthService authService) {
        this.juridicalPersonDAO=juridicalPersonDAO;
        this.authService = authService;
    }

    @Override
    public void render(Listitem item, final JuridicalPerson juridicalPerson, int index) throws Exception {


        item.addEventListener(Events.ON_DOUBLE_CLICK, DOUBLE_CLICK_LISTENER) ;
        item.setContext("juridicalPersonMenuCtx");
        item.setValue(juridicalPerson);


        Listcell cell = new Listcell();
        cell.setLabel(juridicalPerson.getName());
        item.appendChild(cell);
        item.setClass("juridicalPerson");
//        if(item.isSelected()){
//            item.setClass("selectedJuridicalPerson");
//        }else if(index%2==0){
//            item.setClass("nooddJuridicalPerson");
//        } else{
//            item.setClass("oddJuridicalPerson");
//        }
        cell = new Listcell();
        cell.setLabel(DATE_FORMAT.format(juridicalPerson.getCreationDate()));
        item.appendChild(cell);




        cell = new Listcell();
        cell.setLabel(juridicalPerson.getCity());
        item.appendChild(cell);




        cell = new Listcell();
        cell.setLabel(getGroupsNames(juridicalPerson.getPublicGroups()));
        item.appendChild(cell);

        cell = new Listcell();
        cell.setLabel(getMyGroupsNames(juridicalPerson.getPrivateGroups()));
        item.appendChild(cell);



        cell = new Listcell();
        cell.setLabel(juridicalPerson.getRegion());
        item.appendChild(cell);

        cell = new Listcell();
        cell.setLabel(juridicalPerson.getSite());
        item.appendChild(cell);

        cell = new Listcell();
        cell.setLabel(Utils.trunc(juridicalPerson.getNote(), 30));
        item.appendChild(cell);




    }

    private String getMyGroupsNames(List<ItGroup> groups) {
        StringBuilder sb=new StringBuilder();
        for(ItGroup ig:groups){
            if(authService.getEmployee().equals(ig.getOwner())){
                sb.append(ig.getName());
                sb.append(';');
            }
        }

        return sb.toString();
    }

    private String getGroupsNames(List<ItGroup> groups) {

        StringBuilder sb=new StringBuilder();
        for(ItGroup ig:groups){
            sb.append(ig.getName());
            sb.append(';');
        }

        return sb.toString();
    }

    private static class JuridicalPersonEditEventEventListener implements EventListener<Event> {

        public JuridicalPersonEditEventEventListener() {

        }

        @Override
        public void onEvent(Event event) throws Exception {
            Listitem target = (Listitem) event.getTarget();
            JuridicalPerson juridicalPerson= target.getValue();
            HashMap<String, Object> args = new HashMap<String, Object>();
            args.put("juridicalPerson", juridicalPerson);
            BindUtils.postGlobalCommand(null, null, "editJuridicalPerson", args);
        }
    }
}
