package caja.jcastulo.media;

import caja.jcastulo.media.entities.AudioMedia;
import java.io.IOException;
import java.util.Map;

/**
 * Iterates over a audio media that can be sliced up in frames
 * 
 * @author Carlos Juarez
 */
public interface FrameIterator {

    /**
     * Returns true if this reader supports the format of the given media.
     *
     * @param media - media to analyze
     * @param properties - other properties requested to read the media
     * @return <code>true</code> if the media is supported
     */
    public boolean supports(AudioMedia media, Map<String,Object> properties);

    /**
     * Opens the media file. Performs startup code
     *
     * @param media
     * @param properties - other properties requested to read the media
     * @throws IOException
     */
    public void open(AudioMedia media, Map<String,Object> properties) throws IOException;

    /**
     * Reads one frame of the media
     * 
     * @return - the frame just read
     * @throws IOException 
     */
    public Frame next() throws IOException;

    /**
     * Closes the media. Performs cleanup code
     *
     * @throws IOException
     */
    public void close() throws IOException;

    /**
     * @return <code>true</code> if the end of the media is reached.
     */
    public boolean hasNext();
    
}
