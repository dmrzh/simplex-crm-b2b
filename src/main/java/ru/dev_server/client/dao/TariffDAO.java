package ru.dev_server.client.dao;

import net.sf.autodao.Dao;
import net.sf.autodao.Finder;
import ru.dev_server.client.model.Tariff;

import java.util.List;

/**.*/
public interface TariffDAO extends Dao<Tariff, Long> {

    @Finder(query="from Tariff where defaultTariff=true")
     public List<Tariff> findDefault();

}
