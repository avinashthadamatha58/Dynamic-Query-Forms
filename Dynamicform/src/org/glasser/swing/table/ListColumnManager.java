package org.glasser.swing.table;




import javax.swing.*;
import java.util.*;

public class ListColumnManager extends AbstractColumnManager {


    public ListColumnManager(String[] columnNames, Class[] columnClasses) {
        super(columnNames, columnClasses);
    }


    public void setValueAt(Object newCellValue, int rowIndex, int columnIndex, Object rowObject) {
        ((List) rowObject).set(columnIndex, newCellValue);
    }


    public Object getValueAt(int rowIndex, int columnIndex, Object rowObject) {
        if(rowObject == null) return null;
        return ((List) rowObject).get(columnIndex);
    }

}



