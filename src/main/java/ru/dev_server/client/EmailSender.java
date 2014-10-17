package ru.dev_server.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.zkoss.zk.ui.Executions;
import ru.dev_server.client.dao.SingleEmailDAO;
import ru.dev_server.client.model.Client;
import ru.dev_server.client.model.Employee;
import ru.dev_server.client.model.NotificationStatus;
import ru.dev_server.client.model.SingleEmail;
import ru.dev_server.client.utils.ClientUtils;

import javax.annotation.Resource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**.
 */
public class EmailSender {
    private static final Logger LOG = LoggerFactory.getLogger(EmailSender.class);

    @Resource
    private SingleEmailDAO singleEmailDAO;

    @Resource
    private EmailSender emailSender;


    @Transactional
    public void sendSingleEmail(){
        List<SingleEmail> readyToSend = singleEmailDAO.findReadyToSend(new Date());
        for(SingleEmail se:readyToSend){
            emailSender.setSingleEmail(se);
        }
    }

    @Transactional
    public  void setSingleEmail(SingleEmail se) {
        se=singleEmailDAO.get(se.getId());
        Client to = se.getTo();
        try {
            emailSender.sendMessage(ClientUtils.getFirstEmail(to).getValue(),se.getSubject(),se.getText(),se.getFrom(),false);
            se.setNotificationStatus(NotificationStatus.SENT);
        } catch (Exception e) {
            se.setNotificationStatus(NotificationStatus.ERROR);
            LOG.error(e.getMessage(),e);
        }
        singleEmailDAO.saveOrUpdate(se);
    }
    public void sendMessage(String email, String subject, String text, boolean isHtmlFormat ) {
        sendMessage(email,subject, text, null, isHtmlFormat ) ;
    }

    public void sendMessage(String email, String subject, String text, Employee replayTo, boolean isHtmlFormat ) {
        LOG.debug("start email  sending..");
        try {
            final Properties props = new Properties();
            final InputStream stream = getClass().getClassLoader().getResourceAsStream("mail.properties");
            props.load(stream);

            Authenticator auth = new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    final String uname = (String) props.get("simplex.mail.username");
                    final String upassword = (String) props.get("simplex.mail.password");
                    return new PasswordAuthentication(uname, upassword);
                }
            };
            final Session mailSession = Session.getDefaultInstance(props, auth);
            mailSession.setDebug(Boolean.parseBoolean((String)props.get("mail.debug")));

            String contentType=isHtmlFormat?"text/html":"text/plain";

            MimeMessage message = new MimeMessage(mailSession);
            String fromAddress = (String) props.get("mail.from");
            String fromName = (String) props.get("mail.from.name");
            message.setFrom(new InternetAddress(fromAddress,fromName));
            if(replayTo!=null){
                InternetAddress internetAddress = new InternetAddress(replayTo.getEmail(), replayTo.getFio());
                message.setReplyTo(new InternetAddress[]{internetAddress});
            }


            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            message.setSubject(subject, "UTF-8");
            message.setHeader("Content-Type", contentType+"; charset=utf-8");
            message.setHeader("Content-Transfer-Encoding", "quoted-printable");

            //message.setText(text, "UTF-8");

            message.setContent(text, contentType+"; charset=utf-8");


            LOG.debug("Transport.send to "+ email );
            Transport.send(message);
            LOG.info("email message to " + email + " sended");
            LOG.info("email content: "+text);
        } catch (Exception ex) {
            String msg = "email not sended to [" + email + "] with subject=[" + subject + "] with text=[" + text + "]";
            LOG.error(msg, ex);

        }
    }

    private String getApplicationUrl(){
        HttpServletRequest rq = (HttpServletRequest) Executions.getCurrent().getNativeRequest();
        return rq.getScheme()+"://"+rq.getServerName()+":"+rq.getServerPort()+rq.getContextPath();
    }

    public void sendActivationEmail(Employee employee){
        String text="Добро пожаловать в  Simplex-CRM !\n";
        text = text+"Для активации аккаунта пройдите по ссылке:\n";
        text = text+(getApplicationUrl() + "/activate?activationCode=" + employee.getActivationCode()+
                "&email="+employee.getEmail());

        sendMessage(employee.getEmail(), "Активация аккаунта", text, false);
    }

    public void resetPassword(String email, String resetConfimation){

       String url = getApplicationUrl() + "/changePassword.zul?confirmation=" + resetConfimation;
        String text="<html><body>Кто-то запросил сброс пароля. <br>\n";


        text = text+"Если это Вы, то  для сброса пароля перейдите по ссылке:<br>\n " +
                "<a href=\""+url+"\">"+url+"</a><br>\n";
        text = text+"Если это не Вы, то просто проигнорируйте письмо.<body><html>";

        sendMessage(email,"Сброс пароля simpex-crm.ru", text,true);

    }

}
