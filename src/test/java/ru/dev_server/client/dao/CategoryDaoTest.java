package ru.dev_server.client.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import ru.dev_server.client.AuthService;
import ru.dev_server.client.model.Category;
import ru.dev_server.client.model.Company;
import ru.dev_server.client.model.Tariff;

import javax.annotation.Resource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**.*/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class CategoryDaoTest {
    private static final Logger LOG= LoggerFactory.getLogger(CategoryDaoTest.class);

    @Resource
    private TariffDAO tariffDAO;

    @Resource
    private CompanyDAO companyDAO;

    @Resource
    private CategoryDAO categoryDAO;
    @Resource
    private AuthService authService;


    @Test  @Transactional
    public void doIt(){
        LOG.warn("start doIt");
        assertNotNull(categoryDAO);

        Tariff tariff = new Tariff();
        tariff.setDefaultTariff(true);
        tariff.setConstantCost(10);
        tariffDAO.saveOrUpdate(tariff);

        Company company = new Company();
        company.setCurrentTariff(tariffDAO.findDefault().get(0));
        company.setName("qq");
        companyDAO.saveOrUpdate(company);


        Category category = new Category ();
        category.setCompany(company);
        category.setName("Взрослый");

        categoryDAO.saveOrUpdate(category);

        assertEquals(1,categoryDAO.findAll(company).size());
        assertEquals("Взрослый", categoryDAO.findAll(company).get(0).getName());
    }

}
