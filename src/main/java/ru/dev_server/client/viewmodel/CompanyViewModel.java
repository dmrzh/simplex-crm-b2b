package ru.dev_server.client.viewmodel;

import org.zkoss.bind.Property;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Window;
import ru.dev_server.client.AuthService;
import ru.dev_server.client.dao.CompanyDAO;
import ru.dev_server.client.model.Company;

import java.util.Map;

/**.*/
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class CompanyViewModel {
    private Window companyWin;
    private Company currentCompany;
    @WireVariable("authService")
    private AuthService authService;
    @WireVariable("companyDAO")
    private CompanyDAO companyDAO;


    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Window view){
        companyWin=view;
       currentCompany=authService.getCompany();
    }


    @Command
    public void save(){
        companyDAO.merge(currentCompany);
        companyWin.detach();

    }
    @Command
    public void cancel(){
        companyWin.detach();

    }
    public Validator getWorkingHoursValidator(){
        return new AbstractValidator() {
            @Override
            public void validate(ValidationContext ctx) {
                Map<String,Property> beanProps = ctx.getProperties(ctx.getProperty().getBase());
                Integer finishWorkingHours= (Integer)beanProps.get("finishWorkingHours").getValue();
                Integer startWorkingHours= (Integer)beanProps.get("startWorkingHours").getValue();
                if(finishWorkingHours<=startWorkingHours){
                    addInvalidMessage(ctx, "Начало рабочего дня должно быть раньше конца рабочего дня");
                }

            }
        } ;
    }

    public Company getCurrentCompany() {
        return currentCompany;
    }

    public void setCurrentCompany(Company currentCompany) {
        this.currentCompany = currentCompany;
    }
    public double getRubBalance(){
        double balance = currentCompany.getBalance();
        return balance /100;
    }
}
