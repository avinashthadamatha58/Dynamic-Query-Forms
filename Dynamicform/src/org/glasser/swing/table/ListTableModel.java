package org.glasser.swing.table;


import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.util.*;
import java.awt.event.*;
import java.beans.*;
import org.glasser.util.*;




public class ListTableModel extends AbstractTableModel
{

    protected int[] columnMappings = null;


    private ColumnManager columnManager = null;

    private ColumnManagerComparator comparator = null;

    /**
     * This indicates whether the last sort applied to the dataList was in 
     * descending order or not.
     */
    protected boolean descendingSort = false;

    /**
     * Index of the last column on which this table was sorted.
     */
    protected int sortColumn = -1;

    /**
     * This list holds the data for the table, and it is assumed that each
     * element in this list is analogous to one row in the table. The ColumnManager
     * knows, when given an object representing a row, how to get the value
     * for a particular column within that row.
     */
    protected List dataList = null;

    protected SmartEventListenerList listeners = new SmartEventListenerList();



    public void setColumnManager(ColumnManager columnManager) {
        this.columnManager = columnManager;
        if(columnManager != null) {
            comparator = new ColumnManagerComparator(columnManager);
        }
        else {
            comparator = null;
        }
    }


    public ColumnManager getColumnManager() {
        return columnManager;
    }


    public ListTableModel(ColumnManager columnManager, List data)
    {
        super();
        this.columnManager = columnManager;
        if(columnManager != null) {
            comparator = new ColumnManagerComparator(columnManager);
        }
        else {
            comparator = null;
        }
        setDataList(data);

    }

    public ListTableModel()
    {
        super();
        dataList = new Vector(0);
    }


    public int getColumnCount()
    {
        if(columnManager != null) {
            if(columnMappings != null) return columnMappings.length;
            else return columnManager.getColumnCount();
        }
        else {
            return 0;
        }
    }

    public String getColumnName(int columnIndex)
    {
        if(columnManager != null) {
            return columnManager.getColumnName(getMappedColumnIndex(columnIndex));
        }
        else {
            return null;
        }

    }

    public List getDataList()
    {
        return dataList;
    }

    public synchronized void setDataList(List newData)
    {
        if (newData != null)
        {
            this.dataList = newData;
            fireTableDataChanged();
        }
        else
        {
            int rows = getRowCount();
            this.dataList = new Vector(0);
            if (rows > 0)
            {
                fireTableRowsDeleted(0, rows - 1);
            }
        }
    }

    public int getRowCount()
    {
        if (dataList != null) {
            return dataList.size();
        }
        else {
            return 0;
        }
    }

    public Object getObjectAtRow(int row) {
        if(row < 0 || row >= dataList.size()) return null;
        return dataList.get(row);
    }

    public void addObject(Object obj) {
        if(obj == null) return;
        if(dataList == null) dataList = new Vector();
        dataList.add(obj);
        int newRowIndex = dataList.size() - 1;
        fireTableRowsInserted(newRowIndex, newRowIndex);
        fireDataObjectInserted(obj);
    }

    public synchronized void replaceObject(Object oldObject, Object newObject) {
        if(oldObject == null) return;
        int index = dataList.indexOf(oldObject);
        if(index < 0 || index >= dataList.size()) return;
        dataList.set(index, newObject);
        fireTableDataChanged();
        fireDataObjectDeleted(oldObject);
        fireDataObjectInserted(newObject);
    }

    public void removeObject(Object obj) {
        int rowToRemove = dataList.indexOf(obj);
        if(rowToRemove < 0 || rowToRemove > (getRowCount() - 1)) return;
        dataList.remove(obj);
        fireTableRowsDeleted(rowToRemove, rowToRemove);
        fireDataObjectDeleted(obj);

    }

    public void removeObjectAtRow(int rowToRemove) {
        if(rowToRemove < 0 || rowToRemove > (getRowCount() - 1)) return;
        Object obj = dataList.get(rowToRemove);
        if(obj == null) return;
        dataList.remove(rowToRemove);
        fireTableRowsDeleted(rowToRemove, rowToRemove);
        fireDataObjectDeleted(obj);
    }



    public boolean isCellEditable(int row, int column)
    {
        if(columnManager != null) {
            return columnManager.isCellEditable(row,
                                                getMappedColumnIndex(column),
                                                getObjectAtRow(row));
        }
        else {
            return false;
        }

    }


    /**
     * Swaps the indicated table row with the one immediately below it. If
     * it is the bottom row in the table, or out of bounds, nothing happens.
     */
    public void moveRowDown(int row) {
        if(dataList == null || row < 0 || row >= (dataList.size() - 1)) return;

        int upperRow = row;
        int lowerRow = row + 1;

        Object upperObj = getObjectAtRow(upperRow);
        Object lowerObj = getObjectAtRow(lowerRow);

        dataList.set(upperRow, lowerObj);
        dataList.set(lowerRow, upperObj);

        this.fireTableRowsUpdated(upperRow, lowerRow);
    }

