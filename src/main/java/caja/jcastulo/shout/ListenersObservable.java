/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package caja.jcastulo.shout;

/**
 * Supports adding and removing listeners
 * 
 * @author Carlos Juarez
 */
public interface ListenersObservable {
    
    /**
     * @param listenerUpdatesListener - a new listenerUpdatesListener to add
     */
    public void addListenerUpdatesListener(ListenerUpdatesListener listenerUpdatesListener);
    
    /**
     * @param listenerUpdatesListener - the listenerUpdatesListener to remove
     */
    public void removeListenerUpdatesListener(ListenerUpdatesListener listenerUpdatesListener);
    
}
