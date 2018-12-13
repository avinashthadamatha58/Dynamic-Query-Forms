package org.glasser.swing;



import javax.swing.*;
import java.awt.*;



public class ScrollingDesktopPane extends JDesktopPane {



    public ScrollingDesktopPane() {
        setDesktopManager(new ScrollingDesktopManager());
    }
        
    /**
     * This method will examine the size and location of
     * all of the JInternalFrames in this JDesktopPane and 
     * return a preferred size that will encompass all of them.
     */
    public Dimension getPreferredSize() {

        

        JInternalFrame[] frames = this.getAllFrames();
        if(frames == null || frames.length == 0) {
            // nothing to do.
            return super.getPreferredSize();
        }

        int minX=0, minY=0, maxX=0, maxY=0;
        
        for(int j=0; j<frames.length; j++) {

            JInternalFrame jif = frames[j];
            Rectangle r = null;

            // if this internal frame is iconified, we need
            // to use the bounds for its desktop icon.
            if(jif.isIcon()) {
                r = jif.getDesktopIcon().getBounds();
            }
            else {
                r = jif.getBounds();
            }
            
            jif.getBounds();
            minX = (int) Math.min(minX, r.x);
            minY = (int) Math.min(minY, r.y);
            maxX = (int) Math.max(maxX, (r.x + r.width));
            maxY = (int) Math.max(maxY, (r.y + r.height));

        }

        Dimension d = new Dimension(Math.abs(minX) + maxX, Math.abs(minY) + maxY);
        //System.out.println("TRC: " + getClass().getName() + ".getPreferredSize() returning " + d);
        return d;



    }


}
