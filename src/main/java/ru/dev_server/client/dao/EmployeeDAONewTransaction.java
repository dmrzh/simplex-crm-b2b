package ru.dev_server.client.dao;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.dev_server.client.model.Employee;

import javax.annotation.Resource;

/**.
 */
public class EmployeeDAONewTransaction {
    @Resource
    EmployeeDAO employeeDAO;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveOrUpdate(Employee employee){
        employeeDAO.saveOrUpdate(employee);
    }
}
