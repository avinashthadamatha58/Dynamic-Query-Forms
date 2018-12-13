package org.glasser.qform;


import org.glasser.util.*;
import org.glasser.sql.ForeignKeyColumn;
import org.glasser.sql.Column;
import org.glasser.swing.table.*;


/**
 * This ColumnManager is used for the Exported Key Columns table on an Exported Keys panel.
 */
public class ExportedKeyColumnManager extends AbstractColumnManager {


    protected final static String[] COLUMN_NAMES = {"Key Seq", "Foreign Column", "Local Column", "Data Type (Local)", "SQL Type (Local)"};

    protected final static Class[] COLUMN_CLASSES = {Integer.class, String.class, String.class, String.class, Integer.class};

    public ExportedKeyColumnManager() {
        super();
        setColumnNames(COLUMN_NAMES);
        setColumnClasses(COLUMN_CLASSES);
    }

    public Object getValueAt(int row, int column, Object rowObject) {

        ForeignKeyColumn fc = (ForeignKeyColumn) rowObject;
        if(fc == null) return null;
        Column lc = fc.getLocalColumn();
        switch(column) {
            case 0 :
                return new Integer(fc.getKeySeq());
            case 1 :
                return fc.getLocalColumnName();
            case 2 :
                return fc.getForeignColumnName();
            case 3 :
                return lc.getTypeName();
            case 4 :
                return new Integer(lc.getDataType());
            default :
                return null;
        }
    }
}
