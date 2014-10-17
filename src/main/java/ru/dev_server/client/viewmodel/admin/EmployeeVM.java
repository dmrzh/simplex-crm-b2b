package ru.dev_server.client.viewmodel.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Window;
import ru.dev_server.client.EmailSender;
import ru.dev_server.client.dao.EmployeeDAO;
import ru.dev_server.client.dao.EmployeeDAONewTransaction;
import ru.dev_server.client.model.Employee;
import ru.dev_server.client.viewmodel.RegisterEmailValidator;

/**. */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class EmployeeVM {
    private static final Logger LOG= LoggerFactory.getLogger(EmployeeVM.class) ;
    @WireVariable
    private EmployeeDAO employeeDAO;
    @WireVariable
    private EmployeeDAONewTransaction employeeDAONewTransaction;



    private  Window employeeWin;
    private Employee currentEmployee;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Window view,@ExecutionArgParam("employee") Employee employee){
        employeeWin =view;
        currentEmployee=employee;
    }

    @Command
    public void save(){
        employeeDAONewTransaction.saveOrUpdate(currentEmployee);
        employeeWin.detach();
        if(currentEmployee.getActivationCode()!=null){
            sendActivationUrl();
        }
    }


    public Validator getEmailValidator(){
        return new RegisterEmailValidator(employeeDAO);
    }

    private void sendActivationUrl() {
        LOG.info("sending activation code");
        new EmailSender().sendActivationEmail(currentEmployee);
    }
    @Command
    public void cancel(){
        employeeWin.detach();
    }


    public Employee getCurrentEmployee() {
        return currentEmployee;
    }

    public void setCurrentEmployee(Employee currentEmployee) {
        this.currentEmployee = currentEmployee;
    }
}
