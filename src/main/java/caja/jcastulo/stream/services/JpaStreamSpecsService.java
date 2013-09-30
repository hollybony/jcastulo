/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package caja.jcastulo.stream.services;

import caja.jcastulo.stream.entities.StreamSpec;
import caja.jcastulo.stream.repository.StreamSpecRepository;
import com.google.common.collect.Lists;
import java.util.ArrayList;
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
@Service("streamSpecsService")
@Repository
@Transactional
public class JpaStreamSpecsService implements StreamSpecsService{
    
    final org.slf4j.Logger logger = LoggerFactory.getLogger(JpaStreamSpecsService.class);

    @Autowired
    private StreamSpecRepository streamSpecRepository;
    
    @Override
    @Transactional(readOnly=true)
    public List<StreamSpec> getAllStreamSpecs() {
        ArrayList<StreamSpec> streamSpecs = Lists.newArrayList(streamSpecRepository.getAllStreamSpecs());
        logger.debug("stream specs found : " + streamSpecs.size());
        return streamSpecs;
    }
 
    @Override
    @Transactional(readOnly=true)
    public StreamSpec getStreamSpecByName(String name) {
        return streamSpecRepository.findOne(name);
    }
    
    @Override
    public void addStreamSpec(StreamSpec streamSpec) {
        streamSpecRepository.save(streamSpec);
    }

    @Override
    public void removeStreamSpec(String name) {
        streamSpecRepository.delete(name);
    }

    @Override
    public void updateStreamSpec(StreamSpec streamSpec) {
        streamSpecRepository.save(streamSpec);
    }

}
