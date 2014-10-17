package ru.dev_server.client.dao;

import net.sf.autodao.AutoDAO;
import net.sf.autodao.Dao;
import net.sf.autodao.Finder;
import net.sf.autodao.Named;
import ru.dev_server.client.model.PasswordReset;

/**.*/
@AutoDAO
public interface PasswordResetDAO extends Dao<PasswordReset, Long> {
    @Finder(query="from PasswordReset where resetConfimation=:resetConfimation")
    public PasswordReset findByResetConfimation(@Named("resetConfimation") String resetConfimation);
}
