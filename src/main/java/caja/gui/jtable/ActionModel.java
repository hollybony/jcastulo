/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package caja.gui.jtable;

import javax.swing.Icon;

/**
 * Represents a button model within a <code>JTable</code>. It can be used to hold a <code>JButton</code>
 * as a cell value within a <code>JTable<code>
 * 
 * @author Carlos Juarez
 */
public class ActionModel {
    
    /**
     * Represents an action
     */
    private final Icon icon;
    
    /**
     * Text that represents this model
     */
    private final String label;
    
    /**
     * Extra information
     */
    private Object payload;
    
    /**
     * Constructs an instance of <code>ActionModel</code> class
     * 
     * @param icon - the icon to set
     * @param label - the label to set
     */
    public ActionModel(Icon icon, String label){
        this(icon, label, null);
    }
    
    /**
     * Constructs an instance of <code>ActionModel</code> class
     * 
     * @param icon - the icon to set
     * @param label - the label to set
     * @param payload - the payload to set
     */
    public ActionModel(Icon icon, String label, Object payload){
        this.icon = icon;
        this.label = label;
        this.payload = payload;
    }

    /**
     * @return the icon
     */
    public Icon getIcon() {
        return icon;
    }

    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * @return the payload
     */
    public Object getPayload() {
        return payload;
    }
    
    /**
     * Copies the object that holds this reference to a new object
     * 
     * @return the copy
     */
    public ActionModel copy(){
        return new ActionModel(icon, label, payload);
    }
    
    /**
     * Set the payload
     * 
     * @param object - the payload to set
     */
    public void setPayload(Object object){
        payload = object;
    }

    @Override
    public String toString() {
        return "ActionModel{" + "icon=" + icon + ", label=" + label + ", payload=" + payload + '}';
    }
    
}
