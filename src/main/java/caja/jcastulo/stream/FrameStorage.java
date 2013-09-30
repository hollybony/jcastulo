package caja.jcastulo.stream;

import java.util.Iterator;
import java.util.LinkedList;
import org.slf4j.LoggerFactory;

/**
 * Thread safe
 *
 * @author bysse
 *
 */
public class FrameStorage {

    final org.slf4j.Logger logger = LoggerFactory.getLogger(FrameStorage.class);
    
    private LinkedList<TimedFrame> timedFrames = new LinkedList<TimedFrame>();
    
    public static final long LENGTH = 26; // MP3 frame length

    /**
     * Returns the frame that overlaps the given time. If the FrameStorage is
     * empty {@link EmptyFrameStorageException} is be thrown. If no frame
     * could be found for the specified time
     * {@link NoLoadedFrameException} or {@link OldFrameException}
     * is thrown.
     *
     * @param time
     * @return A TimedFrame that overlapped the given time.
     */
    public synchronized TimedFrame find(long time) {
        if (timedFrames.isEmpty()) {
            throw new EmptyFrameStorageException();
        }
        long firstFrameTime = timedFrames.getFirst().getStartTime();
        long lastFrameTime = timedFrames.getLast().getStopTime();
        // make sure the frame is within the represented interval
        if (lastFrameTime <= time) {
            //log.debug("Request: "+time+", LastFrame: "+lastFrameTime+", Diff: "+(time-lastFrameTime));			
            throw new NoLoadedFrameException();
        }
        if(time < firstFrameTime) {
            throw new OldFrameException();
        }
        int index = (int) ((time - firstFrameTime) / LENGTH);
        return timedFrames.get(index);
    }

    /**
     * Adds a frame to the FrameStorage. This method only adds the frame to the
     * end of the storage. So adding out-of-order frames will cause error in
     * playback.
     *
     * @param entry
     */
    public synchronized void add(TimedFrame entry) {
        timedFrames.add(entry);
    }

    /**
     * Removes all frames that do not overlap the given time.
     *
     * @param time
     */
    public synchronized void purgeUntil(long time) {
        Iterator<TimedFrame> iterator = timedFrames.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getStopTime() <= time) {
                iterator.remove();
            } else {
                break;
            }
        }
    }

    /**
     * Returns the end time of the last frame. If the storage is empty
     * {@link EmptyFrameStorageException} will be thrown.
     *
     * @return End time of the last frame in storage.
     */
    public synchronized long getLastFrameTime() {
        if (timedFrames.isEmpty()) {
            throw new EmptyFrameStorageException();
        }
        return timedFrames.getLast().getStopTime();
    }

    /**
     * Clears the frame storage.
     */
    public synchronized void clear() {
        logger.debug("clearing frame storage");
        timedFrames.clear();
    }

    /**
     * Returns the start time of the first frame. If the storage is empty
     * {@link EmptyFrameStorageException} will be thrown.
     *
     * @return Start time of first frame.
     */
    private synchronized long getFirstFrameTime() {
        if (timedFrames.isEmpty()) {
            throw new EmptyFrameStorageException();
        }
        return timedFrames.getFirst().getStartTime();
    }

    
    @Override
    public String toString() {
        return "FixedFrameSizeFrameStorage{" + "number of frames=" + timedFrames.size() + ", frameLength=" + LENGTH + '}';
    }
    
}
