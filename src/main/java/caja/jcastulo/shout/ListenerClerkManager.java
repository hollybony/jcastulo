/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package caja.jcastulo.shout;

import java.net.Socket;
import java.util.List;

/**
 * Handles all the listener clerks
 * 
 * @author Carlos Juarez
 */
public interface ListenerClerkManager extends ListenersObservable{

    /**
     * Starts attending a client socket
     * 
     * @param clientSocket - the client socket
     */
    public void attendListener(Socket clientSocket);

    /**
     * Shutdowns all the communication. Socket clients will be closed
     * 
     * @return <code>true</code> if actually the shutdown was carried out
     * @throws InterruptedException 
     */
    public boolean shutdown() throws InterruptedException;
    
    /**
     * Shutdowns the connection established with the clientSpec given
     * 
     * @param clientSpec - the listener client to close
     */
    public void shutdownListener(ClientSpec clientSpec);
    
    /**
     * Looks for the listeners that are listening the mount point given
     * 
     * @param mountpoint - the mountpoint
     * @return the client specs of the listeners
     */
    public List<ClientSpec> getClientSpecsByMountpoint(String mountpoint);
    
}
