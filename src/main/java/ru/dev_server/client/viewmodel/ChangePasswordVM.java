package ru.dev_server.client.viewmodel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import ru.dev_server.client.dao.EmployeeDAO;
import ru.dev_server.client.dao.PasswordResetDAO;
import ru.dev_server.client.model.Employee;
import ru.dev_server.client.model.PasswordReset;

/**.*/
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ChangePasswordVM {
    private static final Logger LOG= LoggerFactory.getLogger(ChangePasswordVM.class) ;

    @WireVariable
    PasswordResetDAO passwordResetDAO;

    @WireVariable
    EmployeeDAO employeeDAO;

    private String password;
    private String password2;
    private String error;
    Employee employee;


    @Init @NotifyChange("error")
    public void init(){
        String confirmationCode = Executions.getCurrent().getParameter("confirmation");
        LOG.debug("сonfimationCode="+confirmationCode);
        if(confirmationCode==null || confirmationCode.length()==0){
            error="Код подтверждения не верный.";
            return;
        }
        PasswordReset resetPassword = passwordResetDAO.findByResetConfimation(confirmationCode);
        if(resetPassword==null){
            error="Код подтверждения не верный или просрочен.";
            return;
        }
        employee=resetPassword.getEmployee();
        passwordResetDAO.delete(resetPassword);
    }


    @Command @NotifyChange("error")
    public void change(){
        if(employee==null){
            error="Пользователь не найден. Возможно код подтверждения не верен.";
            return;
        }
        if (password==null || password.length()==0 ){
            error="Введите новый пароль";
            return;
        }
        if(!password.equals(password2)){
            error="Пароли не совпадают";
            return;
        }

        employee.setPassword(password);
        employee.setActivationCode(null);
        employeeDAO.saveOrUpdate(employee);


        Executions.sendRedirect("/login.zul");
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
