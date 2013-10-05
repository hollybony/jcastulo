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
import java.util.List;
import org.slf4j.LoggerFactory;

/**
 *
 * <code>StreamProcessorImpl</code> abstract implementation
 *
 * @author Carlos Juarez
 */
public class StreamProcessorImpl implements StreamUpdatable {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(StreamProcessorImpl.class);
    
    protected final FrameStorage frameStorage = new FrameStorage();
    
    private FrameIterator frameIterator;
    
    private FrameIteratorFactory mediaReaderFactory;
    
    private StreamSpec streamSpec;
    
    private List<StreamListener> streamListeners;
    
    private final DataReader dataReader;
    
    private MetadataManager metadataManager;

    private enum Status {
        STOPPED, PLAYING, CURRENT_MEDIA_CHANGE_REQUESTED, PLAYING_SILENCE
    };
    private Status status = Status.STOPPED;

    public StreamProcessorImpl(StreamSpec streamSpec) {
        this(streamSpec, new PreloadStreamDataReader());
    }

    public StreamProcessorImpl(StreamSpec streamSpec, DataReader dataReader) {
        this(streamSpec, dataReader, new FrameIteratorFactory());
    }

    public StreamProcessorImpl(StreamSpec streamSpec, DataReader dataReader, FrameIteratorFactory mediaReaderFactory) {
        this.streamSpec = streamSpec;
        metadataManager = new MetadataManager();
        streamListeners = new ArrayList<StreamListener>();
        this.dataReader = dataReader;
        this.mediaReaderFactory = mediaReaderFactory;
    }

    /**
     *
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
                    updateMediaReader();
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
     * Makes sure that there is a valid {@link FrameIterator} instantiated as long
     * as there are more entries in the queue. If the first media of the queue
     * is being reading a new reader is created and open If the current reader
     * has reached the eof and there are remaining medias, reader for the next
     * media is created and open
     */
    private void updateMediaReader() {
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
            }else if (frameIterator != null && frameIterator.hasNext()) {
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
    
    private void playSilence(){
        frameIterator = SilentMediaReader.getInstance();
        updateCurrentMedia(null);
        status = Status.PLAYING_SILENCE;
        for (StreamListener listener : streamListeners) {
            listener.mediaChanged();
        }
    }
    
    private void playMedia() throws IOException{
        frameIterator = mediaReaderFactory.getFrameIterator(streamSpec.getAudioMedias().get(0));
        frameIterator.open(streamSpec.getAudioMedias().get(0));
        updateCurrentMedia(streamSpec.getAudioMedias().get(0));
        logger.info("now playing " + currentMetadata());
        status = Status.PLAYING;
        for (StreamListener listener : streamListeners) {
            listener.mediaChanged();
        }
    }
    
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

    private boolean consumeMedia(int index) {
        synchronized (streamSpec.getAudioMedias()) {
            if (!streamSpec.getAudioMedias().isEmpty()) {
                streamSpec.getAudioMedias().remove(index);
                return true;
            }
            return false;
        }
    }

    @Override
    public void addMedia(AudioMedia media) {
        streamSpec.getAudioMedias().add(media);
        for (StreamListener streamListener : streamListeners) {
            streamListener.queueChanged();
        }
    }

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
     * first take off then put in
     * @param sourceIndex
     * @param targetIndex 
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
    
    @Override
    public void removeListener(StreamListener listener) {
        streamListeners.remove(listener);
    }

    @Override
    public byte[] currentMetadataBytes() {
        return metadataManager.getBytesMetaData();
    }

    @Override
    public SongMetadata currentMetadata() {
        return metadataManager.getCurrentMetadata();
    }

    @Override
    public FrameStorage getFrameStorage() {
        return frameStorage;
    }

    @Override
    public void addListener(StreamListener listener) {
        streamListeners.add(listener);
    }

  
}
