/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package caja.jcastulo.media.audio;

import caja.jcastulo.media.Frame;
import caja.jcastulo.media.FrameIterator;
import caja.jcastulo.media.entities.AudioMedia;
import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.MediaListenerAdapter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.mediatool.event.IAddStreamEvent;
import com.xuggle.xuggler.IStreamCoder;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Carlos Juarez
 */
public class RencoderMp3Iterator implements FrameIterator {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(RencoderMp3Iterator.class);
    
    public static final String CACHE_DIR = "C:\\temp";
    
    private DirHandler dirHandler = new DirHandler();
    
    private PacketFrameCatcher packetFrameCatcher = new PacketFrameCatcher();
    
    private IMediaReader mediaReader;
    
    private IMediaWriter mediaWriter;
    
//    private Mp3FrameHeaderFinder frameHeaderFinder = new Mp3FrameHeaderFinderImpl();
    
    /**
     * The bitrates allowed
     */
    static final int[] bitratesAllowed = {32000, 64000, 96000, 128000};

    /**
     *
     * @param media
     * @param properties the property allowed is bitrate with either of the
     * following values 32000,64000,96000,128000 if no properties specified the
     * default bitrate is 64000
     * @return
     */
    @Override
    public boolean supports(AudioMedia media, Map<String, Object> properties) {
        if (media.getPathname().toString().toLowerCase().endsWith(".mp3")) {
            if (properties == null || properties.isEmpty()) {
                return true;
            } else {
                int bitrate = (Integer) properties.get("bitrate");
                return isBitrateValid(bitrate);
            }
        }
        return false;
    }

    private boolean isBitrateValid(final int bitrate) {
        for (int currBitrate : bitratesAllowed) {
            if (currBitrate == bitrate) {
                return true;
            }
        }
        logger.debug("bitrate : " + bitrate + " is not supported. These are the supported bitrates : " + Arrays.asList(bitratesAllowed));
        return false;
    }

    @Override
    public void open(AudioMedia media, Map<String, Object> properties) throws IOException {
        final int bitrate = (Integer) properties.get("bitrate");
        if (!isBitrateValid(bitrate)) {
            throw new RuntimeException(bitrate + " is a bitrate not valid");
        }
        dirHandler.checkIfCacheDirExists();
        mediaReader = ToolFactory.makeReader(media.getPathname());
        // create a media writer
        mediaWriter = ToolFactory.makeWriter(dirHandler.bitrateFile(media, bitrate), mediaReader);
        // add a writer to the reader, to create the output file
        mediaReader.addListener(mediaWriter);
        mediaWriter.addListener(new MediaListenerAdapter() {
            @Override
            public void onAddStream(IAddStreamEvent event) {
                IStreamCoder streamCoder = event.getSource().getContainer().getStream(event.getStreamIndex()).getStreamCoder();
                streamCoder.setFlag(IStreamCoder.Flags.FLAG_QSCALE, false);
                streamCoder.setBitRate(bitrate);
                streamCoder.setBitRateTolerance(0);
            }
        });
        mediaWriter.addListener(packetFrameCatcher);
    }

    @Override
    public Frame next() throws IOException {
        if(!packetFrameCatcher.isNewFrame()){
            if(hasNext()){
                return packetFrameCatcher.getCurrentFrame();
            }else{
                return null;
            }
        }
        return packetFrameCatcher.getCurrentFrame();
    }

    @Override
    public void close() throws IOException {
        if(mediaReader!=null){
            mediaReader.close();
            mediaReader = null;
        }
//        if(mediaWriter!=null){
//            mediaWriter.close();
//            mediaWriter = null;
//        }
    }

    @Override
    public boolean hasNext() {
        if(packetFrameCatcher.isNewFrame()){
            return true;
        }else{
            while(true){
                try{
                    if(mediaReader.readPacket() == null){
                        if(packetFrameCatcher.isNewFrame()){
                            return true;
                        }
                    }else{
                        return false;
                    }
                    
                }catch(Exception ex){
                }
            }
        }
    }

    class DirHandler {

        private void checkIfCacheDirExists() {
            File file = new File(CACHE_DIR);
            if (file.isFile()) {
                throw new RuntimeException("Directory for cache cannot be a file name");
            }
            file.mkdir();
            for (int bitrate : bitratesAllowed) {
                new File(bitrateDir(bitrate)).mkdir();
            }
        }

        private String bitrateDir(int bitrate) {
            String name = bitrate / 1000 + "kbps";
            return CACHE_DIR + System.getProperties().getProperty("file.separator") + name;
        }
        private String bitrateFile(AudioMedia media, int bitrate){
            String bitrateDir = bitrateDir(bitrate);
            String fileName = media.getPathname().substring(media.getPathname().lastIndexOf(System.getProperties().getProperty("file.separator")) + 1); 
            return bitrateDir + System.getProperties().getProperty("file.separator") + fileName;
        }
    }
}
