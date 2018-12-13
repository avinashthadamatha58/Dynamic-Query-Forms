package org.glasser.sql;


import org.glasser.util.*;


public class NumberFieldFormatter implements Formatter {



    public final static NumberFieldFormatter SINGLETON = new NumberFieldFormatter();


    public String getFormattedString(Object obj) {

        if(obj == null) return "NULL";
        return obj.toString();
        
    }
}
