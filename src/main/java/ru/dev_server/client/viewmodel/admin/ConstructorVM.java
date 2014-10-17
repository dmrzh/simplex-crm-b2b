package ru.dev_server.client.viewmodel.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.Converter;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import ru.dev_server.client.AuthService;
import ru.dev_server.client.I18nConverter;
import ru.dev_server.client.TrueFalseConverter;
import ru.dev_server.client.dao.ClientDAO;
import ru.dev_server.client.dao.ClientDynamicValueDAO;
import ru.dev_server.client.dao.DynamicColumnDAO;
import ru.dev_server.client.dao.MeetingDynamicValueDAO;
import ru.dev_server.client.model.ClientDynamicValue;
import ru.dev_server.client.model.DynamicColumn;
import ru.dev_server.client.model.ExstendedTables;
import ru.dev_server.client.model.MeetingDynamicValue;

import java.util.HashMap;
import java.util.List;

/**. */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ConstructorVM {
    private static final Logger LOG= LoggerFactory.getLogger(ConstructorVM.class);
    @WireVariable("authService")
    private AuthService authService;

    @WireVariable
    private DynamicColumnDAO dynamicColumnDAO;

    @WireVariable
    private ClientDynamicValueDAO clientDynamicValueDAO;

    @WireVariable
    private MeetingDynamicValueDAO meetingDynamicValueDAO;

    @WireVariable
    private ClientDAO clientDAO;

    List<DynamicColumn> dynamicColumnList;
    private DynamicColumn currentDynamicColumn;
    private TrueFalseConverter trueFalseConverter=TrueFalseConverter.getInstance();


    private ExstendedTables[] exstensions = ExstendedTables.values();
    private ExstendedTables exstendedTables=ExstendedTables.CLIENT;

    @Init
    public void init(){
        refreshDynamicColumnList();
    }

    @GlobalCommand
    @NotifyChange("dynamicColumnList")
    public void refreshDynamicColumnList(){
        dynamicColumnList=dynamicColumnDAO.findAll(authService.getCompany(), exstendedTables);

    }


    @Command
    @NotifyChange("dynamicColumnList")
    public void create(){
        DynamicColumn dynamicColumn = new DynamicColumn();
        dynamicColumn.setType(String.class);
        dynamicColumn.setExstendedTables(exstendedTables);
        dynamicColumn.setCompany(authService.getCompany());
        HashMap<String, DynamicColumn> arg = new HashMap<String, DynamicColumn>();
        arg.put("dynamicColumn", dynamicColumn);
        Executions. createComponents("/admin/dynamicColumn.zul", null, arg);
    }

    @Command
    public void edit(){
        if(currentDynamicColumn==null) return;
        HashMap<String, DynamicColumn> arg = new HashMap<String, DynamicColumn>();
        arg.put("dynamicColumn", currentDynamicColumn);
        Executions. createComponents("/admin/dynamicColumn.zul", null, arg);
    }
    @Command  @NotifyChange("dynamicColumnList")
    public void delete(){
        if(currentDynamicColumn== null){return;}
        if(exstendedTables==ExstendedTables.CLIENT){
            List<ClientDynamicValue> valueList = clientDynamicValueDAO.findByColumn(currentDynamicColumn);
            for(ClientDynamicValue value:valueList){
                clientDynamicValueDAO.delete(value);
                value.getClient().getDynamicValueList().remove(currentDynamicColumn);
            }
        }else{
            List<MeetingDynamicValue> valueList = meetingDynamicValueDAO.findByColumn(currentDynamicColumn);
            for(MeetingDynamicValue value:valueList){
                meetingDynamicValueDAO.delete(value);
            }
        }
        dynamicColumnDAO.delete(dynamicColumnDAO.merge(currentDynamicColumn));

        refreshDynamicColumnList();
    }




    public List<DynamicColumn> getDynamicColumnList() {
        return dynamicColumnList;
    }

    public void setDynamicColumnList(List<DynamicColumn> dynamicColumnList) {
        this.dynamicColumnList = dynamicColumnList;
    }

    public DynamicColumn getCurrentDynamicColumn() {
        return currentDynamicColumn;
    }

    public void setCurrentDynamicColumn(DynamicColumn currentDynamicColumn) {
        this.currentDynamicColumn = currentDynamicColumn;
    }

    public ExstendedTables[] getExstensions() {
        return exstensions;
    }

    public void setExstensions(ExstendedTables[] exstensions) {
        this.exstensions = exstensions;
    }

    public ExstendedTables getExstendedTables() {
        return exstendedTables;
    }

    public void setExstendedTables(ExstendedTables exstendedTables) {
        this.exstendedTables = exstendedTables;
    }
    public Converter getExtConverter(){
        return new I18nConverter(ExstendedTables.class);
    }

    public TrueFalseConverter getTrueFalseConverter() {
        return trueFalseConverter;
    }
}
