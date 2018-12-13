
package org.glasser.sql;


import org.glasser.util.*;


public class LiteralFormatter implements Formatter {

    public String literal = null;

    /**
     * Constructor.
     * 
     * @param literal The value that is always returned by the getFormattedString() method,
     * regardless of what kind of objects are passed in.
     */
    public LiteralFormatter(String literal) {
        this.literal = literal;
    }

    /**
     * Always returns the value of the "literal" field.
     */
    public String getFormattedString(Object obj) {

        return literal;
        
    }
}

