package caja.jcastulo.shout;

import caja.jcastulo.shout.ShoutServer;
import caja.jcastulo.AbstractServiceTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ShoutServerTest extends AbstractServiceTest {
    
    @Autowired
    private ShoutServer shoutServer;
    
    @Test
    public void testServerStart() {
//        Media media = new Media();
//        media.setMediaFile(new File("test.mp3"));
//
//        MediaReaderFactory mediaReaderFactory = new MediaReaderFactory(new Mp3MediaReader());
//
//        MediaQueue queue = new MediaQueueImpl("mediaQueue", media);
//        queue.setMountPoint("/media.mp3");
//        queue.setFrameStorage(new FixedFrameSizeFrameStorage());
//
//        MediaQueueProcessor processor = new PreloadDataMediaQueueProcessor();
//        processor.setMediaQueue(queue);
//        processor.setMediaReaderFactory(mediaReaderFactory);
//
//        ShoutRequestManager manager = new ShoutRequestManagerImpl(queue);
//
//        ShoutServer server = new ShoutServer();
//        server.setServerPort(8001);
//
//        server.setRequestManager(manager);
        shoutServer.run();
    }
}
