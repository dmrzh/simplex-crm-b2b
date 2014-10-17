package ru.dev_server.client.viewmodel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Window;
import ru.dev_server.client.AuthService;
import ru.dev_server.client.dao.itevents.ItGroupDAO;
import ru.dev_server.client.model.ItGroup;
import ru.dev_server.client.model.JuridicalPersonFilter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * .
 */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class JuridicalPersonFilterVM {
    private static final Logger LOG= LoggerFactory.getLogger(JuridicalPersonFilterVM.class);

    private JuridicalPersonFilter filter=new JuridicalPersonFilter();


    private Window juridicalPersonFilterWin;


    private List<ItGroup> selectedPublicGroup=new LinkedList<ItGroup>();
    private List<ItGroup> selectedPrivateGroup=new LinkedList<ItGroup>();


    @WireVariable
    private ItGroupDAO itGroupDAO;

    @WireVariable
    private AuthService authService;



    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Window view,@ExecutionArgParam("filter") JuridicalPersonFilter filter){
        juridicalPersonFilterWin =view;
        this.filter=filter;

    }


    @Command
    public void applyFilter(){
        juridicalPersonFilterWin.detach();


        HashMap<String, Object> args = new HashMap<String, Object>();
        args.put("filter",filter);
        BindUtils.postGlobalCommand(null, null, "filterJuridicalPersons", args);

    }
    @Command
    public void choosePublicGroups(){
        HashMap<String, Object> arg = new HashMap<String, Object>();
        arg.put("publicGroup", Boolean.TRUE);
        arg.put("groupResultList", filter.getSelectedPublicGroups());
        Executions.createComponents("/WEB-INF/zul/italtools/choseGroups.zul", null, arg);
    }

    @Command @GlobalCommand @NotifyChange({"publicGroupNames"})
    public void clearPublicGroups(){
        filter.getSelectedPublicGroups().clear();
    }
    @Command  @GlobalCommand @NotifyChange({"privateGroupNames"})
    public void clearPrivteGroups(){
        filter.getSelectedPrivateGroups().clear();
    }



    @Command
    public void choosePrivateGroups(){
        HashMap<String, Object> arg = new HashMap<String, Object>();
        arg.put("publicGroup", Boolean.FALSE);
        arg.put("groupResultList", filter.getSelectedPrivateGroups());
        Executions.createComponents("/WEB-INF/zul/italtools/choseGroups.zul", null, arg);
    }
    @GlobalCommand @NotifyChange({"privateGroupNames","publicGroupNames"})
    public void refreshSelectedGroupsInJuridicalPersonsFilter(
            @BindingParam("publicGroup") Boolean publicGroup,
            @BindingParam("groupResultList") List<ItGroup> groupResultList){
        if(publicGroup){
            filter.setSelectedPublicGroups(groupResultList);
        }else{
            filter.setSelectedPrivateGroups(groupResultList);
        }
    }
    public String getPublicGroupNames(){
        return getGroupNames(filter.getSelectedPublicGroups());
    }
    public String getPrivateGroupNames(){
        return getGroupNames(filter.getSelectedPrivateGroups());
    }

    public String getGroupNames(List<ItGroup> groups){
        if(groups.size()==0){
            return "";
        }
        String s="";
        for(ItGroup g:groups){
            s=s+g.getName()+";";
        }

        return s.substring(0,s.length()-1);
    }



    public JuridicalPersonFilter getFilter() {
        return filter;
    }


    public List<ItGroup> getSelectedPublicGroup() {
        return selectedPublicGroup;
    }

    public void setSelectedPublicGroup(List<ItGroup> selectedPublicGroup) {
        this.selectedPublicGroup = selectedPublicGroup;
    }

    public List<ItGroup> getSelectedPrivateGroup() {
        return selectedPrivateGroup;
    }

    public void setSelectedPrivateGroup(List<ItGroup> selectedPrivateGroup) {
        this.selectedPrivateGroup = selectedPrivateGroup;
    }
}
