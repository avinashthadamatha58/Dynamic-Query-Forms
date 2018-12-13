package org.glasser.util.comparators;


import java.util.*;


/**
 * This class is essentially the same as ListComparator, except its fields
 * are exposed and mutable, and objects of this class are therefore not
 * threadsafe.
 * 
 * @author David Glasser
 */
public class MutableListComparator extends ListComparator {


    /**
     * Constructs a MutableListComparator instance with the default field values. The resulting
     * instance is not threadsafe.
     */
    public MutableListComparator() {}

    /**
     * Constructs a MutableListComparator instance with the given elementIndex and default
     * values for the other fields. The resulting
     * instance is not threadsafe.
     */
    public MutableListComparator(int elementIndex) {
        super(elementIndex);
    }


    /**
     * Constructs a MutableListComparator instance with the given field values. The resulting
     * instance is not threadsafe.
     */
    public MutableListComparator(boolean nullIsGreater, 
                               boolean sortDescending, 
                               Comparator nestedComparator,
                               int elementIndex,
                               Comparator valueComparator) {
        super(nullIsGreater, sortDescending, nestedComparator, elementIndex, valueComparator);
    }

    public void setElementIndex(int elementIndex) {
        this.elementIndex = elementIndex;
    }

    public void setNullIsGreater(boolean nullIsGreater) {
        super.setNullIsGreater(nullIsGreater);
    }

    public void setSortDescending(boolean sortDescending) {
        super.setSortDescending(sortDescending);
    }

    public void setNestedComparator(Comparator nestedComparator) {
        super.setNestedComparator(nestedComparator);
    }

    public void setValueComparator(Comparator valueComparator) {
        this.valueComparator = valueComparator;
    }

    public int getElementIndex() {
        return elementIndex;
    }

    public Comparator getNestedComparator() {
        return nestedComparator;
    }

}



