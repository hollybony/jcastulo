/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package caja.jcastulo.stream;

import caja.jcastulo.media.FrameIterator;

/**
 * Updates frame storage with new data from a frame iterator. Concrete implementation
 * would provide strategy to read the bytes
 * 
 * @author Carlos Juarez
 */
public interface FrameStorageUpdater {
    
    /**
     * Updates fromeStorage with new frames. The strategy about how the frame storage is going
     * to be updated the frame storage depends on the concrete implementations
     * 
     * @param iterator - frame iterator where the bytes are read
     * @param frameStorage - frame storage that is updated with the read bytes
     * @throws InterruptedException 
     */
    public void readData(final FrameIterator iterator, final FrameStorage frameStorage) throws InterruptedException;
    
}
