
package org.glasser.sql;


import org.glasser.util.*;


public class CharFieldFormatter implements Formatter {


    public final static CharFieldFormatter SINGLETON = new CharFieldFormatter();


    public String getFormattedString(Object obj) {
        if(obj == null) return "NULL";
        return "'" + DBUtil.escape(obj.toString()) + "'";
    }

}
