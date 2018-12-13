package org.glasser.util;

/**
 * This is an interface for classes that transform an object of some type into
 * a String.
 * 
 * @author Dave Glasser
 */
public interface Formatter extends java.io.Serializable {

    public String getFormattedString(Object obj);

}
