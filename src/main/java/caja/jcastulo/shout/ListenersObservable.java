/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package caja.jcastulo.shout;

/**
 *
 * @author Carlos Juarez
 */
public interface ListenersObservable {
    
    public void addListenerUpdatesListener(ListenerUpdatesListener listenerUpdatesListener);
    
    public void removeListenerUpdatesListener(ListenerUpdatesListener listenerUpdatesListener);
    
}
