package org.glasser.qform;



import org.glasser.util.*;



/**
 * Listener interface for the QueryPanelEvent.
 */
public interface QueryPanelListener extends java.util.EventListener {

    public void queryPanelStateChanged(QueryPanelEvent event);

    public void queryPanelHorizontalScrollbarStateChanged(QueryPanelEvent event);

}
