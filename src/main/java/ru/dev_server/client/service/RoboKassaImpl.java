package ru.dev_server.client.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.dev_server.client.AuthService;
import ru.dev_server.client.dao.PaymentDAO;
import ru.dev_server.client.model.Company;
import ru.dev_server.client.model.Payment;
import ru.dev_server.client.utils.MD5Utils;

import javax.annotation.Resource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.Date;

@Path("/RoboKassa")
public class RoboKassaImpl {
    private static final Logger LOG = LoggerFactory.getLogger(RoboKassaImpl.class);
    static final int KOPEEK_IN_RUBL = 100;




    @Resource
    private AuthService authService;
    @Resource
    private PaymentDAO paymentDAO;

    private String password2;



    //http://localhost:8080/med/service/RoboKassa/result?OutSum=1000&InvId=11763712&SignatureValue=A21116011BFED84F983A3959C00727AE
    @GET
    @Path("/result")
    @Produces("text/plain")
    public String result(@QueryParam("OutSum") long outSum, @QueryParam("InvId") long orderId, @QueryParam("SignatureValue") String crc) {
        LOG.debug("outSum="+"{} orderId={} crc={}", new Object[]{outSum,orderId,crc});
        String strToCRC=""+outSum+":"+orderId+":"+password2;
        String calculatedSignature = MD5Utils.calculateSignature(strToCRC);
        if(! calculatedSignature.equalsIgnoreCase(crc)){
            throw new IllegalArgumentException("SignatureValue error: singnature must be: ");
        }
        Payment payment = paymentDAO.get(orderId);
        if(payment==null){
            throw new IllegalArgumentException("no such payment payment.id="+orderId);
        }
        if(payment.getPaymentState()!= Payment.PaymentState.PAYING){
            return Payment.PaymentState.PAYING.name();
        }
        payment.setPaymentState(Payment.PaymentState.PAYED);
        payment.setAmount(outSum);
        Company company = payment.getCompany();

        company.setBalance(company.getBalance()+outSum* KOPEEK_IN_RUBL);
        payment.setPayedDate(new Date());
        paymentDAO.saveOrUpdate(payment);
        return payment.getPaymentState().name();
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }
}