package org.glasser.swing;


import java.awt.*;
import java.util.*;
import javax.swing.*;

import org.glasser.sql.*;
import org.glasser.swing.text.*;
import org.glasser.util.*;



public class LocalDataSourceConfigPanel extends JPanel {




    public final JTextField txtDisplayName = new JTextField();

    public final DriverClassSelector cmbDriverClass = new DriverClassSelector();

    public final JTextField txtUrl = new JTextField();

    public final JCheckBox chkLoginRequired = new JCheckBox();

    public final JTextField txtUserName = new JTextField();

    public final JPasswordField txtPassword = new JPasswordField();

    public final JTextField txtMaxConnections = new JTextField();

    public final JTextField txtLoginTimeout = new JTextField();

    public Object[][] config = 
    {
         {txtDisplayName, "Display Name", "Name for this connection."}
        ,{cmbDriverClass, "Driver Class", "The fully-qualified class name for the JDBC driver to be used. (Enter it manually if it does not appear in the dropdown.)"}
        ,{txtUrl, "Database URL", "The URL that will be used to connect to this data source."}
        ,{chkLoginRequired, "Requires Login", "Indicates if a user/password is needed to connect to this data source."}
        ,{txtUserName, "User Name", "If required, the user name used to connect to this data source. (Leave blank to prompt for user name.)"}
        ,{txtPassword, "Password", "If required, the password used to connect to this data source. (Leave blank to prompt for password.)"}
        ,{txtMaxConnections, "Max Connections", "The maximum number of connections that will be pooled. (Enter 0 or leave blank for no maximum.)"}
        ,{txtLoginTimeout, "Login Timeout", "The maximum time, in seconds, to wait for a successful login to this data source. (Enter 0 or leave blank for no maximum.)"}
    };


    public LocalDataSourceConfigPanel() {

        GUIHelper.buildFormPanel(this, config, 250, 6, null, Color.black, false, -1);

        // make txtMaxConnections and txtLoginTimeout numeric-input only.
        NumericDocument nd = new NumericDocument();
        nd.setMinValue(0);
        nd.setMaxValue(Integer.MAX_VALUE);
        txtMaxConnections.setDocument(nd);

        nd = new NumericDocument();
        nd.setMinValue(0);
        nd.setMaxValue(Integer.MAX_VALUE);
        txtLoginTimeout.setDocument(nd);

    }


    public void setEditable(boolean b) {

        txtDisplayName.setEditable(b);
        cmbDriverClass.setEnabled(b);
        txtUrl.setEditable(b);
        chkLoginRequired.setEnabled(b);
        txtUserName.setEditable(b);
        txtPassword.setEditable(b);
        txtMaxConnections.setEditable(b);
        txtLoginTimeout.setEditable(b);

    }

    public void displayObject(LocalDataSourceConfig config) {

        txtDisplayName.setText(config.getDisplayName());

        String driverClass = config.getDriverClassName();
        if(driverClass == null || (driverClass = driverClass.trim()).length() == 0) {
            driverClass = DriverClassSelector.EMPTY_ITEM;
        }
        boolean cmbState = cmbDriverClass.isEditable();
        cmbDriverClass.setEditable(true);
        cmbDriverClass.setSelectedItem(driverClass);
        cmbDriverClass.setEditable(cmbState);

        txtUrl.setText(config.getUrl());
        chkLoginRequired.setSelected(config.isLoginRequired());
        txtUserName.setText(config.getUser());
        txtPassword.setText(config.getPassword());

        Integer maxConnections = config.getMaxConnections();
        String s = maxConnections == null ? null : maxConnections.toString();
        txtMaxConnections.setText(s);

        Integer loginTimeout = config.getLoginTimeout();
        s = loginTimeout == null ? null : loginTimeout.toString();
        txtLoginTimeout.setText(s);

    }


    public void clearFields() {
        txtDisplayName.setText(null);
        cmbDriverClass.setSelectedItem(cmbDriverClass.EMPTY_ITEM);
        txtUrl.setText(null);
        chkLoginRequired.setSelected(false);
        txtUserName.setText(null);
        txtPassword.setText(null);
        txtMaxConnections.setText(null);
        txtLoginTimeout.setText(null);
    }

    public void updateObject(LocalDataSourceConfig config) {

        config.setDisplayName(Util.trimToNull(txtDisplayName.getText()));
        config.setDriverClassName(Util.trimToNull((String) cmbDriverClass.getSelectedItem()));
        config.setUrl(Util.trimToNull(txtUrl.getText()));
        config.setLoginRequired(chkLoginRequired.isSelected());
        
        config.setUser(Util.trimToNull(txtUserName.getText()));
        config.setPassword(Util.trimToNull(txtPassword.getText()));
        
        String s = Util.trimToNull(txtMaxConnections.getText());
        if(s != null) config.setMaxConnections(new Integer(s));
        else config.setMaxConnections(null);
        
        s = Util.trimToNull(txtLoginTimeout.getText());
        if(s != null) config.setLoginTimeout(new Integer(s));
        else config.setLoginTimeout(null);
    }



}
