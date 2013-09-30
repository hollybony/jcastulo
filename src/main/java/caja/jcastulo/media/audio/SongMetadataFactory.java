/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package caja.jcastulo.media.audio;

import java.io.File;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Carlos Juarez
 */
public class SongMetadataFactory{
    
    final static org.slf4j.Logger logger = LoggerFactory.getLogger(SongMetadataFactory.class);

    public static SongMetadata createMedia(String path) {
        SongMetadata metadata = null;
        AudioFile audioFile;
        try {
            audioFile = AudioFileIO.read(new File(path));
            Tag tag = audioFile.getTag();
            metadata = new SongMetadata(tag.getFirst(FieldKey.ARTIST), tag.getFirst(FieldKey.ALBUM), tag.getFirst(FieldKey.TITLE));
            logger.debug("reading media data from " + metadata);
        } catch (Exception ex) {
            logger.error("can't read tag from " + path, ex);
        }finally{
            
        }       
        return metadata;
    }
    
}
