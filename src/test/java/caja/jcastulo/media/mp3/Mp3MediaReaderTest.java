package caja.jcastulo.media.mp3;

import java.io.File;
import java.io.IOException;
import junit.framework.TestCase;
import caja.jcastulo.media.Frame;
import caja.jcastulo.media.entities.AudioMedia;
import caja.jcastulo.media.audio.Mp3FrameIterator;
import org.junit.Before;
import org.junit.Test;

public class Mp3MediaReaderTest extends TestCase {

    private static final File base = new File("target/test-classes");
    
    private AudioMedia media = null;
    
    private Mp3FrameIterator reader = null;

    @Before
    @Override
    public void setUp() {
        media = new AudioMedia();
        media.setPathname(new File(base, "test.mp3").getPath());

        reader = new Mp3FrameIterator();
    }

    @Test
    public void testRead() {
        try {
            reader.open(media);
            Frame frame;

            frame = reader.next();
            frame = reader.next();
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }
}
