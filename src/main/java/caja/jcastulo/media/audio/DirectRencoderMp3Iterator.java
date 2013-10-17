/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package caja.jcastulo.media.audio;

import caja.jcastulo.media.Frame;
import caja.jcastulo.media.FrameIterator;
import caja.jcastulo.media.entities.AudioMedia;
import com.xuggle.xuggler.IAudioSamples;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IPacket;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Carlos Juarez
 */
public class DirectRencoderMp3Iterator implements FrameIterator {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(DirectRencoderMp3Iterator.class);
    
    /**
     * The bitrates allowed
     */
    static final int[] bitratesAllowed = {32000, 64000, 96000, 128000};
    
    private IContainer inputContainer;
    
    private int audioStreamIndex;
    
    private IStreamCoder decoder;
    
    private IStreamCoder encoder;
    
    private PacketFrameCatcher packetFrameCatcher = new PacketFrameCatcher();

    @Override
    public boolean supports(AudioMedia media, Map<String, Object> properties) {
        if (media.getPathname().toString().toLowerCase().endsWith(".mp3")) {
            if (properties == null || properties.isEmpty()) {
                return true;
            } else {
                int bitrate = (Integer) properties.get("bitrate");
                return isBitrateValid(bitrate);
            }
        }
        return false;
    }

    private boolean isBitrateValid(final int bitrate) {
        for (int currBitrate : bitratesAllowed) {
            if (currBitrate == bitrate) {
                return true;
            }
        }
        logger.debug("bitrate : " + bitrate + " is not supported. These are the supported bitrates : " + Arrays.asList(bitratesAllowed));
        return false;
    }

    @Override
    public void open(AudioMedia media, Map<String, Object> properties) throws IOException {
        final int bitrate = (Integer) properties.get("bitrate");
        if (!isBitrateValid(bitrate)) {
            throw new RuntimeException(bitrate + " is a bitrate not valid");
        }
        setupReencoding(media.getPathname(), bitrate, packetFrameCatcher);
    }

    @Override
    public Frame next() throws IOException {
        if (!packetFrameCatcher.isNewFrame()) {
            if (hasNext()) {
                return packetFrameCatcher.getCurrentFrame();
            } else {
                return null;
            }
        }
        return packetFrameCatcher.getCurrentFrame();
    }

    @Override
    public boolean hasNext() {
        if (packetFrameCatcher.isNewFrame()) {
            return true;
        } else {
            while (true) {
                try {
                    if (readNextPacket()) {
                        if (packetFrameCatcher.isNewFrame()) {
                            return true;
                        }
                    } else {
                        return false;
                    }
                } catch (Exception ex) {
                }
            }
        }
    }

    @Override
    public void close() throws IOException {
        if (decoder != null) {
            decoder.close();
            decoder = null;
        }
        if (inputContainer != null) {
            inputContainer.close();
            inputContainer = null;
        }
        if (encoder != null) {
            encoder.close();
        }
    }

    public void setupReencoding(String pathname, final int bps, PacketFrameCatcher packetFrameCatcher) {
        inputContainer = IContainer.make();
        if (inputContainer.open(pathname, IContainer.Type.READ, null) < 0) {
            throw new IllegalArgumentException("could not open file: " + pathname);
        }
        int numStreams = inputContainer.getNumStreams();
        decoder = null;
        audioStreamIndex = -1;
        for (int i = 0; i < numStreams; i++) {
            IStream stream = inputContainer.getStream(i);
            IStreamCoder coder = stream.getStreamCoder();
            if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_AUDIO) {
                decoder = coder;
                audioStreamIndex = i;
                break;
            }
        }
        if (decoder != null) {
            if (decoder.open(null, null) < 0) {
                throw new RuntimeException("could not open audio decoder for container: " + pathname);
            }
            // configre the stream coder
            encoder = IStreamCoder.make(IStreamCoder.Direction.ENCODING, ICodec.findEncodingCodec(ICodec.ID.CODEC_ID_MP3));
            encoder.setChannels(decoder.getChannels());
            encoder.setSampleRate(decoder.getSampleRate());
            encoder.setSampleFormat(IAudioSamples.Format.FMT_S16);
            //set out bitrate
            encoder.setFlag(IStreamCoder.Flags.FLAG_QSCALE, false);
            encoder.setBitRate(bps);
            encoder.setBitRateTolerance(0);
            if (encoder.open(null, null) < 0) {
                throw new RuntimeException("could not open audio enconder");
            }
        } else {
            throw new RuntimeException("There were no streams");
        }
    }

    private boolean readNextPacket() {
        IPacket packet = IPacket.make();
        //reading packets
        int readNextPacket = inputContainer.readNextPacket(packet);
        if (readNextPacket >= 0) {
            if (packet.getStreamIndex() == audioStreamIndex) {
                IAudioSamples samples = IAudioSamples.make(1024, decoder.getChannels());
                int offset = 0;
                while (offset < packet.getSize()) {
                    int bytesDecoded = decoder.decodeAudio(samples, packet, offset);
                    if (bytesDecoded < 0) {
                        break;
//                            throw new RuntimeException("got error decoding audio in: " + pathname);
                    }
                    offset += bytesDecoded;
                    if (samples.isComplete()) {
                        encodeAudio(encoder, samples, packetFrameCatcher);
                    }
                }
            } else {
                do {
                } while (false);
            }
        }
        return readNextPacket>=0;
    }

    private void encodeAudio(IStreamCoder coder, IAudioSamples samples, PacketFrameCatcher packetFrameCatcher) {
        for (int consumed = 0; consumed < samples.getNumSamples();) {
            // encode audio
            IPacket packet = IPacket.make();
            try {
                int result = coder.encodeAudio(packet, samples, consumed);
                if (result < 0) {
                    throw new RuntimeException("failed to encode audio");
                }
                // update total consumed
                consumed += result;
                // if a complete packed was produced write it out
                if (packet.isComplete()) {
                    packetFrameCatcher.writePacket(packet);
                }
            } finally {
                if (packet != null) {
                    packet.delete();
                }
            }
        }
    }
}
