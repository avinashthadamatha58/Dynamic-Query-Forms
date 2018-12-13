package org.glasser.util.comparators;


import java.util.*;

/**
 * This is a comparator class that's used to compare one List with another List.
 * The comparison is actually performed by comparing the Nth element of
 * the first List with the Nth element of the second list. This comparator can
 * be used to sort a table, when each row of the table is contained in a List, and
 * the table is to be sorted on a particular (Nth) column.
 * 
 * @author David Glasser
 */
public class ListComparator extends BaseComparator {



    protected int elementIndex = 0;

    /**
     * This is the comparator that compares the elements read from the two lists being
     * compared. If this field is null, then the elements are compared as Comparables 
     * (if they both implement Comparables) or Strings (from their toString() methods.)
     */
    protected Comparator valueComparator = null;


    /**
     * Constructs a ListComparator instance with the default field values. The resulting
     * instance is immutable and thread-safe, unless its fields have been exposed
     * by subclasses.
     */
    public ListComparator() { 
    }

    /**
     * Constructs a ListComparator instance with the given elementIndex and default
     * values for the other fields. The resulting
     * instance is immutable and thread-safe, unless its fields have been exposed
     * by subclasses.
     */
    public ListComparator(int elementIndex) {
        this.elementIndex = elementIndex;
    }

    /**
     * Constructs a ListComparator instance with the given field values. The resulting
     * instance is immutable and thread-safe, unless its fields have been exposed
     * by subclasses.
     * 
     * @param elementIndex
     */
    protected ListComparator(boolean nullIsGreater, 
        boolean sortDescending, 
        Comparator nestedComparator,
        int elementIndex,
        Comparator valueComparator) 
    {
        super(nullIsGreater, sortDescending, nestedComparator);
        this.elementIndex = elementIndex;
        this.valueComparator = valueComparator;
    }




    /**
     * This method is implemented by subclasses, which should know about the types
     * of the two objects being passed in and how to order them. This is a template
     * method which is called from within compare(), and both arguments are guaranteed
     * to be non-null. Implementations of this method should not be concerned with flipping
     * the sign of the return value for descending sorts; that task will be handled within this
     * (the base) class.
     */
    protected int doCompare(Object o1, Object o2) {

        Object val1 = ((List) o1).get(elementIndex);
        Object val2 = ((List) o2).get(elementIndex);

        if(val1 == null && val2 == null) return 0;

        int retVal = compareForNulls(val1, val2);
        
        if(retVal != 0) return retVal;
        
        // both values are non-null
        if(valueComparator != null) return valueComparator.compare(val1, val2);
        
        if(val1 instanceof Comparable && val2 instanceof Comparable) {
            return ((Comparable) val1).compareTo((Comparable) val2);
        }
        
        return val1.toString().compareTo(val2.toString());
        
    }


}
