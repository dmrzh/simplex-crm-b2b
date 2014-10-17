package ru.dev_server.client.dao.itevents;

import net.sf.autodao.AutoDAO;
import net.sf.autodao.Dao;
import net.sf.autodao.Finder;
import net.sf.autodao.Limit;
import net.sf.autodao.Named;
import net.sf.autodao.Offset;
import ru.dev_server.client.model.Company;
import ru.dev_server.client.model.Employee;
import ru.dev_server.client.model.ItEvent;
import ru.dev_server.client.model.JuridicalPerson;

import java.util.Date;
import java.util.List;

/**
 * .
 */
@AutoDAO
public interface EventDAO extends Dao<ItEvent,Long> {

    @Finder(query="from ItEvent  where client=:juridicalPerson order by eventTime desc")
    public List<ItEvent> findByJuridicalPerson(@Named("juridicalPerson")JuridicalPerson juridicalPerson,
                                               @Offset int offset, @Limit int limit);


    @Finder(query="from ItEvent  where owner=:employee AND " +
            " eventTime<=:endDate AND eventTime>=:startDate AND completed=false" )
    public List<ItEvent> findOwned(@Named("employee")Employee employee,
                                   @Named("startDate")Date startDate,
                                   @Named("endDate")Date endDate);


    @Finder(query="select count(ie) from ItEvent ie where ie.owner=:employee" )
    public Long getAllOwnedCount(@Named("employee")Employee employee);





    @Finder(query="from ItEvent  where owner.company=:company AND" +
                                    " eventTime<=:endDate AND eventTime>=:startDate AND completed=false")
    public List<ItEvent> findOwned(@Named("company")Company company,
                                   @Named("startDate")Date startDate,
                                   @Named("endDate")Date endDate);



    @Finder(query="from ItEvent  where owner=:employee AND  remind = true AND completed=false AND eventTime<:currentTime" )
    public List<ItEvent> findForNotification(@Named("employee")Employee employee,
                                   @Named("currentTime")Date currentTime);





}
