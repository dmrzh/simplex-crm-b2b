package ru.dev_server.client.viewmodel.italtools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Window;
import ru.dev_server.client.dao.itevents.ItGroupDAO;
import ru.dev_server.client.model.ItGroup;

/**
 * .
 */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class GroupVM {
    private static final Logger LOG= LoggerFactory.getLogger( GroupVM.class);

    @WireVariable
    private ItGroupDAO itGroupDAO;

    private ItGroup group;
    private Window itGroupWin;
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Window view,@ExecutionArgParam("group") ItGroup group){
        this.group=group;
        this.itGroupWin=view;
    }

    @Command
    public void save(){
        itGroupDAO.saveOrUpdate(group);
        itGroupWin.detach();
    }

    public ItGroup getGroup() {
        return group;
    }

    public void setGroup(ItGroup group) {
        this.group = group;
    }
}
