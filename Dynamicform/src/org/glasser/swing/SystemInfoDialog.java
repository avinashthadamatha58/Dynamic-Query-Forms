package org.glasser.swing;


import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.datatransfer.*;



/**
 * This is a simple dialog box that displays the system properties of a
 * running app, along with the memory usage and the program startup time
 * (if a Date instance is passed to the constructor.)
 * 
 * @author Dave Glasser
 */
public class SystemInfoDialog extends JDialog implements ActionListener {

    JButton btnCopy = new JButton("Copy to Clipboard");

    JButton btnClose = new JButton("Close");

    Object[][] buttonConfig =
    {
        {btnCopy, "C", "COPY", "Copy the displayed information to the clipboard."}
        ,{btnClose, "S", "CLOSE", "Close this dialog."}
    };

    SystemPanel systemPanel = null;


    public SystemInfoDialog() {
        this(null, null);
    }


    /**
     * If a Date is passed to this constructor, it will be displayed
     * on the dialog with the label "Up since:". Otherwise it won't be.
     * 
     * @param runningSince a java.util.Date that is used to indicate when the app
     * that owns this dialog box was started.
     * 
     */
    public SystemInfoDialog(Date runningSince) {
        this(null, runningSince);
    }

    /**
     * If a Date is passed to this constructor, it will be displayed
     * on the dialog with the label "Up since:". Otherwise it won't be.
     * 
     * @owner the Frame, if any, that "owns" this dialog.
     * 
     * @param runningSince a java.util.Date that is used to indicate when the app
     * that owns this dialog box was started.
     * 
     */
    public SystemInfoDialog(Frame owner, Date runningSince) {
        super(owner);
        setTitle("System Information");

        JPanel cp = new JPanel();
        cp.setLayout(new BorderLayout());
        
        systemPanel = new SystemPanel(runningSince);
        systemPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        cp.add(systemPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        GUIHelper.buildButtonPanel(buttonPanel, buttonConfig, this);
        buttonPanel.setBorder(new EmptyBorder(0, 10, 10, 10));
        cp.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(cp);
        pack();
    }

    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if(cmd.equals("CLOSE")) {
            setVisible(false);
        }
        else if(cmd.equals("COPY")) {
            String info = systemPanel.getInfoAsString("\n");
            Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
            cb.setContents(new StringSelection(info), null);
        }
    }

    /**
     * Launches a small program to test this class.
     */
    public static void main(String[] args) throws Exception {
        SystemInfoDialog d = new SystemInfoDialog(new Date());
        d.setModal(true);
        d.setVisible(true);
        System.exit(0);
    } 


    public void setVisible(boolean b) {
        if(b) {
            systemPanel.refreshSystemPropertiesTable();
        }
        super.setVisible(b);

    }


}
