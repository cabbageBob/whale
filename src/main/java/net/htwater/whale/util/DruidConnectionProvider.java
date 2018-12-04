package net.htwater.whale.util;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.Getter;
import lombok.Setter;
import org.quartz.SchedulerException;
import org.quartz.utils.ConnectionProvider;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author wangzhifan
 */
@Getter
@Setter
public class DruidConnectionProvider implements ConnectionProvider {
    public String driver;
    public String URL;
    public String user;
    public String password;
    public int maxConnection;
    public String validationQuery;
    private boolean validateOnCheckout;
    private int idleConnectionValidationSeconds;
    public String maxCachedStatementsPerConnection;
    private String discardIdleConnectionsSeconds;
    public static final int DEFAULT_DB_MAX_CONNECTIONS = 10;
    public static final int DEFAULT_DB_MAX_CACHED_STATEMENTS_PER_CONNECTION = 120;
    private DruidDataSource datasource;
    @Override
    public Connection getConnection() throws SQLException {
        return datasource.getConnection();
    }
    @Override
    public void shutdown() throws SQLException {
        datasource.close();
    }
    @Override
    public void initialize() throws SQLException{
        if (this.URL == null) {
            throw new SQLException("DBPool could not be created: DB URL cannot be null");
        }
        if (this.driver == null) {
            throw new SQLException("DBPool driver could not be created: DB driver class name cannot be null!");
        }
        if (this.maxConnection < 0) {
            throw new SQLException("DBPool max Connections could not be created: Max connections must be greater than zero!");
        }
        datasource = new DruidDataSource();
        try{
            datasource.setDriverClassName(this.driver);
        } catch (Exception e) {
            try {
                throw new SchedulerException("Problem setting driver class name on datasource: " + e.getMessage(), e);
            } catch (SchedulerException e1) {
            }
        }
        datasource.setUrl(this.URL);
        datasource.setUsername(this.user);
        datasource.setPassword(this.password);
        datasource.setMaxActive(this.maxConnection);
        datasource.setMinIdle(1);
        datasource.setMaxWait(0);
        datasource.setMaxPoolPreparedStatementPerConnectionSize(DEFAULT_DB_MAX_CONNECTIONS);
        if (this.validationQuery != null) {
            datasource.setValidationQuery(this.validationQuery);
            if(!this.validateOnCheckout) {
                datasource.setTestOnReturn(true);
            }
            else {
                datasource.setTestOnBorrow(true);
            }
            datasource.setValidationQueryTimeout(this.idleConnectionValidationSeconds);
        }
    }

}
