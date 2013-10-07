/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package caja.jcastulo.stream;

/**
 * Provides information about a stream which could be running
 * 
 * @author Carlos Juarez
 */
public interface StreamProvider {
    
    /**
     * @return <code>true</code> is the stream is running
     */
    public boolean isRunning();
    
    /**
     * @return the mount point
     */
    public String getMountPoint();
    
    /**
     * @return a name that identifies this stream
     */
    public String getStreamName();
    
    /**
     * Finds a timed frame by milliseconds
     * 
     * @param time - the milliseconds 
     * @return the found timed frame or null it was not found
     */
    public TimedFrame findTimedFrame(long time);
    
    /**
     * @return the current encoded metadata
     */
    public byte[] getMetadata();
    
}
