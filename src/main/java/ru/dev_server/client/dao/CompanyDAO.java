package ru.dev_server.client.dao;

import net.sf.autodao.AutoDAO;
import net.sf.autodao.Dao;
import net.sf.autodao.Finder;
import net.sf.autodao.Named;
import ru.dev_server.client.model.Company;

import java.util.List;

/**.*/
@AutoDAO
public interface CompanyDAO extends Dao<Company, Long>{
    @Finder(query="from Company")
    public List<Company> findAll();
    @Finder(query="from Company where name=:name")
    public Company findByName(@Named("name")String name);
    @Finder(query="from Company where lower(name)=lower(:name)")
    public Company findByNameIgnoreCase(@Named("name")String name);

}
