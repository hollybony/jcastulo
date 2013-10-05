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
     * Constructs an instance of <code>Frame</code> class
     */
    public Frame() {
        length = 26;
        size = 0;
        // Maximum frame size for an mp3 is 2881
        data = new byte[3000];
    }

    /**
     * Constructs an instance of <code>Frame</code> class
     * 
     * Makes a deep copy of the given frame.
     *
     * @param frame - frame to copy
     */
    public Frame(Frame frame) {
        length = frame.length;
        size = frame.size;
        data = new byte[size];
        System.arraycopy(frame.data, 0, data, 0, size);
    }

    /**
     * @return length of frame in milliseconds
     */
    public long getLength() {
        return length;
    }

    /**
     * @param length - length to set
     */
    public void setLength(long length) {
        this.length = length;
    }

    /**
     * @return Size of frame in bytes
     */
    public int getSize() {
        return size;
    }

    /**
     * @param size - the frame size
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * @return the data contained
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
