package org.glasser.qform;


import org.glasser.util.*;
import org.glasser.sql.ForeignKeyColumn;
import org.glasser.sql.Column;
import org.glasser.swing.table.*;

/**
 * This ColumnManager is used for the Foreign Key Columns table on a Foreign Keys panel.
 */
public class ForeignKeyColumnManager extends AbstractColumnManager {


    protected final static String[] COLUMN_NAMES = {"Key Seq", "Local Column", "Data Type", "SQL Type", "Nullable", "Foreign Column"};

    protected final static Class[] COLUMN_CLASSES = {Integer.class, String.class, String.class, Integer.class, Boolean.class, String.class};

    public ForeignKeyColumnManager() {
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
                return lc.getTypeName();
            case 3 :
                return new Integer(lc.getDataType());
            case 4 :
                if(lc.getNullable()) return Boolean.TRUE;
                else return Boolean.FALSE;
            case 5 :
                return fc.getForeignColumnName();

            default :
                return null;
        }
    }
}
