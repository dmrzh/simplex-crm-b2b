package ru.dev_server.client;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.dev_server.client.dao.EmployeeDAO;
import ru.dev_server.client.model.Company;
import ru.dev_server.client.model.Employee;

import javax.annotation.Resource;

/**.*/
public class AuthService {
    @Resource
    EmployeeDAO employeeDAO;
    public Company getCompany(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        return employeeDAO.findByEmail(name).getCompany();
    }

    public Employee getEmployee(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        return employeeDAO.findByEmail(name);
    }

}
