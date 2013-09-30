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
 *
 * @author Carlos Juarez
 */
@Entity
@Table(name="AUDIO_MEDIAS")
public class AudioMedia{

    private String pathname;
    
//    @Transient
    private SongMetadata songMetadata;

    public AudioMedia() {
        this(null);
    }

    public AudioMedia(String path) {
        setPathname(path);
    }
    
    @Id
    public String getPathname() {
        return pathname;
    }

    public void setPathname(String filePath) {
        if (filePath != null) {
            songMetadata = SongMetadataFactory.createMedia(filePath);
        }
        pathname = filePath;
    }

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
