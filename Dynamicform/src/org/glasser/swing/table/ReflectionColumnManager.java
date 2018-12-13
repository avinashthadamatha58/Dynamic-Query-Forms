package org.glasser.swing.table;


import javax.swing.table.*;
import java.lang.reflect.*;
import org.glasser.util.*;


public class ReflectionColumnManager extends AbstractColumnManager {


    Class objectClass = null;

    Method[] getters = null;

    Method[] setters = null;

    public ReflectionColumnManager(String[] columnNames, Class objectClass, String[] getterNames, String[] setterNames) 
        throws NoSuchMethodException
    {
        super.setColumnNames(columnNames);
        getters = new Method[getterNames.length];
        Class[] columnClasses = new Class[getterNames.length];

        for(int j=0; j<getters.length; j++) {
            getters[j] = objectClass.getMethod(getterNames[j], (Class[]) null);
            columnClasses[j] = getters[j].getReturnType();
        }

        Class[] paramTypes = new Class[1];

        for(int j=0; setterNames != null && j<setterNames.length; j++) {
            String setterName = setterNames[j];
            if(setterName == null) continue;
            paramTypes[0] = columnClasses[j];
            setters[j] = objectClass.getMethod(setterName, paramTypes);
        }

        // now, if any of the columnClasses are for primitive types 
        // (int, long, etc.) then we'll convert them to their corresponding
        // wrapper class. This must be done after the setters have been
        // created.
        for(int j=0; j<columnClasses.length; j++) {
            Class wrapperClass = Util.getWrapperClass(columnClasses[j]);
            if(wrapperClass != null) columnClasses[j] = wrapperClass;
        }

        // now send the columnClasses array to the AbstractColumnManager superclass.
        super.setColumnClasses(columnClasses);

    }


    public Object getValueAt(int rowIndex, int columnIndex, Object rowObject) {

        Method getter = getters[columnIndex];
        try {
            if(rowObject == null) return null;
            return getter.invoke(rowObject, (Object[]) null);
        }
        catch(Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Caught " + ex.getClass().getName() + ": Object class is " 
                + objectClass.getName()
                + ", method is " + getter.getName());
        }
    }

    public void setValueAt(Object newCellValue, int rowIndex, int columnIndex, Object rowObject) {

        Method setter = setters[columnIndex];
        try {
            setter.invoke(rowObject, new Object[] { newCellValue });
        }
        catch(Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Caught " + ex.getClass().getName() + ": Object class is " 
                + objectClass.getName()
                + ", setter method is " + setter.getName());
        }
    }


    public boolean isCellEditable(int rowIndex, int columnIndex, Object rowObject) {
        return rowObject != null && setters != null && setters[columnIndex] != null;
    }

}


