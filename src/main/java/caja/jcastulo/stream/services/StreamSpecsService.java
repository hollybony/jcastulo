/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package caja.jcastulo.stream.services;

import caja.jcastulo.stream.entities.StreamSpec;
import java.util.List;

/**
 *
 * @author Carlos Juarez
 */
public interface StreamSpecsService {
    
    public List<StreamSpec> getAllStreamSpecs();
    
    public StreamSpec getStreamSpecByName(String name);
    
    public void addStreamSpec(StreamSpec streamSpec);
    
    public void removeStreamSpec(String name);

    public void updateStreamSpec(StreamSpec streamSpec);
    
}
