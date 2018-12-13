

package org.glasser.sql;


public class DateFormatter implements org.glasser.util.Formatter {

    private java.text.SimpleDateFormat format = null;

    public DateFormatter(String formatString) {
        format = new java.text.SimpleDateFormat(formatString);
    }

    public String getFormattedString(Object obj) {
        if(obj == null) return "NULL";
        else return format.format(obj);
    }
}



