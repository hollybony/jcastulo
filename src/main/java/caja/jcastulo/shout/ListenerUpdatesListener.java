/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package caja.jcastulo.shout;

/**
 *
 * @author Carlos Juarez
 */
public interface ListenerUpdatesListener {

    public void listenerHasArrived(ClientSpec clientSpec);

    public void listenerHasGone(ClientSpec clientSpec);
}
