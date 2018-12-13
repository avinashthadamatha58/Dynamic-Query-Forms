package org.glasser.swing.table;


import java.util.*;
import javax.swing.table.*;
import javax.swing.event.*;

import org.glasser.util.*;

public class ListTableModelEvent extends EventObject implements SmartEvent {

    public final static int UPDATE = TableModelEvent.UPDATE;

    public final static int DELETE = TableModelEvent.DELETE;

    public final static int INSERT = TableModelEvent.INSERT;

    protected int type = UPDATE;

    protected Object rowObject = null;

    public ListTableModelEvent(ListTableModel source, Object rowObject)  {
        super(source);
        this.rowObject = rowObject;
    }


    public ListTableModelEvent(ListTableModel source, Object rowObject, int type)  {
        super(source);
        this.rowObject = rowObject;
        this.type = type;
    }


    public Object getRowObject() {
        return rowObject;
    }

    public int getType() {
        return type;
    }

    public void notifyListener(EventListener listener) 
    {

        ListTableModelListener ltml = (ListTableModelListener) listener;
        ltml.dataListChanged(this);
    }

    public Class getListenerClass() {

        return org.glasser.swing.table.ListTableModelListener.class;

    }

}
