/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package caja.jcastulo.shout;

import java.net.Socket;
import java.util.List;

/**
 *
 * @author Carlos Juarez
 */
public interface ListenerClerksManager extends ListenersObservable{

    public void createAndRunListenerClerk(Socket clientSocket);

    public boolean shutdown() throws InterruptedException;
    
    public void shutdownListenerClerk(ClientSpec clientSpec);
    
    public List<ClientSpec> getListenerClerksByMountpoint(String mountpoint);
    
}
