/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package caja.jcastulo.stream;

/**
 *
 * @author Carlos Juarez
 */
public interface StreamProvider {
    
    public boolean isRunning();
    
    public String getMountPoint();
    
    public String getStreamName();
            
    public TimedFrame findTimedFrame(long time);
    
    public byte[] getMetadata();
    
}
