package org.glasser.swing.table;

import javax.swing.table.*;
import javax.swing.*;

/**
 * This class provides the linkage between a PushButtonTableHeader and a ListTableModel.
 * It should be added as a mouse listener to a PushButtonTableHeader which has been
 * installed in a JTable that also uses a ListTableModel.
 */
public class ListTableModelSorter extends PushButtonTableHeaderListener {


    /**
     * This is the method that knows how to sort the table in response to a column
     * header being clicked, and the implementation must be provided by a
     * subclass.
     */
    protected boolean sortTable(PushButtonTableHeader header, 
        int modelColumn, 
        int viewColumn, 
        boolean sortDescending) {

        JTable table = header.getTable();
        if(table == null) return false;

        ListTableModel model = (ListTableModel) table.getModel();
        if(model == null || model.getRowCount() == 0) return false;

        model.sort(modelColumn, sortDescending);

        return true;
    }

}
