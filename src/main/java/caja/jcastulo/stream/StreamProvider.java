/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package caja.jcastulo.stream;

/**
 * Represents a provider of stream that runs as a service or thread it depends of the implementation
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
     * @return the found timed frame
     * 
     * @throws EmptyFrameStorageException
     */
    public TimedFrame findTimedFrame(long time);
    
    /**
     * @return the current encoded metadata
     */
    public byte[] getMetadata();
    
}
