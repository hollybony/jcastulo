package caja.jcastulo.media.mp3;

import caja.jcastulo.media.Frame;
import caja.jcastulo.media.audio.BasicMp3Iterator;
import caja.jcastulo.media.entities.AudioMedia;
import java.io.File;
import java.io.IOException;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

public class Mp3MediaReaderTest extends TestCase {

    private static final File base = new File("target/test-classes");
    
    private AudioMedia media = null;
    
    private BasicMp3Iterator frameIterator = null;

    @Before
    @Override
    public void setUp() {
        media = new AudioMedia();
        media.setPathname(new File(base, "test.mp3").getPath());

        frameIterator = new BasicMp3Iterator();
    }

    @Test
    public void testRead() {
        try {
            frameIterator.open(media, null);
            Frame frame;

            frame = frameIterator.next();
            frame = frameIterator.next();
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }
}
