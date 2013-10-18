/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package caja.jcastulo.stream;

import caja.jcastulo.media.entities.AudioMedia;


/**
 * Specifies a <code>StreamProcessor</code> that supports media updates
 * 
 * @author Carlos Juarez
 */
public interface StreamUpdateable extends StreamProcessor{
    
    /**
     * Adds a given media
     * 
     * @param media - the media to add
     */
    public void addMedia(AudioMedia media);
    
    /**
     * @param index - the index of the media to be removed
     */
    public void removeMedia(int index);
    
    /**
     * Empties all the media files in the queue
     */
    public void emptyMediaQueue();
    
    /**
     * Moves a media from one place to another
     * 
     * @param sourceIndex - index where the media is going to be removed
     * @param targetIndex - the media removed from sourceIndex is placed in this targetIndex
     */
    public void moveMedia(int sourceIndex, int targetIndex);
}
