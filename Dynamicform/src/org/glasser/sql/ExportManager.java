package org.glasser.sql;



import org.glasser.util.Formatter;
import org.glasser.util.Util;
import java.util.HashMap;
import java.util.List;
import java.util.Arrays;
import java.io.*;
import java.sql.*;



public class ExportManager {



    private static int lastInsertStatementLength = 100;


    public static String buildInsertStatement(List rowData, String tableName, String[] columnNames, Formatter[] formatters) {
        return buildInsertStatement(rowData, tableName, columnNames, formatters, false);
    }

    private final static String ENDL = System.getProperty("line.separator");

    private final static String BROKEN_VALUES = ") " + ENDL + "VALUES(";

    public static String buildInsertStatement(List rowData, String tableName, String[] columnNames, Formatter[] formatters, boolean foldLine) {

        StringBuffer buffer = new StringBuffer(lastInsertStatementLength + 50);

        
        buffer.append("INSERT INTO ");
        buffer.append(tableName);
        buffer.append(" (");
        boolean needComma = false;
        for(int j=0; j<columnNames.length; j++) { 
            String columnName = columnNames[j];
            if(columnName == null) continue;
            if(needComma) {
                buffer.append(", ");
            }
            else {
                needComma = true;
            }
            buffer.append(columnName);
        }
        if(foldLine) {
            buffer.append(BROKEN_VALUES);
        }
        else {
            buffer.append(") VALUES (");
        }

        needComma = false;
        for(int j=0; j<columnNames.length; j++) { 
            String columnName = columnNames[j];
            if(columnName == null) continue;
            if(needComma) {
                buffer.append(", ");
            }
            else {
                needComma = true;
            }

            Formatter formatter = formatters[j];

            if(formatters[j] == null) {
                buffer.append("NULL");
            }
            else {
                buffer.append(formatter.getFormattedString(rowData.get(j)));
            }
        }
        buffer.append(")");

        String s = buffer.toString();
        lastInsertStatementLength = Math.max(lastInsertStatementLength, 100);
        return s;
    }


    private final static Formatter nullFormatter = new LiteralFormatter("NULL");

    private final static Object[][] dateFormatMappings = 
    {
         {new Integer(Types.DATE), "'{d' ''yyyy-MM-dd'''}'"}
        ,{new Integer(Types.TIME), "'{t' ''HH:mm:ss'''}'"}
        ,{new Integer(Types.TIMESTAMP), "'{ts' ''yyyy-MM-dd HH:mm:ss'''}'"}
    };

    private final static HashMap defaultDateFormatMap =
        Util.buildMap(dateFormatMappings);


    public static void exportResultSet(PrintWriter writer,
                                       ResultSet resultSet, 
                                       String tableName, 
                                       String lineTerminal,
                                       HashMap typeMap,
                                       HashMap dateFormatMap,
                                       HashMap columnNameMap,
                                       Character openQuoteChar,
                                       Character closeQuoteChar,
                                       boolean foldLines,
                                       int maxRows) 
        throws SQLException, IOException  {

        char openQuote = '"', closeQuote = '"';
        if(openQuoteChar != null) openQuote = openQuoteChar.charValue();
        if(closeQuoteChar != null) closeQuote = closeQuoteChar.charValue();

        if(dateFormatMap == null) {
            dateFormatMap = defaultDateFormatMap;
        }

        ResultSetMetaData rsmd = resultSet.getMetaData();
        int colCount = rsmd.getColumnCount();
        String[] columnNames = new String[colCount];
        Formatter[] formatters = new Formatter[colCount];
        for(int j=0; j<colCount; j++) {

            columnNames[j] = rsmd.getColumnName(j+1);
            if(columnNames[j] == null || columnNames[j].trim().length() == 0) {
                columnNames[j] = "@COLUMN-" + (j+1) + "@";
            }
            if(columnNameMap != null) {
                String tmp = (String) columnNameMap.get(columnNames[j]);
                if(tmp != null) columnNames[j] = tmp;
            }
            if(columnNames[j].indexOf(" ") > -1) {
                columnNames[j] = openQuote + columnNames[j] + closeQuote;
            }

            Integer colType = new Integer(rsmd.getColumnType(j+1));
            if(typeMap != null) {
                Integer tmp = (Integer) typeMap.get(colType);
                if(tmp != null) colType = tmp;
            }
            int sqlType = colType.intValue();

            if(DBUtil.isCharType(sqlType)) {
                formatters[j] = CharFieldFormatter.SINGLETON;
            }
            else if(DBUtil.isNumericType(sqlType)) {
                formatters[j] = CharFieldFormatter.SINGLETON;
            }
            else if(DBUtil.isDateTimeType(sqlType)) {
                String formatString = (String) dateFormatMap.get(colType);
                if(formatString == null) formatString = (String) defaultDateFormatMap.get(colType);
                formatters[j] = new DateFormatter(formatString);
            }
            else {
                formatters[j] = nullFormatter;
            }
        }

        List[] rows = DBUtil.readResultSet2(resultSet, maxRows);

        exportInsertStatements(writer, 
                               Arrays.asList(rows),
                               null,
                               tableName,
                               columnNames,
                               formatters,
                               lineTerminal,
                               foldLines);
    }


