package org.glasser.swing;



import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.JTextComponent;
import javax.swing.border.EmptyBorder;
import org.glasser.util.*;
import org.glasser.sql.LoginHandler;
import org.glasser.sql.LoginHandlerException;


public class LoginDialog extends JDialog implements ActionListener {


    private Object loginResult = null;

    private JTextField txtUserId = new JTextField();

    private JPasswordField txtPassword = new JPasswordField();

    private JButton btnSubmit = new JButton("Submit");

    private JButton btnCancel = new JButton("Cancel");

    private LoginHandler loginHandler = null;


    public Object getLoginResult() {
        return loginResult;
    }

    public void setLoginHandler(LoginHandler loginHandler) {
        this.loginHandler = loginHandler;
    }       

    public LoginHandler getLoginHandler() {
        return loginHandler;
    }       

    public void setUserId(String userID) {
        this.txtUserId.setText(userID);
    }

    public void setPassword(String password) {
        txtPassword.setText(password);
    }

    public LoginDialog() {
        this(null);
    }

    public LoginDialog(Frame parent) {
        super(parent);

        setModal(true);
        
        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(10,10,10,10));
        GridBagConstraints gc = new GridBagConstraints();
        panel.setLayout(new GridBagLayout());
        JLabel idLbl = new JLabel("User ID:");
        JLabel pwLbl = new JLabel("Password:");
        Insets labelInsets = new Insets(0,0,0,0);
        Insets fieldInsets = new Insets(0,5,5,0);

        // add the User ID label
        gc.anchor = gc.NORTH;
        gc.fill = gc.HORIZONTAL;
        gc.insets = labelInsets;
        panel.add(idLbl, gc);

        // add the user ID textfield.
        gc.gridx = 1;
        gc.weightx = 1;
        gc.insets = fieldInsets;
        panel.add(this.txtUserId, gc);

        // add the password label
        gc.gridx = 0;
        gc.gridy = 1;
        gc.weightx = 0;
        gc.insets = labelInsets;
        panel.add(pwLbl, gc);

        // now add the password field
        gc.gridx = 1;
        gc.weightx = 1;
        gc.insets = fieldInsets;
        panel.add(this.txtPassword, gc);

        // add the button panel
        JPanel buttonPanel = new JPanel();
        gc.gridx = 0;
        gc.gridwidth = 2;
        gc.gridy = 2;
        // make this row "stretchable"
        gc.weighty = 1;
        gc.ipadx = 20;
        panel.add(buttonPanel, gc);

        // add the buttons to the button panel
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(btnSubmit);
        buttonPanel.add(btnCancel);

        btnSubmit.addActionListener(this);
        btnCancel.addActionListener(this);

        GUIHelper.enterPressesWhenFocused(btnSubmit);
        GUIHelper.enterPressesWhenFocused(btnCancel);

        // instantiate a key listener for the text boxes. This
        // will be used to make the focus move to their next focusable
        // component when the enter key is pressed.
        KeyHandler keyHandler = new KeyHandler();
        txtUserId.addKeyListener(keyHandler);
        txtPassword.addKeyListener(keyHandler);
        setTitle("Login");


        // this WindowListener will place the cursor into the first
        // blank textfield when the dialog is opened.
        this.addWindowListener(new WindowAdapter() {
                /**
                 * Focus the first blank field when the dialog is opened.
                 */
                public void windowOpened(WindowEvent e) {
                    placeCursor();
                }

                /**
                 * Focus the first blank field when the dialog is opened.
                 */
                public void windowActivated(WindowEvent e) {
                    placeCursor();
                }
            });

        setContentPane(panel);
        pack();

    }

    /**
     * This method is called when the dialog is opened. It places the
     * cursor in the first blank textbox it finds.
     * 
     * @return true if a blank field was found and a call to its requestFocus()
     * method was made, false otherwise.
     */
    private boolean placeCursor() {
        String s = txtUserId.getText();

        // if the userID field is blank, focus it.
        if(s == null || s.trim().length() == 0) {
            txtUserId.requestFocus();
            return true;
        }
        else {

            // if the password field is blank, focus it.
            s = null;
            char[] pw = txtPassword.getPassword();
            if(pw != null) {
                s = new String( txtPassword.getPassword() );
            }
            if(s == null || s.trim().length() == 0) {
                txtPassword.requestFocus();
                return true;
            }
        }
        return false;
    }


            

    public void actionPerformed(ActionEvent e) {

        if(e.getSource() == btnSubmit) {
            try {
                if(loginHandler == null) {
                    errMsg("No LoginHandler has been set for this LoginDialog.", "Application Error");
                    return;
                }

                String userID = this.txtUserId.getText();
                if(userID != null && userID.trim().length() == 0) {
                    userID = null;
                }
                
                char[] pwChars = txtPassword.getPassword();

                String pw = null;
                if(pwChars != null) pw = new String(pwChars); 

                loginResult = loginHandler.login(userID, pw);

                txtUserId.setText(null);
                txtPassword.setText(null);
                setVisible(false);
            }
            catch(Exception ex) {
                errMsg(ex.getMessage(), "Login Error");
            }
        }
        else if(e.getSource() == btnCancel) {
            setVisible(false);
        }
    }
 

    public void errMsg(String msg, String title) {
        if(title == null) title = "QueryForm";
        Toolkit.getDefaultToolkit().beep();
        JOptionPane.showMessageDialog(null,
                                      msg,
                                      title,
                                      JOptionPane.ERROR_MESSAGE);
    }

    /**
     * This will select all of the text in the given JTextField object. 
     */
    public void selectAll(JTextComponent field) {

        if(field == null) return;
        if(field.getText() == null) return;

        field.setSelectionStart(0);
        field.setSelectionEnd(field.getText().length());
        field.requestFocus();

    }


    class KeyHandler extends KeyAdapter {

        public void keyReleased(KeyEvent e) {
            if(e.getKeyCode() == e.VK_ENTER)  {
                if(e.getSource() == txtUserId) {
                    selectAll(txtPassword);
                }
                else if(e.getSource() == txtPassword) {
                    btnSubmit.doClick();
                }
            }
            else if(e.getKeyCode() == e.VK_ESCAPE) {
                txtUserId.setText(null);
                txtPassword.setText(null);
                setVisible(false);
            }
        }
    }


}
