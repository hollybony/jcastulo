/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package caja.jcastulo.stream;

import caja.jcastulo.media.entities.AudioMedia;


/**
 *
 * @author Carlos Juarez
 */
public interface StreamUpdatable extends StreamProcessor{
    
    public void addMedia(AudioMedia media);
        
    public void removeMedia(int index);
    
    public void moveMedia(int sourceIndex, int targetIndex);
}
