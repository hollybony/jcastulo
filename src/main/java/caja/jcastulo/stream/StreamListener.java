/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package caja.jcastulo.stream;

/**
 * Listener of stream changes
 *
 * @author Carlos Juarez
 */
public interface StreamListener {
    
    /**
     * Invoked when the media playing has changed
     */
    public void mediaChanged();
    
    /**Invoked when the media queue has been modified
     * 
     */
    public void queueChanged();
    
}
