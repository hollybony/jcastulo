/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package caja.gui.jtable;

import javax.swing.Icon;

/**
 *
 * @author Carlos Juarez
 */
public class ActionModel {
    
    private final Icon icon;
    
    private final String label;
    
    private Object payload;
    
    public ActionModel(Icon icon, String label){
        this(icon, label, null);
    }
    
    public ActionModel(Icon icon, String label, Object payload){
        this.icon = icon;
        this.label = label;
        this.payload = payload;
    }

    public Icon getIcon() {
        return icon;
    }

    public String getLabel() {
        return label;
    }

    public Object getPayload() {
        return payload;
    }
    
    public ActionModel copy(){
        return new ActionModel(icon, label, payload);
    }
    
    public void setPayload(Object object){
        payload = object;
    }

    @Override
    public String toString() {
        return "ActionModel{" + "icon=" + icon + ", label=" + label + ", payload=" + payload + '}';
    }
    
}
