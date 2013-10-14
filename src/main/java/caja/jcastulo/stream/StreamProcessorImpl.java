package caja.jcastulo.stream;

import caja.jcastulo.media.FrameIterator;
import caja.jcastulo.media.FrameIteratorFactory;
import caja.jcastulo.media.MetadataManager;
import caja.jcastulo.media.SilentMediaReader;
import caja.jcastulo.media.audio.SongMetadata;
import caja.jcastulo.media.entities.AudioMedia;
import caja.jcastulo.stream.entities.StreamSpec;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.LoggerFactory;

/**
 * Default implementation of <code>StreamUpdateable</code> as it is <code>Runnable</code> class the main method
 * is run() it means that when this thread is running, the audio medias contained in the streamSpec streamSpec.getAudioMedias() 
 * are managed as a queue in such a way that the first audio media audioMedias.get(0) in that list is going to be streamed
 * by updating the frameStorage and the metadataManager frame by frame then the audio media is going to be removed from
 * the queue and the following audio media is the next to be streamed repeating the same process until the queue got empty
 *
 * @author Carlos Juarez
 */
public class StreamProcessorImpl implements StreamUpdateable {

    /**
     * The logger
     */
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(StreamProcessorImpl.class);
    
    /**
     * The frame storage
     */
    protected final FrameStorage frameStorage = new FrameStorage();
    
    /**
     * The current frame iterator
     */
    private FrameIterator frameIterator;
    
    /**
     * The frameIteratorFactory
     */
    private FrameIteratorFactory frameIteratorFactory;
    
    /**
     * The streamSpec
     */
    private StreamSpec streamSpec;
    
    /**
     * The streamListeners
     */
    private List<StreamListener> streamListeners;
    
    /**
     * The dataReader
     */
    private final FrameStorageUpdater dataReader;
    
    /**
     * The metadataManager
     */
    private MetadataManager metadataManager;

    /**
     * Different internal StreamProcessor status
     */
    private enum Status {
        STOPPED, PLAYING, CURRENT_MEDIA_CHANGE_REQUESTED, PLAYING_SILENCE
    };
    
    /**
     * The current status
     */
    private Status status = Status.STOPPED;

    /**
     * Constructs an instance of <code>StreamProcessorImpl</code> class
     * 
     * @param streamSpec - the streamSpec to set
     */
    public StreamProcessorImpl(StreamSpec streamSpec) {
        this(streamSpec, new PreloadStreamDataReader());
    }

    /**
     * Constructs an instance of <code>StreamProcessorImpl</code> class
     * 
     * @param streamSpec - the streamSpec to set
     * @param dataReader - the dataReader to set
     */
    public StreamProcessorImpl(StreamSpec streamSpec, FrameStorageUpdater dataReader) {
        this(streamSpec, dataReader, new FrameIteratorFactory());
    }

    /**
     * Constructs an instance of <code>StreamProcessorImpl</code> class
     * 
     * @param streamSpec - the streamSpec to set
     * @param dataReader - the dataReader to set
     * @param mediaReaderFactory - the mediaReaderFactory to set
     */
    public StreamProcessorImpl(StreamSpec streamSpec, FrameStorageUpdater dataReader, FrameIteratorFactory mediaReaderFactory) {
        this.streamSpec = streamSpec;
        metadataManager = new MetadataManager();
        streamListeners = new ArrayList<StreamListener>();
        this.dataReader = dataReader;
        this.frameIteratorFactory = mediaReaderFactory;
    }

    /**
     * Runs the stream processor until an interruption or error. Basically iterates over frames and if necessary
     * change the current and medias as well as its frame iterator
     */
    @Override
    public void run() {
        synchronized (this) {
            status = Status.PLAYING;
        }
        //every iteration pulls a new frame from the media reader
        while (true) {
            try {
                synchronized (this) {
                    checkForStatusChanges();
                }
            } catch (Exception ex) {
                logger.error(Thread.currentThread().getName() + " exception while updating media reader", ex);
                cleanup();
                return;
            }
            try {
                if (frameIterator != null) {
                    dataReader.readData(frameIterator, frameStorage);
                }
            } catch (InterruptedException ex) {
                logger.debug(Thread.currentThread().getName() + " processor interrupted while reading data", ex);
                cleanup();
                return;
            } catch (Exception ex) {
                logger.error(Thread.currentThread().getName() + " exception while reading data", ex);
            }
            if (Thread.currentThread().isInterrupted()) {
                cleanup();
                return;
            }
        }
    }

