package org.glasser.swing;

import java.awt.event.*;
import javax.swing.*;



public class EnterEscapeKeyHandler extends KeyAdapter {

    JButton enterButton = null;
    JButton escapeButton = null;


    public EnterEscapeKeyHandler(JButton enterButton, JButton escapeButton) {

        this.enterButton = enterButton;
        this.escapeButton = escapeButton;

    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if(key == e.VK_ENTER) {
            if(enterButton != null) enterButton.doClick();
        }
        else if(key == e.VK_ESCAPE) {
            if(escapeButton != null) escapeButton.doClick();
        }
    }

}

