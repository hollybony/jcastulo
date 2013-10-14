package caja.jcastulo.media;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import org.slf4j.LoggerFactory;

/**
 * Used by sub packages of this module
 * 
 * @author Carlos Juarez
 */
public class ByteStreamReader {

    /**
     * The logger
     */
    final org.slf4j.Logger logger = LoggerFactory.getLogger(ByteStreamReader.class);
    
    /**
     * The inputStream
     */
    private InputStream inputStream = null;
    
    /**
     * readIndex              writeIndex
     *      |                     |
     *      V                     V
     * |||||||||||||||||||||||||||||||||||||||||||
     *                 buffer
     */
    private ByteBuffer buffer = null;
    
    /**
     * intermediary structure between the inputStrean and the buffer
     */
    private byte[] tempBuffer = null;
    
    /**
     * keeps the track of the latest read position in the buffer
     */
    private int readIndex = 0;
    
    /**
     * keeps the track of the latest written position in the buffer
     */
    private int writeIndex = 0;
    
    /**
     * an internal offset index
     */
    private int indexOffset = 0;

    /**
     * Constructs an instance of <code>ByteStreamReader</code> class
     */
    public ByteStreamReader() {
        //1 MB of capacity
        buffer = ByteBuffer.allocateDirect(1024 * 1024);
        tempBuffer = new byte[65536];
    }

    /**
     * If necessary reads a chunk of bytes from the input stream
     * 
     * Gets the byte from the current position.
     *
     * @return
     * @throws IOException
     */
    public byte read() throws IOException {
        if (readIndex >= writeIndex) {
            if (readData() == 0) {
                throw new EOFException();
            }
        }
        return buffer.get(readIndex++);
    }
    
    /**
     * @returnthe position of the read-cursor
     */
    public int getOffset() {
        return indexOffset + readIndex;
    }

    /**
     *
     * @param destBuffer
     * @param offset
     * @param length
     * @throws IOException
     */
    public void read(byte[] destBuffer, int offset, int length) throws IOException {
        assert length < tempBuffer.length : "The requested data is bigger than the temp Buffer";
        if (readIndex + length >= writeIndex) {
            if (0 == readData()) {
                throw new EOFException();
            }
        }
        buffer.position(readIndex);
        buffer.get(destBuffer, offset, length);
        readIndex += length;
    }

    /**
     * The bytes are written in the position of writeIndex value
     * and writeIndex is updated
     * @return
     * @throws IOException 
     */
    private int readData() throws IOException {
        //if there is no more room in the buffer for a tempBuffer load, it is time to recycle
        if (buffer.limit() - writeIndex < tempBuffer.length) {
            recycleBuffer();
        }
        int bytes = inputStream.read(tempBuffer, 0, tempBuffer.length);
        buffer.position(writeIndex);
        buffer.put(tempBuffer, 0, bytes);
        writeIndex += bytes;
        logger.trace(bytes + " bytes read");
        return bytes;
    }

    /**
     * Recycle the buffer
     */
    private void recycleBuffer() {
        if (readIndex <= 0) {
            return;
        }
        logger.trace("recycling ByteBuffer");
        int indexAdjustment = readIndex;
        int recycleIndex = 0;
        // the data that is to be recycled is larger than tempbuffer
        while (writeIndex - readIndex > tempBuffer.length) {
            buffer.position(readIndex);
            buffer.get(tempBuffer, 0, tempBuffer.length);
            buffer.position(recycleIndex);
            buffer.put(tempBuffer, 0, tempBuffer.length);
            recycleIndex += tempBuffer.length;
            readIndex += tempBuffer.length;
        }
        if (writeIndex - readIndex > 0) {
            buffer.position(readIndex);
            buffer.get(tempBuffer, 0, writeIndex - readIndex);
            buffer.position(recycleIndex);
            buffer.put(tempBuffer, 0, writeIndex - readIndex);
        }
        indexOffset += indexAdjustment;
        writeIndex -= indexAdjustment;
        readIndex = 0;
    }

    /**
     * @return the inputStream
     */
    public InputStream getInputStream() {
        return inputStream;
    }

    /**
     * @param inputStream - the inputStream to set
     */
    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }
}
