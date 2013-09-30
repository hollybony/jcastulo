/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package caja.jcastulo.stream;

import caja.jcastulo.media.FrameIterator;

/**
 *
 * @author Carlos Juarez
 */
public interface DataReader {
    
    /**
     * Reads a frame from the media and stores it in the FrameStorage.
     *
     * @param reader
     * @return
     */
    public void readData(final FrameIterator reader, final FrameStorage frameStorage) throws InterruptedException;

    
}
