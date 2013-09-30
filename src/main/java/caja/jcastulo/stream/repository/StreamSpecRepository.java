/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package caja.jcastulo.stream.repository;

import caja.jcastulo.stream.entities.StreamSpec;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 *
 * @author Carlos Juarez
 */
public interface StreamSpecRepository extends PagingAndSortingRepository<StreamSpec,String>{
    
    @Query("select distinct s from StreamSpec s left join fetch s.audioMedias")
    public List<StreamSpec> getAllStreamSpecs();
    
}
