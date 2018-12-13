package org.glasser.swing.table;


import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 * This is a MouseListener designed to detect mouse-clicks on  a JTableHeader and 
 * sort the corresponding JTable on the clicked column. It is designed to
 * trigger a sort only when the mouse is pressed and released within the bounds
 * of the same (view-based) column header. It does not trigger a sort if the user right-clicks
 * on the table header, or left-clicks the border between two column headers for
 * the purpose of resizing the column, or drags a column to a new location.<p>
 * 
 * This abstract base class only knows about the JTableHeader; subclasses should be created that know
 * how to cause the corresponding JTable to be sorted. Subclasses should override the sortTable() method, which
 * is called whenever the tableheader has been clicked in a way that would trigger the sort.
 * 
 * @author Dave Glasser
 */
public abstract class TableHeaderListener extends MouseAdapter implements java.io.Serializable {

    boolean descendingSort = false;

    int sortedColumn = -1;

    TableColumn resizingColumn = null;

    int pressedColumn = -1;


    public void mousePressed(MouseEvent e) {

        int modifiers = e.getModifiers();
        if((modifiers & e.BUTTON1_MASK) == 0) return;


        JTableHeader header = (JTableHeader) e.getSource();

        // if the user has clicked on the border between two columns
        // for the purpose of resizing one of them, this call will return non-null.
        // We'll hold a reference to let the mouseReleased event know that the
        // column is being resized, because getResizingColumn() always seems
        // to return null inside mouseReleased().
        resizingColumn = header.getResizingColumn();


        // save the view index of the column that was clicked on. This allows
        // us to detect in the release event whether or not the user has
        // dragged off the original column header or moved it to a new location.
        pressedColumn = header.columnAtPoint(e.getPoint());
    }

    /**
     * This is called when a mousepress that originated on the table header is
     * released.
     */
    public void mouseReleased(MouseEvent e) {

        int modifiers = e.getModifiers();
        if((modifiers & e.BUTTON1_MASK) == 0) return;


        // if the user has just finished resizing a column, do nothing.
        if(resizingColumn != null) {
            resizingColumn = null;
            return;
        }

        JTableHeader header = (JTableHeader) e.getSource();

        // if the user dragged off of the header before releasing the mouse
        // button, do nothing.
        if(header.contains(e.getPoint()) == false) return;

        // this is the view-based index of the clicked column, which may be
        // different from the actual model-based index, if the user has dragged
        // columns around.
        int viewColumn = header.columnAtPoint(e.getPoint());

		// when a JTable is in a JScrollPane, the table header can extend
		// beyond the columns in some cases.
		if(viewColumn < 0) return;

        // if the viewColumn on which the mouse was released doesn't equal
        // the same column on which the mouse was pressed, that means either that
        // the tableHeader doesn't allow reordering, and the user dragged
        // off of the column header before releasing the mouse, or the table
        // does allow reordering and the user dragged the column to a new location.
        // In either case we'll assume the user doesn't want to sort the table.
        if(viewColumn != pressedColumn) return;

        int modelColumn = header.getTable().convertColumnIndexToModel(viewColumn);

        // the default is ascending sort...
        boolean desc = false;

        // but if we're resorting on the same column, reverse the order.
        if(modelColumn == sortedColumn) desc = !descendingSort;

        // call the template method that knows how to sort the table.
        boolean wasSorted = this.sortTable( header, modelColumn, viewColumn, desc);


        if(wasSorted) {
            // update the variables
            sortedColumn = modelColumn;
            descendingSort = desc;
        }
    }


    /**
     * This is the method that knows how to sort the table in response to a column
     * header being clicked, and the implementation must be provided by a
     * subclass.
     * 
     * @param header the JTableHeader that was clicked upon to trigger a table sort.
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
    protected abstract boolean sortTable(JTableHeader header, 
                                      int modelColumn, 
                                      int viewColumn, 
                                      boolean sortDescending);




}
