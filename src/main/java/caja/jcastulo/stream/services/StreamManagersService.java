/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package caja.jcastulo.stream.services;

import caja.jcastulo.stream.StreamManager;
import caja.jcastulo.stream.entities.StreamSpec;
import java.util.List;

/**
 *
 * @author Carlos Juarez
 */
public interface StreamManagersService {
    
    public List<StreamManager> getAllStreamManagers();
    
    public StreamManager getStreamManagerByName(String name);
    
    public void addStreamManager(StreamSpec streamSpec);
    
    public void removeStreamManager(String name);

    public void persistStreamSpecs();
    
}
