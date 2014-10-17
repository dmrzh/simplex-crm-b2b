package ru.dev_server.client.dao;


import net.sf.autodao.AutoDAO;
import net.sf.autodao.Dao;
import net.sf.autodao.Finder;
import net.sf.autodao.Named;
import ru.dev_server.client.model.ClientDynamicValue;
import ru.dev_server.client.model.DynamicColumn;
import ru.dev_server.client.model.MeetingDynamicValue;

import java.util.List;

/**. */
@AutoDAO
public interface MeetingDynamicValueDAO extends Dao<MeetingDynamicValue,Long> {
    @Finder(query="from MeetingDynamicValue where dynamicColumn=:dynamicColumn" )
    public List<MeetingDynamicValue> findByColumn(@Named("dynamicColumn") DynamicColumn dynamicColumn);

}
