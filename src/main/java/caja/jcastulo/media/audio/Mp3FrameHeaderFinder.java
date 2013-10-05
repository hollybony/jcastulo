/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package caja.jcastulo.media.audio;

import caja.jcastulo.media.ByteStreamReader;
import java.io.IOException;

/**
 * The strategy to retrieve frame headers from a <code>ByteStreamReader</code>
 * 
 * @author Carlos Juarez
 */
public interface Mp3FrameHeaderFinder {
    
    /**
     * Finds the next Frame header in the byteStreamReader
     * 
     * @param currentHeader which will be updated with the data of the next frame header found
     * @param byteStreamReader where the frame header is going to be found
     * @return the number of bytes read to find the next frame header
     * @throws IOException 
     */
    long findNextHeader(Mp3FrameHeader currentHeader, ByteStreamReader byteStreamReader) throws IOException;
    
}
