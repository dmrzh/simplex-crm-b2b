package ru.dev_server.client.dao;

import net.sf.autodao.Dao;
import net.sf.autodao.Finder;
import net.sf.autodao.Limit;
import net.sf.autodao.Named;
import net.sf.autodao.Offset;
import ru.dev_server.client.model.Company;
import ru.dev_server.client.model.MassSms;

import java.util.List;

/**.*/
public interface MassSmsDAO extends Dao<MassSms, Long> {
    @Finder(query = "select ms, ms.smsNotifications.size , " +
            " (select count(sn) from SmsNotification sn where sn.massSms=ms AND sn.notificationStatus='WAITING'), " +
            " (select count(sn) from SmsNotification sn where sn.massSms=ms AND sn.notificationStatus='ERROR') " +
            " from  MassSms ms " +
            " WHERE ms.company=:company AND deleted=false " +
            " ORDER BY ms.startDate desc"
    )
    public List<Object[]> findAll(@Named("company") Company company,@Offset int offset, @Limit int limit);

}
