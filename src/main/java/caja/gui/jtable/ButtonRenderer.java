/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package caja.gui.jtable;

import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

/**
 * <code>TableCellRenderer</code> that displayed a button
 * @author Carlos Juarez
 */
public class ButtonRenderer extends JButton implements TableCellRenderer {
    
    /**
     * Creates a new instance of <code>ButtonRenderer</code> class
     */
    public ButtonRenderer() {
//        setOpaque(true);
    }
    
    /**
     * Creates a new instance of <code>ButtonRenderer</code> class
     * 
     * @param icon - icon to set
     */
    public ButtonRenderer(Icon icon) {
        super(icon);
//        setOpaque(true);
    }

    /**
     * Configure the button to be renderer
     * 
     * @param table
     * @param value - a <code>ActionModel</code> object is expected
     * @param isSelected
     * @param hasFocus
     * @param row
     * @param column
     * @return the button
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (isSelected) {
            setForeground(table.getSelectionForeground());
            setBackground(table.getSelectionBackground());
        } else {
            setForeground(table.getForeground());
            setBackground(UIManager.getColor("Button.background"));
        }
        if(value!=null && value instanceof ActionModel){
            ActionModel actionModel = (ActionModel) value;
//            setText(actionModel.getLabel());
            setToolTipText(actionModel.getLabel());
            setIcon(actionModel.getIcon());
        }
        return this;
    }
}
