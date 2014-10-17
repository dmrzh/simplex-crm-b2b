package ru.dev_server.client.dao;

import net.sf.autodao.AutoDAO;
import net.sf.autodao.Dao;
import net.sf.autodao.Finder;
import net.sf.autodao.Limit;
import net.sf.autodao.Named;
import ru.dev_server.client.model.Client;
import ru.dev_server.client.model.SmsNotification;

import java.util.Date;
import java.util.List;

/**.*/
@AutoDAO
public interface SmsNotificationDAO extends Dao<SmsNotification, Long>{
    /**
     * Find meetings need for client notification.
     * @param date current date. added for portability becouse database not save timezone, java and database can have different timezones.
     * @return found meetings.
     */
    @Finder(query="select distinct n from Meeting m, Client c, Contact ct, SmsNotification n where n.meeting=m and n.client=c and c=ct.client and " +
            "  n.client is not null and ct.contactType='MOBILE' and" +
            " n.notificationStatus='WAITING' and m.notificationDate is not null and m.notificationDate < :date and m.beginDate > :date")
    public List<SmsNotification> findForNotification(@Named("date") Date date);

    @Finder(query="select n from SmsNotification n where  n.notificationStatus='WAITING' AND n.massSms.active=true AND n.massSms.startDate<:now" )
    public List<SmsNotification> findForMassSmsNotification(@Named("now") Date now, @Limit int limit);


    @Finder(query="select n from SmsNotification n where  n.notificationStatus='WAITING' AND n.singleSms IS NOT NULL AND n.singleSms.startDate <:now" )
    public List<SmsNotification> findForSingleSmsNotification(@Named("now") Date now, @Limit int limit);


    @Finder(query="select n from SmsNotification n where  client=:client order by id desc" )
    public List<SmsNotification> findByClient(@Named("client") Client client,@Limit int limit);
}
