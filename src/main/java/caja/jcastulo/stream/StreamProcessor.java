package caja.jcastulo.stream;

import caja.jcastulo.media.audio.SongMetadata;
import caja.jcastulo.stream.entities.StreamSpec;
import caja.jcastulo.util.Observable;


/**
 * Performs the frame and media metadata reading and puts them available through the frame storage and metadata bytes
 * 
 * @author bysse
 */
public interface StreamProcessor extends Runnable, Observable<StreamListener> {

    /**
     * @return current encoded metadata bytes
     */
    public byte[] currentMetadataBytes();
    
    /**
     * @return current song metadata
     */
    public SongMetadata currentMetadata();
    
    /**
     * @return the frame storage
     */
    public FrameStorage getFrameStorage();
    
    /**
     * @return the stream specification
     */
    public StreamSpec getStreamSpec();
    
}
