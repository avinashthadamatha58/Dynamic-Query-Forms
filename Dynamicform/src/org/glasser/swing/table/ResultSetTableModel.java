package org.glasser.swing.table;

import org.glasser.sql.*;
import java.util.*;
import org.glasser.util.*;


public class ResultSetTableModel extends ListTableModel implements ResultSetBufferListener {


    public ResultSetTableModel(ResultSetBuffer buffer) {
        super(new ListColumnManager(buffer.getColumnNames(), buffer.getColumnClasses()), buffer);
        buffer.addResultSetBufferListener(this);
    }


    /**
     * This constructor allows an instance to be constructed before any query
     * results are available to display.
     */
    public ResultSetTableModel(String[] columnNames) {
        super();
        super.setColumnManager(new ListColumnManager(columnNames, null));
    }

    public void setColumnManager(ColumnManager columnManager) {
        throw new UnsupportedOperationException("The ColumnManager cannot be set for this TableModel.");
    }


    private boolean[] displayableColumns = null;


    public void setDataList(List newData) {
//        if(newData instanceof ResultSetBuffer == false) {
//            throw new IllegalArgumentException("newData argument must be a ResultSetBuffer.");
//        }

        List currentBuffer = (List) this.getDataList();
        if(currentBuffer != null && currentBuffer instanceof ResultSetBuffer) {
            ((ResultSetBuffer) currentBuffer).removeResultSetBufferListener(this);
        }
        if(newData == null) {
            super.setDataList(null);
            return;
        }
        ResultSetBuffer buffer = (ResultSetBuffer) newData;
        buffer.addResultSetBufferListener(this);
        
        ListColumnManager listColumnManager = (ListColumnManager) super.getColumnManager();

        if(listColumnManager != null) {
            listColumnManager.setColumnNames(buffer.getColumnNames());
            listColumnManager.setColumnClasses(buffer.getColumnClasses());
        }
        super.setDataList(newData);

    }

    protected ResultSetBuffer getResultSetBuffer() {
        List list = this.getDataList();
        if(list instanceof ResultSetBuffer) {
            return (ResultSetBuffer) list;
        }
        else {
            return null;
        }

    }


    /**
     * Sorts the model's contents on the given column.
     * 
     * @param columnIndex the zero-based index of the column on which the data
     * will be sorted.
     * 
     * @param sortDescending if true, the data is sorted in descending order, otherwise
     * it will be sorted in ascending order.
     */
    public void sort(int columnIndex, boolean sortDescending) {
        ResultSetBuffer buffer = getResultSetBuffer();
        if(buffer == null) return;
        buffer.sort(this.getMappedColumnIndex(columnIndex), sortDescending);
    }

    /**
     * Sorts the buffer on the indicated column, and returns true if the sort was in
     * descending order, or false if it was in ascending order. The order
     * will usually be ascending, unless the same column is sorted multiple times
     * consecutively, in which case the sort order is reversed each time.
     */
    public boolean sort(int columnIndex) {
        ResultSetBuffer buffer = getResultSetBuffer();
        if(buffer == null) return false;
        return buffer.sort(getMappedColumnIndex(columnIndex));
    }


    public void moreRowsRead(ResultSetBufferEvent e) {
        fireTableDataChanged();
    }

    public void endOfResultsReached(ResultSetBufferEvent e) {

    }

    public void bufferSorted(ResultSetBufferEvent e) {
        fireTableDataChanged();
    }
}
