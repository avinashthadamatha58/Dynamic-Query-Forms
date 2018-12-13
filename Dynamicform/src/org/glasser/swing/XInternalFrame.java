package org.glasser.swing;


import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;


public class XInternalFrame extends JInternalFrame implements ActionListener {

    private final JMenuItem menuItem = new JMenuItem();

    private ImageIcon selectedIcon = null;

    private ImageIcon deselectedIcon = null;

    public static boolean debug = System.getProperty("XInternalFrame.debug") != null
        || System.getProperty("DEBUG") != null;


    private class Listener implements InternalFrameListener {
        /**
         * Invoked when an internal frame is activated.
         * @see javax.swing.JInternalFrame#setSelected
         */
        public void internalFrameActivated(InternalFrameEvent e) {

            menuItem.setIcon(selectedIcon);
        }
        /**
         * Invoked when a internal frame has been opened.
         * @see javax.swing.JInternalFrame#show
         */
        public void internalFrameOpened(InternalFrameEvent e) {
        }
        /**
         * Invoked when an internal frame is in the process of being closed.
         * The close operation can be overridden at this point.
         * @see javax.swing.JInternalFrame#setDefaultCloseOperation
         */
        public void internalFrameClosing(InternalFrameEvent e) {
            if(debug) System.out.println("TRC: " + getClass().getName() + ".internalFrameClosing()");
        }
        /**
         * Invoked when an internal frame is de-activated.
         * @see javax.swing.JInternalFrame#setSelected
         */
        public void internalFrameDeactivated(InternalFrameEvent e) {

            menuItem.setIcon(deselectedIcon);

        }
        /**
         * Invoked when an internal frame has been closed.
         * @see javax.swing.JInternalFrame#setClosed
         */
        public void internalFrameClosed(InternalFrameEvent e) {
            if(debug) System.out.println("TRC: " + getClass().getName() + ".internalFrameClosed()");
            Container c = menuItem.getParent();
            if(c != null) c.remove(menuItem);
            menuItem.removeActionListener(XInternalFrame.this);
        }
        /**
         * Invoked when an internal frame is iconified.
         * @see javax.swing.JInternalFrame#setIcon
         */
        public void internalFrameIconified(InternalFrameEvent e) {
        }
        /**
         * Invoked when an internal frame is de-iconified.
         * @see javax.swing.JInternalFrame#setIcon
         */
        public void internalFrameDeiconified(InternalFrameEvent e) {
        }

    }


    {
        menuItem.addActionListener(this);
        selectedIcon = GUIHelper.getImageIconFromClasspath("org/glasser/swing/images/Play16.gif");
        deselectedIcon = GUIHelper.getImageIconFromClasspath("org/glasser/swing/images/Clear16.gif");
        addInternalFrameListener(new Listener());

    }




    public XInternalFrame() {
        super();
    }

    public XInternalFrame(String title, boolean resizable) {
        super(title, resizable);
        menuItem.setText(title);
    }

    public XInternalFrame(String title, boolean resizable, boolean closable) {
        super(title, resizable, closable);
        menuItem.setText(title);
    }

    public XInternalFrame(String title, boolean resizable, boolean closable, boolean maximizable) {
        super(title,resizable, closable, maximizable);
        menuItem.setText(title);
    }

    public XInternalFrame(String title, boolean resizable, boolean closable, boolean maximizable, boolean iconifiable) {
        super(title, resizable, closable, maximizable, iconifiable);
        menuItem.setText(title);
    }

    public void actionPerformed(ActionEvent e) {

        if(e.getSource() == menuItem) {

            try {
                if(this.isIcon()) {
                    this.setIcon(false);
                }
    
                JDesktopPane dp = this.getDesktopPane();

                if(dp != null) {
                    DesktopManager dm = dp.getDesktopManager();
                    if(dm != null) dm.activateFrame(this);
                    setSelected(true);
                    menuItem.setIcon(selectedIcon);
                }
            }
            catch(Exception ex) {
                ex.printStackTrace();
            }
        }
    }



    public JMenuItem getMenuItem() {
        return menuItem;
    }



    public JViewport getViewport() {
        Container parent = super.getParent();
        while(parent != null) {
            if(parent instanceof JViewport) return (JViewport) parent;
            parent = parent.getParent();
        }

        return null;

    }

    public void finalize() {
        if(debug) {
            System.out.println("XInternalFrame.finalize(): " + getTitle());
        }
    }

}
