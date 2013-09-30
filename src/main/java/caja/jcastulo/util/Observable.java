/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package caja.jcastulo.util;

/**
 *
 * @author Carlos Juarez
 */
public interface Observable<T> {

    public void addListener(T listener);

    public void removeListener(T listener);
}
