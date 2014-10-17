package ru.dev_server.client.dao;

import net.sf.autodao.AutoDAO;
import net.sf.autodao.Dao;
import net.sf.autodao.Finder;
import net.sf.autodao.Limit;
import net.sf.autodao.Named;
import net.sf.autodao.Offset;
import ru.dev_server.client.model.Category;
import ru.dev_server.client.model.Client;
import ru.dev_server.client.model.Company;
import ru.dev_server.client.model.JuridicalPerson;

import java.util.List;

/**.*/
@AutoDAO
public interface ClientDAO extends Dao<Client, Long>{
    //todo remove
    @Finder(query="from Client where company=:company  AND deleted=false ORDER BY UPPER(lastName), UPPER(firstName), UPPER(middleName) ")
    public List<Client> findAll(@Named("company")  Company company);


    @Finder(query="from Client  where company=:company AND deleted=false ORDER BY UPPER(lastName), UPPER(firstName), UPPER(middleName)  ")
    public List<Client> findAll(@Named("company")  Company company,@Offset int offset, @Limit int limit);

    //todo remove
    @Finder(query="from Client where " +
            "(UPPER(firstName) like UPPER(:filter) or UPPER(lastName) like UPPER(:filter) or " +
            "UPPER(middleName) like UPPER(:filter)) AND deleted=false " +
            " AND company=:company ORDER BY UPPER(lastName), UPPER(firstName), UPPER(middleName) ")
    public List<Client> findByFilter(@Named("filter") String filter, @Named("company")Company company);

    @Finder(query="from Client where " +
            "(UPPER(firstName) like UPPER(:filter) or UPPER(lastName) like UPPER(:filter) or " +
            "UPPER(middleName) like UPPER(:filter)) " +
            " AND company=:company AND deleted=false " +
            " ORDER BY UPPER(lastName), UPPER(firstName), UPPER(middleName) " )
    public List<Client> findByFilter(@Named("filter") String filter, @Named("company")Company company,@Offset int offset, @Limit int limit);



    @Finder(query="from Client where category=:category AND" +
            "(UPPER(firstName) like UPPER(:filter) or UPPER(lastName) like UPPER(:filter) or " +
            "UPPER(middleName) like UPPER(:filter)) " +
            " AND company=:company AND deleted=false " +
            " ORDER BY UPPER(lastName), UPPER(firstName), UPPER(middleName) " )
    public List<Client> findByFilterAndCategory(@Named("filter") String filter, @Named("category")  Category category, @Named("company")Company company,@Offset int offset, @Limit int limit);



    @Finder(query="select count (*) from Client where category=:category AND" +
            "(UPPER(firstName) like UPPER(:filter) or UPPER(lastName) like UPPER(:filter) or " +
            "UPPER(middleName) like UPPER(:filter)) " +
            " AND company=:company " +
            "AND deleted=false ")
    public Long getClientCountByFilterAndCategory(@Named("filter") String filter, @Named("category")  Category category, @Named("company")Company company);


    @Finder(query="select count (*) from Client where category=:category AND company=:company AND deleted=false ")
    public Long getClientCount(@Named("company")  Company company, @Named("category")  Category category);

    @Finder(query="select count (*) from Client where company=:company AND deleted=false ")
    public Long getClientCount(@Named("company")  Company company);

    @Finder(query="select count (*) from Client where " +
            "(UPPER(firstName) like UPPER(:filter) or UPPER(lastName) like UPPER(:filter) or " +
            "UPPER(middleName) like UPPER(:filter)) " +
            " AND company=:company AND deleted=false ")
    public Long getClientCountByFilter(@Named("filter") String filter, @Named("company")Company company);

    @Finder(query="from Client where category=:category AND company=:company AND deleted=false " +
            "ORDER BY UPPER(lastName), UPPER(firstName), UPPER(middleName) " )
    public List<Client> findByCategory(@Named("company")  Company company, @Named("category")  Category category);

    @Finder(query = "from Client where juridicalPerson=:juridicalPerson")
    public List<Client> findContactPersonsJuridicalPerson(@Named("juridicalPerson") JuridicalPerson juridicalPerson);

}