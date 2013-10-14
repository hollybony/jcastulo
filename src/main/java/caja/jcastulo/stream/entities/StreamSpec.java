/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package caja.jcastulo.stream.entities;

import caja.jcastulo.media.entities.AudioMedia;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * Represents a Stream specification
 * 
 * @author Carlos Juarez
 */
@Entity
@Table(name="STREAM_SPECS")
public class StreamSpec {
    
    /**
     * The name of the stream
     */
    @Id
    @Column(length=32)
    private String name;
    
    /**
     * The mount point of the stream. It is used by the clients to connect with this
     */
    @Column(length=32, nullable=false)
    private String mountPoint;
    
    /**
     * The queue of audio medias
     */
    @ManyToMany(cascade= CascadeType.ALL)
    private List<AudioMedia> audioMedias;

    /**
     * Constructs an instance of <code>StreamSpec</code> class
     */
    public StreamSpec() {
        this(null);
    }

    /**
     * Constructs an instance of <code>StreamSpec</code> class
     * 
     * @param name - the name to set
     */
    public StreamSpec(String name){        
        this(name, new LinkedList<AudioMedia>());
    }
    
    /**
     * Constructs an instance of <code>StreamSpec</code> class
     * 
     * @param name - the name to set
     * @param oneMedia - a media to add to the queue
     */
    public StreamSpec(String name, AudioMedia oneMedia){
        this(name, new LinkedList<AudioMedia>());
        this.audioMedias.add(oneMedia);
    }
    
    /**
     * Constructs an instance of <code>StreamSpec</code> class
     * 
     * @param name - the name to set
     * @param queue - the queue to set
     */
    public StreamSpec(String name, List<AudioMedia> queue){
        this.name = name;
        if(name!=null){
            this.mountPoint = "/" + name + ".mp3";
        }
        this.audioMedias = Collections.synchronizedList(queue);
    }
    
    /**
     * @return the name of the stream
     */
    public String getName() {
        return name;
    }

    /**
     * @param name - the stream name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the mount point
     */
    public String getMountPoint() {
        return mountPoint;
    }

    /**
     * @param mountPoint - the mount point to set
     */
    public void setMountPoint(String mountPoint) {
        this.mountPoint = mountPoint;
    }

    /**
     * @return the media queue
     */
    public List<AudioMedia> getAudioMedias() {
        return audioMedias;
    }

    /**
     * @param queue - the queue as set as audio medias
     */
    public void setAudioMedias(List<AudioMedia> queue) {
        this.audioMedias = queue;
    }

    @Override
    public String toString() {
        return "StreamSpec{" + "name=" + name + ", mountPoint=" + mountPoint + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + (this.mountPoint != null ? this.mountPoint.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final StreamSpec other = (StreamSpec) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        return true;
    }
    
}
