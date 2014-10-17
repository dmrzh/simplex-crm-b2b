package ru.dev_server.client.viewmodel.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import ru.dev_server.client.AuthService;
import ru.dev_server.client.dao.PaymentDAO;
import ru.dev_server.client.model.Payment;
import ru.dev_server.client.utils.MD5Utils;

/**.*/
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class RKPayVM {
    private static final Logger LOG = LoggerFactory.getLogger(RKPayVM.class);

    @WireVariable
    private PaymentDAO paymentDAO;

    @WireVariable
    private AuthService authService;

    @WireVariable("robocassa.login")
    private String robocassaLogin;

    @WireVariable("robocassa.pw1")
    private String robocassaPw1;


    private Payment payment;
    private String email;

    @Wire("#signatureValue")
    private Textbox signatureValue;
    @AfterCompose
    public void doAfteCompose(@ContextParam(ContextType.VIEW) Window view){
        Selectors.wireComponents(view, this, false);
        payment = new Payment();
        payment.setCompany(authService.getCompany());
        payment.setAmount(1000);
        paymentDAO.saveOrUpdate(payment);
        email=authService.getEmployee().getEmail();
    }


    @Command
    public void pay(){
        signatureValue.setValue(getSignatureValue());
        Clients.submitForm("payForm");
    }

    public Payment getPayment() {
        return payment;
    }

    public String getEmail() {
        return email;
    }
    public String getSignatureValue(){
        String str=robocassaLogin+":"+payment.getAmount()+":"+payment.getId()+":"+robocassaPw1;
        return MD5Utils.calculateSignature(str);
    }

}

