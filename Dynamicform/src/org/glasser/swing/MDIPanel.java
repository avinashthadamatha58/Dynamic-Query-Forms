package org.glasser.swing;



import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;


public class MDIPanel extends JPanel implements StatusMessageDisplay {


    protected JMenuBar menuBar = new JMenuBar();

    protected JPanel toolbarHolder = new JPanel();

    protected JToolBar toolBar = new JToolBar();

    protected JDesktopPane desktop = new ScrollingDesktopPane();

    protected JPanel statusBar = new JPanel();

    protected JLabel statusLabel = new JLabel();    

    public MenuItemListener menuItemListener = new MenuItemListener(this);


    public MDIPanel() {


        setPreferredSize(new Dimension(760, 560));


        this.setLayout(new BorderLayout());


        /////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////
        //
        // add the menubar
        //
        add(menuBar, BorderLayout.NORTH);

        /////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////
        //
        // add the toolbar
        //
        add(toolbarHolder, BorderLayout.CENTER);
        toolbarHolder.setLayout(new BorderLayout());
        toolbarHolder.add(toolBar, BorderLayout.NORTH);


        /////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////
        //
        // configure the desktop
        //
//        desktop.setBackground(new java.awt.Color(127, 127, 127));
        desktop.putClientProperty("JDesktopPane.dragMode",
                          "outline");
//        desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
        desktop.setBorder(new ThinBevelBorder(ThinBevelBorder.LOWERED));
//        desktop.setDesktopManager(new ScrollingDesktopManager());
        toolbarHolder.add(new JScrollPane(desktop), BorderLayout.CENTER);


        /////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////
        //
        // add the statusbar
        //
        add(statusBar, BorderLayout.SOUTH);
        statusBar.setLayout(new BorderLayout());
        statusLabel.setOpaque(true);
        statusLabel.setFont(new Font("Dialog", Font.PLAIN, 10));
        statusLabel.setForeground(java.awt.Color.black);
        statusLabel.setText("  ");
        statusLabel.setBorder(new CompoundBorder(
            new ThinBevelBorder(ThinBevelBorder.LOWERED),
            new EmptyBorder(2, 6, 2, 0)));

        statusLabel.setMinimumSize(statusLabel.getPreferredSize());
        statusLabel.setPreferredSize(statusLabel.getPreferredSize());
        
        statusBar.add(statusLabel);
        statusBar.setBorder(new EmptyBorder(5,2,2,2));

    }



    /**
     * Displays the String "message" on the status bar.
     */
    public void setStatusMessage(String message) {
        statusLabel.setText(message);
    }

    public String getStatusMessage() {
        return statusLabel.getText();
    }


}
