package org.glasser.swing;



import javax.swing.*;
import java.awt.*;



/**
 * This is a very limited, basic Icon implementation that can be used as a beveled
 * arrow pointing up or down on a JButton.
 */
public class BevelArrowIcon implements Icon, java.io.Serializable {

    /**
     * Given the base of an equilateral triangle, this multiplier will give you its height.
     */
    protected final static double heightMultiplier = 0.86602540378443864676372317075294;

    protected double size = 16.0;


    public final static int UP = 0;

    public final static int DOWN = 1;

    protected int direction = UP;



    protected int margin;

    protected int height;

    protected int width;

    protected int middle;

    /**
     * Constructs a BevelArrowIcon with a base of 10 pixels, and the given direction.
     * 
     * @param direction indicates what direction the arrow should point, either BevelArrowIcon.UP
     * or BevelArrowIcon.DOWN.
     */
    public BevelArrowIcon(int direction) {
        this(10.0, direction);
    }

    /**
     * Constructs a BevelArrowIcon with a base of "size" pixels, and the given direction.
     * 
     * @param direction indicates what direction the arrow should point, either BevelArrowIcon.UP
     * or BevelArrowIcon.DOWN.
     */
    public BevelArrowIcon(double size, int direction) {
        if(size < 8 || size > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("BevelArrowIcon(int size): size must be between 8 and "
                + Integer.MAX_VALUE + ".");
        }

        this.size = size;

        width = (int) size;

        height = (int) (heightMultiplier * size);

        margin = (int) ((size - (heightMultiplier * size)) / 2.0);

        middle = (int) (size / 2.0);

        if(direction == UP) {
            this.direction = UP;
        }
        else {
            this.direction = DOWN;
        }
    }


    /**
     * Draw the icon at the specified location.  Icon implementations
     * may use the Component argument to get properties useful for
     * painting, e.g. the foreground or background color.
     */
    public void paintIcon(Component c, Graphics g, int x, int y) {

        Color background = c.getBackground();
        Color highlight = background.brighter();
        Color shadowColor = background.darker();
        if(c instanceof AbstractButton) {
            ButtonModel model = ((AbstractButton) c).getModel();

            // if the parent component is a button that is "armed" (pressed
            // but not yet released) then paint the background in the
            // dark shadow color.
            if(model.isArmed()) {
                shadowColor = shadowColor.darker();
            }
        }


        int topY = y + margin;
        int bottomY = topY + height;
        int rightX = x + width;
        int axisX = x + middle;


        if(direction == UP) {

            g.setColor(highlight);
            g.drawLine(x, bottomY, rightX, bottomY);
            g.drawLine(rightX, bottomY, axisX, topY);

            g.setColor(shadowColor);
            g.drawLine(axisX, topY, x, bottomY);

        }
        else {
            g.setColor(shadowColor);
            g.drawLine(x, topY, rightX, topY);
            g.drawLine(x, topY, axisX, bottomY);

            g.setColor(highlight);
            g.drawLine(axisX, bottomY, rightX, topY);

        }
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
            BevelArrowIcon upIcon = new BevelArrowIcon(UP);
            BevelArrowIcon downIcon = new BevelArrowIcon(DOWN);
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    JButton button = (JButton) e.getSource();
                    counter++;
                    if(counter % 2 == 0) {
                        button.setIcon(upIcon);
                    }
                    else {
                        button.setIcon(downIcon);
                    }

                }
        });

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);



    } 
    




}
