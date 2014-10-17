package ru.dev_server.client.dao;


import net.sf.autodao.AutoDAO;
import net.sf.autodao.Dao;
import net.sf.autodao.Finder;
import net.sf.autodao.Named;
import ru.dev_server.client.model.Company;
import ru.dev_server.client.model.DynamicColumn;
import ru.dev_server.client.model.ExstendedTables;

import java.util.Date;
import java.util.List;

/**. */
@AutoDAO
public interface DynamicColumnDAO extends Dao<DynamicColumn,Long> {
    @Finder(query="from  DynamicColumn where company=:company" )
    public List< DynamicColumn> findAll(@Named("company") Company company);


    @Finder(query="from  DynamicColumn where company=:company and exstendedTables=:exstendedTables" )
    public List< DynamicColumn> findAll(@Named("company") Company company,@Named("exstendedTables")ExstendedTables exstendedTables);

    @Finder(query="from  DynamicColumn where company=:company and exstendedTables=:exstendedTables and showInList = TRUE" )
    public List< DynamicColumn> findToShow(@Named("company") Company company,@Named("exstendedTables")ExstendedTables exstendedTables);

     @Finder(query = "select sum(dv.doubleValue) from MeetingDynamicValue dv " +
             "where dv.dynamicColumn.name=:columnName  AND dv.dynamicColumn.company=:company " +
             " AND dv.meeting.beginDate>:startDate AND dv.meeting.beginDate<=:finishDate")
     public Double getMeetingReport(@Named("company") Company company, @Named("columnName") String columnName,
                                    @Named("startDate") Date startDate, @Named("finishDate") Date finishDate);

    @Finder(query = "select DAY(dv.meeting.beginDate), month(dv.meeting.beginDate), year(dv.meeting.beginDate), sum(dv.doubleValue) from MeetingDynamicValue dv " +
            "where dv.dynamicColumn.name=:columnName  AND dv.dynamicColumn.company=:company  " +
            " AND dv.meeting.beginDate>:startDate AND dv.meeting.beginDate<=:finishDate " +
            " group by DAY(dv.meeting.beginDate), month(dv.meeting.beginDate), year(dv.meeting.beginDate)")
    public List<Object[]> getMeetingDaylyReport(@Named("company") Company company, @Named("columnName") String columnName,
                                                @Named("startDate") Date startDate, @Named("finishDate") Date finishDate);



}
