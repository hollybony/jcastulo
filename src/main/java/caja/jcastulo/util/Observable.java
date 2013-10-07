/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package caja.jcastulo.util;

/**
 * Observable support to be implemented in case some one wants to implements observable patten by composition
 * 
 * @author Carlos Juarez
 */
public interface Observable<T> {

    /**
     * Adds the given listener
     * 
     * @param listener - listener to add
     */
    public void addListener(T listener);

    /**
     * Removes the given listener
     * 
     * @param listener - listener to remove
     */
    public void removeListener(T listener);
}
