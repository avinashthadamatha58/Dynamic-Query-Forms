package org.glasser.swing;



import javax.swing.*;
import java.awt.*;



/**
 * This is a blank icon. It does no actual painting, so whatever component it is contained
 * by should paint the area beneath it if necessary. It's primary use is as a spacer.
 */
public class BlankIcon implements Icon, java.io.Serializable {


    protected double size = 16.0;

    int width = 16;

    int height = 16;


    /**
     * Constructs a BlankIcon with a height and width of 16 pixels.
     */
    public BlankIcon() {
    }

    /**
     * Constructs a BlankIcon with a height and width equal to "size".
     */
    public BlankIcon(double size) {
        if(size < 1 || size > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("BlankIcon(int size): size must be between 1 and "
                + Integer.MAX_VALUE + ".");
        }

        this.size = size;

        width = (int) size;

        height = (int) size;

    }





    /**
     * This is an empty implementation, that does no actual painting.
     */
    public void paintIcon(Component c, Graphics g, int x, int y) {

    }


    /**
     * Returns the icon's width.
     *
     * @return an int specifying the fixed width of the icon.
     */
    public int getIconWidth() {
        return width;
    }
    /**
     * Returns the icon's height.
     *
     * @return an int specifying the fixed height of the icon.
     */
    public int getIconHeight() {
        return width;
    }


    /**
     * Launches a small demonstration program.
     */
    public static void main(String[] args) throws Exception {
         
        JFrame frame = new JFrame();
        JButton button = new JButton("Click");
        frame.setSize(200, 200);
        frame.setContentPane(new JPanel());
        frame.getContentPane().add(button);

        button.addActionListener(new java.awt.event.ActionListener() {
            int counter = 0;
            BevelArrowIcon upIcon = new BevelArrowIcon(10.0, BevelArrowIcon.UP);
            BevelArrowIcon downIcon = new BevelArrowIcon(10.0, BevelArrowIcon.DOWN);
            BlankIcon blankIcon = new BlankIcon(10.0);

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    JButton button = (JButton) e.getSource();
                    int  i = counter++ % 3;
                    if(i == 0) {
                        button.setIcon(upIcon);
                    }
                    else if(i == 1) {
                        button.setIcon(downIcon);
                    }
                    else {
                        button.setIcon(blankIcon);
                    }

                }
        });

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);



    } 
    




}
