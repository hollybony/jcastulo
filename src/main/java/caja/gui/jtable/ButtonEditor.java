/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package caja.gui.jtable;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

/**
 * Cell editor that allows to set a <code>JButton</code> at edition time
 * 
 * @author Carlos Juarez
 */
public class ButtonEditor extends DefaultCellEditor {

    /**
     * The button that will be displayed during edition
     */
    protected JButton button;
    
    /**
     * The label used as tooltip of the button
     */
    private String label;
    
    /**
     * Indicates whether the button is pushed
     */
    private boolean isPushed;
    
    /**
     * Action to perform in the button click event
     */
    private ActionListener actionListener;
    
    /**
     * event to be sent in the button click event
     */
    private ActionEvent event;
    
    /**
     * Addition data to be set as the source of the event
     */
    private Object payload;

    /**
     * Creates an instance of <code>ButtonEditor</code> class
     */
    public ButtonEditor() {
        this(null);
    }
    
    /**
     * Creates an instance of <code>ButtonEditor</code> class
     * 
     * @param actionListener - actionListener to set
     */
    public ButtonEditor(ActionListener actionListener) {
        super(new JCheckBox());
        button = new JButton();
        this.actionListener = actionListener;
        button.setOpaque(true);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                event = e;
                event.setSource(payload);
                fireEditingStopped();
            }
        });
    }
    
    /**
     * A button is used as cell editor component
     * @param table
     * @param value
     * @param isSelected
     * @param row
     * @param column
     * @return the button
     */
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        if (isSelected) {
            button.setForeground(table.getSelectionForeground());
            button.setBackground(table.getSelectionBackground());
        } else {
            button.setForeground(table.getForeground());
            button.setBackground(table.getBackground());
        }
        if(value!=null && value instanceof ActionModel){
            ActionModel actionModel = (ActionModel) value;
//            button.setText(actionModel.getLabel());
            button.setToolTipText(actionModel.getLabel());
            button.setIcon(actionModel.getIcon());
            label = actionModel.getLabel();
            if(actionModel.getPayload()==null){
                Object[] rowData = new Object[table.getModel().getColumnCount()];
                for(int i = 0; i<table.getModel().getColumnCount();i++){
                    rowData[i]=table.getModel().getValueAt(row, i);
                }
                payload = rowData;
            }else{
                payload = actionModel.getPayload();
            }
        }
        isPushed = true;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        if (isPushed) {
            if(actionListener!=null){
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        actionListener.actionPerformed(event);
                    }
                });
            }
        }
        isPushed = false;
        return label;
    }

    @Override
    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }

    @Override
    protected void fireEditingStopped() {
        super.fireEditingStopped();
    }
}
