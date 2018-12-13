
package org.glasser.sql;


import java.sql.*;
import javax.sql.*;
import java.util.*;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.*;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;

import org.glasser.sql.LocalDataSourceConfig;
import org.glasser.util.ExtensionClassLoader;


public class DataSourceManager {


    public static DataSource getLocalDataSource(LocalDataSourceConfig config, String userName, String password)
        throws ClassNotFoundException, Exception
    {

        Driver driver = null;
        Class driverClass = ExtensionClassLoader.getSingleton().loadClass(config.getDriverClassName());
        driver = (Driver) driverClass.newInstance();

        if(userName == null) userName = config.getUser();
        if(password == null) password = config.getPassword();

        int maxConnections = 1000;
        Integer n = config.getMaxConnections();
        if(n != null) maxConnections = n.intValue();

        Properties props = new Properties();
        if(userName != null) {
            props.setProperty("user", userName);
        }
        if(password != null) {
            props.setProperty("password", password);
        }

        if(driver.acceptsURL(config.getUrl()) == false) {
            throw new RuntimeException("The URL \"" + config.getUrl()
                + "\" is invalid for the JDBC driver class " + driverClass.getName() + ".");
        }

        DriverConnectionFactory connFactory = new DriverConnectionFactory(driver, config.getUrl(), props); 
        
        GenericObjectPool genericPool = new GenericObjectPool(null, maxConnections);
        genericPool.setWhenExhaustedAction(genericPool.WHEN_EXHAUSTED_FAIL);
//        genericPool.setMaxWait(1);
        PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(connFactory, genericPool,null, null, false, true);
        PoolingDataSource dataSource = new PoolingDataSource(genericPool);
        return dataSource;
    }


}

