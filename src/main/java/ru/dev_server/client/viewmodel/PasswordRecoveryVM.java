package ru.dev_server.client.viewmodel;

import org.hibernate.validator.internal.constraintvalidators.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import ru.dev_server.client.EmailSender;
import ru.dev_server.client.dao.EmployeeDAO;
import ru.dev_server.client.dao.PasswordResetDAO;
import ru.dev_server.client.model.Employee;
import ru.dev_server.client.model.PasswordReset;

import java.security.SecureRandom;

/**.*/
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class PasswordRecoveryVM {
    private static final Logger LOG= LoggerFactory.getLogger(PasswordRecoveryVM.class);
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final int alfaLength=(int)'z'-(int)'a'+1;

    @WireVariable
    private PasswordResetDAO passwordResetDAO;
    @WireVariable
    private EmployeeDAO employeeDAO;

    private String email;
    private String captchaVal=generate();
    private String text;
    private String error="";


    @Command @NotifyChange({"captchaVal","error"})
    public void send(){
        if(captchaVal.equals(text)){
            if ( email != null && email.length() > 0 && new EmailValidator().isValid(email, null)){
                Employee employee = employeeDAO.findByEmail(email);
                if(employee==null){
                    error="неверный email адрес";
                }else {
                    PasswordReset r = new PasswordReset();
                    r.setEmployee(employee);
                    passwordResetDAO.saveOrUpdate(r);
                     new EmailSender().resetPassword(email,r.getResetConfimation());
                    error="Cсылка для сброса пароля выслана на почту.";
                }
            }else{
                error="неверный email адрес";

            }

        }else{
            error="Текст с картинки ведён неправильно.";
            LOG.info("captcha: "+text);
        }
        captchaVal=generate();
    }

    public static String generate() {
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<5;i++){
            int rnd= secureRandom.nextInt(alfaLength);
            sb.append((char)((int)'a'+rnd));
        }
        return sb.toString();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCaptchaVal() {
        return captchaVal;
    }

    public void setCaptchaVal(String captchaVal) {
        this.captchaVal = captchaVal;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getError() {
        return error;
    }
}
