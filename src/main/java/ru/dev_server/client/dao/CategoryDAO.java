package ru.dev_server.client.dao;

import net.sf.autodao.AutoDAO;
import net.sf.autodao.Dao;
import net.sf.autodao.Finder;
import net.sf.autodao.Named;
import ru.dev_server.client.model.Category;
import ru.dev_server.client.model.Company;

import java.util.List;

/**.*/
@AutoDAO
public interface CategoryDAO extends Dao<Category, Long>{
    @Finder(query="from Category where company=:company")
    public List<Category> findAll(@Named("company") Company company);

    @Finder(query="from Category where company=:company and name=:name")
    public Category findByName(@Named("company") Company company,@Named("name") String name);
}
