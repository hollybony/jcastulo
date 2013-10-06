/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package caja.jcastulo.shout;

/**
 * Represents a listener that listens a stream listener
 * 
 * @author Carlos Juarez
 */
public interface ListenerUpdatesListener {

    /**
     * Invoked when the stream listener starts the connection
     * 
     * @param clientSpec 
     */
    public void listenerHasArrived(ClientSpec clientSpec);

    /**
     * Invoked when the stream listener terminates the connection
     * 
     * @param clientSpec 
     */
    public void listenerHasGone(ClientSpec clientSpec);
}
