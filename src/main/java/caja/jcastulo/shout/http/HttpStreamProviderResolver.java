package caja.jcastulo.shout.http;

import caja.jcastulo.shout.StreamProviderResolver;
import caja.jcastulo.stream.StreamManager;
import caja.jcastulo.stream.StreamProvider;
import caja.jcastulo.stream.services.StreamManagersService;
import java.util.List;
import org.slf4j.LoggerFactory;

/**
 * Handles the mapping between a request and a SingleStreamProvider.
 *
 * @author bysse
 */
public class HttpStreamProviderResolver implements StreamProviderResolver<Request> {

    final org.slf4j.Logger logger = LoggerFactory.getLogger(HttpStreamProviderResolver.class);
    
    private StreamManagersService streamManagersService;
    
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
