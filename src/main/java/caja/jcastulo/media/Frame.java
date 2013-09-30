package caja.jcastulo.media;

/**
 * MP3 streams are arranged into "frames", each with a header that provides basic information about
 * encoding type, bitrate, sampling frequency, etc.
 * 
 * 
 * @author Carlos Juarez
 */
public class Frame {

    /**
     * Frame length in milliseconds
     */
    private long length;
    
    /**
     * Frame size in bytes
     */
    private int size;
    
    /**
     * First four bytes are the header
     * Data buffer.
     */
    private final byte[] data;

    /**
     * 
     * @param allocationSize size of byte array
     */
    public Frame() {
        length = 26;
        size = 0;
        // Maximum frame size for an mp3 is 2881
        data = new byte[3000];
    }

    /**
     * Makes a deep copy of the given frame.
     *
     * @param frame
     */
    public Frame(Frame frame) {
        length = frame.length;
        size = frame.size;
        data = new byte[size];
        System.arraycopy(frame.data, 0, data, 0, size);
    }

    /**
     * Get the length
     *
     * @return Length of frame in Ms
     */
    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    /**
     * Get the size of the frame in bytes.
     *
     * @return Size of frame in bytes
     */
    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    /**
     * Returns a reference to the data
     *
     * @return
     */
    public byte[] getData() {
        return data;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(getLength()).append(" ms [");
        str.append(getSize()).append(" bytes]");
        return str.toString();
    }
}
