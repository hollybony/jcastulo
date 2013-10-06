package caja.jcastulo.shout.http;

import caja.jcastulo.shout.StreamProviderResolver;
import caja.jcastulo.stream.StreamManager;
import caja.jcastulo.stream.StreamProvider;
import caja.jcastulo.stream.services.StreamManagersService;
import java.util.List;
import org.slf4j.LoggerFactory;

/**
 * Implementation of <code>StreamProviderResolver</code> that resolves the stream
 * according the http request
 *
 * @author bysse
 */
public class HttpStreamProviderResolver implements StreamProviderResolver<Request> {

    /**
     * The logger
     */
    final org.slf4j.Logger logger = LoggerFactory.getLogger(HttpStreamProviderResolver.class);
    
    /**
     * The streamManagersService
     */
    private StreamManagersService streamManagersService;
    
    /**
     * Constructs an instance of <code>HttpStreamProviderResolver</code> class
     * 
     * @param streamManagersService 
     */
    public HttpStreamProviderResolver(StreamManagersService streamManagersService) {
        this.streamManagersService = streamManagersService;
    }
    

    /**
     * {@inheritDoc}
     */
    @Override
    public StreamProvider findStreamProvider(Request request) {
        List<StreamManager> streamManagers = streamManagersService.getAllStreamManagers();
        String path = request.getPath();
        for(StreamManager streamManager : streamManagers){
            if(streamManager.getMountPoint().equals(path)&&streamManager.isRunning()){
                if(streamManager.isRunning()){
                    return streamManager;
                }
            }
        }
        return null;
    }

    @Override
    public List<? extends StreamProvider> getAllStreamProviders() {
        return streamManagersService.getAllStreamManagers();
    }
}