    public static void exportInsertStatements(PrintWriter writer,
                                              List resultSet, 
                                              int[] rowsToExport, 
                                              String tableName, 
                                              String[] columnNames, 
                                              Formatter[] formatters,
                                              String lineTerminal) throws IOException {
        exportInsertStatements(writer, resultSet, rowsToExport, tableName, columnNames, formatters, lineTerminal, false);
    }



    public static void exportInsertStatements(PrintWriter writer,
                                              List resultSet, 
                                              int[] rowsToExport, 
                                              String tableName, 
                                              String[] columnNames, 
                                              Formatter[] formatters,
                                              String lineTerminal,
                                              boolean foldLines) 
        throws IOException
    {

        int limit = resultSet.size();

        if(rowsToExport != null) {
            limit = rowsToExport.length;
        }

        for(int j=0; j<limit; j++) {

            int rowToExport = j;
            if(rowsToExport != null) {
                rowToExport = rowsToExport[j];
            }

            String insertStatement = buildInsertStatement((List) resultSet.get(rowToExport),tableName,columnNames,formatters,foldLines);
            if(lineTerminal == null) {
                writer.println(insertStatement);
            }
            else {
                writer.print(insertStatement);
                writer.print(lineTerminal);
                writer.println();
            }
        }
    }

    private static int lastDelimitedRecordLength = 100;

    public static String buildDelimitedRecord(List rowData, Formatter[] formatters, String delimiter) {

        StringBuffer buffer = new StringBuffer(lastDelimitedRecordLength + 50);
        boolean firstFieldWritten = false;
        for(int j=0; j<formatters.length; j++) {
            if(formatters[j] == null) continue;
            if(firstFieldWritten) {
                buffer.append(delimiter);
            }
            else {
                firstFieldWritten = true;
            }
            buffer.append(formatters[j].getFormattedString(rowData.get(j)));
        }

        String s = buffer.toString();
        lastDelimitedRecordLength = Math.max(100, s.length());
        return s;
    }

    public static void exportDelimited(PrintWriter writer,
                                 List resultSet, 
                                 int[] rowsToExport, 
                                 String delimiter, 
                                 String[] columnHeaders, 
                                 Formatter[] formatters,
                                 String lineTerminal) 
        throws IOException
    {


        // if the columnHeaders array is non-null, write a header record.
        if(columnHeaders != null) {
            StringBuffer buffer = new StringBuffer(200);
            boolean firstFieldWritten = false;
            for(int j=0; j<formatters.length; j++) {
                if(formatters[j] == null) continue;
                if(firstFieldWritten) {
                    buffer.append(delimiter);
                }
                else {
                    firstFieldWritten = true;
                }
                buffer.append(columnHeaders[j]);
            }
            String header = buffer.toString();
            if(lineTerminal == null) {
                writer.println(header);
            }
            else {
                writer.print(header);
                writer.println(lineTerminal);
            }
        }


        // now write the rows to be exported
        int limit = resultSet.size();

        if(rowsToExport != null) {
            limit = rowsToExport.length;
        }

        for(int j=0; j<limit; j++) {

            int rowToExport = j;
            if(rowsToExport != null) {
                rowToExport = rowsToExport[j];
            }

            String record = buildDelimitedRecord((List) resultSet.get(rowToExport), formatters, delimiter);
            if(lineTerminal == null) {
                writer.println(record);
            }
            else {
                writer.print(record);
                writer.println(lineTerminal);
            }
        }
    }


}
