/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package caja.jcastulo.media.audio;

import caja.jcastulo.media.ByteStreamReader;
import java.io.IOException;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Carlos Juarez
 */
public class Mp3FrameHeaderFinderImpl implements Mp3FrameHeaderFinder{
    
    final org.slf4j.Logger logger = LoggerFactory.getLogger(Mp3FrameHeaderFinderImpl.class);

//    @Override
//    public long findNextHeader(Mp3FrameHeader currentHeader, ByteStreamReader byteStreamReader) throws IOException {
//        byte currentByte = 0;
//        while (true) {
//            byte lastByte = currentByte;
//            currentByte = byteStreamReader.read();
//            // Check for the start of the Mp3 Frame Header 
//            //first byte must be FF the second must starts with 111, so with bitwise and 1110,0000
//            //is possible to know if the second byte starts with 111
//            if (lastByte == (byte) 0xff && (currentByte & 0xE0) == 0xE0) {
//                long oldOffset = currentHeader.getOffset();
//                currentHeader.setOffset(byteStreamReader.getOffset() - 2);
//                currentHeader.setData(lastByte, currentByte, byteStreamReader.read(), byteStreamReader.read());
//                //log.info("Found frame start at index [" + (currentHeader.getOffset()) + "]");
//                logger.trace("oldOffset = "  + oldOffset + ", getOffset() = " + currentHeader.getOffset() + ", size = " + (currentHeader.getOffset()-oldOffset));
//                return currentHeader.getOffset()-oldOffset;
//            }
//        }
//    }
    
    @Override
    public long findNextHeader(Mp3FrameHeader currentHeader, ByteStreamReader byteStreamReader) throws IOException {
        byte byte2 = 0;
        byte byte3 = 0;
        while (true) {
            byte byte1 = byte2;
            byte2 = byte3;
            byte3 = byteStreamReader.read();
            // Check for the start of the Mp3 Frame Header 
            //first byte must be FF the second must starts with 111, so with bitwise and 1110,0000
            //is possible to know if the second byte starts with 111
            if (byte1 == (byte) 0xFF && (byte2 & 0xE0) == 0xE0 && (byte3 & 0xF0)!=0xF0) {
                long oldOffset = currentHeader.getOffset();
                currentHeader.setOffset(byteStreamReader.getOffset() - 3);
//                try{
//                    if(currentHeader.getOffset()>oldOffset + currentHeader.getFrameSize()){
//                        logger.debug("bytes between frame headers : " +  (currentHeader.getOffset()-(oldOffset + currentHeader.getFrameSize())));
//                    }
//                }catch(RuntimeException ex){   
//                }
                currentHeader.setData(byte1, byte2, byte3, byteStreamReader.read());
                //log.info("Found frame start at index [" + (currentHeader.getOffset()) + "]");
                logger.trace("oldOffset = "  + oldOffset + ", getOffset() = " + currentHeader.getOffset() + ", size = " + (currentHeader.getOffset()-oldOffset));
                return currentHeader.getOffset()-oldOffset;
            }
        }
    }
    
}
