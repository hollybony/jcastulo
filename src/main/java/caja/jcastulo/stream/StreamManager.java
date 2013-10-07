package caja.jcastulo.stream;

import org.slf4j.LoggerFactory;

/**
 * A thread safe <code>StreamManager</code> implementations
 * 
 * @author Carlos Juarez
 */
public class StreamManager implements StreamProvider{

    /**
     * The logger
     */
    final org.slf4j.Logger logger = LoggerFactory.getLogger(StreamManager.class);
                        
    /**
     * The stream processor that supports updates
     */
    private final StreamUpdateable processor;
    
    /**
     * The thread where the stream manager runs
     */
    private Thread processorThread;
    
    /**
     * Constructs an instance of <code>StreamManager</code> class
     * 
     * @param processor stream updateable to set
     */
    public StreamManager(StreamUpdateable processor) {
        this.processor = processor;
    }
    
    @Override
    public TimedFrame findTimedFrame(long time) {
        return processor.getFrameStorage().find(time);
    }
    
    @Override
    public byte[] getMetadata(){
        return processor.currentMetadataBytes();
    }

    @Override
    public String getMountPoint() {
        return processor.getStreamSpec().getMountPoint();
    }

    @Override
    public String getStreamName() {
        return processor.getStreamSpec().getName();
    }

    /**
     * Starts the streaming by starting the processThread as long as it is stopped
     */
    public synchronized void start() {
        if(!isRunning()){
            logger.info("starting streaming " + processor.getStreamSpec());
            //opens file generates metadata
            processorThread = new Thread(processor, "QueueProcessor[" + getMountPoint() + "]");
            processorThread.start();
        }else{
            logger.info("streaming " + processor.getStreamSpec() + " is already running");
        }
    }

    /**
     * Stops the streaming as long as it is running
     */
    public synchronized void stop() {
        if(isRunning()){
            logger.info("stopping streaming " + processor.getStreamSpec());
            processorThread.interrupt();
            int attempts = 3;
            while(attempts>0&&processorThread.isAlive()){
                try {
                    Thread.sleep(333);
                    attempts--;
                } catch (InterruptedException ex) {
                    throw new RuntimeException("interruption while trying to stop the streaming", ex);
                }
            }
            if(processorThread.isAlive()){
                throw new RuntimeException("stream provider could not be stopped");
            }else{
                logger.info("streaming " + processor.getStreamSpec() + " has been stopped");
                processorThread = null;
            }
        }else{
            logger.info("the streaming " + processor.getStreamSpec() + " is not running so can't be stopped");
        }
    }

    @Override
    public synchronized boolean isRunning() {
        return processorThread != null && processorThread.isAlive();
    }
    
    /**
     * @return the stream updateable
     */
    public StreamUpdateable getProcessor() {
        return processor;
    }

    @Override
    public String toString() {
        return "StreamManager{" + "streamName=" + getStreamName() + ", mountPoint=" + getMountPoint() + ", running=" + isRunning() + '}';
    }
    
    
}
