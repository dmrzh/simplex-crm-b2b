package ru.dev_server.client.viewmodel;

import org.hibernate.validator.internal.constraintvalidators.EmailValidator;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;
import ru.dev_server.client.dao.EmployeeDAO;
import ru.dev_server.client.model.Employee;

/**.*/
public class RegisterEmailValidator extends AbstractValidator {
    private EmployeeDAO employeeDAO;

    public RegisterEmailValidator(EmployeeDAO employeeDAO) {
        this.employeeDAO=employeeDAO;
    }

    @Override
    public void validate(ValidationContext ctx) {
        String val = (String)ctx.getProperty().getValue();
        if(val==null||val.trim().length()==0){
            addInvalidMessage(ctx, "Email должен быть не пустой");
            return;
        }
        val=val.trim();
        if(!  new EmailValidator().isValid(val,null)){
            addInvalidMessage(ctx, "email не верный");
            return;
        }
        Employee emp = employeeDAO.findByEmail(val);
        if(emp!=null && !emp.equals(ctx.getProperty().getBase())){
            addInvalidMessage(ctx, "пользователь стаким email уже существует");
            return;
        }
    }
}
