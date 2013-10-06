package caja.jcastulo.stream;

import caja.jcastulo.media.Frame;
import caja.jcastulo.media.FrameIterator;
import java.io.IOException;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Carlos Juarez
 */
class PreloadStreamDataReader implements DataReader {

    final org.slf4j.Logger logger = LoggerFactory.getLogger(PreloadStreamDataReader.class);
    
    /**
     * min pre load in milliseconds
     */
    private static final long MAXIMUM_PRELOAD = 5000;
    
    /**
     * max history in milliseconds
     */
    private static final long MAXIMUM_HISTORY = 5000;

    /**
     *
     */
    @Override
    public void readData(final FrameIterator reader, final FrameStorage frameStorage) throws InterruptedException {
        long currentTime = System.currentTimeMillis();
        // first purge the history. Remove frames older than currentTime - MAXIMUM_HISTORY
        frameStorage.purgeUntil(currentTime - MAXIMUM_HISTORY);
        Long lastFrameTime = frameStorage.getLastFrameTime();
        if(lastFrameTime==null){
            lastFrameTime = currentTime;
        }
        // Make sure we don't use old time stamps when we store frames
        if (lastFrameTime < currentTime) {
            lastFrameTime = currentTime;
            logger.warn(Thread.currentThread().getName() + " frame reading is lagging");
        }
        if (lastFrameTime > currentTime + MAXIMUM_PRELOAD) {
            // the maximum data preload have been reached, stall the thread for a while
            //Wow!! this computer is really fast
                Thread.sleep(250);
            return;
        }
        //.. read more data from the media
        try {
            Frame frame = reader.next();
//            logger.debug("frame : " + frame);
            frameStorage.add(new TimedFrame(lastFrameTime, frame));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
