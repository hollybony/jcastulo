package caja.jcastulo.stream;

import caja.jcastulo.media.Frame;


/**
 * Immutable class that wraps a frame with start and stop times. That means the frame is in the time context
 *
 * @author bysse
 */
public class TimedFrame implements Comparable<TimedFrame> {

    /**
     * Start time of the frame in milliseconds
     */
    private final long startTime;
    
    /**
     * Stop time of the frame in milliseconds
     */
    private final long stopTime;
    
    /**
     * The frame info
     */
    private final Frame frame;

    /**
     * Constructs an instance of <code>TimedFrame</code> class
     * 
     * @param time - the starting time of the frame
     * @param frame - the frame itself
     */
    public TimedFrame(long time, Frame frame) {
        this.startTime = time;
        this.stopTime = time + frame.getLength();
        this.frame = frame;
    }

    /**
     * @return the start time
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * @return the stop time
     */
    public long getStopTime() {
        return stopTime;
    }

    /**
     * @return the frame
     */
    public Frame getFrame() {
        return frame;
    }

    /**
     * This implementation considers overlapping intervals to be equal.
     */
    @Override
    public int compareTo(TimedFrame o) {
        if (stopTime < o.startTime) {
            return -1;
        }
        if (startTime >= o.stopTime) {
            return 1;
        }
        return 0;
    }

    @Override
    public String toString() {
        return "Spans from " + getStartTime() + " to " + getStopTime() + " (" + getFrame() + ")";
    }
}
