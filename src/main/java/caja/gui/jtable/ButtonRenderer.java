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
 *
 * @author Carlos Juarez
 */
public class ButtonRenderer extends JButton implements TableCellRenderer {
    
    public ButtonRenderer() {
//        setOpaque(true);
    }
    
    public ButtonRenderer(Icon icon) {
        super(icon);
//        setOpaque(true);
    }

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
