package org.glasser.qform;

import javax.swing.*;
import org.glasser.util.*;
import org.glasser.sql.*;
import org.glasser.swing.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.text.SimpleDateFormat;



public class InsertExportPanel extends ExportPanel  {



    private static final Formatter charFormatter =
        new CharFieldFormatter() {
            public String toString() {
                return "<Copy Value>";
            }
        };

    private static final Formatter numFormatter =
        new NumberFieldFormatter() {
            public String toString() {
                return "<Copy Value>";
            }
        };

    private static final Formatter nullFormatter =
        new Formatter() {

            public String getFormattedString(Object o) {
                return "NULL";
            }

            public String toString() {
                return "NULL";
            }
        };


    private static class TimestampFormatter implements Formatter {

        public final static int TIME_ESCAPE = 0;

        public final static int DATE_ESCAPE = 1;

        public final static int TIMESTAMP_ESCAPE = 2;

        public final static int TIME_STRING = 3;

        public final static int DATE_STRING = 4;

        public final static int TIMESTAMP_STRING = 5;

        private int formatStyle = TIMESTAMP_STRING;

        private final static SimpleDateFormat TIME_FORMATTER =
            new SimpleDateFormat("HH:mm:ss");

        private final static SimpleDateFormat DATE_FORMATTER =
            new SimpleDateFormat("yyyy-MM-dd");

        private final static SimpleDateFormat TIMESTAMP_FORMATTER =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        public TimestampFormatter() {}

        public TimestampFormatter(int formatStyle) {
            this.formatStyle = formatStyle;
        }



        public String getFormattedString(Object obj) {
            if(obj == null) return "NULL";

            java.util.Date date = (java.util.Date) obj;

            switch(formatStyle) {
                case TIME_ESCAPE :
                    return "{t '" + TIME_FORMATTER.format(date) + "'}";
                case DATE_ESCAPE :
                    return "{d '" + DATE_FORMATTER.format(date) + "'}";
                case TIMESTAMP_ESCAPE :
                    return "{ts '" + TIMESTAMP_FORMATTER.format(date) + "'}";
                case TIME_STRING :
                    return "'" + TIME_FORMATTER.format(date) + "'";
                case DATE_STRING :
                    return "'" + DATE_FORMATTER.format(date) + "'";
                default :
                    return "'" + TIMESTAMP_FORMATTER.format(date) + "'";

            }
        }

        public String toString() {

            switch(formatStyle) {
                case TIME_ESCAPE :
                    return "{t 'hh:mm:ss'}";
                case DATE_ESCAPE :
                    return "{d 'YYYY-MM-DD'}";
                case TIMESTAMP_ESCAPE :
                    return "{ts 'YYYY-MM-DD hh:mm:ss'}";
                case TIME_STRING :
                    return "'hh:mm:ss'";
                case DATE_STRING :
                    return "'YYYY-MM-DD'";
                default : // TIMESTAMP_STRING
                    return "'YYYY-MM-DD hh:mm:ss'";

            }
        }

    }


    private static final Object[] charChoices = {"", charFormatter, nullFormatter};

    private static final Object[] numChoices = {"", numFormatter, nullFormatter};

    private static final Object[] binChoices = {"", nullFormatter};

    private static final Object[] dateTimeChoices =
    {
        "" 
        ,nullFormatter
        ,new TimestampFormatter(TimestampFormatter.TIMESTAMP_STRING)
        ,new TimestampFormatter(TimestampFormatter.DATE_STRING)
        ,new TimestampFormatter(TimestampFormatter.TIME_STRING)
        ,new TimestampFormatter(TimestampFormatter.TIMESTAMP_ESCAPE)
        ,new TimestampFormatter(TimestampFormatter.DATE_ESCAPE)
        ,new TimestampFormatter(TimestampFormatter.TIME_ESCAPE)
    };




    public InsertExportPanel(TableInfo ti) {
        super(ti);

    }

    
    protected Object[] getFormatterChoices(int type) {
        if(DBUtil.isCharType(type)) {
            return charChoices;
        }
        else if(DBUtil.isNumericType(type)) {
            return numChoices;
        }
        else if(DBUtil.isDateTimeType(type)) {
            return dateTimeChoices;
        }
        else {
            return binChoices;
        }

        
    }

}
