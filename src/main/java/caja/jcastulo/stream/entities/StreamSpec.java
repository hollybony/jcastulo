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
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 *
 * @author Carlos Juarez
 */
@Entity
@Table(name="STREAM_SPECS")
public class StreamSpec {
    
    @Id
    private String name;
    
    private String mountPoint;
    
    @ManyToMany(cascade= CascadeType.ALL)
    private List<AudioMedia> audioMedias;

    public StreamSpec() {
        this(null);
    }

    public StreamSpec(String name){        
        this(name, new LinkedList<AudioMedia>());
    }
    
    public StreamSpec(String name, AudioMedia oneMedia){
        this(name, new LinkedList<AudioMedia>());
        this.audioMedias.add(oneMedia);
    }
    
    public StreamSpec(String name, List<AudioMedia> queue){
        this.name = name;
        if(name!=null){
            this.mountPoint = "/" + name + ".mp3";
        }
        this.audioMedias = Collections.synchronizedList(queue);
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMountPoint() {
        return mountPoint;
    }

    public void setMountPoint(String mountPoint) {
        this.mountPoint = mountPoint;
    }

    public List<AudioMedia> getAudioMedias() {
        return audioMedias;
    }

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
