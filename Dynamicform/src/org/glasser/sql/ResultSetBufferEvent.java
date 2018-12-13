package org.glasser.sql;


import java.util.*;
import org.glasser.util.*;


/**
 * @author David F. Glasser
 */
public final class ResultSetBufferEvent implements SmartEvent, java.io.Serializable {


    public final static int MORE_ROWS_READ = 0;

    public final static int END_OF_RESULTS_REACHED = 1;

    public final static int BUFFER_SORTED = 2;

    private int type = MORE_ROWS_READ;

    private ResultSetBuffer source = null;

    /**
     * Constructs a ResultSetBufferEvent objects.
     * 
     * @param source the ResultSetBuffer which originated this event.
     * @param type an it indicating what type of ResultSetBufferEvent this
     * is, either MORE_ROWS_READ or END_OF_RESULTS_REACHED.
     */
    public ResultSetBufferEvent(ResultSetBuffer source, int type) {
        this.source = source;
        this.type = type;
    }

    /**
     * Returns the Class for the interface that receives this type of event; in this
     * particular case is is ResultSetBufferListener.class.
     */
    public Class getListenerClass() {
        return ResultSetBufferListener.class;
    }

    /**
     * Returns an int indicating the type of ResultSetBufferEvent this is, either
     * MORE_ROWS_READ or END_OF_RESULTS_REACHED.
     */
    public int getType() {
        return type;
    }

    /**
     * This method, specified by the org.glasser.util.SmartEventListener interface,
     * is passed a reference to one of the listeners registered to receive this
     * event. It then calls the appropriate method on the listener interface,
     * notifying the object of the event.
     */
    public void notifyListener(EventListener listener) {

        ResultSetBufferListener bufferListener = (ResultSetBufferListener) listener;

        switch(type) {
            case MORE_ROWS_READ :
                bufferListener.moreRowsRead(this);
                break;
            case END_OF_RESULTS_REACHED :
                bufferListener.endOfResultsReached(this);
                break;
            case BUFFER_SORTED :
                bufferListener.bufferSorted(this);
                break;
            default :
                throw new RuntimeException("ResultSetBufferEvent: Invalid Type: " + type);
        }

    }

}
