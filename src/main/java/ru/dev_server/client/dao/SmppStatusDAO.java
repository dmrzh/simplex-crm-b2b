package ru.dev_server.client.dao;

import net.sf.autodao.AutoDAO;
import net.sf.autodao.Dao;
import net.sf.autodao.Finder;
import net.sf.autodao.Named;
import ru.dev_server.client.model.SmppStatus;

/**.*/
@AutoDAO
public interface SmppStatusDAO extends Dao<SmppStatus, Long>{


    @Finder(query="select distinct s from SmppStatus s  where  s.smppId=:smppId AND s.notification.sendTo=:to AND s.notification.sendFrom=:from" )
    public SmppStatus findBySmppId(@Named("smppId") Long smppId, @Named("from") String from, @Named("to") String to);
}
