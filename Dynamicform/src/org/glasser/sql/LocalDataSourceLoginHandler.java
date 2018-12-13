package org.glasser.sql;



import java.sql.*;
import org.glasser.swing.*;



public class LocalDataSourceLoginHandler implements LoginHandler {


    private LocalDataSourceConfig config = null;

    public Object login(String userName, String password) throws LoginHandlerException {

        LocalDataSourceConfig temp = config;
        if(temp == null) throw new RuntimeException("No local datasource configuration has been provided.");

        try {
            return DataSourceManager.getLocalDataSource(temp,userName,password);
        }
        catch(ClassNotFoundException ex) {
            ex.printStackTrace();
            throw new LoginHandlerException("The JDBC driver class ("
                + temp.getDriverClassName() + ") was not found in the classpath.");
        }
        catch(Exception ex) {
            ex.printStackTrace();
            throw new LoginHandlerException(ex.getMessage());
        }
        
    }

    public LocalDataSourceLoginHandler(LocalDataSourceConfig config) {
        this.config = config;
    }

    
    public void setConfig(LocalDataSourceConfig config) {
        this.config = config;
    }

    public LocalDataSourceConfig getConfig() {
        return config;
    }

}
