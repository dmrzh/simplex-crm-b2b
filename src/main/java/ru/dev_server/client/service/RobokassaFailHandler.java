package ru.dev_server.client.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MarkerFactory;
import org.springframework.web.HttpRequestHandler;
import ru.dev_server.client.controllers.TabsController;
import ru.dev_server.client.dao.PaymentDAO;
import ru.dev_server.client.model.Payment;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**.*/
public class RobokassaFailHandler implements HttpRequestHandler {
    private static final Logger LOG = LoggerFactory.getLogger(RobokassaFailHandler.class);

    @Resource
    private PaymentDAO paymentDAO;
    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String outSum = request.getParameter("OutSum");
        String invId = request.getParameter("InvId");
        LOG.debug("OutSum="+"{} InvId={} ", new Object[]{outSum,invId});

        Payment payment = paymentDAO.get(Long.parseLong(invId));
        if(payment.getPaymentState()==Payment.PaymentState.PAYED){
            LOG.error(MarkerFactory.getMarker("email"),"RobokassaFailHandler: CANCEL PAYED PAYMENT!");
            String message="Ошибка! Платёж не прошёл.";
            request.getSession().setAttribute(TabsController.MESSAGE_ATTRIBUTE, message);
            response.sendRedirect(request.getContextPath()+ "/");
        }else{
            payment.setPaymentState(Payment.PaymentState.ERROR);
            paymentDAO.saveOrUpdate(payment);
            String message="Ошибка! Платёж не прошёл.";
            request.getSession().setAttribute(TabsController.MESSAGE_ATTRIBUTE, message);
            response.sendRedirect(request.getContextPath()+ "/");
        }


    }
}
