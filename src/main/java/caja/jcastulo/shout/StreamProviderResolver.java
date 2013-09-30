package caja.jcastulo.shout;

import caja.jcastulo.stream.StreamProvider;
import java.util.List;

/**
 *
 * @author carlos juarez
 */
public interface StreamProviderResolver<T> {

    /**
     * Returns a SingleMediaQueue object for the request or throws an exception.
     * @param request
     * @return
     */
    public StreamProvider findStreamProvider(T request);
    
    public List<? extends StreamProvider> getAllStreamProviders();
    
}
