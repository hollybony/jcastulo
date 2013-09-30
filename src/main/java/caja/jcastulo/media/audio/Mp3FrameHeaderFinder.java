/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package caja.jcastulo.media.audio;

import caja.jcastulo.media.ByteStreamReader;
import java.io.IOException;

/**
 *
 * @author Carlos Juarez
 */
public interface Mp3FrameHeaderFinder {
    
    long findNextHeader(Mp3FrameHeader currentHeader, ByteStreamReader byteStreamReader) throws IOException;
    
}
