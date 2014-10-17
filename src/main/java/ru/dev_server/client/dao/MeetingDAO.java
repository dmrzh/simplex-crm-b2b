package ru.dev_server.client.dao;

import net.sf.autodao.AutoDAO;
import net.sf.autodao.Dao;
import net.sf.autodao.Finder;
import net.sf.autodao.Named;
import ru.dev_server.client.model.Client;
import ru.dev_server.client.model.Company;
import ru.dev_server.client.model.Employee;
import ru.dev_server.client.model.Meeting;

import java.util.Date;
import java.util.List;

/**.*/
@AutoDAO
public interface MeetingDAO extends Dao<Meeting, Long>{
    @Finder(query="from Meeting where employee.company=:company")
    public List<Meeting> findAll(@Named("company") Company company);


    @Finder(query="from Meeting where employee.company=:company AND beginDate<=:end AND endDate>=:start ")
    public List<Meeting> findAll(@Named("company") Company company, @Named("start") Date start, @Named("end")Date end);


    @Finder(query="from Meeting where employee=:employee AND beginDate<=:end AND endDate>=:start")
    public List<Meeting> findByEmployee( @Named("employee") Employee employee, @Named("start") Date start, @Named("end")Date end);

    @Finder(query="select count(*) from Meeting where employee=:employee")
    public Long countByEmployee( @Named("employee") Employee employee);


    /**
     * Find meetings need for client notification.
     * @param date current date. added for portability becouse database not save timezone, java and database can have different timezones.
     * @return found meetings.
     */
    @Finder(query="select m from Meeting m, SmsNotification n where n.meeting=m and n.client is not null and " +
            " n.notificationStatus='WAITING' and m.notificationDate is not null and m.notificationDate < :date and m.beginDate > :date")
    public List<Meeting> findForNotification(@Named("date") Date date);

    @Finder(query="select m from Meeting m, Client c where  c=:client AND (c=m.client OR (c in elements(m.clientList)))")
    public List<Meeting> findByClient(@Named("client") Client client);
}
