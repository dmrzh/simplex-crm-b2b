package ru.dev_server.client.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.HttpRequestHandler;
import ru.dev_server.client.controllers.TabsController;
import ru.dev_server.client.dao.PaymentDAO;
import ru.dev_server.client.model.Company;
import ru.dev_server.client.model.Payment;
import ru.dev_server.client.utils.MD5Utils;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**.*/
public class RobokassaSuccessHandler implements HttpRequestHandler {
    private static final Logger LOG = LoggerFactory.getLogger(RobokassaSuccessHandler.class);

    @Resource(name = "robocassa.pw1")
    private String password1;
    @Resource
    private PaymentDAO paymentDAO;

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        String outSum = request.getParameter("OutSum");
        String invId = request.getParameter("InvId");
        String signatureValue = request.getParameter("SignatureValue");
        LOG.debug("OutSum="+"{} InvId={} SignatureValue={}", new Object[]{outSum,invId,signatureValue});
        String strToCRC=outSum+":"+invId+":"+password1;
        String crc = MD5Utils.calculateSignature(strToCRC);
        if(crc.equalsIgnoreCase(signatureValue)){
            String message="Платёж прошёл.";
            Payment payment = paymentDAO.get(Long.parseLong(invId));
            if(payment==null){
                wrong(request, response);
                return;
            }
            if(payment.getPaymentState()== Payment.PaymentState.PAYING){
                payment.setPaymentState(Payment.PaymentState.PAYED);
                Company company = payment.getCompany();
                company.setBalance(company.getBalance()+Long.parseLong(outSum)*100);

            }
            paymentDAO.saveOrUpdate(payment);
            request.getSession().setAttribute(TabsController.MESSAGE_ATTRIBUTE, message);
            response.sendRedirect(request.getContextPath() + "/");
        }else{
            LOG.warn("payment wrong. crc="+crc+" ");

            Payment payment = paymentDAO.get(Long.parseLong(invId));
            if(payment!=null){
                payment.setPaymentState(Payment.PaymentState.ERROR);
                paymentDAO.saveOrUpdate(payment);
            }

            wrong(request, response);
            return;
        }
    }

    private void wrong(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String message="Платёж не прошёл.";
        request.getSession().setAttribute(TabsController.MESSAGE_ATTRIBUTE,message);
        response.sendRedirect(request.getContextPath() + "/");
    }

    public void setPassword1(String password1) {
        this.password1 = password1;
    }
}
