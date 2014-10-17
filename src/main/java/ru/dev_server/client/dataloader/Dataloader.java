package ru.dev_server.client.dataloader;

import org.springframework.transaction.annotation.Transactional;
import ru.dev_server.client.dao.CategoryDAO;
import ru.dev_server.client.dao.ClientDAO;
import ru.dev_server.client.dao.CompanyDAO;
import ru.dev_server.client.dao.EmployeeDAO;
import ru.dev_server.client.dao.TariffDAO;
import ru.dev_server.client.model.Category;
import ru.dev_server.client.model.Company;
import ru.dev_server.client.model.Employee;
import ru.dev_server.client.model.Tariff;

import javax.annotation.Resource;

/**Fill empty database.*/

public class Dataloader {

    @Resource
    private TariffDAO tariffDAO;

    @Resource
    private ClientDAO clientDAO;
    @Resource
    private CategoryDAO categoryDAO;

    @Resource
    private EmployeeDAO employeeDAO;

    @Resource
    private CompanyDAO companyDAO;


    Category[] cats;
    Company company;


    @Transactional
    public void fillDatabase(){
        if(companyDAO.findAll().size()!=0){
            return;
        }
        Tariff tariff = new Tariff();
        tariff.setDefaultTariff(true);
        tariff.setConstantCost(10);
        tariffDAO.saveOrUpdate(tariff);

        company = new Company();
        company.setCurrentTariff(tariffDAO.findDefault().get(0) );
        company.setName("ItalTools");
        companyDAO.saveOrUpdate(company);



        Employee employee= new Employee();
        employee.setEmail("89162233196@mail.ru");
        employee.setPassword("italtools");
        employee.setFio("Петр Гундров");
        employee.setRole("ROLE_ADMIN");
        employee.setCompany(company);
        employee.setActivationCode(null);
        employeeDAO.saveOrUpdate(employee);


        cats = new Category[]{addCategory("VIP"),addCategory("A"),addCategory("B"),addCategory("C"),null} ;

    }



    public Category addCategory(String name){
        Category category = new Category();
        category.setName(name);
        category.setCompany(company);
        categoryDAO.saveOrUpdate(category);
        return category;

    }

}
