/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package caja.jcastulo.media.entities;

import caja.jcastulo.media.audio.SongMetadata;
import caja.jcastulo.media.audio.SongMetadataFactory;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Represents an audio media in a file system
 * 
 * @author Carlos Juarez
 */
@Entity
@Table(name="AUDIO_MEDIAS")
public class AudioMedia{

    /**
     * The pathname of the media file
     */
    private String pathname;
    
    /**
     * The song metadata. It is a transient property
     */
    private SongMetadata songMetadata;

    /**
     * Constructs an instance of <code>AudioMedia</code> class
     */
    public AudioMedia() {
        this(null);
    }

    /**
     * Constructs an instance of <code>AudioMedia</code> class
     * 
     * @param pathname - the pathname to set
     */
    public AudioMedia(String pathname) {
        setPathname(pathname);
    }
    
    /**
     * @return the pathname
     */
    @Id
    public String getPathname() {
        return pathname;
    }

    /**
     * @param pathname - the pathname to set
     */
    public void setPathname(String pathname) {
        if (pathname != null) {
            songMetadata = SongMetadataFactory.createMedia(pathname);
        }
        this.pathname = pathname;
    }

    /**
     * @return the songMetadata
     */
    @Transient
    public SongMetadata getSongMetadata() {
        return songMetadata;
    }

    @Override
    public String toString() {
        return getSongMetadata()==null ? pathname : getSongMetadata().toString();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (this.getSongMetadata() != null ? this.getSongMetadata().hashCode() : 0);
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
        final AudioMedia other = (AudioMedia) obj;
        if ((this.pathname == null) ? (other.pathname != null) : !this.pathname.equals(other.pathname)) {
            return false;
        }
        return true;
    }
    
}