    /**
     * Swaps the indicated table row with the one immediately above it. If
     * it is the top row in the table, or out of bounds, nothing happens.
     */
    public void moveRowUp(int row) {
        if(dataList == null || row < 0 || row >= (dataList.size() - 1)) return;

        int upperRow = row - 1;
        int lowerRow = row;

        Object upperObj = getObjectAtRow(upperRow);
        Object lowerObj = getObjectAtRow(lowerRow);

        dataList.set(upperRow, lowerObj);
        dataList.set(lowerRow, upperObj);

        this.fireTableRowsUpdated(upperRow, lowerRow);
    }

    /**
     * Inserts the given object into the data list at the given row.
     * 
     * @throws UnsupportedOperationException if the underlying List does not
     * support the addAll() operation.
     */
    public void insertObjectAtRow(Object obj, int row) {
        if(obj == null || row < 0 ||  dataList == null || row > dataList.size()) return;
        ArrayList a = new ArrayList(1);
        a.add(obj);
        dataList.addAll(row, a);
        fireTableRowsInserted(row, row);
        fireDataObjectInserted(obj);
    }


    public java.lang.Class getColumnClass(int column)
    {
        if(columnManager != null) {
            return columnManager.getColumnClass(getMappedColumnIndex(column));
        }
        else {
            return java.lang.Object.class;
        }
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        if(columnManager != null) {
            return columnManager.getValueAt(rowIndex,
                                            getMappedColumnIndex(columnIndex),
                                            getObjectAtRow(rowIndex));
        }
        else {
            return null;
        }
    }


    public void fireTableDataChanged() {
        super.fireTableDataChanged();
    }


    public void setColumnMappings(int[] columnMappings) {
        if(columnMappings != null) {
            this.columnMappings = (int[]) columnMappings.clone();
        }
        else {
            this.columnMappings = null;
        }
    }


    public int[] getColumnMappings() {
        if(columnMappings == null) return null;
        return (int[]) columnMappings.clone();
    }

    public int getMappedColumnIndex(int columnIndex) {
        if(columnMappings == null) return columnIndex;
        return columnMappings[columnIndex];
    }


    public void addListTableModelListener(ListTableModelListener listener) {
        listeners.add(org.glasser.swing.table.ListTableModelListener.class, listener);
    }

    public void removeListTableModelListener(ListTableModelListener listener) {
        listeners.remove(org.glasser.swing.table.ListTableModelListener.class, listener);
    }


    private void fireDataObjectInserted(Object rowObject) {
        ListTableModelEvent event = new ListTableModelEvent(this, rowObject, ListTableModelEvent.INSERT);
        listeners.fireSmartEvent(event);
    }

    private void fireDataObjectDeleted(Object rowObject) {
        ListTableModelEvent event = new ListTableModelEvent(this, rowObject, ListTableModelEvent.DELETE);
        listeners.fireSmartEvent(event);
    }

    private void fireDataObjectUpdated(Object rowObject) {
        ListTableModelEvent event = new ListTableModelEvent(this, rowObject, ListTableModelEvent.UPDATE);
        listeners.fireSmartEvent(event);
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
        if(columnManager == null || dataList == null) return;
        comparator.setSortColumn(columnIndex);
        comparator.setSortDescending(sortDescending);
        Collections.sort(dataList, comparator);
        this.fireTableDataChanged();
    }

    /**
     * Sorts the buffer on the indicated column, and returns true if the sort was in
     * descending order, or false if it was in ascending order. The order
     * will usually be ascending, unless the same column is sorted multiple times
     * consecutively, in which case the sort order is reversed each time.
     */
    public boolean sort(int columnIndex) {
        boolean desc = false;

        // if the column to be sorted is the same as the last column
        // that was sorted, then reverse the last sort order.
        if(this.sortColumn == columnIndex) desc = !descendingSort;

        sort(columnIndex, desc);

        return desc;

    }

    public void setValueAt(Object newCellValue,  int rowIndex,  int columnIndex) {
        Object rowObject = dataList.get(rowIndex);
        columnManager.setValueAt(newCellValue, rowIndex, columnIndex,  rowObject);
        fireTableDataChanged();
        fireDataObjectUpdated(rowObject);
    }

    /**
     * This method is used to set a particular Comparator that will be used when 
     * sorting on a particular column. It is especially useful for sorting String columns
     * in case-insensitive order, by passing String.CASE_INSENSITIVE_ORDER to this method.
     * 
     * @throws RuntimeException, if the ColumnManager for this ListTableModel is set to null.
     */
    public void setColumnComparator(int columnIndex, Comparator columnComparator) {
        if(columnManager == null) {
            throw new RuntimeException("ListTableModel.setColumnComparator() cannot be called until a ColumnManager has been set.");
        }
        comparator.setColumnComparator(columnIndex, columnComparator);
    }

}
