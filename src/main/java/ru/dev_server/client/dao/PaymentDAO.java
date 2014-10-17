package ru.dev_server.client.dao;

import net.sf.autodao.AutoDAO;
import net.sf.autodao.Dao;
import net.sf.autodao.Finder;
import net.sf.autodao.Named;
import ru.dev_server.client.model.Company;
import ru.dev_server.client.model.Payment;

import java.util.List;

/**.*/
@AutoDAO
public interface PaymentDAO extends Dao<Payment, Long> {

    @Finder(query="from Payment where company=:company order by startDate")
    public List<Payment> findAll(@Named("company")Company company);
}
