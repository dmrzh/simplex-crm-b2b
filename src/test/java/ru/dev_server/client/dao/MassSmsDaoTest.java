package ru.dev_server.client.dao;

import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import ru.dev_server.client.model.Client;
import ru.dev_server.client.model.Company;
import ru.dev_server.client.model.MassSms;
import ru.dev_server.client.model.NotificationStatus;
import ru.dev_server.client.model.SmsNotification;
import ru.dev_server.client.model.Tariff;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**.*/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class MassSmsDaoTest {
    private static final Logger LOG= LoggerFactory.getLogger(MassSmsDaoTest.class);

    @Resource
    private TariffDAO tariffDAO;

    @Resource
    MassSmsDAO massSmsDao;

    @Resource
    SmsNotificationDAO smsNotificationDAO;


    @Resource
    CompanyDAO companyDAO;

    @Resource
    private ClientDAO clientDAO;

    @Test
    @Transactional
    public void doIt(){
        LOG.warn("start doIt");

        Tariff tariff = new Tariff();
        tariff.setDefaultTariff(true);
        tariff.setConstantCost(10);
        tariffDAO.saveOrUpdate(tariff);

        Company company = new Company();
        company.setCurrentTariff(tariffDAO.findDefault().get(0));
        company.setName("qq");
        companyDAO.saveOrUpdate(company);

        Client client = new Client();
        client.setCompany(company);
        clientDAO.saveOrUpdate(client);


        MassSms massSms = new MassSms();
        massSms.setCompany(company);
        massSms.setStartDate(new Date());
        massSms.setName("ww");
        massSmsDao.saveOrUpdate(massSms);



        massSms = new MassSms();
        massSms.setCompany(company);
        massSms.setStartDate(new Date());
        massSms.setName("ww");
        massSmsDao.saveOrUpdate(massSms);

        SmsNotification smsNotification = new SmsNotification();
        smsNotification.setNotificationStatus(NotificationStatus.ERROR);
        smsNotification.setClient(client);
        smsNotification.setMassSms(massSms);
        smsNotificationDAO.saveOrUpdate(smsNotification);

        massSms.getSmsNotifications().put(client, smsNotification);
        massSmsDao.saveOrUpdate(massSms);

        List<Object[]> all = massSmsDao.findAll(company, 0, 1);
        Assert.assertEquals(1,all.size());
    }

}
