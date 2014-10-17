package ru.dev_server.client.viewmodel.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.Converter;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.sys.BinderCtrl;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;
import ru.dev_server.client.AuthService;
import ru.dev_server.client.HibernateDaoImpl;
import ru.dev_server.client.PropConverter;
import ru.dev_server.client.TrueFalseConverter;
import ru.dev_server.client.dao.EmployeeDAO;
import ru.dev_server.client.dao.MeetingDAO;
import ru.dev_server.client.dao.itevents.EventDAO;
import ru.dev_server.client.model.Employee;
import ru.dev_server.client.model.ItEvent;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**.*/
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class EmployeeListVM {
    private static final Logger LOG= LoggerFactory.getLogger(EmployeeListVM.class) ;

    @WireVariable("authService")
    private AuthService authService;

    @WireVariable
    private EmployeeDAO employeeDAO;

    @WireVariable
    private MeetingDAO meetingDAO;

    @WireVariable
    private EventDAO itEventDAO;

    List<Employee> employeeList;
    private Employee currentEmployee;
    Boolean leader_role;

    private PropConverter roleConverter=new PropConverter("ru.dev_server.client.model.Role.properties");
    private TrueFalseConverter trueFalseConverter = TrueFalseConverter.getInstance();


    @Init
    public void init(){
        refreshEmployeeList();
        leader_role = HibernateDaoImpl.getGrantedAuthorities(authService.getEmployee()).contains(HibernateDaoImpl.ROLE_LEADER);
    }

    @GlobalCommand    @NotifyChange("employeeList")
    public void refreshEmployeeList(){
        employeeList = employeeDAO.findAll(authService.getCompany());
    }


    @Command @NotifyChange("employeeList")
    public void create(){
        Employee employee = new Employee();
        employee.setCompany(authService.getCompany());
        HashMap<String, Employee> arg = new HashMap<String, Employee>();
        arg.put("employee", employee);
        Executions. createComponents("/admin/employee.zul", null, arg);
        init();
    }

    @Command
    public void edit(){
        if(currentEmployee==null) return;
        HashMap<String, Employee> arg = new HashMap<String, Employee>();
        arg.put("employee", currentEmployee);
        Executions. createComponents("/admin/employee.zul", null, arg);
    }
    @Command  @NotifyChange("employeeList")
    public void delete(){
        if(currentEmployee== null){return;}
        Long count = meetingDAO.countByEmployee(currentEmployee);
        count=count+itEventDAO.getAllOwnedCount(currentEmployee);

        if(count>0){
            Messagebox.show("К сотруднику привязаны объекты. Удалить сотрудника нельзя", "Внимание!",
                    Messagebox.OK," EXCLAMATION");
            return;
        }

        employeeDAO.delete(employeeDAO.get(currentEmployee.getId()));
        refreshEmployeeList();
    }

    @Command
    public void setOwner(){
        if(leader_role) {
            HashMap<String, Object> args = new HashMap<String, Object>();
            args.put("owner", currentEmployee);
            BindUtils.postGlobalCommand(BinderCtrl.DEFAULT_QUEUE_NAME, null, "setOwner", args);
        }
    }


    public void onTimer(){
        List<ItEvent> forNotification = itEventDAO.findForNotification(authService.getEmployee(), new Date());
//        for(ItEvent itEvent:forNotification){
//            HashMap params = new HashMap();
//            params.put("itEvent", itEvent);
//            if(itEvent.getEventType()== ItEvent.EventType.BILL){
//                Executions. createComponents("/WEB-INF/zul/italtools/bill.zul", null, params);
//            }else{
//                Executions. createComponents("/WEB-INF/zul/italtools/meetingOrCall.zul", null, params);
//            }
//        }
    }

    public List<Employee> getEmployeeList() {
        return employeeList;
    }

    public void setEmployeeList(List<Employee> employeeList) {
        this.employeeList = employeeList;
    }

    public Employee getCurrentEmployee() {
        return currentEmployee;
    }

    public void setCurrentEmployee(Employee currentEmployee) {
        this.currentEmployee = currentEmployee;
    }

    public Converter getRoleConverter() {
        return roleConverter;
    }

    public Converter getTrueFalseConverter() {
        return trueFalseConverter;
    }
}
