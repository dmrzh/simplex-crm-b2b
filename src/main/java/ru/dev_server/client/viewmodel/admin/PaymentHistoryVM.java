package ru.dev_server.client.viewmodel.admin;

import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import ru.dev_server.client.AuthService;
import ru.dev_server.client.I18nConverter;
import ru.dev_server.client.dao.PaymentDAO;
import ru.dev_server.client.model.Payment;

import java.util.List;

/**
 * Payment history ViewModel.
 */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class PaymentHistoryVM {

    private I18nConverter paymentStateConverter=new I18nConverter(Payment.PaymentState.class);

    @WireVariable
    private AuthService authService;

    @WireVariable
    private PaymentDAO paymentDAO;

    private List<Payment> allPayments;

    @Init
    public void init(){
       allPayments = paymentDAO.findAll(authService.getCompany());
    }

    public List<Payment> getAllPayments() {
        return allPayments;
    }

    public I18nConverter getPaymentStateConverter() {
        return paymentStateConverter;
    }
}
