package org.glasser.swing;


import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import org.glasser.sql.*;
import org.glasser.util.*;


public class LocalDataSourceConfigDialog extends JDialog implements ActionListener, ListSelectionListener {



    public static boolean debug = System.getProperty("LocalDataSourceConfigDialog.debug") != null;

    LocalDataSourceConfigPanel configPanel = new LocalDataSourceConfigPanel();

    KeySelectableJList listbox = new KeySelectableJList();

    JButton btnNew = new JButton("New");

    JButton btnEdit = new JButton("Edit");

    JButton btnDelete = new JButton("Delete");

    JButton btnSave = new JButton("Save");

    JButton btnCancel = new JButton("Cancel");

    JButton btnClose = new JButton("Close");

    JButton btnConnect = new JButton("Connect");

    Vector configVector = new Vector();


    Object[][] buttonConfig = 
    {
         {btnNew,   "N", "OPEN_NEW", "Configure a new data source."}
        ,{btnEdit, "E", "EDIT_EXISTING", "Edit the selected data source."}
        ,{btnDelete, "D", "DELETE_EXISTING", "Delete the selected data source configuration."}
        ,{btnSave, "S", "SAVE", "Save changes."}
        ,{btnCancel, "C", "CANCEL", "Discard changes."}
        ,{btnClose, "L", "CLOSE", "Close dialog."}
        ,{btnConnect, "T", "CONNECT", "Connect to selected data source."}
    };


    public final static int NOTHING_SELECTED = 0;

    public final static int ITEM_SELECTED = 1;

    public final static int EDIT_NEW = 2;

    public final static int EDIT_EXISTING = 3;


    private int screenState = NOTHING_SELECTED;

    
    private final static boolean[][] buttonStates = 
    {
        {true,  false,  false,  false,  false,  true,   false}   // 0 NOTHING_SELECTED
       ,{true,  true,   true,   false,  false,  true,   true}    // 1 ITEM_SELECTED
       ,{false, false,  false,  true,   true,   false,  false}   // 2 EDIT_NEW
       ,{false, false,  false,  true,   true,   false,  false}   // 3 EDIT_EXISTING

    };

    private boolean listStates[] = {true, true, false, false};

    private boolean configPanelStates[] = {false, false, true, true};

    private boolean disableSelectionEvents = false;


    public void setScreenState(int newState) {

        int index = listbox.getSelectedIndex();

        for(int j=0; j<buttonConfig.length; j++) {
            JButton button = (JButton) buttonConfig[j][0];
            button.setEnabled(buttonStates[newState][j]);
        }
        
        switch(newState) {
            case EDIT_NEW :

				LocalDataSourceConfig config = new LocalDataSourceConfig();
				configPanel.displayObject(config);
				selectedConfig = null;

                if(index > -1) {
                    // this flag is set to keep this method from being called
                    // from the valueChanged(ListSelectionEvent) method that will
                    // get fired as a result of removing the selection.
                    disableSelectionEvents = true;
                    listbox.removeSelectionInterval(index, index);
                }
                break;
            case EDIT_EXISTING :
                selectedConfig = (LocalDataSourceConfig) configVector.get(index);
                break;
            case NOTHING_SELECTED :
                configPanel.clearFields();
                break;
            case ITEM_SELECTED :
                LocalDataSourceConfig ld = (LocalDataSourceConfig) configVector.get(index);
                this.configPanel.displayObject(ld);
                break;

        }

        this.listbox.setEnabled(listStates[newState]);
        this.configPanel.setEditable(configPanelStates[newState]);

        screenState = newState;

    }

    LocalDataSourceConfig selectedConfig = null;

