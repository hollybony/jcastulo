/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package caja.gui.jtable;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

/**
 *
 * @author Carlos Juarez
 */
public class ColorCellRenderer extends DefaultTableCellRenderer{
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if(!isSelected && value instanceof Colorable){
            Color color = ((Colorable)value).getColor();
            if(color!=null){
                label.setBackground(color);
            }else{
                label.setBackground(table.getBackground());
            }
        }
        //Get the status for the current row.

        //Return the JLabel which renders the cell.
        return label;
    }
    
    
}
