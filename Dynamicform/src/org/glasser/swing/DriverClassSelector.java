package org.glasser.swing;


import javax.swing.*;
import java.util.*;
import org.glasser.sql.*;
import java.awt.*;


public class DriverClassSelector extends JComboBox {


    protected static String[] availableDrivers = DriverClassList.getAvailableDriverClassNames();


    public final static String EMPTY_ITEM = "  ";


    public DriverClassSelector() {
        super(availableDrivers);
        this.insertItemAt(EMPTY_ITEM, 0);
        this.setSelectedIndex(0);
        this.setEditable(true);
        Dimension d = (Dimension) getPreferredSize().clone();
        d.width = 250;
        setPreferredSize(d);

    }


    public static void main(String[] args) throws Exception {
        JPanel jp = new JPanel();

        DriverClassSelector cmb = new DriverClassSelector();
        cmb.setEditable(true);
        jp.add(cmb);
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(3);
        frame.setContentPane(jp);
        frame.pack();
        frame.setVisible(true);
    } 
    





}

