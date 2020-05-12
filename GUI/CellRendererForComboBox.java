package GUI;

import javax.swing.*;
import java.awt.*;

//got help for creating this class from:
//http://www.java2s.com/Tutorial/Java/0240__Swing/Comboboxcellrenderer.htm


/**
 * this class is a renderer for the JComboBox used in the tabs of body
 * @author      Negin Kheirmand <venuskheirmand@gmail.com>
 * @version     1.1
 * @since       1.0
 */


public class CellRendererForComboBox implements ListCellRenderer {
    protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
    JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);

    /**
     * the main method of this class
     * @param list the list passed to it
     * @param value the value passed to it
     * @param index the index passed to it
     * @param isSelected the boolean passed to it
     * @param cellHasFocus the boolean  passed to it
     * @return the component to be added in the JCombo box with this class as its renderer
     */
    public Component getListCellRendererComponent(JList list, Object value, int index,
                                                  boolean isSelected, boolean cellHasFocus) {
        Font theFont = null;
        Color theForeground = null;
        String theText = null;

        JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        if (value instanceof Object[]) {
            Object values[] = (Object[]) value;
            theFont = (Font) values[0];
            theForeground = (Color) values[1];
            theText = (String) values[2];
        } else {
            theFont = list.getFont();
            theForeground = list.getForeground();
            theText = "";
        }
        if (!isSelected) {
            renderer.setForeground(theForeground);
        }
        if(theText .equals("SEPARATOR")){
            return(separator);
        }
        renderer.setText(theText);
        renderer.setFont(theFont);
        return renderer;
    }
}
