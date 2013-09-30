package caja.jcastulo.media;

import caja.jcastulo.media.entities.AudioMedia;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Carlos Juarez
 */
public class MediaReaderFactory {

    private List<FrameIterator> mediaReaders = new ArrayList<FrameIterator>();
    
    public MediaReaderFactory(){
        this(null);
    }
    
    public MediaReaderFactory(FrameIterator oneMediaReader){
        if(oneMediaReader!=null){
            addMediaReader(oneMediaReader);
        }
    }

    public final void addMediaReader(FrameIterator mediaReader) {
        mediaReaders.add(mediaReader);
    }

    /**
     * Creates a FrameIterator that can handle the given media file.
     *
     * @throws NotSupportedMediaReaderException
     * @param media
     * @return
     */
    public FrameIterator getMediaReader(AudioMedia media) {
        for (FrameIterator reader : mediaReaders) {
            if (reader.supports(media)) {
                try {
                    return (FrameIterator) reader.getClass().newInstance();
                } catch (InstantiationException e) {
                    throw new NotSupportedMediaReaderException(e);
                } catch (IllegalAccessException e) {
                    throw new NotSupportedMediaReaderException(e);
                }
            }
        }
        throw new NotSupportedMediaReaderException("No supported reader found for the media file [" + media.getPathname() + "]");
    }
}
