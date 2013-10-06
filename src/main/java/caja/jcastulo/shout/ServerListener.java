/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package caja.jcastulo.shout;

/**
 * To receive server changing status
 * 
 * @author Carlos Juarez
 */
public interface ServerListener {
    
    /**
     * Called when the server has changed its status
     * 
     * @param serverStatus - the new status
     */
    public void notifyNewServerStatus(final ServerObservable.ServerStatus serverStatus);
        
}
