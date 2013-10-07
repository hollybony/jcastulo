/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package caja.jcastulo.stream;

import caja.jcastulo.media.FrameIterator;

/**
 * Reader frame data concrete implementation would provide strategy to read bytes
 * 
 * @author Carlos Juarez
 */
public interface DataReader {
    
    /**
     * Reads a frame from the media and stores it in the FrameStorage.
     * 
     * @param reader - frame iterator where the bytes are read
     * @param frameStorage - frame storage that is updated with the read bytes
     * @throws InterruptedException 
     */
    public void readData(final FrameIterator reader, final FrameStorage frameStorage) throws InterruptedException;

    
}
