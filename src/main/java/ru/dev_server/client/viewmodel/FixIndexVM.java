package ru.dev_server.client.viewmodel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * .
 */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class FixIndexVM {
    private static final Logger LOG= LoggerFactory.getLogger(FixIndexVM.class);
    @WireVariable
    private DataSource dataSource;

    @AfterCompose
    public void afterCompose(){
          onIndexFix();
    }

    public void onIndexFix() {
        Connection connection=null;
        try {
            connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate("update contact set index=10  where index is null");
        } catch (SQLException e) {
            LOG.error(e.getMessage(),e);
        }finally {
            try {
                connection.close();
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
        }

    }
}
