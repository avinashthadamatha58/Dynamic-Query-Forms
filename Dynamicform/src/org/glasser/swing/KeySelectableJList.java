package org.glasser.swing;

import javax.swing.*;
import javax.swing.ListModel;
import java.awt.event.*;
import java.util.*;
import java.awt.*;
import javax.swing.text.*;

/**
 * This is a JList that allows items to be selected with character keys
 * in the same way as the JComboBox does.
 */
public class KeySelectableJList extends JList {


    public KeySelectableJList() {
        super();
    }

    public KeySelectableJList(ListModel model) {
        super(model);
    }

    public KeySelectableJList(final Object[] listData) {
        super(listData);
    }

    public KeySelectableJList(final Vector listData) {
        super(listData);
    }


    final static boolean javaVersionOnePointFourOrGreater;

    static  {
        String version = System.getProperty("java.version");
        System.out.println("Java Version: " + version);
        if(version != null && version.compareTo("1.3.9") > 0) {
            javaVersionOnePointFourOrGreater = true;
            System.out.println("Java version is 1.4 or greater, built-in JList key selection functionality will be used.");
        }
        else {
            javaVersionOnePointFourOrGreater = false;
        }
    }



    public void processKeyEvent(KeyEvent ev) {

        //System.out.println("\nPROCESS KEY EVENT.");

        super.processKeyEvent(ev);

        if(javaVersionOnePointFourOrGreater) {

            // if this is version 1.4 or greater, the only thing we want
            // to do is scroll the list to the selected item (if it is in a
            // JScrollPane.)
            int index = this.getSelectedIndex();
            if(index > -1) {
                this.ensureIndexIsVisible(index);
            }
            return;
        }


        // if this a Java version prior to 1.4, we will invoke the key selection
        // logic.
        if(ev.getID() == KeyEvent.KEY_PRESSED) {

            // see if this is a letter key.
            char keyChar = ev.getKeyChar();
            if((Character.isLetter(keyChar) && ev.isAltDown() == false)) { 
                int index = selectionForKey(keyChar);
                if(index > -1) {
//                    System.out.println("SETTING SELECTED INDEX TO: " + index);
                    this.setSelectedIndex(index);
                    this.ensureIndexIsVisible(index);
                }
            }
        }
    }


    public int selectionForKey(char key) {


        int selectedIndex = -1;
        ListModel model = this.getModel();
        Object selectedItem = this.getSelectedValue();

        // if there's already an item selected...
        if (selectedItem != null) {

            // get the string value of the selected item
            selectedItem = selectedItem.toString();

            for (int j=0; j<model.getSize(); j++) {
                if (selectedItem.equals(model.getElementAt(j).toString())) {
                    selectedIndex = j;
                    break;
                }
            }
        }

//        System.out.println("Selected index is " + selectedIndex);

        key = Character.toLowerCase(key);

        for (int j=selectedIndex+1; j<model.getSize(); j++) {
            String displayString = model.getElementAt(j).toString();
            if (displayString.length() > 0 && Character.toLowerCase(displayString.charAt(0)) == key) {
//                System.out.println(">>MATCH FOUND: " + displayString);
                return j;
            }
        }

        for (int j=0 ; j<selectedIndex ; j++) {
            String displayString = model.getElementAt(j).toString();
            if ( displayString.length() > 0 && Character.toLowerCase(displayString.charAt(0)) == key ) {
//                System.out.println("<<MATCH FOUND: " + displayString);
                return j;
            }
        }

        return -1;
    }


    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(3);
        JPanel cp = new JPanel();
        cp.setLayout(new BorderLayout());

        JList list = new KeySelectableJList(
            new String[] {
                "Apples",
                "Grapes",
                "Oranges",
                "Peaches",
                "Pears",
                "Watermelons"
            });

        cp.add(new JScrollPane(list), BorderLayout.CENTER);
        frame.setContentPane(cp);
        frame.pack();
        frame.setVisible(true);

    } 
    

}

