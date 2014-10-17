package ru.dev_server.client.dao.itevents;

import net.sf.autodao.AutoDAO;
import net.sf.autodao.Dao;
import net.sf.autodao.Finder;
import net.sf.autodao.Named;
import ru.dev_server.client.model.AbstractClient;
import ru.dev_server.client.model.Employee;
import ru.dev_server.client.model.ItEvent;
import ru.dev_server.client.model.JuridicalPerson;

import java.util.Date;
import java.util.List;

/**
 * .
 */
@AutoDAO
public interface ReportDAO extends Dao<AbstractClient,Long>{

    @Finder(query = "select  count(ev) from ItEvent ev where ev.owner=:employee AND eventType=:eventType " +
            " AND eventTime>=:startDate AND eventTime<:endDate AND ev.completed=true")
    public long collectStat(@Named("employee")Employee employee,@Named("eventType")ItEvent.EventType eventType,
                            @Named("startDate")Date startDate,@Named("endDate") Date endDate);

    @Finder(query = "select sum(ev.rub) from ItEvent ev where ev.owner=:employee AND eventType='BILL' "+
            " AND eventTime>=:startDate AND eventTime<:endDate AND ev.completed=true")
    public Double collectSumRub(@Named("employee")Employee employee,
                                @Named("startDate")Date startDate,@Named("endDate") Date endDate);

    @Finder(query = "select  sum(ev.euro) from ItEvent ev where ev.owner=:employee AND eventType='BILL' "+
            " AND eventTime>=:startDate AND eventTime<:endDate AND ev.completed=true")
    public Double collectSumEuro(@Named("employee")Employee employee,@Named("startDate")Date startDate,@Named("endDate") Date endDate);

    @Finder(query = "select distinct ev.client from ItEvent ev where ev.owner=:employee"+
            " AND eventTime>=:startDate AND eventTime<:endDate AND ev.completed=true")
    public List<AbstractClient> collectClient(@Named("employee")Employee employee,
                                              @Named("startDate")Date startDate,@Named("endDate") Date endDate);

    @Finder(query = "select  ev from ItEvent ev where ev.owner=:employee AND client=:juridicalPerson"+
            " AND eventTime>=:startDate AND eventTime<:endDate AND ev.completed=true")
    public List<ItEvent> collectItEvent(@Named("employee")Employee employee, @Named("juridicalPerson")JuridicalPerson juridicalPerson,
                                        @Named("startDate")Date startDate,@Named("endDate") Date endDate);

}
