package org.glasser.swing;


import javax.swing.*; 
import java.awt.event.*;


/**
 * An Action that calls doClick() on a JButton if it is enabled. It's useful for
 * binding keystrokes to buttons without using mnemonics.
 */
public class ButtonClicker extends AbstractAction {

    protected Action nestedAction = null;

    protected AbstractButton button = null;

    public ButtonClicker(AbstractButton button) {
        if(button == null) throw new IllegalArgumentException("Required argument missing.");
        this.button = button;
    }

    public void actionPerformed(ActionEvent e) { 
        if(button.isEnabled()) {
            button.doClick();
        }
        else if(nestedAction != null) {
            nestedAction.actionPerformed(e);
        }
    }

    /**
     * If a nested Action is set, any ActionEvents passed to the actionPerformed
     * method of this Action will be forwarded to the nested Action if the button
     * is in a disabled state.
     */
    public void setNestedAction(Action nestedAction) {
        this.nestedAction = nestedAction;
    }

    public Action getNestedAction() {
        return nestedAction;
    }


}
