package ru.dev_server.client.viewmodel;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.DefaultTreeModel;
import org.zkoss.zul.DefaultTreeNode;
import org.zkoss.zul.TreeModel;
import org.zkoss.zul.TreeNode;
import org.zkoss.zul.TreeitemRenderer;
import ru.dev_server.client.AuthService;
import ru.dev_server.client.HibernateDaoImpl;
import ru.dev_server.client.dao.EmployeeDAO;
import ru.dev_server.client.dao.itevents.EventDAO;
import ru.dev_server.client.dao.itevents.ItGroupDAO;
import ru.dev_server.client.dao.itevents.ReportDAO;
import ru.dev_server.client.model.AbstractClient;
import ru.dev_server.client.model.Employee;
import ru.dev_server.client.model.ItEvent;
import ru.dev_server.client.model.ItGroup;
import ru.dev_server.client.model.JuridicalPerson;
import ru.dev_server.client.viewmodel.report.ReportInfoRenderer;
import ru.dev_server.client.viewmodel.report.RowInfo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * .
 */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ReportVM {
    private static final Logger LOG= LoggerFactory.getLogger(ReportVM.class);


    @WireVariable
    EmployeeDAO employeeDAO;
    @WireVariable
    private AuthService authService;
    @WireVariable
    EventDAO itEventDAO;

    @WireVariable
    private ItGroupDAO itGroupDAO;

    @WireVariable
    private ReportDAO reportDAO;

    public Date startDate;
    public Date endDate;


    private DefaultTreeModel<String> itemTree;

    private TreeitemRenderer employeeRenderer ;

    @AfterCompose
    public void afterCompose(){
        Calendar instance = Calendar.getInstance();
        setEndDate(instance.getTime());

        instance.add(Calendar.DAY_OF_MONTH,-1);

        setStartDate(instance.getTime());

    }

    @Command @NotifyChange("itemTree")
    public void  refreshReport() {
        employeeRenderer=new ReportInfoRenderer();
        List<Employee> employeeList=employeeDAO.findAll(authService.getCompany());
        Employee workingEmployee = authService.getEmployee();
        boolean isLeader = HibernateDaoImpl.getGrantedAuthorities(workingEmployee).contains(HibernateDaoImpl.ROLE_LEADER);


        List<DefaultTreeNode> employeesNodes=new ArrayList<DefaultTreeNode>();

        double rubSum=0;
        double euroSum=0;
        long meetingsCount=0;
        long callsCount=0;

        for(Employee e:employeeList){
            if(!isLeader){
                if(!workingEmployee.equals(e)){
                    continue;
                }
            }
            List<DefaultTreeNode> clientTreeNodes = clientTree(e);


            RowInfo rowInfo = new RowInfo();
            rowInfo.setName(e.getFio());
            rowInfo.setCoreObject(e);


            long callCt = reportDAO.collectStat(e, ItEvent.EventType.CALL,startDate,endDate);
            rowInfo.setCalls("" + callCt);
            callsCount=callsCount+callCt;

            long meetingsCt = reportDAO.collectStat(e, ItEvent.EventType.MEETING,startDate,endDate);
            rowInfo.setMeetings("" + meetingsCt);
            meetingsCount=meetingsCount+meetingsCt;

            Double rubsSm = reportDAO.collectSumRub(e,startDate,endDate);
            if(rubsSm!=null){
                rowInfo.setRub("" + rubsSm);
                rubSum = rubSum + rubsSm;
            }

            Double euroSm = reportDAO.collectSumEuro(e,startDate,endDate);
            rowInfo.setEuro(euroSm);
            if(euroSm!=null){
                euroSum=euroSum+euroSm;
            }
            DefaultTreeNode  eNode = new DefaultTreeNode(rowInfo,clientTreeNodes);
            employeesNodes.add(eNode);
        }
        RowInfo rowInfo = new RowInfo();
        rowInfo.setName("ИТОГО");
        rowInfo.setMeetings(""+meetingsCount);
        rowInfo.setCalls(""+callsCount);
        rowInfo.setRub(""+rubSum);
        rowInfo.setEuro(euroSum);

        DefaultTreeNode allSumm = new DefaultTreeNode(rowInfo, employeesNodes);
        LinkedList<DefaultTreeNode> allList = new LinkedList<DefaultTreeNode>();
        allList.add(allSumm);

        DefaultTreeNode root = new DefaultTreeNode(null, allList);

        itemTree = new DefaultTreeModel(root);
        itemTree.addOpenObject(allSumm);
        for(DefaultTreeNode n:employeesNodes){
            itemTree.addOpenObject(n);
        }
    }

    private List<DefaultTreeNode> clientTree(Employee e) {

        List<AbstractClient> abstractClients = reportDAO.collectClient(e,startDate,endDate);
        ArrayList<DefaultTreeNode> nodes = new ArrayList<DefaultTreeNode>();
        for(AbstractClient c:abstractClients){
            if(!(c instanceof JuridicalPerson )){
                continue;
            }
            JuridicalPerson jurPers=(JuridicalPerson )c;
            RowInfo rowInfo = new RowInfo();
            rowInfo.setName(jurPers.getName());

            rowInfo.setPrivateProup(groupString(itGroupDAO.findPrivate(authService.getEmployee(),jurPers)));
            rowInfo.setPublicProup(groupString(jurPers.getPublicGroups()));
            rowInfo.setCoreObject(jurPers);



            List<DefaultTreeNode> children = eventTree(e, jurPers, rowInfo);
            nodes.add(new DefaultTreeNode(rowInfo, children));
        }

        return nodes;
    }

    private String groupString(List<ItGroup> groupList) {
        String groupsStr="";
        int size = groupList.size();
        for(int i=0;i<size;i++){
           groupsStr=groupsStr+groupList.get(i).getName();
            if(i!= size -1){
               groupsStr=groupsStr+",";
           }
        }
        return groupsStr;
    }

    private List<DefaultTreeNode> eventTree(Employee employee, JuridicalPerson juridicalPerson, RowInfo rowInfoToFill) {
        List<ItEvent> itEvents = reportDAO.collectItEvent(employee, juridicalPerson,startDate,endDate);
        List<DefaultTreeNode> nodes = new ArrayList<DefaultTreeNode>();
        double rubSum=0;
        double euroSum=0;
        int meetingsCount=0;
        int callsCount=0;

        for(ItEvent ev:itEvents){
            RowInfo rowInfo = new RowInfo();
            DateFormat dateInstance = new SimpleDateFormat("yyyy.MM.dd HH:mm");
            rowInfo.setName(""+ dateInstance.format(ev.getEventTime()));
            ItEvent.EventType eventType = ev.getEventType();
            rowInfo.setEventType(eventType);
            rowInfo.setCoreObject(ev);
            switch (eventType){
                case BILL:
                    Double rub1 = ev.getRub();
                    if(rub1!=null){
                        rowInfo.setRub(""+ rub1);
                        rubSum=rubSum+rub1;
                    }
                    Double euro = ev.getEuro();
                    rowInfo.setEuro(euro);
                    if(euro!=null){
                        euroSum=euroSum+euro;
                    }

                    break;
                case CALL:
                    rowInfo.setCalls(StringUtils.defaultString(ev.getText()));
                    callsCount++;
                    break;
                case MEETING:
                    rowInfo.setMeetings(StringUtils.defaultString(ev.getText()));
                    meetingsCount++;
                    break;
            }


            nodes.add(new DefaultTreeNode(rowInfo));
        }
        rowInfoToFill.setCalls(""+callsCount);
        rowInfoToFill.setMeetings("" + meetingsCount);
        rowInfoToFill.setRub(""+rubSum);
        rowInfoToFill.setEuro(euroSum);

       return nodes;
    }

    @Command
    public void onOpen(){

    }


    public TreeModel<TreeNode<String>> getItemTree() {
        return itemTree;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
        startDate.setHours(0);
        startDate.setMinutes(0);
        startDate.setSeconds(0);
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
        endDate.setHours(23);
        endDate.setMinutes(59);
        endDate.setSeconds(59);
    }
}
