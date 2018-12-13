package org.glasser.qform;


import java.util.*;
import org.glasser.util.*;


/**
 * This is a SmartEvent that is fired by a QueryPanel to notify listeners of
 * various types of events.
 * 
 * @author David F. Glasser
 */
public final class QueryPanelEvent implements SmartEvent, java.io.Serializable {


    public final static int STATE_CHANGED = 0;

    public final static int HORIZONTAL_SCROLLBAR_STATE_CHANGED = 1;

    private int type = STATE_CHANGED;

    

    private QueryPanel source = null;

    /**
     * Constructs a QueryPanelEvent object.
     * 
     * @param source the QueryPanel which originated this event.
     * @param type an it indicating what type of event occurred. Currently the only
     * type of event is STATE_CHANGED.
     */
    public QueryPanelEvent(QueryPanel source, int type) {
        this.source = source;
        this.type = type;
    }

    /**
     * Returns the Class for the interface that receives this type of event; in this
     * particular case is is QueryPanelListener.class.
     */
    public Class getListenerClass() {
        return QueryPanelListener.class;
    }

    /**
     * Returns an int indicating the type of event occurred. Currently the only
     * type of event is STATE_CHANGED.
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

        QueryPanelListener panelListener = (QueryPanelListener) listener;

        switch(type) {  // a switch is used in anticipation of adding more event types.
            case STATE_CHANGED :
                panelListener.queryPanelStateChanged(this);
                break;
            case HORIZONTAL_SCROLLBAR_STATE_CHANGED :
                panelListener.queryPanelHorizontalScrollbarStateChanged(this);
                break;
            default :
                throw new RuntimeException("QueryPanelEvent: Invalid Type: " + type);
        }
    }
}
