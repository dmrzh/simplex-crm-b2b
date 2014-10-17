package ru.dev_server.client.viewmodel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Window;
import ru.dev_server.client.EmailSender;
import ru.dev_server.client.dao.CompanyDAO;
import ru.dev_server.client.dao.EmployeeDAO;
import ru.dev_server.client.dao.TariffDAO;
import ru.dev_server.client.model.Company;
import ru.dev_server.client.model.Employee;

/**.*/
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class RegisterViewModel {
    private static final Logger LOG= LoggerFactory.getLogger(RegisterViewModel.class);

    @WireVariable
    private TariffDAO tariffDAO;

    @WireVariable("companyDAO")
    private CompanyDAO companyDAO;
    @WireVariable("employeeDAO")
    EmployeeDAO employeeDAO;
    @WireVariable
    private Page _page;


    private Company company=new Company();
    private Employee employee = new Employee();
    private String repeatPassword;
    private String captcha;
    private String redirect="";
    private String referer="";


    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Window view){
        employee.setRole("ROLE_ADMIN");
        referer = Executions.getCurrent().getParameter("referer");
    }



    @Command("register")
    @NotifyChange({"redirect"})
    public void register(){
            company.setCurrentTariff(tariffDAO.findDefault().get(0));
            if(referer!=null){
                Employee byEmail = employeeDAO.findByEmail(referer);
                if(byEmail!=null){
                    company.setReferer(byEmail.getCompany());
                }
            }
            companyDAO.saveOrUpdate(company);
            employee.setCompany(company);
            employeeDAO.saveOrUpdate(employee);
        sendActivationUrl();
        Executions.sendRedirect("/login.zul?login_error=checkEmail");
    }

    private void sendActivationUrl() {
        new EmailSender().sendActivationEmail(employee);
    }

    public Validator getCompanyNameValidator(){
        return new AbstractValidator() {
            @Override
            public void validate(ValidationContext ctx) {
                String val = (String)ctx.getProperty().getValue();
                if(val==null||val.trim().length()==0){
                    addInvalidMessage(ctx, "Имя компании должно быть не пустое");
                    return;
                }
                val=val.trim().trim();
                Company c = companyDAO.findByNameIgnoreCase(val);
                if(c!=null){
                    addInvalidMessage(ctx, "Компания "+val+ " уже существует");
                }
            }
        } ;
    }

    public Validator getPasswordValidator(){
      return new AbstractValidator() {
          @Override
          public void validate(ValidationContext ctx) {
              String val = (String)ctx.getProperty().getValue();
              if(val==null||val.trim().length()==0){
                  addInvalidMessage(ctx, "Пароль должен быть не пустой");
              }
          }
      };
    }
    public Validator getRepeatPasswordValidator(){
        return new AbstractValidator() {
            @Override
            public void validate(ValidationContext ctx) {
                String val = (String)ctx.getProperty().getValue();
                String repeatPass = RegisterViewModel.this.getEmployee().getPassword();
                if(val==null||val.trim().length()==0){
                    addInvalidMessage(ctx, "Пароль должен быть не пустой");
                    return;
                }
                if(val.trim().length()<6){
                    addInvalidMessage(ctx, "Длина пароля должена быть 6 или больше знаков");
                }
                if(val.trim().length()>31){
                    addInvalidMessage(ctx, "Длина пароля должена быть меньше 32 знаков");
                }
                val=val.trim();
                if(! val.equals(repeatPass)){
                    addInvalidMessage(ctx, "Пароли не совпадают");
                }
            }
        };
    }


    public Validator getRefererValidator(){
        return new AbstractValidator() {
            @Override
            public void validate(ValidationContext ctx) {
                String val = (String)ctx.getProperty().getValue();
                if(val==null||val.trim().length()==0 ){
                    return;
                }
                Employee byEmail = employeeDAO.findByEmail(val);
                if(byEmail==null){
                    addInvalidMessage(ctx, "Нет такого пользователя в базе.");
                }

            }
        };
    }

    public Validator getEmailValidator(){
        return new RegisterEmailValidator(employeeDAO);
    }


    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }
}
