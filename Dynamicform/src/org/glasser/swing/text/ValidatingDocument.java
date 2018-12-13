
package org.glasser.swing.text;



import javax.swing.*;
import javax.swing.text.*;

public abstract class ValidatingDocument extends PlainDocument {


    abstract protected boolean isValidValue(String newText);

    public void insertString(int offset, String s, 
                             AttributeSet a) 
    throws BadLocationException 
    {  
        if(s == null) return;

        // first, assemble a copy of what the new contents will look
        // like if this string is inserted.
        String currText = getText(0, getLength());
        StringBuffer buffer = new StringBuffer(currText.length() + s.length() + 10);
        buffer.append(currText.substring(0, offset));
        buffer.append(s);
        buffer.append(currText.substring(offset));
        String newText = buffer.toString();

        // if the new contents would be valid, insert the string, 
        // otherwise return quietly.
        if(isValidValue(newText)) {
            super.insertString(offset, s, a);
        }
    }

}
