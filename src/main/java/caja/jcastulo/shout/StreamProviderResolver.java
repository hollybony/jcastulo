package caja.jcastulo.shout;

import caja.jcastulo.stream.StreamProvider;
import java.util.List;

/**
 * Retrieves a stream provider for a given request as well as list all the available stream providers.
 * 
 * @author carlos juarez
 */
public interface StreamProviderResolver<T> {

    /**
     * Returns a StreamProvider object for the request or throws an exception.
     * 
     * @param request
     * @return the stream provider found
     */
    public StreamProvider findStreamProvider(T request);
    
    /**
     * @return lists of current stream providers
     */
    public List<? extends StreamProvider> getAllStreamProviders();
    
}
