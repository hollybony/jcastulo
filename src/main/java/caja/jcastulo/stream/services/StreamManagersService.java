/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package caja.jcastulo.stream.services;

import caja.jcastulo.stream.StreamManager;
import caja.jcastulo.stream.entities.StreamSpec;
import java.util.List;

/**
 * Provides services related with Stream managers
 * 
 * @author Carlos Juarez
 */
public interface StreamManagersService {
    
    /**
     * @return all the stream managers
     */
    public List<StreamManager> getAllStreamManagers();
    
    /**
     * @param name - the name of the stream managers to look for
     * @return the stream manager found or null if it was not found
     */
    public StreamManager getStreamManagerByName(String name);
    
    /**
     * Adds a stream manager by using the given stream spec
     * 
     * @param streamSpec the stream specs with which the stream manager is created
     */
    public void addStreamManager(StreamSpec streamSpec);
    
    /**
     * Removes the stream manager with the given name
     * 
     * @param name - the name of the stream manager to remove
     */
    public void removeStreamManager(String name);

    /**
     * Performs a flush of the current stream managers
     */
    public void flushStreamSpecs();
    
}