    public void actionPerformed(ActionEvent e) {

        String command = e.getActionCommand();

        if(command == null) command = "";
                
        if(command.equals("OPEN_NEW")) {
            setScreenState(EDIT_NEW);
        }
        else if(command.equals("EDIT_EXISTING")) {
            setScreenState(EDIT_EXISTING);
        }
        else if(command.equals("DELETE_EXISTING")) {

            int reply = JOptionPane.showConfirmDialog(this,
                "Do you want to delete the selected data source configuration?"
                + "\n(This operation cannot be undone.)", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if(reply == JOptionPane.NO_OPTION) return;

            int index = listbox.getSelectedIndex();
            if(index > -1) configVector.remove(index);
            listbox.setListData(configVector);
            configPanel.clearFields();
            setScreenState(NOTHING_SELECTED);

        }
        else if(command.equals("SAVE")) {

            // make sure a display name was entered.
            String displayName = configPanel.txtDisplayName.getText();
            if(Util.isNothing(displayName)) {
                GUIHelper.errMsg(this,
                    "Please enter a display name for this data source.",
                    null);
                configPanel.txtDisplayName.requestFocus();
                return;
            }

            if(selectedConfig != null) {
                configPanel.updateObject(selectedConfig);
            }
            else {
                selectedConfig = new LocalDataSourceConfig();
                configPanel.updateObject(selectedConfig);
                configVector.add(selectedConfig);
            }

            Collections.sort(configVector, LocalDataSourceConfig.DISPLAY_NAME_COMPARATOR);
            listbox.setListData(configVector);
            listbox.setSelectedValue(selectedConfig, true);
            
            selectedConfig = null;

            setScreenState(ITEM_SELECTED);
        
        }
        else if(command.equals("CANCEL")) {
            disableAndRefreshFields();
        }
        else if(command.equals("CLOSE")) {
            selectedItem = null;
            super.setVisible(false);
        }
        else if(command.equals("CONNECT")) {
            selectedItem = (LocalDataSourceConfig) listbox.getSelectedValue();
            super.setVisible(false);
        }
    }

    private LocalDataSourceConfig selectedItem = null;


    public LocalDataSourceConfig getSelectedItem() {
        return selectedItem;
    }


    public void setVisible(boolean b) {

        // clear any previous selections when the dialog is opened.
        if(b) {
            selectedItem = null;
            disableAndRefreshFields();        
        }
        super.setVisible(b);
    }



    /**
     * Called whenever the value of the selection changes.
     * @param e the event that characterizes the change.
     */
    public void valueChanged(ListSelectionEvent e) {
        if(disableSelectionEvents) {
            // when the selection is changed programatically, this flag can be
            // set to false to keep the event listener (this method) from
            // doing anything. The flag is reset automatically everytime it's
            // read to be true.
            disableSelectionEvents = false;
            return;
        }
        disableAndRefreshFields();
    }

    /**
     * If nothing is selected in the listbox, this will make sure that the fields
     * are cleared, and set the screen state to NOTHING_SELECTED. If something is
     * selected, this will populate the fields from the selected object and set
     * the screen state to ITEM_SELECTED.
     */
    private void disableAndRefreshFields() {
        int index = listbox.getSelectedIndex();
        if(index > -1) {
            setScreenState(ITEM_SELECTED);
        }
        else {
            setScreenState(NOTHING_SELECTED);
        }
    }


    public void setList(Vector configs) {

        Collections.sort(configs, LocalDataSourceConfig.DISPLAY_NAME_COMPARATOR);
        configVector = configs;
        listbox.setListData(configs);

    }

    public Vector getList() {
        return configVector;
    }



    public LocalDataSourceConfigDialog(Frame parent) {
        super(parent);

        JPanel cp = new JPanel();
        cp.setBorder(new EmptyBorder(10,10,10,10));
        cp.setLayout(new BorderLayout());


        JPanel p = new JPanel();
        p.setLayout(new BorderLayout());
        JLabel label = new JLabel("Data Sources");
        label.setForeground(Color.black);
        p.add(label, BorderLayout.NORTH);
        listbox.setVisibleRowCount(10);
//        listbox.setPreferredSize(new Dimension(200, 120));
//        listbox.setBorder(new ThinBevelBorder(ThinBevelBorder.LOWERED));
        listbox.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listbox.getSelectionModel().addListSelectionListener(this);
        p.setBorder(new EmptyBorder(0,0,10, 0));
        p.add(new JScrollPane(listbox), BorderLayout.CENTER);

        cp.add(p, BorderLayout.CENTER);
       


        p = new JPanel();
        p.setLayout(new BorderLayout());
        p.add(configPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        for(int j=0; j<buttonConfig.length; j++) {
            JButton button = (JButton) buttonConfig[j][0];
            button.setMnemonic(((String) buttonConfig[j][1]).charAt(0));
            button.setActionCommand((String) buttonConfig[j][2]);
            button.setToolTipText((String) buttonConfig[j][3]);
            button.addActionListener(this);
            buttonPanel.add(button);
        }
        p.add(buttonPanel, BorderLayout.SOUTH);

        // create an Action that clicks the cancel button
        ButtonClicker cancelClicker = new ButtonClicker(btnCancel);

        // give the cancelClicker a nested action that clicks the close button,
        // so if the cancel button is disabled, the close button will be clicked.
        cancelClicker.setNestedAction(new ButtonClicker(btnClose));

        // now bind the action to the ESCAPE key.
        KeyStroke esc = KeyStroke.getKeyStroke("ESCAPE");
        cp.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(esc, "_ESCAPE_");
        cp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(esc, "_ESCAPE_");
        cp.getActionMap().put("_ESCAPE_", cancelClicker);


        cp.add(p, BorderLayout.SOUTH);

        setContentPane(cp);

        setScreenState(NOTHING_SELECTED);

        setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);

        listbox.addMouseListener(new MouseAdapter() {
                                               public void mouseClicked(MouseEvent e) {
                                                   if(e.getClickCount() == 2) {
                                                       Point p = e.getPoint();
                                                       if(debug) System.out.println("clickpoint = " + p);
                                                       int clickedIndex = listbox.locationToIndex(p);

                                                       if(debug) System.out.println("clickedindex = " + clickedIndex);
                                                       if(clickedIndex > -1 && btnConnect.isEnabled()) {
                                                           btnConnect.doClick(0);
                                                       }
                                                   }
                                               }
                                           });


        setModal(true);
        pack();

    }

    public LocalDataSourceConfigDialog() {
        this(null);
    }

}
