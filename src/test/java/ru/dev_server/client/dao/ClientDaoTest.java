package ru.dev_server.client.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import ru.dev_server.client.AuthService;
import ru.dev_server.client.model.Client;
import ru.dev_server.client.model.Company;
import ru.dev_server.client.model.Tariff;

import javax.annotation.Resource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**.*/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class ClientDaoTest {
    private static final Logger LOG= LoggerFactory.getLogger(ClientDaoTest.class);

    @Resource
    private TariffDAO tariffDAO;

    @Resource
    CompanyDAO companyDAO;

    @Resource
    ClientDAO clientDAO;
    @Resource
    private AuthService authService;

    @Test  @Transactional
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
        assertNotNull(clientDAO);
        Client client = new Client();
        client.setCompany(company);
        client.setName("Дмитрий");
        clientDAO.saveOrUpdate(client);

        assertEquals(1,clientDAO.findAll(company).size());
        assertEquals("Дмитрий", clientDAO.findAll(company).get(0).getName());
    }

}
