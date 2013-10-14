package caja.jcastulo.media;

import caja.jcastulo.media.entities.AudioMedia;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Factory class that resolves the suitable <code>FrameIterator</code> for a given media
 * 
 * @author Carlos Juarez
 */
public class FrameIteratorFactory {

    /**
     * List of available frameIterators
     */
    private List<FrameIterator> frameIterators = new ArrayList<FrameIterator>();
    
    /**
     * Constructs an instance of <code>FrameIteratorFactory</code> class
     */
    public FrameIteratorFactory(){
        this(null);
    }
    
    /**
     * Constructs an instance of <code>FrameIteratorFactory</code> class
     * 
     * @param oneFrameIterator - one frameIterator to add
     */
    public FrameIteratorFactory(List<FrameIterator> frameIterators){
        this.frameIterators = frameIterators;
    }

    /**
     * Adds a frameIterator to the available frameIterator list
     * 
     * @param frameIterator - frameIterator to add
     */
    public final void addFrameIterator(FrameIterator frameIterator) {
        frameIterators.add(frameIterator);
    }

    /**
     * Finds and creates a compatible <code>FrameIterator</code> that can handle the given audio media.
     * 
     * @throws NotSupportedFrameIteratorException if suitable frameIterator is not found
     * @param media - the media for which frameIterator is found
     * @param bitrate - the desire bitrate with which the media is going to be read. To read the audio
     * media with the original bitrate just set this parameter as 0
     * @return FrameIterator found
     */
    public FrameIterator getIterator(AudioMedia media, Map<String,Object> properties) {
        for (FrameIterator iterator : frameIterators) {
            if (iterator.supports(media, properties)) {
                try {
                    return (FrameIterator) iterator.getClass().newInstance();
                } catch (InstantiationException ex) {
                    throw new NotSupportedFrameIteratorException(ex);
                } catch (IllegalAccessException ex) {
                    throw new NotSupportedFrameIteratorException(ex);
                }
            }
        }
        throw new NotSupportedFrameIteratorException("No supported reader found for the media file [" + media.getPathname() + "]");
    }
}
