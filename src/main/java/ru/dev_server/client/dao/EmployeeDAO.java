package ru.dev_server.client.dao;

import net.sf.autodao.AutoDAO;
import net.sf.autodao.Dao;
import net.sf.autodao.Finder;
import net.sf.autodao.Named;
import ru.dev_server.client.model.Company;
import ru.dev_server.client.model.Employee;

import java.util.List;

/**.*/
@AutoDAO
public interface EmployeeDAO extends Dao<Employee, Long>{
    @Finder(query="from Employee where company=:company" )
    public List<Employee> findAll(@Named("company") Company company);

    @Finder(query="from Employee where email=:email")
    public Employee findByEmail(@Named("email") String email);
}
