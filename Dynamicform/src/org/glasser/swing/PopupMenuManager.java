package org.glasser.swing;



import javax.swing.*;
import java.awt.event.*;





public class PopupMenuManager extends MouseAdapter {

    private JPopupMenu popup = null;

    private boolean popupEnabled = true;

    public PopupMenuManager(JPopupMenu popup) {
        this.popup = popup;
    }

    /**
     * Invoked when a mouse button has been pressed on a component.
     */
    public void mousePressed(MouseEvent e) {
        if(popupEnabled == false) return;
        if(e.isPopupTrigger()) {
            popup.show(e.getComponent(),
                       e.getX(), e.getY());
        }
    }

        
    /**
     * Invoked when a mouse button has been released on a component.
     */
    public void mouseReleased(MouseEvent e) {
        if(popupEnabled == false) return;
        if(e.isPopupTrigger()) {
            popup.show(e.getComponent(),
                       e.getX(), e.getY());
        }
    }

    public void setPopup(JPopupMenu popup) {
        this.popup = popup;
    }

    public void setPopupEnabled(boolean popupEnabled) {
        this.popupEnabled = popupEnabled;
    }

    public JPopupMenu getPopup() {
        return popup;
    }

    public boolean isPopupEnabled() {
        return popupEnabled;
    }




}
