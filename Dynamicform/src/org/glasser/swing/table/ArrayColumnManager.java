package org.glasser.swing.table;



import javax.swing.*;
import java.util.*;
import java.awt.*;

/**
 * This is a ColumnManager implementation that assumes each row of a table
 * is contained in an Object array, with each element of the array mapping to the
 * corresponding column of the table.
 * 
 * @author Dave Glasser
 */
public class ArrayColumnManager extends AbstractColumnManager {


    public ArrayColumnManager(String[] columnNames, Class[] columnClasses) {
        super(columnNames, columnClasses);
    }


    /**
     * The rowObject passed into this method is assumed to be an object array that contains
     * the data for a single row of the table.
     */
    public void setValueAt(Object newCellValue, int rowIndex, int columnIndex, Object rowObject) {
        ((Object[]) rowObject)[columnIndex] = newCellValue;
    }

    /**
     * The rowObject passed into this method is assumed to be an object array that contains
     * the data for a single row of the table.
     */
    public Object getValueAt(int rowIndex, int columnIndex, Object rowObject) {
        Object[] row = (Object[]) rowObject;
        if(row == null || row.length <= columnIndex) return null;
        return row[columnIndex];
    }


    /**
     * Launches a small program demonstrating the use of this class.
     */
    public static void main(String[] args) throws Exception {

        int numRows = 20;
        int numCols = 5;

        String[] colNames = new String[numCols];
        for(int j=0; j<numCols; j++) {
            colNames[j] = "** " + j + " **";
        }

        // each element in this list represents a row in the table,
        // and will be an Object array.
        ArrayList list = new ArrayList();

        for(int j=0; j<numRows; j++) {
            // create the array that will be the row.
            Object[] rowArray = new Object[numCols];
            for(int k=0; k<numCols; k++) {
                rowArray[k] = "Row " + j + ", Col " + k;
            }
            list.add(rowArray);
        }

        // instantiate the ColumnManager
        ArrayColumnManager colMgr = new ArrayColumnManager(colNames, null);

        // instantiate the ListTableModel
        ListTableModel model = new ListTableModel(colMgr, list);

        JFrame frame = new JFrame();
        JPanel cp = new JPanel();
        cp.setLayout(new BorderLayout());
        JTable table = new JTable(model);
        cp.add(new JScrollPane(table), BorderLayout.CENTER);
        frame.setContentPane(cp);
        frame.pack();
        frame.setDefaultCloseOperation(3);
        frame.setVisible(true);


    } 
    

}
