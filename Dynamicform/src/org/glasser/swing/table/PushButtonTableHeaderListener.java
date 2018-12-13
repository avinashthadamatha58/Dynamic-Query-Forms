package org.glasser.swing.table;


import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.*;

/**
 * This is a version of TableHeaderListener that's meant to be used with a
 * PushButtonTableHeader. It will handle updating the up/down arrows on the
 * PushButtonTableHeader when the table is sorted on various columns.<p>
 * 
 * Subclasses should override the sortTable() method that takes a PushButtonTableHeader
 * argument instead of a JTableHeader argument.
 * 
 * @author Dave Glasser
 */
public abstract class PushButtonTableHeaderListener extends TableHeaderListener {


    /**
     * See the Javadoc for TableHeaderListener for an explanation of this method. This method
     * has been made final, so superclasses should override the sortTable() method that takes
     * a PushButtonTableHeader argument.
     */
    protected final boolean sortTable(JTableHeader header, 
                              int modelColumn, 
                              int viewColumn, 
                              boolean sortDescending) {

        PushButtonTableHeader pushButtonTableHeader = (PushButtonTableHeader) header;

        boolean wasSorted = sortTable(pushButtonTableHeader,
                                      modelColumn,
                                      viewColumn,
                                      sortDescending);

        // if a sort occurred, update the up/down arrows on the PushButtonTableHeader.
        if(wasSorted) {
            // notice here that the table header is given the _view-based_ column index
            // that was sorted. Table models ususally care about the model-based
            // index when the sort the model.
            pushButtonTableHeader.setSortedColumn(viewColumn,  sortDescending);
        }

        return wasSorted;

    }


    /**
     * This is the method that knows how to sort the table in response to a column
     * header being clicked, and the implementation must be provided by a
     * subclass.
     * 
     * @param header the PushButtonTableHeader that was clicked upon to trigger a table sort.
     * @param the column index, based on the TableModel, of the column that was clicked. This 
     *        is likely the index that implementations of this method will use to sort
     *        a table.
     * @param viewColumn the column index of the clicked column, from the user's, or the
     *        "view" perspective. This may differ from the modelColumn if the user has 
     *        rearranged the column order by dragging them with the mouse.
     * @param sortDescending if true, the table should be sorted in descending order, if
     *        false, the table should be sorted in ascending order.
     * 
     * @return a boolean indicating whether or not the table was sorted. This allows the sort to
     *        be "vetoed."
     */
    protected abstract boolean sortTable(PushButtonTableHeader header, 
                              int modelColumn, 
                              int viewColumn, 
                              boolean sortDescending);





}