    /**
     * Performs cleanup. Clears frame storage and closes frameIterator
     */
    protected synchronized void cleanup() {
        frameStorage.clear();
        if (frameIterator != null) {
            try {
                frameIterator.close();
            } catch (IOException ex) {
                logger.error(Thread.currentThread().getName() + " IOException while closing the mediaReader", ex);
            }
        }
        status = Status.STOPPED;
        frameIterator = null;
    }

    /**
     * Makes sure there is a valid {@link FrameIterator} instantiated as long
     * as there are more medias in the queue. If the first media of the queue
     * is what is being reading then a new frame iterator is created and open If the
     * current frame iterator has reached the eof and there are remaining medias,
     * frame iterator for the next media is created and open
     */
    private void checkForStatusChanges() {
        if (status.equals(Status.CURRENT_MEDIA_CHANGE_REQUESTED)) {
            logger.info("skipping current media " + currentMetadata());
            if (frameIterator != null) {
                try {
                    frameIterator.close();
                } catch (IOException ex) {}
            }
            consumeMedia(0);
            if (!streamSpec.getAudioMedias().isEmpty()) {
                try {
                    playMedia();
                } catch (IOException ex) {
                    processCurrentMediaError(ex);
                }
            } else {
                playSilence();
            }
        }else if(status.equals(Status.PLAYING)){
            if (frameIterator == null && !streamSpec.getAudioMedias().isEmpty()) {
                try {
                    playMedia();
                } catch (IOException ex) {
                    processCurrentMediaError(ex);
                }
            }else if (frameIterator != null && !frameIterator.hasNext()) {
                consumeMedia(0);
                try{
                    frameIterator.close();
                }catch(IOException ex){}
                frameIterator = null;
                // consume the played file
                if (!streamSpec.getAudioMedias().isEmpty()) {
                    try {
                        playMedia();
                    } catch (IOException ex) {
                        processCurrentMediaError(ex);
                    }
                } else {
                    playSilence();
                }
            }
        }else if(status.equals(Status.PLAYING_SILENCE)){
            if (!streamSpec.getAudioMedias().isEmpty()) {
                try {
                    playMedia();
                } catch (IOException ex) {
                    processCurrentMediaError(ex);
                }
            }
        }
    }
    
    /**
     * Sets as current frameIterator a silent frame
     */
    private void playSilence(){
        frameIterator = SilentMediaReader.getInstance();
        updateCurrentMedia(null);
        status = Status.PLAYING_SILENCE;
        for (StreamListener listener : streamListeners) {
            listener.mediaChanged();
        }
    }
    
    /**
     * Updates frame iterator with the one for the media at the top of the queue as well as the metadata
     * 
     * @throws IOException 
     */
    private void playMedia() throws IOException{
        synchronized (streamSpec.getAudioMedias()) {
            Map<String,Object> properties = new HashMap<String, Object>();
            properties.put("bitrate", 64000);
            frameIterator = frameIteratorFactory.getIterator(streamSpec.getAudioMedias().get(0), properties);
            frameIterator.open(streamSpec.getAudioMedias().get(0), properties);
            updateCurrentMedia(streamSpec.getAudioMedias().get(0));
        }
        logger.info("now playing " + currentMetadata());
        status = Status.PLAYING;
        for (StreamListener listener : streamListeners) {
            listener.mediaChanged();
        }
    }
    
    /**
     * Handles exception when a media cannot be open. Updates the frame iterator with silent and set nothing playing
     * as metadata
     * 
     * @param ex 
     */
    private void processCurrentMediaError(Exception ex){
        logger.error(Thread.currentThread().getName() + " can't open [" + streamSpec.getAudioMedias().get(0) + "], skipping file", ex);
        consumeMedia(0);
        frameIterator = SilentMediaReader.getInstance();
        updateCurrentMedia(null);
        status = Status.PLAYING_SILENCE;
        for (StreamListener listener : streamListeners) {
            listener.mediaChanged();
        }
    }
    
    /**
     * @return the streamSpec
     */
    @Override
    public StreamSpec getStreamSpec() {
        return streamSpec;
    }

