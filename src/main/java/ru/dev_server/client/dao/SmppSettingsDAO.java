package ru.dev_server.client.dao;

import net.sf.autodao.AutoDAO;
import net.sf.autodao.Dao;
import net.sf.autodao.Finder;
import ru.dev_server.client.model.SmppSettings;

import java.util.List;

/**.*/
@AutoDAO
public interface SmppSettingsDAO extends Dao<SmppSettings, Long> {


    @Finder(query="from SmppSettings order by id")
    public List<SmppSettings> findAll();

}
