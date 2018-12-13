package org.glasser.swing;


import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * This Border class is essentially the same as javax.swing.border.BevelBorder, except that
 * it is one pixel wide instead of two.
 */
public class ThinBevelBorder implements java.io.Serializable, javax.swing.border.Border {

    public final static int RAISED = BevelBorder.RAISED;
    public final static int LOWERED = BevelBorder.LOWERED;

    protected int bevelType = RAISED;

    protected Color shadowColor = null;
    
    protected Color highlightColor = null;


    public Insets getBorderInsets(Component c) {
        return new Insets(2, 2, 2, 2);
    }

    /** 
     * Reinitialize the insets parameter with this Border's current Insets. 
     * @param c the component for which this border insets value applies
     * @param insets the object to be reinitialized
     */
    public Insets getBorderInsets(Component c, Insets insets) {
        insets.left = insets.top = insets.right = insets.bottom = 2;
        return insets;
    }

    public int getBevelType() {
        return bevelType;
    }

    public void setBevelType(int type) {
        bevelType = type;
    }

    public boolean isBorderOpaque() {
        return true;
    }

    public ThinBevelBorder() {
    }

    public ThinBevelBorder(int bevelType) {
        this.bevelType = bevelType;
    }

    public ThinBevelBorder(int bevelType, Color shadowColor, Color highlightColor) {
        this.bevelType = bevelType;
        this.shadowColor = shadowColor;
        this.highlightColor = highlightColor;
    }


    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {

        Color highlight = highlightColor == null ? c.getBackground().brighter() : highlightColor;
        Color shadow = shadowColor == null ? c.getBackground().darker() : shadowColor;

        Color upperLeft, lowerRight;

        if(bevelType == RAISED) {
            upperLeft = highlight;
            lowerRight = shadow;
        }
        else {
            upperLeft = shadow;
            lowerRight = highlight;
        }

        int left = x;
        int right = width - 1;
        int top = y;
        int bottom = height - 1;

        g.setColor(upperLeft);
        g.drawLine(left, top, right, top);
        g.drawLine(left, top, left, bottom);

        g.setColor(lowerRight);
        g.drawLine(right, bottom, left, bottom);
        g.drawLine(right, bottom, right, top);

    }
}

