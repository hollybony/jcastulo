/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package caja.jcastulo.stream.services;

import caja.jcastulo.media.MediaReaderFactory;
import caja.jcastulo.stream.DataReader;
import caja.jcastulo.stream.StreamManager;
import caja.jcastulo.stream.StreamProcessorImpl;
import caja.jcastulo.stream.entities.StreamSpec;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Carlos Juarez
 */
@Service("streamManagersService")
public class StreamManagersServiceImpl implements StreamManagersService{
    
    final org.slf4j.Logger logger = LoggerFactory.getLogger(StreamManagersServiceImpl.class);

    @Autowired
    private StreamSpecsService streamSpecsService;
    
    @Autowired
    private DataReader dataReader;
    
    @Autowired
    private MediaReaderFactory mediaReaderFactory;
    
    List<StreamManager> streamManagers;
    
    @Override
    @Transactional(readOnly=true)
    public List<StreamManager> getAllStreamManagers() {
        if(streamManagers==null){
            streamManagers = new ArrayList<StreamManager>();
    //        Iterable<StreamSpec> streamSpecs = streamSpecRepository.findAll();
            Iterable<StreamSpec> streamSpecs = streamSpecsService.getAllStreamSpecs();
//            logger.debug("streamSpecs gotten : " + streamSpecs);
            for(StreamSpec streamSpec : streamSpecs){
                streamManagers.add(buildStreamManager(streamSpec));
            }
        }
        return streamManagers;
    }
    
    private StreamManager buildStreamManager(StreamSpec streamSpec) {
        return new StreamManager(new StreamProcessorImpl(streamSpec, dataReader, mediaReaderFactory));
    }

    @Override
    @Transactional(readOnly=true)
    public StreamManager getStreamManagerByName(String name) {
        if(streamManagers==null){
            getAllStreamManagers();
        }
        for(StreamManager streamManager : streamManagers){
            if(streamManager.getStreamName().equals(name)){
                return streamManager;
            }
        }
        return null;
    }

    @Override
    public void addStreamManager(StreamSpec streamSpec) {
        if(streamManagers==null){
            getAllStreamManagers();
        }
        for(StreamManager streamManager : streamManagers){
            if(streamManager.getStreamName().equals(streamSpec.getName())){
                return;
            }
        }
        streamManagers.add(buildStreamManager(streamSpec));
    }

    @Override
    public void removeStreamManager(String name) {
        if(streamManagers==null){
            getAllStreamManagers();
        }
        Iterator<StreamManager> iterator = streamManagers.iterator();
        while(iterator.hasNext()){
            StreamManager streamManager = iterator.next();
            if(streamManager.getStreamName().equals(name)){
                logger.debug("removing : " + streamManager);
                streamManagers.remove(streamManager);
                streamSpecsService.removeStreamSpec(name);
            }
        }
    }

    @Override
    public void persistStreamSpecs() {
        if(streamManagers!=null){
            for(StreamManager streamManager : streamManagers){
                streamSpecsService.updateStreamSpec(streamManager.getProcessor().getStreamSpec());
            }
        }
    }
    
}
