package caja.jcastulo.media;

import caja.jcastulo.media.entities.AudioMedia;
import java.util.ArrayList;
import java.util.List;

/**
 * Factory class that provides the suitable <code>FrameIterator</code> for reading a given media
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
    public FrameIteratorFactory(FrameIterator oneFrameIterator){
        if(oneFrameIterator!=null){
            addFrameIterator(oneFrameIterator);
        }
    }

    /**
     * Add a frameIterator to the available frameIterator list
     * 
     * @param frameIterator - frameIterator to add
     */
    public final void addFrameIterator(FrameIterator frameIterator) {
        frameIterators.add(frameIterator);
    }

    /**
     * Finds and creates a compatible <code>FrameIterator</code>  that can handle the given media file.
     *
     * @throws NotSupportedFrameIteratorException if frameIterator is not found
     * @param media - the media for which frameIterator is found
     * @return FrameIterator found
     */
    public FrameIterator getFrameIterator(AudioMedia media) {
        for (FrameIterator reader : frameIterators) {
            if (reader.supports(media)) {
                try {
                    return (FrameIterator) reader.getClass().newInstance();
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
