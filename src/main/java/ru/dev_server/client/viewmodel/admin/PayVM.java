package ru.dev_server.client.viewmodel.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Window;
import ru.dev_server.client.AuthService;
import ru.dev_server.client.dao.PaymentDAO;
import ru.dev_server.client.model.Payment;

/**.*/
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class PayVM {
    private static final Logger LOG = LoggerFactory.getLogger(PayVM.class);

    @WireVariable
    private PaymentDAO paymentDAO;

    @WireVariable
    private AuthService authService;
;

    Payment payment;

    @AfterCompose
    public void doAfteCompose(@ContextParam(ContextType.VIEW) Window view){
        Selectors.wireComponents(view,this,false);
        payment = new Payment();
        payment.setCompany(authService.getCompany());
        payment.setAmount(1000);
        //payment.setUuid(UUID.randomUUID().toString());

    }


    @Command
    public void pay(){
        paymentDAO.saveOrUpdate(payment);
        payment = new Payment();
        payment.setCompany(authService.getCompany());;
       // payment.setUuid(UUID.randomUUID().toString());
        Clients.submitForm("payForm");
    }

    public Payment getPayment() {
        return payment;
    }
}

