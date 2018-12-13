package org.glasser.util.comparators;



import java.util.*;


/**
 * This Comparator is used for sorting a collection of Maps. An instance
 * is given a "key" object when it's constructed. Each time it is given
 * two Maps to compare, it will use the key to get a value from each map,
 * and compare those two values to determine the ordering of the two 
 * maps.
 * <p>
 * If a separate "value comparator" is provided, it will be used to compare
 * the values fetched from the Maps. Otherwise, if the fetched values are
 * java.util.Comparables, they'll be compared with the compareTo() method
 * of the first value. Finally, if no value comparator was provided and the fetched
 * values are not Comparables, their toString() values will be compared.
 */
public class MapComparator extends BaseComparator {



    private Object key = null;

    private Comparator valueComparator = null;


    public MapComparator(Object key) {
        this.key = key;
    }

    public MapComparator(Object key, Comparator valueComparator) {
        this.key = key;
        this.valueComparator = valueComparator;
    }


    public MapComparator(Object key, 
                         Comparator valueComparator, 
                         boolean nullIsGreater, 
                         boolean sortDescending, 
                         Comparator nestedComparator) 
    {
        super(nullIsGreater, sortDescending, nestedComparator);
        this.key = key;
        this.valueComparator = valueComparator;
    }


    public int doCompare(Object o1, Object o2) {

        Object val1 = ((Map) o1).get(key);
        Object val2 = ((Map) o2).get(key);

        if(val1 == null && val2 == null) return 0;

        int retVal = super.compareForNulls(val1, val2);

        if(retVal != 0) return retVal;

        // both values are non-null
        if(valueComparator != null) return valueComparator.compare(val1, val2);

        if(val1 instanceof Comparable) return ((Comparable) val1).compareTo((Comparable) val2);

        return val1.toString().compareTo(val2.toString());
        
        
    }


}
