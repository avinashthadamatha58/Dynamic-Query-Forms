package org.glasser.sql;


/**
 * Objects that implement this interface can register with one or more
 * ResultSetBuffer objects and receive notification when various events
 * occur.
 * 
 * @author Dave Glasser
 */
public interface ResultSetBufferListener extends java.util.EventListener {

    /**
     * This method is called to notify a listener that a ResultSetBuffer has
     * read more rows from its ResultSet.
     */
    public void moreRowsRead(ResultSetBufferEvent e);

    /**
     * This method is called to notify a listener that a ResultSetBuffer has
     * read the last row of its ResultSet.
     */
    public void endOfResultsReached(ResultSetBufferEvent e);

    /**
     * This method notifies a listener that the rows that have been read into
     * a ResultSetBuffer so far have been sorted. This can occur before the entire
     * ResultSet has been read from the database.
     */
    public void bufferSorted(ResultSetBufferEvent e);

}
