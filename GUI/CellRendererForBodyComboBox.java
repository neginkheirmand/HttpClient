package GUI;

import javax.swing.*;
import java.awt.*;

//got help for creating this class from:
//http://www.java2s.com/Tutorial/Java/0240__Swing/Comboboxcellrenderer.htm

public class CellRendererForBodyComboBox implements ListCellRenderer {
    protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
    JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Font theFont = null;
        Color theForeground = null;
        Icon theIcon = null;
        String theText = null;
        JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

//        { new Font("Serif", Font.PLAIN, 15), new java.awt.Color(128,128,128), icon, "STRUCTURED}
        if (value instanceof Object[]) {
            Object values[] = (Object[]) value;
            theFont = (Font) values[0];
            theForeground = (Color) values[1];
            theIcon = (Icon) values[2];
            theText = (String) values[3];

        } else {
            theFont = list.getFont();
            theForeground = list.getForeground();
            theText = "";
        }
        if (!isSelected) {
            renderer.setForeground(theForeground);
        }
        if (theIcon != null) {
            renderer.setIcon(theIcon);
        }

        if (theText.equals("SEPARATOR")) {
            return (separator);
        }
        renderer.setText(theText);
        renderer.setFont(theFont);
        return renderer;
    }
}