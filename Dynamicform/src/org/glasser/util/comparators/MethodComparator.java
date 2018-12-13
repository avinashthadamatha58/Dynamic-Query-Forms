package org.glasser.util.comparators;


import java.lang.reflect.*;
import java.util.*;
import java.io.*;

/**
 * This Comparator is used to compare two objects of a common type, based
 * on the value returned from a no-argument method common to both of them.
 * (Typically, this will be a getter method.)
 */
public class MethodComparator extends BaseComparator {

    private Class objectClass = null;

    private String getterName = null;

    private transient Method getter = null;

    private boolean returnTypeIsString = false;

    private boolean returnTypeIsComparable = false;

    private boolean caseSensitive = false;


    public MethodComparator(Class objectClass, String getterName) {
        this(objectClass, getterName, false, false, null, false);
    }


    public MethodComparator(Class objectClass, 
                            String getterName, 
                            boolean nullIsGreater, 
                            boolean sortDescending, 
                            Comparator nestedComparator,
                            boolean caseSensitive) 
    {
        super(nullIsGreater, sortDescending, nestedComparator);

        this.caseSensitive = caseSensitive;
        this.objectClass = objectClass;
        this.getterName = getterName;

        try {
            getter = objectClass.getMethod(getterName, new Class[0]);
        }
        catch(NoSuchMethodException ex) {
            throw new IllegalArgumentException("Invalid getterName: " + getterName
                                               + " method not found in " + objectClass);
        }
        Class returnTypeClass = getter.getReturnType();
        if(returnTypeClass.equals(java.lang.String.class)) {
            returnTypeIsString = true;
            returnTypeIsComparable = true;
        }
        else if(java.lang.Comparable.class.isAssignableFrom(returnTypeClass)) {
            returnTypeIsComparable = true;
        }
    }


    protected int doCompare(Object o1, Object o2) {

        try {
            Object val1 = getter.invoke(o1, (Object[]) null);
            Object val2 = getter.invoke(o2, (Object[]) null);
    
            if(val1 == null && val2 == null) return 0;
            int retVal = compareForNulls(val1, val2);
            if(retVal != 0) return retVal;
    
            // we know now that both val1 and val2 are non-null
            if(returnTypeIsString) {
                if(caseSensitive) {
                    return ((String) val1).compareTo((String) val2);
                }
                else {
                    return String.CASE_INSENSITIVE_ORDER.compare((String) val1, (String) val2);
                }
            }
            else if(returnTypeIsComparable) {
                return ((Comparable) val1).compareTo((Comparable) val2);
            }
            else {
                String s1 = val1.toString();
                String s2 = val2.toString();
                if(caseSensitive) {
                    return (s1).compareTo(s2);
                }
                else {
                    return String.CASE_INSENSITIVE_ORDER.compare(s1, s2);
                }
            }
        }
        catch(Exception ex) {
            throw new RuntimeException("A "
                                       + ex.getClass().getName()
                                       + " occurred in MethodComparator.doCompare(). objectClass="
                                       + objectClass.getName() + ", getterName=" + getterName);
        }
    }


    public boolean isCaseSensitive() {
        return caseSensitive;
    }


    /**
     * This must be implemented to make the MethodComparator class serializable,
     * because the getter field (which is a Method) is not.
     */
    private void writeObject(java.io.ObjectOutputStream out)
        throws IOException
    {
        out.defaultWriteObject();
    }

    /**
     * This must be implemented to make the MethodComparator class serializable,
     * because the getter field (which is a Method) is not.
     */
    private void readObject(java.io.ObjectInputStream in)
        throws IOException, ClassNotFoundException {
    
        in.defaultReadObject();
    
        try {
            getter = objectClass.getMethod(getterName, (Class[]) null);
        }
        catch(Exception ex) {
            throw new IOException("A "
                                   + ex.getClass().getName()
                                   + " occurred in MethodComparator(): objectClass="
                                   + objectClass.getName() + ", getterName=" + getterName);
        }
    
    }


}
