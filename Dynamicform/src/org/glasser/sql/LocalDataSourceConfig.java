
package org.glasser.sql;

import org.glasser.util.*;
import org.glasser.util.comparators.*;


public class LocalDataSourceConfig implements java.io.Serializable, Cloneable {



    public final static MethodComparator DISPLAY_NAME_COMPARATOR =
        new MethodComparator(org.glasser.sql.LocalDataSourceConfig.class, "getDisplayName");


    protected String displayName = null;

    protected String driverClassName = null;

    protected boolean loginRequired = false;    

    protected String url = null;

    protected String user = null;

    protected String password = null;

    protected Integer maxConnections = null;

    protected Integer loginTimeout = null;


    // setters

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public void setLoginRequired(boolean loginRequired) {
        this.loginRequired = loginRequired;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setMaxConnections(Integer maxConnections) {
        this.maxConnections = maxConnections;
    }

    public void setLoginTimeout(Integer loginTimeout) {
        this.loginTimeout = loginTimeout;
    }


    // getters

    public String getDisplayName() {
        return displayName;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public boolean isLoginRequired() {
        return loginRequired;
    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public Integer getMaxConnections() {
        return maxConnections;
    }

    public Integer getLoginTimeout() {
        return loginTimeout;
    }


    public String toString() {
        if(displayName == null) return "";
        return displayName;
    }

    public Object clone() {
        try {
            return super.clone();
        }
        catch(CloneNotSupportedException ex) {
            // shouldn't happen because this class is cloneable.
            throw new java.lang.UnknownError(getClass().getName() + ".clone(): Clone failed, when it shouldn't have!");
        }
    }

    public String debugString() {
        StringBuffer buffer = new StringBuffer(200);
        buffer.append(getClass().getName());
        buffer.append("[");
        buffer.append("displayName=");
        buffer.append(displayName);
        buffer.append(",driverClassName=");
        buffer.append(driverClassName);
        buffer.append(",loginRequired=");
        buffer.append(loginRequired);
        buffer.append(",url=");
        buffer.append(url);
        buffer.append(",user=");
        buffer.append(user);
        buffer.append(",password=");
        buffer.append(password);
        buffer.append(",maxConnections=");
        buffer.append(maxConnections);
        buffer.append(",loginTimeout=");
        buffer.append(loginTimeout);
    
        buffer.append("]");
        return buffer.toString();
    }













}
