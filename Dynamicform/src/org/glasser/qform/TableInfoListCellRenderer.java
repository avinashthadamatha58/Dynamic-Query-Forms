package org.glasser.qform;

import javax.swing.*;
import java.awt.*;
import org.glasser.sql.*;



public class TableInfoListCellRenderer extends DefaultListCellRenderer {


	java.util.HashMap italicFontMap = new java.util.HashMap();

    public Component getListCellRendererComponent(JList list,
                                              Object value,
                                              int index,
                                              boolean isSelected,
                                              boolean cellHasFocus) {

        JLabel component = (JLabel) super.getListCellRendererComponent(list,value,index,isSelected,cellHasFocus);

        if(value instanceof TableInfo) {
            TableInfo ti = (TableInfo) value;
            if("VIEW".equals(ti.getTableType())) {
				Font font = component.getFont();
				Font viewFont = (Font) italicFontMap.get(font);
				if(viewFont == null) {
					viewFont = font.deriveFont(Font.ITALIC);
					italicFontMap.put(font, viewFont);
				}
                component.setFont(viewFont);
            }
        }

        return component;

    }


}
