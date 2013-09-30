package caja.jcastulo.media.audio;

import caja.jcastulo.media.ByteStreamReader;
import caja.jcastulo.media.Frame;
import caja.jcastulo.media.FrameIterator;
import caja.jcastulo.media.SilentMediaReader;
import caja.jcastulo.media.entities.AudioMedia;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.slf4j.LoggerFactory;

/*
 * 128kbps 44.1kHz layer II uses a lot of 418 bytes and some of 417 bytes long.
 * Regardless of the bitrate of the file, a frame in an MPEG-1 file lasts for 26ms (26/1000 of a second).
 * 
 * It uses jaudiotagger to get the metadata
 * 
 * @author Carlos Juarez
 */
public class Mp3FrameIterator implements FrameIterator {

    final org.slf4j.Logger logger = LoggerFactory.getLogger(Mp3FrameIterator.class);
    
    private ByteStreamReader byteStreamReader;
    
    private Mp3FrameHeader currentHeader = new Mp3FrameHeader();
    
    private Mp3FrameHeaderFinder frameHeaderFinder = new Mp3FrameHeaderFinderImpl();
    
    public Mp3FrameIterator() {
        super();
    }

    /**
     * return <code>true</code> if the media is supported by this object
     */
    @Override
    public boolean supports(final AudioMedia media) {
        return media.getPathname().toString().toLowerCase().endsWith(".mp3");
    }

    /**
     * With this method the life cycle starts
     * The media data is retrieved by using
     * The media file is read
     */
    @Override
    public void open(AudioMedia media) throws IOException {
        if (byteStreamReader != null) {
            close();
        }
        byteStreamReader = new ByteStreamReader();
        logger.debug("openning media : " + media.getPathname());
        byteStreamReader.setInputStream(new FileInputStream(new File(media.getPathname())));
    }
  
    /**
     * All the frames last 26 ms the size of bytes depends on the bitrate
     * 
     * @param frame
     * @return
     * @throws IOException 
     */
    @Override
    public Frame next() throws IOException {
        Frame frame = new Frame();
        frameHeaderFinder.findNextHeader(currentHeader, byteStreamReader);
        if(currentHeader.getBitRate()==0 && !currentHeader.isCbr()){
            int size = (int)frameHeaderFinder.findNextHeader(currentHeader, byteStreamReader);
            logger.debug("CBR media frame size : " + size);
            currentHeader.setFixedFrameSize(size);
            currentHeader.setCbr(true);
        }
        if(currentHeader.getBitRate()==-1000){
            logger.debug("bitrate not valid");
            return SilentMediaReader.frame;
        }
        logger.trace("currentHeader = " + currentHeader);
        frame.setSize(currentHeader.getFrameSize());
        //the header info is copied
        System.arraycopy(currentHeader.getData(), 0, frame.getData(), 0, 4);
        //since we already have the size of the frame we want we are going to take them from the reader
        //we skip 4 bytes that represent the header
        byteStreamReader.read(frame.getData(), 4, (int) frame.getSize() - 4);
        return frame;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws IOException {
        if (byteStreamReader != null) {
            byteStreamReader.getInputStream().close();
            byteStreamReader.setInputStream(null);
            byteStreamReader = null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasNext() {
        try {
            return byteStreamReader.getInputStream().available() <= 0;
        } catch (IOException ex) {
            return true;
        }
    }
    
}
