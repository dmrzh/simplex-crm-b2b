package ru.dev_server.client.dao;


import net.sf.autodao.AutoDAO;
import net.sf.autodao.Dao;
import net.sf.autodao.Finder;
import net.sf.autodao.Named;
import ru.dev_server.client.model.ClientDynamicValue;
import ru.dev_server.client.model.DynamicColumn;

import java.util.List;

/**. */
@AutoDAO
public interface ClientDynamicValueDAO extends Dao<ClientDynamicValue,Long> {
    @Finder(query="from ClientDynamicValue where dynamicColumn=:dynamicColumn" )
    public List<ClientDynamicValue> findByColumn(@Named("dynamicColumn") DynamicColumn dynamicColumn);

}
