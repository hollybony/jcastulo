package caja.jcastulo.media;

import caja.jcastulo.media.entities.AudioMedia;
import java.io.IOException;
import java.util.Map;

/**
 * It provides an mp3 silent frame
 * 
 * @author Carlos Juarez
 */
public class SilentMediaReader implements FrameIterator {

    /**
     * Silent frame in bytes
     */
    private final static byte[] emptyFrame = new byte[]{(byte) 0xff, (byte) 0xf2, 0x10, (byte) 0xc4, 0x1b, 0x27, 0x0, 0x0, 0x0, 0x3, (byte) 0xfc, 0x0, 0x0, 0x0, 0x0, 0x4c, 0x41, 0x4d, 0x45, 0x33, 0x2e, 0x39, 0x37, 0x0, 0x0, 0x0, (byte) 0xff, (byte) 0xf2, 0x10, (byte) 0xc4, 0x1b, 0x27, 0x0, 0x0, 0x0, 0x3, (byte) 0xfc, 0x0, 0x0, 0x0, 0x0, 0x4c, 0x41, 0x4d, 0x45, 0x33, 0x2e, 0x39, 0x37, 0x0, 0x0, 0x0};

    /**
     * The silent frame
     */
    public final static Frame frame;
    
    static{
        frame = new Frame(); // Maximum frame size for an mp3 is 2881
        frame.setSize(emptyFrame.length);
        System.arraycopy(emptyFrame, 0, frame.getData(), 0, emptyFrame.length);
    }
    
    /**
     * The singleton instance
     */
    private static SilentMediaReader instance;
    
    private SilentMediaReader(){
    }
    
    /**
     * @return the singleton instance
     */
    public static SilentMediaReader getInstance(){
        if(instance == null){
            instance = new SilentMediaReader();
        }
        return instance;
    }
    
    
    @Override
    public void close() throws IOException {
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public void open(AudioMedia media, Map<String, Object> properties) throws IOException {
    }

    @Override
    public Frame next() throws IOException {
        return frame;
    }

    /**
     * This method always returns false so the reader isn't used by mistake.
     */
    @Override
    public boolean supports(AudioMedia media, Map<String, Object> properties) {
        return false;
    }

}
