package org.glasser.swing.table;




import java.text.*;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * This TableCellRenderer implementation renders objects by using a 
 * java.text.Format instance. If null is passed as the constructor
 * argument, then it renders objects using their toString() method.
 */
public class FormattedCellRenderer extends DefaultTableCellRenderer {

    protected Format format = null;

    /**
     * Constructs an instance that will render objects using the supplied format
     * object. If the format object is null, then the constructed instance will
     * render objects using their toString() method.
     */
    public FormattedCellRenderer(Format format) {
        this.format = format;
    }

    protected void setValue(Object o) {

        if(o == null) {
            setText("");
            return;
        }

        if(format == null) {
            setText(o.toString());
        }
        else {
            setText(format.format(o));
        }
    }
}






