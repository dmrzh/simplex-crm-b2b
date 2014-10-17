package ru.dev_server.client.dao;

import net.sf.autodao.AutoDAO;
import net.sf.autodao.Dao;
import ru.dev_server.client.model.Contact;

/**.*/
@AutoDAO
public interface ContactDAO extends Dao<Contact, Long>{
}
