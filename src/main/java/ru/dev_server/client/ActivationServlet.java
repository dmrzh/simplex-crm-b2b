package ru.dev_server.client;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.HttpRequestHandler;
import ru.dev_server.client.dao.CompanyDAO;
import ru.dev_server.client.dao.EmployeeDAO;
import ru.dev_server.client.dao.PaymentDAO;
import ru.dev_server.client.model.Company;
import ru.dev_server.client.model.Employee;
import ru.dev_server.client.model.Payment;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**Handle email activation.*/
public class ActivationServlet implements HttpRequestHandler {

    @Resource
    private EmployeeDAO employeeDAO;
    @Resource
    private CompanyDAO companyDAO;
    @Resource
    private PaymentDAO paymentDAO;

    @Override
    public void handleRequest(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        String activationCode = req.getParameter("activationCode");
        String email = req.getParameter("email");
        Employee employee = employeeDAO.findByEmail(email);
        if(employee!=null&&activationCode!=null&&!activationCode.isEmpty()&&activationCode.equals(employee.getActivationCode())){       //todo test
            employee.setActivationCode(null);
            employeeDAO.saveOrUpdate(employee);


            Company company = employee.getCompany();
            Company referer = company.getReferer();
            if(referer!=null){
                company.setBalance(company.getBalance()+500);
                referer.setBalance(referer.getBalance() + 500);
                companyDAO.saveOrUpdate(company);
                companyDAO.saveOrUpdate(referer);
                Payment payment = new Payment();
                payment.setAmount(500);
                payment.setNote("Подарок за регистрацию по рекомендации.");
                payment.setPaymentState(Payment.PaymentState.PAYED);
                payment.setCompany(company);
                paymentDAO.saveOrUpdate(payment);

                payment = new Payment();
                payment.setAmount(500);
                payment.setNote("Подарок за приглашение.");
                payment.setPaymentState(Payment.PaymentState.PAYED);
                payment.setCompany(referer);
                paymentDAO.saveOrUpdate(payment);

            }

            ArrayList<GrantedAuthority> grantedAuthorities = HibernateDaoImpl.getGrantedAuthorities(employee);
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(employee.getEmail(), employee.getPassword(),grantedAuthorities);
            SecurityContextHolder.getContext().setAuthentication(token);

            response.sendRedirect(req.getContextPath()+"/");
            return;
        }
        response.sendRedirect(req.getContextPath()+"/login.zul?login_error=NotActivated");
    }

}
