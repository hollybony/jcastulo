/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package caja.jcastulo.stream.services;

import caja.jcastulo.stream.entities.StreamSpec;
import java.util.List;

/**
 * Provides services related with stream specs
 * 
 * @author Carlos Juarez
 */
public interface StreamSpecsService {
    
    /**
     * @return all the stream specs found in the repository
     */
    public List<StreamSpec> getAllStreamSpecs();
    
    /**
     * @param name - the name with which the stream spec is looked for
     * @return name the stream spec with the given name 
     */
    public StreamSpec getStreamSpecByName(String name);
    
    /**
     * Adds the given stream spec to the repository
     * 
     * @param streamSpec - the stream spec to add
     */
    public void addStreamSpec(StreamSpec streamSpec);
    
    /**
     * Removes from the repository the stream spec with the given name
     * 
     * @param name - name of the stream spec to remove
     */
    public void removeStreamSpec(String name);

    /**
     * Updates the given stream spec
     * 
     * @param streamSpec - the stream spec to update
     */
    public void updateStreamSpec(StreamSpec streamSpec);
    
}
