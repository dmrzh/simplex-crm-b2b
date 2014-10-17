package ru.dev_server.client.dao;

import net.sf.autodao.AutoDAO;
import net.sf.autodao.Dao;
import net.sf.autodao.Finder;
import net.sf.autodao.Named;
import ru.dev_server.client.model.SingleEmail;

import java.util.Date;
import java.util.List;

/**.*/
@AutoDAO
public interface SingleEmailDAO extends Dao<SingleEmail, Long>{
    @Finder(query = "select se from SingleEmail se where notificationStatus='WAITING' AND startDate<:now")
    public List<SingleEmail> findReadyToSend(@Named("now") Date now);
}
