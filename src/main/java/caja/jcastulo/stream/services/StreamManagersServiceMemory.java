/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package caja.jcastulo.stream.services;

import caja.jcastulo.media.FrameIteratorFactory;
import caja.jcastulo.stream.DataReader;
import caja.jcastulo.stream.StreamManager;
import caja.jcastulo.stream.StreamProcessorImpl;
import caja.jcastulo.stream.entities.StreamSpec;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Carlos Juarez
 */
public class StreamManagersServiceMemory implements StreamManagersService{
    
    private List<StreamManager> streamManagers;
    
    private FrameIteratorFactory mediaReaderFactory;
    
    private DataReader dataReader;
    
    public StreamManagersServiceMemory(List<StreamSpec> streamSpecs, DataReader dataReader, FrameIteratorFactory mediaReaderFactory){
        streamManagers = new ArrayList<StreamManager>();
        this.mediaReaderFactory = mediaReaderFactory;
        this.dataReader = dataReader;
        for(StreamSpec streamSpec : streamSpecs){
            addStreamManager(streamSpec);
        }
    }

    @Override
    public List<StreamManager> getAllStreamManagers() {
        return streamManagers;
    }

    @Override
    public void addStreamManager(StreamSpec streamSpec) {
        streamManagers.add(new StreamManager(new StreamProcessorImpl(streamSpec, dataReader, mediaReaderFactory)));
    }

    @Override
    public void removeStreamManager(String name) {
        Iterator<StreamManager> iterator = streamManagers.iterator();
        while(iterator.hasNext()){
            StreamManager streamManager = iterator.next();
            if(streamManager.getStreamName().equals(name)){
                streamManagers.remove(streamManager);
            }
        }
    }

    @Override
    public StreamManager getStreamManagerByName(String name) {
        for(StreamManager streamManager : streamManagers){
            if(streamManager.getStreamName().equals(name)){
                return streamManager;
            }
        }
        return null;
    }

    public void persistStreamSpecs(){
        
    }
    
}
