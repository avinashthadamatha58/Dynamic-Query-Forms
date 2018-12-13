package org.glasser.swing;


import javax.swing.*;
import org.glasser.util.*;
import org.glasser.sql.*;
import org.glasser.swing.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.util.*;



public class AppendOverwriteDialog extends JDialog implements ActionListener {


    private JButton btnAppend = new JButton("Append");

    private JButton btnOverwrite = new JButton("Overwrite");

    private JButton btnCancel = new JButton("Cancel");


    public final static String CANCEL_OPTION = "CANCEL";

    public final static String APPEND_OPTION = "APPEND";

    public final static String OVERWRITE_OPTION = "OVERWRITE";

    JTextArea textArea = new JTextArea();


    Object[][] buttonConfig =
    {
        {btnAppend, "A", APPEND_OPTION, "Append output to the selected file."}
        ,{btnOverwrite, "O", OVERWRITE_OPTION, "Overwrite existing file."}
        ,{btnCancel, "C", CANCEL_OPTION, "Cancel operation."}
    };


    public AppendOverwriteDialog(Frame parent) {
        super(parent);

        JPanel cp = new JPanel();
        setTitle("Confirm File Overwrite");
        setContentPane(cp);
        cp.setLayout(new BorderLayout());
        textArea.setOpaque(false);
        textArea.setBorder(new EmptyBorder(15, 15, 15, 15));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        textArea.setEnabled(false);
        textArea.setDisabledTextColor(Color.black);

        Font f = (Font) UIManager.getDefaults().get("Button.font");
        if(f != null) {
            textArea.setFont(f);
        }

        cp.add(textArea, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.setBorder(new EmptyBorder(15,0, 15, 0));
        GUIHelper.buildButtonPanel(buttonPanel,buttonConfig,this);
        cp.add(buttonPanel, BorderLayout.SOUTH);
        setModal(true);
    }


    public void openDialog(String fileName, boolean showAppendButton) {
        
        setModal(true);
        btnAppend.setVisible(showAppendButton);
        if(fileName != null) {
            textArea.setText("The file you have selected:\n\n"
                + fileName
                + "\n\nalready exists. How do you want to proceed?");
        }
        else {
            textArea.setText("The selected file already exists.\n\nHow do you want to proceed?");
        }

        pack();
        selection = CANCEL_OPTION;
        
        super.setVisible(true);
    }

    public void setVisible(boolean b) {
        if(b) throw new RuntimeException("Use openDialog() to make dialog visible.");
    }

    private String selection = null;

    public void actionPerformed(ActionEvent e) {
        selection = e.getActionCommand();
        super.setVisible(false);
    }

    public String getSelection() {
        return selection;
    }


    public static void main(String[] args) throws Exception {


        AppendOverwriteDialog d = new AppendOverwriteDialog(null);
        d.openDialog("C:\\autoexec.bat", true);
        System.out.println(d.getSelection());
        System.exit(0);
        
    } 
    
       

}
