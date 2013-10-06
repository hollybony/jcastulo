/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package caja.jcastulo.shout;

/**
 * Supports adding and removing server listeners
 * 
 * @author Carlos Juarez
 */
public interface ServerObservable {
    
    /**
     * Different server status
     */
    public enum ServerStatus{
        RUNNING, STOPPED
    }
    
    /**
     * @param serverListener - a new server listener to add
     */
    public void addServerListener(ServerListener serverListener);
    
    /**
     * @param serverListener - the server listener to be removed
     */
    public void removeServerListener(ServerListener serverListener);
    
}
