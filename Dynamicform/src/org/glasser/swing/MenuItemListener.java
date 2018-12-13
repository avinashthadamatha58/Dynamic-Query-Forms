package org.glasser.swing;


import javax.swing.event.*;
import java.awt.event.*;
import javax.swing.JMenuItem;
import javax.swing.JMenu;

/**
 * This listener can be used to transmit status messages to a status bar 
 * whenever a JMenuItem is passed over in an open menu. It should be
 * added as both a ChangeListener and ItemListener to a JMenuItem.
 */
public class MenuItemListener implements ChangeListener, ItemListener {

    private StatusMessageDisplay display = null;

    /**
     * Constructor.
     *
     * @param statusMsg the message that is sent to the StatusMessageDisplay whenever
     * a JMenuItem is passed over.  May be null.
     *
     * @param  display an object implementing the StatusMessageDisplay interface, which will
     * receive the status messages related to the menu item.
     */
    public MenuItemListener(StatusMessageDisplay display) {
        if(display == null) throw new IllegalArgumentException("Required argument missing.");
        this.display = display;
    }

    JMenuItem lastItem = null;
    boolean lastArmed = false;

    public void stateChanged(ChangeEvent e) {

        Object src = e.getSource();
        if(src instanceof JMenu) {
            JMenuItem menuItem = (JMenuItem) src;
            String statusMsg = menuItem.getToolTipText();
            boolean isArmed = menuItem.isSelected();
//            System.out.println("JMenu STATE_CHANGED: (" + menuItem.isArmed() + "/" + menuItem.isSelected() + ") " + menuItem.getText());
            if(isArmed && (menuItem != lastItem || isArmed != lastArmed)) {
                display.setStatusMessage(statusMsg);
            }
            else {
                String currentStatusMessage = display.getStatusMessage();
//                System.out.println(statusMsg + "|" + currentStatusMessage);
                if(statusMsg != null && statusMsg.equals(currentStatusMessage)) {
                    display.setStatusMessage(null);
                }
            }
            lastItem = menuItem;
            lastArmed = isArmed;

        }
        else if(src instanceof JMenuItem) {
            JMenuItem menuItem = (JMenuItem) src;
            String statusMsg = menuItem.getToolTipText();
            boolean isArmed = menuItem.isArmed();
//            System.out.println("STATE_CHANGED: (" + menuItem.isArmed() + "/" + menuItem.isSelected() + ") " + menuItem.getText());
            if(isArmed && (menuItem != lastItem || isArmed != lastArmed)) {
                display.setStatusMessage(statusMsg);
            }
            else {
                String currentStatusMessage = display.getStatusMessage();
//                System.out.println(statusMsg + "|" + currentStatusMessage);
                if(statusMsg != null && statusMsg.equals(currentStatusMessage)) {
                    display.setStatusMessage(null);
                }
            }
            lastItem = menuItem;
            lastArmed = isArmed;

        }
    }

    public void itemStateChanged(ItemEvent e) {
        
        Object src = e.getSource();
        if(src instanceof JMenuItem) {
            JMenuItem menuItem = (JMenuItem) src;
            String statusMsg = menuItem.getToolTipText();
//            System.out.println("ITEM_STATE_CHANGED: (" + menuItem.isArmed() + ") " + menuItem.getText());
            if(menuItem.isArmed() && ! menuItem.isSelected()) {
                display.setStatusMessage(statusMsg);
            }
            else {
                String currentStatusMessage = display.getStatusMessage();
//                System.out.println(statusMsg + "|" + currentStatusMessage);
                if(statusMsg != null && statusMsg.equals(currentStatusMessage)) {
                    display.setStatusMessage(null);
                }
            }
        }
    }
}

