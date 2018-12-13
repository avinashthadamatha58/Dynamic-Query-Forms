package org.glasser.swing.table;


import javax.swing.table.*;





public abstract class AbstractColumnManager implements ColumnManager {



    protected String[] columnNames = null;

    protected Class[] columnClasses = null;

    protected boolean[] editableColumns = null;


    protected void setColumnNames(String[] columnNames) {
        this.columnNames = columnNames;
    }

    protected void setColumnClasses(Class[] columnClasses) {
        this.columnClasses = columnClasses;
    }

    public void setEditableColumns(boolean[] editableColumns) {
        this.editableColumns = editableColumns;
    }

    protected String[] getColumnNames() {
        return columnNames;
    }

    protected Class[] getColumnClasses() {
        return columnClasses;
    }

    public boolean[] getEditableColumns() {
        return editableColumns;
    }

    public AbstractColumnManager() {}

    public AbstractColumnManager(String[] columnNames, Class[] columnClasses) {
        this.columnNames = columnNames;
        this.columnClasses = columnClasses;
    }


    /**
     * Sets the value in the cell at <code>columnIndex</code> and
     * <code>rowIndex</code> to <code>aValue</code>.
     *
     * @param   newCellValue         the new value
     * @param   rowIndex     the row whose value is to be changed
     * @param   columnIndex      the column whose value is to be changed
     * @param   rowObject the object which contains the entire row for this table.
     *   modifying the cell value will likely change this object in some way.
     * @see #getValueAt
     * @see #isCellEditable
     */
    public void setValueAt(Object newCellValue, int rowIndex, int columnIndex, Object rowObject) {
        throw new RuntimeException("setValueAt() has not been implemented in " + getClass().getName() + ".");
    }

    /**
     * Returns the value in the cell at <code>columnIndex</code> and
     * <code>rowIndex</code>.
     * 
     * @param rowIndex the row containing the cell.
     * @param columnIndex the columnContaining the cell.
     * @param rowObject the object containing the row's data.
     */
    public abstract Object getValueAt(int rowIndex, int columnIndex, Object rowObject);

    /**
     * Returns the most specific superclass for all the cell values
     * in the column.  This is used by the <code>JTable</code> to set up a
     * default renderer and editor for the column.
     *
     * @param columnIndex  the index of the column
     * @return the common ancestor class of the object values in the model.
     */
    public Class getColumnClass(int columnIndex) {
        if(columnClasses == null) return java.lang.Object.class;
        return columnClasses[columnIndex];
    }
    /**
     * Returns true if the cell at <code>rowIndex</code> and
     * <code>columnIndex</code>
     * is editable.  Otherwise, <code>setValueAt</code> on the cell will not
     * change the value of that cell.
     *
     * @param   rowIndex    the row whose value to be queried
     * @param   columnIndex the column whose value to be queried
     * @param   rowObject the object which contains the entire row for this table.
     * @return  true if the cell is editable
     * @see #setValueAt
     */
    public boolean isCellEditable(int rowIndex, int columnIndex, Object rowObject) {
        if(editableColumns == null) return false;
        return editableColumns[columnIndex];
    }
    /**
     * Returns the number of columns in the model. A
     * <code>JTable</code> uses this method to determine how many columns it
     * should create and display by default.
     *
     * @return the number of columns in the model
     * @see #getRowCount
     */
    public int getColumnCount() {
        if(columnNames == null) return 0;
        return columnNames.length;
    }
    /**
     * Returns the name of the column at <code>columnIndex</code>.  This is used
     * to initialize the table's column header name.  Note: this name does
     * not need to be unique; two columns in a table can have the same name.
     *
     * @param   columnIndex the index of the column
     * @return  the name of the column
     */
    public String getColumnName(int columnIndex) {
        if(columnNames == null) return null;
        return columnNames[columnIndex];
    }

}
