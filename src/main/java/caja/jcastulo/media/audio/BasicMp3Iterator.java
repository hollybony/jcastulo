package caja.jcastulo.media.audio;

import caja.jcastulo.media.ByteStreamReader;
import caja.jcastulo.media.Frame;
import caja.jcastulo.media.FrameIterator;
import caja.jcastulo.media.SilentMediaReader;
import caja.jcastulo.media.entities.AudioMedia;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import org.slf4j.LoggerFactory;

/*
 * 128kbps 44.1kHz layer II uses a lot of 418 bytes and some of 417 bytes long.
 * Regardless of the bitrate of the file, a frame in an MPEG-1 file lasts for 26ms (26/1000 of a second).
 * 
 * An instance of this class cannot be share through different threads as it represents in someway a file which
 * is being reading
 * 
 * It uses jaudiotagger to get the metadata
 * 
 * @author Carlos Juarez
 */
public class BasicMp3Iterator implements FrameIterator {

    /**
     * The logger
     */
    final org.slf4j.Logger logger = LoggerFactory.getLogger(BasicMp3Iterator.class);
    
    /**
     * The byteStreamReader
     */
    private ByteStreamReader byteStreamReader;
    
    /**
     * The currentHeader
     */
    private Mp3FrameHeader currentHeader = new Mp3FrameHeader();
    
    /**
     * The frameHeaderFinder
     */
    private Mp3FrameHeaderFinder frameHeaderFinder = new Mp3FrameHeaderFinderImpl();
    
    /**
     * Constructs an instance of <code>BasicMp3Iterator</code> class
     */
    public BasicMp3Iterator() {
        super();
    }

    /**
     * 
     * @param media
     * @param properties
     * @return <code>true</code> if the media is supported by this object
     */
    @Override
    public boolean supports(final AudioMedia media, Map<String,Object> properties) {
        if(properties==null || properties.isEmpty() || ((Integer)properties.get("bitrate"))<=0){
            return media.getPathname().toString().toLowerCase().endsWith(".mp3");
        }
        return false;
    }

    /**
     * With this method the life cycle starts. The media data is retrieved and is ready to be read
     * by using byteStreamReader
     */
    @Override
    public void open(AudioMedia media, Map<String, Object> properties) throws IOException {
        if (byteStreamReader != null) {
            close();
        }
        byteStreamReader = buildByteStreamReader(media.getPathname());
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
        logger.trace("currentHeader : "  + currentHeader);
        if(currentHeader.getBitRate()==0 && !currentHeader.isCbr()){
            int size = (int)frameHeaderFinder.findNextHeader(currentHeader, byteStreamReader);
            logger.debug("CBR media frame size : " + size);
            currentHeader.setFixedFrameSize(size);
            currentHeader.setCbr(true);
            logger.debug("CBR header : " + currentHeader);
        }
        if(currentHeader.getBitRate()==-1000){
            logger.debug("bitrate not valid");
            return SilentMediaReader.frame;
        }
        frame.setSize(currentHeader.getFrameSize());
        //the header info is copied
        System.arraycopy(currentHeader.getData(), 0, frame.getData(), 0, 4);
        //since we already have the size of the frame we want we are going to take them from the reader
        //we skip 4 bytes that represent the header
        byteStreamReader.read(frame.getData(), 4, frame.getSize() - 4);
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
//            logger.debug("available" + byteStreamReader.getInputStream().available());
            return byteStreamReader.getInputStream().available()>0;
        } catch (IOException ex) {
            return true;
        }
    }
    
    /**
     * Opens and setup the byteStreamReader with the media pointed by pathname
     * 
     * @param pathname
     * @return
     * @throws FileNotFoundException 
     */
    private ByteStreamReader buildByteStreamReader(String pathname) throws FileNotFoundException{
        ByteStreamReader streamReader = new ByteStreamReader();
        logger.debug("openning media : " + pathname);
        streamReader.setInputStream(new FileInputStream(new File(pathname)));
        return streamReader;
    }
    
}
