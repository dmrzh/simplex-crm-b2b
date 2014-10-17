package ru.dev_server.client.viewmodel.italtools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Window;
import ru.dev_server.client.AuthService;
import ru.dev_server.client.dao.JuridicalPersonDAO;
import ru.dev_server.client.dao.itevents.ItGroupDAO;
import ru.dev_server.client.model.ItGroup;

import java.util.HashMap;
import java.util.List;

/**
 * .
 */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ChooseGroupsVM {
    private static final Logger LOG= LoggerFactory.getLogger(ChooseGroupsVM.class);


    @WireVariable
    private ItGroupDAO itGroupDAO;
    @WireVariable
    private JuridicalPersonDAO juridicalPersonDAO;

    @WireVariable
    private AuthService authService;

    private List<ItGroup> groups;


    private Window chooseGroupWin;

    private Boolean publicGroup=false;
    List<ItGroup> groupResultList;


    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Window view,
                             @ExecutionArgParam("publicGroup") Boolean publicGroup,
                             @ExecutionArgParam("groupResultList") List<ItGroup> groupResultList){
        this.chooseGroupWin=view;
        this.publicGroup=publicGroup;
        this.groupResultList=groupResultList;
        refreshGroups();
    }

    @GlobalCommand
    @NotifyChange("groups")
    public void refreshGroups() {
        if(publicGroup){
            groups=itGroupDAO.findPublic(authService.getCompany());
        }else{
            groups=itGroupDAO.findPrivate(authService.getEmployee());
        }
    }

    @Command
    public void close(){
        HashMap<String, Object> args = new HashMap<String, Object>();
        args.put("publicGroup",publicGroup);
        args.put("groupResultList",groupResultList);
        BindUtils.postGlobalCommand(null, null,"refreshSelectedGroupsInJuridicalPersonsFilter", args);
        chooseGroupWin.detach();
    }


    public List<ItGroup> getGroups() {
        return groups;
    }

    public void setGroups(List<ItGroup> groups) {
        this.groups = groups;
    }

    public List<ItGroup> getGroupResultList() {
        return groupResultList;
    }

    public void setGroupResultList(List<ItGroup> groupResultList) {
        this.groupResultList = groupResultList;
    }
}
