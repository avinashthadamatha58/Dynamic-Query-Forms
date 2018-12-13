package org.glasser.sql;


public interface LoginHandler {

    public Object login(String userName, String password) 
        throws LoginHandlerException;

}
