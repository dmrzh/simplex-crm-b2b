package ru.dev_server.client.dao.itevents;

import net.sf.autodao.Dao;
import net.sf.autodao.Finder;
import net.sf.autodao.Named;
import ru.dev_server.client.model.Company;
import ru.dev_server.client.model.Employee;
import ru.dev_server.client.model.ItGroup;
import ru.dev_server.client.model.JuridicalPerson;

import java.util.List;

/**
 * .
 */
public interface ItGroupDAO extends Dao<ItGroup,Long> {
    @Finder(query = "from ItGroup where owner=:employee")
    public List<ItGroup> findPrivate(@Named("employee") Employee employee);


    @Finder(query = "from ItGroup where company=:company")
    public List<ItGroup> findPublic(@Named("company")Company company);


    /**check for juridicalPerson is persistent.*/
    @Finder(query = "select gr from ItGroup gr, JuridicalPerson jp where gr.owner=:employee AND gr in elements (jp.privateGroups) AND jp=:juridicalPerson")
    public List<ItGroup> findPrivate(@Named("employee") Employee employee, @Named("juridicalPerson")JuridicalPerson juridicalPerson);
}
