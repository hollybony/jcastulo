package caja.jcastulo.stream;

import caja.jcastulo.media.audio.SongMetadata;
import caja.jcastulo.stream.entities.StreamSpec;
import caja.jcastulo.util.Observable;


/**
 *
 * @author bysse
 */
public interface StreamProcessor extends Runnable, Observable<StreamListener> {

    public byte[] currentMetadataBytes();
    
    public SongMetadata currentMetadata();
    
    public FrameStorage getFrameStorage();
    
    public StreamSpec getStreamSpec();
    
}
