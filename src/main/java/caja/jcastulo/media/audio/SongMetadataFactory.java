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
 * Creates a <code>SongMetadata</code> instance for a particular media file
 * 
 * @author Carlos Juarez
 */
public class SongMetadataFactory{
    
    /**
     * The logger
     */
    final static org.slf4j.Logger logger = LoggerFactory.getLogger(SongMetadataFactory.class);

    /**
     * The metadata info is retrieved by using jaudiotagger library
     * 
     * @param pathname - pathname of the media file
     * @return the songmetadata from the media file
     */
    public static SongMetadata createMedia(String pathname) {
        SongMetadata metadata = new SongMetadata("unknown", null, null);
        AudioFile audioFile;
        try {
            audioFile = AudioFileIO.read(new File(pathname));
            Tag tag = audioFile.getTag();
            metadata = new SongMetadata(tag.getFirst(FieldKey.ARTIST), tag.getFirst(FieldKey.ALBUM), tag.getFirst(FieldKey.TITLE));
            logger.debug("reading media data from " + metadata);
        } catch (Exception ex) {
            logger.error("can't read tag from " + pathname, ex);
        }      
        return metadata;
    }
    
}
