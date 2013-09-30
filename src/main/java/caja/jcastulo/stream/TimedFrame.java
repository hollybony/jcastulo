package caja.jcastulo.stream;

import caja.jcastulo.media.Frame;


/**
 * Immutable class that wraps a frame with start and stop times.
 *
 * @author bysse
 */
public class TimedFrame implements Comparable<TimedFrame> {

    private final long startTime;
    
    private final long stopTime;
    
    private final Frame frame;

    public TimedFrame(long time, Frame frame) {
        this.startTime = time;
        this.stopTime = time + frame.getLength();
        this.frame = frame;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getStopTime() {
        return stopTime;
    }

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
