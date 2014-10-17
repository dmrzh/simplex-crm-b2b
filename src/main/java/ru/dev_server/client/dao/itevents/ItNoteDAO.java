package ru.dev_server.client.dao.itevents;

import net.sf.autodao.Dao;
import net.sf.autodao.Finder;
import net.sf.autodao.Named;
import ru.dev_server.client.model.Employee;
import ru.dev_server.client.model.ItNote;
import ru.dev_server.client.model.JuridicalPerson;

import java.util.List;

/**
 * .
 */
public interface ItNoteDAO extends Dao<ItNote,Long> {
    @Finder(query = "from ItNote where author=:employee AND juridicalPerson=:juridicalPerson order by date desc")
    public List<ItNote> findByJuridicalPerson(@Named("juridicalPerson")JuridicalPerson juridicalPerson, @Named("employee")Employee employee);
}
