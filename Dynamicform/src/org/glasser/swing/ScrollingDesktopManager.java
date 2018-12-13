package org.glasser.swing;



import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.beans.PropertyVetoException;



public class ScrollingDesktopManager extends DefaultDesktopManager {


    /**
     * Searches up the ancestor hierarchy for the given JInternalFrame until
     * it finds a JScrollPane, and then calls validate() on that scrollpane.
     */
    private void validateScrollPane(JComponent f) {

        Component parent = f;
        while((parent = parent.getParent()) != null) {
            if(parent instanceof JScrollPane) {
//                System.out.println("About to validate scrollpane");
                parent.validate();
                break;
            }
        }
    }


    /** Generally, this indicates that the frame should be restored to it's
      * size and position prior to a maximizeFrame() call.
      */
    public void minimizeFrame(JInternalFrame f) {
       
        super.minimizeFrame(f);
        validateScrollPane(f);
        
    }


    /** If possible, display this frame in an appropriate location.
      * Normally, this is not called, as the creator of the JInternalFrame
      * will add the frame to the appropriate parent.
      */
    public void openFrame(JInternalFrame f) {
        super.openFrame(f);
        validateScrollPane(f);
    }


    /** Generally, this call should remove the frame from it's parent. */
    public void closeFrame(JInternalFrame f) {
        super.closeFrame(f);
        validateScrollPane(f);
    }


    /** Generally, remove any iconic representation that is present and restore the
      * frame to it's original size and location.
      */
    public void deiconifyFrame(JInternalFrame f) {
        super.deiconifyFrame(f);
        validateScrollPane(f);
    }


    /** Generally, the frame should be resized to match it's parents bounds. */
    /**
     * Resizes the frame to fill its parents bounds.
     * @param the frame to be resized
     */
    public void maximizeFrame(JInternalFrame f) {
    
        Rectangle p;
        if(!f.isIcon()) {
            Container c = GUIHelper.getViewport(f);
            if(c == null) return;
            p = c.getBounds();
        } 
        else {
            Container c = f.getDesktopIcon().getParent();
            if(c == null)
                return;
            p = c.getBounds();
            try {
                f.setIcon(false);
            } 
            catch(PropertyVetoException e2) {
            }
        }
        f.setNormalBounds(f.getBounds());
        setBoundsForFrame(f, 0, 0, p.width, p.height);
        try {
            f.setSelected(true);
        } 
        catch(PropertyVetoException e2) {
        }
    
        removeIconFor(f);
    }



//    /**
//     * Generally, indicate that this frame has lost focus. This is usually called
//     * after the JInternalFrame's IS_SELECTED_PROPERTY has been set to false.
//     */
//    public void deactivateFrame(JInternalFrame f) {
//        super.deactivateFrame(f);
//        validateScrollPane(f);
//    }
//
//
    /** Generally, remove this frame from it's parent and add an iconic representation. */
    public void iconifyFrame(JInternalFrame f) {
        super.iconifyFrame(f);
        validateScrollPane(f);
    }
//
//
////    /** The user has moved the frame. Calls to this method will be preceded by calls
//      * to beginDraggingFrame().
//      *  Normally <b>f</b> will be a JInternalFrame.
//      */
//    public void dragFrame(JComponent f, int newX, int newY) {
//        super.dragFrame(f, newX, newY);
//    }
//
//
//    /**
//     * Generally, indicate that this frame has focus. This is usually called after
//     * the JInternalFrame's IS_SELECTED_PROPERTY has been set to true.
//     */
//    public void activateFrame(JInternalFrame f) {
//        super.activateFrame(f);
//        validateScrollPane(f);
//    }


    /** This methods is normally called when the user has indicated that
      * they will begin resizing the frame. This method should be called
      * prior to any resizeFrame() calls to allow the DesktopManager to prepare any
      * necessary state.  Normally <b>f</b> will be a JInternalFrame.
      */
    public void beginResizingFrame(JComponent f, int direction) {
        super.beginResizingFrame(f, direction);
    }

    /** This method is normally called when the user has indicated that
      * they will begin dragging a component around. This method should be called
      * prior to any dragFrame() calls to allow the DesktopManager to prepare any
      * necessary state. Normally <b>f</b> will be a JInternalFrame.
      */
    public void beginDraggingFrame(JComponent f) {
        super.beginDraggingFrame(f);
    }
    /** This method signals the end of the resize session. Any state maintained by
      * the DesktopManager can be removed here.  Normally <b>f</b> will be a JInternalFrame.
      */
    public void endResizingFrame(JComponent f) {
        super.endResizingFrame(f);
//        System.out.println("END RESIZING " + f.getBounds());
        if(f instanceof JInternalFrame) {
            validateScrollPane((JInternalFrame) f);
        }
    }
    /** This method signals the end of the dragging session. Any state maintained by
      * the DesktopManager can be removed here.  Normally <b>f</b> will be a JInternalFrame.
      */
    public void endDraggingFrame(JComponent f) {
        super.endDraggingFrame(f);
//        System.out.println("END DRAGGING " + f.getBounds());
//        if(f instanceof JInternalFrame) {
            validateScrollPane(f);
//        }    
    }
    /** The user has resized the component. Calls to this method will be preceded by calls
      * to beginResizingFrame().
      *  Normally <b>f</b> will be a JInternalFrame.
      */
    public void resizeFrame(JComponent f, int newX, int newY, int newWidth, int newHeight) {
//        System.out.println("TRC: " + getClass().getName() + ".resizeFrame()");
        super.resizeFrame(f, newX, newY, newWidth, newHeight);
    }
    /** This is a primitive reshape method.*/
    public void setBoundsForFrame(JComponent f, int newX, int newY, int newWidth, int newHeight) {
//        System.out.println("TRC: " + getClass().getName() + ".setBoundsForFrame()");
        super.setBoundsForFrame(f, newX, newY, newWidth, newHeight);
    }



}
