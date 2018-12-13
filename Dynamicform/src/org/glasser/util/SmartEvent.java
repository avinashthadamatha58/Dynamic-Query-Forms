package org.glasser.util;


import java.util.EventListener;

/**
 * Interface for "SmartEvent" objects. A "SmartEvent" is "smart" because
 * it knows about its listener interface, and how to notify one of its listeners.
 * This eliminates the need for objects that generate SmartEvents to
 * write separate code for firing each type of SmartEvent.
 */
public interface SmartEvent {

    public Class getListenerClass();

    public void notifyListener(EventListener listener);

}
