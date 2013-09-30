/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package caja.jcastulo.shout;

/**
 *
 * @author Carlos Juarez
 */
public interface ServerObservable {
    
    public enum ServerStatus{
        RUNNING, STOPPED
    }
    
    public void addServerListener(ServerListener serverListener);
    
    public void removeServerListener(ServerListener serverListener);
    
}