    /**
     * Calls the nextMedia method in all registered MediaQueueListeners
     *
     * @param media
     */
    protected void updateCurrentMedia(final AudioMedia media) {
        if (frameIterator == SilentMediaReader.getInstance()) {
            metadataManager.setMetadata(new SongMetadata("silence...", null, null));
        } else {
            SongMetadata songMetadata = media.getSongMetadata();
            metadataManager.setMetadata(songMetadata);
        }
    }

    /**
     * Removes a media from the queue
     * 
     * @param index - the index of the media being removed
     * @return <code>true</code> if the medias was removed
     */
    private boolean consumeMedia(int index) {
        synchronized (streamSpec.getAudioMedias()) {
            if (!streamSpec.getAudioMedias().isEmpty()) {
                streamSpec.getAudioMedias().remove(index);
                return true;
            }
            return false;
        }
    }

    /**
     * Adds a media to the queue and notifies listeners
     * 
     * @param media - the media to add
     */
    @Override
    public void addMedia(AudioMedia media) {
        streamSpec.getAudioMedias().add(media);
        for (StreamListener streamListener : streamListeners) {
            streamListener.queueChanged();
        }
    }

    /**
     * Removes a media from the queue and notifies listeners. If the stream is running and the media to remove is
     * playing this will be stopped and removed
     * 
     * @param index - index of the media to remove
     */
    @Override
    public void removeMedia(final int index) {
        synchronized (this) {
            if (index > 0 || (index == 0 && status.equals(Status.STOPPED))) {
                if (consumeMedia(index)) {
                    for (StreamListener streamListener : streamListeners) {
                        streamListener.queueChanged();
                    }
                }
            } else if (status.equals(Status.PLAYING)) {
                status = Status.CURRENT_MEDIA_CHANGE_REQUESTED;
            }
        }
    }
    
    /**
     * Moves a media from one place to another in the queue and notifies listeners
     * 
     * @param sourceIndex - index where the media is taken
     * @param targetIndex - index where the media is putted
     */
    @Override
    public void moveMedia(int sourceIndex, int targetIndex) {
        synchronized (this) {
            AudioMedia sourceMedia;
            synchronized (streamSpec.getAudioMedias()) {
                try{
                    sourceMedia = streamSpec.getAudioMedias().get(sourceIndex);        
                    if(sourceIndex==0 && status.equals(Status.PLAYING)){
                        streamSpec.getAudioMedias().add(targetIndex, sourceMedia);
                        status = Status.CURRENT_MEDIA_CHANGE_REQUESTED;
                    }else if(targetIndex==0 && status.equals(Status.PLAYING)){
                        streamSpec.getAudioMedias().remove(sourceIndex);
                        streamSpec.getAudioMedias().add(1, sourceMedia);
                        streamSpec.getAudioMedias().add(2, streamSpec.getAudioMedias().get(0));
                        status = Status.CURRENT_MEDIA_CHANGE_REQUESTED;
                    }else{
                        streamSpec.getAudioMedias().add(targetIndex, sourceMedia);
                        streamSpec.getAudioMedias().remove(sourceIndex<targetIndex?sourceIndex:sourceIndex+1);
                        for (StreamListener streamListener : streamListeners) {
                            streamListener.queueChanged();
                        }
                    }
                }catch(IndexOutOfBoundsException ex){
                }
            }
        }
    }
    
    /**
     * Removes a listener from the list of listeners
     * 
     * @param listener - the listener to remove
     */
    @Override
    public void removeListener(StreamListener listener) {
        streamListeners.remove(listener);
    }

    /**
     * @return the encoded bytes array from the current metadata
     */
    @Override
    public byte[] currentMetadataBytes() {
        return metadataManager.getBytesMetaData();
    }

    /**
     * @return current metadata
     */
    @Override
    public SongMetadata currentMetadata() {
        return metadataManager.getCurrentMetadata();
    }

    /**
     * @return the frameStorage
     */
    @Override
    public FrameStorage getFrameStorage() {
        return frameStorage;
    }

    /**
     * Adds a stream listeners to the listeners list
     * 
     * @param listener - the listener to add
     */
    @Override
    public void addListener(StreamListener listener) {
        streamListeners.add(listener);
    }
  
}
