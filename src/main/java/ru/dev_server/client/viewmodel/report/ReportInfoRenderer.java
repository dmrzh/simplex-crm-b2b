package ru.dev_server.client.viewmodel.report;

import org.zkoss.bind.BindUtils;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.DefaultTreeNode;
import org.zkoss.zul.TreeNode;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Treerow;
import ru.dev_server.client.I18nConverter;
import ru.dev_server.client.model.Employee;
import ru.dev_server.client.model.ItEvent;
import ru.dev_server.client.model.JuridicalPerson;

import java.util.HashMap;

/**
 * .
 */

public class ReportInfoRenderer implements TreeitemRenderer<DefaultTreeNode<RowInfo>> {
    private static final String CLIENT_COLOR = "#efb989";
    private I18nConverter i18nConverter = new I18nConverter(ItEvent.EventType.class);
    private static final String BORDER_WIDTH="1";



    public void render(Treeitem item, DefaultTreeNode<RowInfo> data, int index) throws Exception {
        RowInfo rowInfo = data.getData();
        Object coreObject = rowInfo.getCoreObject();
        if(coreObject!=null&&coreObject instanceof JuridicalPerson){
            final JuridicalPerson jp=(JuridicalPerson)coreObject;
            item.addEventListener(Events.ON_DOUBLE_CLICK, new JuridicalPersonEditEventEventListener(jp));
        }


        Treerow tr = new Treerow();

        item.appendChild(tr);

        tr.appendChild(createCell(rowInfo.getName()));

        tr.appendChild(createCell(i18nConverter.coerceToUi(rowInfo.getEventType(), null, null)));
        tr.appendChild(createCell(rowInfo.getPublicProup()));
        tr.appendChild(createCell(rowInfo.getPrivateProup()));

        tr.appendChild(createCell(rowInfo.getCalls()));

        tr.appendChild(createCell(""+rowInfo.getMeetings()));
        tr.appendChild(createCell(""+rowInfo.getRub()));

        Double euro = rowInfo.getEuro();
        tr.appendChild(createCell(euro==null?"":""+euro));

        if(coreObject!=null){
            if(coreObject instanceof Employee){
                tr.setClass("employeeColor");
            }else if(coreObject instanceof JuridicalPerson){
                if(index%2==0){
                    tr.setClass("lightBee");
                } else{
                    tr.setClass("darkBee");
                }
            }else if(coreObject instanceof ItEvent){
                TreeNode<RowInfo> clientNode = data.getParent();
                TreeNode<RowInfo> employeeNode = clientNode.getParent();
                int clientIndex = employeeNode.getIndex(clientNode);
                if(clientIndex %2==0){
                    tr.setClass("lightBee");
                } else{
                    tr.setClass("darkBee");
                }


            }
        }

    }

    private Treecell createCell(String text) {
        Treecell treecell = new Treecell(text);
        treecell.setStyle("border-width:"+BORDER_WIDTH+";border-color:black;");
        return treecell;
    }

    private static class JuridicalPersonEditEventEventListener implements EventListener<Event> {
        private JuridicalPerson juridicalPerson;

        public JuridicalPersonEditEventEventListener(JuridicalPerson juridicalPerson) {
            this.juridicalPerson=juridicalPerson;

        }

        @Override
        public void onEvent(Event event) throws Exception {
            HashMap<String, Object> args = new HashMap<String, Object>();
            args.put("juridicalPerson", juridicalPerson);
            BindUtils.postGlobalCommand(null, null, "editJuridicalPerson", args);
        }
    }
}