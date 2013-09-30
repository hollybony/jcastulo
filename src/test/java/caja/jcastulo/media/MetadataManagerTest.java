package caja.jcastulo.media;

import caja.jcastulo.media.audio.SongMetadata;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class MetadataManagerTest {

    private MetadataManager mm = new MetadataManager();

    @Test
    public void testParse() {
        SongMetadata info = new SongMetadata("ARTIST", "ALBUM", "TITLE");

        String result = mm.parseFormat("${artist} $album$track ?(title,-) ${title}", info);
        assertEquals("ARTIST ALBUM - TITLE", result);
    }
}
