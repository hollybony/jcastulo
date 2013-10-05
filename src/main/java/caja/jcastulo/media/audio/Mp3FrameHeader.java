package caja.jcastulo.media.audio;

import org.slf4j.LoggerFactory;

/**
 * Represents an Mp3 frame header (by setting data @see setData method) and provides all the encoded methods
 * necessary to know the details about the header
 * 
 * @author Carlos Juarez
 */
public class Mp3FrameHeader {
    
    /**
     * The logger
     */
    final org.slf4j.Logger logger = LoggerFactory.getLogger(Mp3FrameHeader.class);

    /**
     * Mp3 Frame header is represented by 4 bytes (32 bits)
     * 
     * The mp3 encoding, rate, etc. come in a four byte MPEG header block
     * Any mp3 player requires this info in order to reproduce audio
     * The first byte represents a 0xFF is just a marker
     * 
     * 
     * @see http://sphere.sourceforge.net/flik/docs/streaming.html
     * @see http://jicyshout.sourceforge.net/oreilly-article/java-streaming-mp3-pt2/java-streaming-mp3-pt2.html
     * @see http://jicyshout.sourceforge.net/oreilly-article/java-streaming-mp3-pt1/java-streaming-mp3-pt1.html
     * 
     * 
     */
    private byte header[] = new byte[4];
    
    /**
     * The number of byte where this header was gotten
     */
    private long offset;
    
    /**
     * DUMMY, MPEG1, MPEG2, MPEG2.5
     */
    private static final int sampleRateTable[][] = {
        {0, 44100, 22050, 11025},
        {0, 48000, 24000, 12000},
        {0, 32000, 16000, 8000},
        {0, 0, 0, 0}};
    
    /**
     * V1 L1, V1 L2, V1 L3, V2 L1, V2 L2 & L3 which mean
     * 
     * V1 - MPEG Version 1
     * V2 - MPEG Version 2 and Version 2.5
     * L1 - Layer I
     * L2 - Layer II
     * L3 - Layer III
     */
    private static final int bitRateTable[][] = {
        {0,     0,      0,      0,      0},
        {32,    32,     32,     32,     8},
        {64,    48,     40,     48,     16},
        {96,    56,     48,     56,     24},
        {128,   64,     56,     64,     32},
        {160,   80,     64,     80,     40},
        {192,   96,     80,     96,     48},
        {224,   112,    96,     112,    56},
        {256,   128,    112,    128,    64},
        {288,   160,    128,    144,    80},
        {320,   192,    160,    160,    96},
        {352,   224,    192,    176,    112},
        {384,   256,    224,    192,    128},
        {416,   320,    256,    224,    144},
        {448,   384,    320,    256,    160},
        {-1,     -1,     -1,     -1,     -1}};
    
    /**
     * if the frame is constant bitrate
     */
    private boolean cbr = false;

    /**
     * The fixed frame size only apply when the frame is constant bitrate
     */
    private int fixedFrameSize;
    
    /**
     * Constructs an instance of <code>Mp3FrameHeader</code> class
     */
    public Mp3FrameHeader() {
    }

    /**
     * Sets the all four bytes that represent the header. By setting this data all the getters methods will return
     * updated values according this new data
     * 
     * @param b1
     * @param b2
     * @param b3
     * @param b4 
     */
    public void setData(byte b1, byte b2, byte b3, byte b4) {
        header[0] = b1;
        header[1] = b2;
        header[2] = b3;
        header[3] = b4;
    }

    public byte[] getData() {
        return header;
    }

    /*
     * Found in 20,19 bits
     * 
     * AAAAAAAA AAABBCCD EEEEFFGH IIJJKLMM
     *             ||
     *             VV
     *            MPEG version
     * 
     * 0x18 => 00011000 so with bitwise AND we would get bits 20 and 19
     * then with >>3 we remove the first 3 bits
     * 
     * 7 6 5 4 3 2 1 0 128 64 32 16 8 4 2 1 0x80 0x40 0x20 0x10 0x08 0x04 0x02
     * 0x01
     * 
     * A = 10 B = 11 C = 12 D = 13 E = 14 F = 15
     * 
     * @return 
     */
    public int getMPEGVersion() {
        return 4 - ((header[1] & 0x18) >> 3);
    }

    /**
     * 
     * @return layer description between 1 and 4
     */
    public int getLayerDescription() {
        return 4 - ((header[1] & 0x06) >> 1);
    }

    public boolean isCRCProtected() {
        return (header[1] & 0x01) == 1;
    }

    /**
     * The bitrate is getting from the header byte that represents the MPEG version
     * 0xF0 => 11110000
     * >>4 remove the 4 first bits
     * @return 
     */
    public int getBitRate() {
        int index = (header[2] & 0xf0) >> 4;
        int mpgVersion = getMPEGVersion();
        int layer = getLayerDescription();
        int index2 = Math.min((mpgVersion - 1) * 3 + (layer - 1), 4);
        return bitRateTable[index][index2] * 1000;
    }

    /**
     * @return if the frame is padded
     */
    public boolean isPadded() {
        return (header[2] & 0x2) == 2;
    }

    /**
     * @return sampling rate in Hz
     */
    public int getSampleRate() {
        int index = (header[2] & 0x0e) >> 2;
        if (index < 0 || index > 3) {
            return 0;
        }
        int version = getMPEGVersion();
        return sampleRateTable[index][version];
    }

    /**
     * @return the channel mode
     */
    public int getChannelMode() {
        return ((header[3] & 0xC0) >> 6);
    }

    /**
     * @return the mode extension
     */
    public int getModeExtension() {
        return ((header[3] & 0x30) >> 4);
    }

    /**
     * @return <code>true</code> if the frame is copyright protected
     */
    public boolean isCopyrighted() {
        return (header[3] & 0x08) != 0;
    }

    /**
     * @return <code>true</code> if this is not a copied file
     */
    public boolean isOriginal() {
        return (header[3] & 0x04) != 0;
    }

    /**
     * @return the emphasis
     */
    public int getEmphasis() {
        return (header[3] & 0x03);
    }

    /**
     * @return the frame size in bytes
     */
    public int getFrameSize() {
        if(isCbr()){
            return fixedFrameSize;
        }else{
            int bitrate = getBitRate();
            int samplerate = getSampleRate();
            int padding = isPadded() ? 1 : 0;
            if(samplerate + padding==0){
                throw new RuntimeException("samplerate + padding = " + (samplerate + padding) + " it would cause / by zero");
            }
            if(getLayerDescription()==1 || getLayerDescription()==4){
                logger.warn("layer description not allowed = " + getLayerDescription());
            }
            
            return 144 * bitrate / samplerate + padding;
        }
    }

    /**
     * @return the offset byte within the file
     */
    public long getOffset() {
        return offset;
    }

    /**
     * @param offset - the offset to set
     */
    public void setOffset(long offset) {
        this.offset = offset;
    }

    /**
     * @return <code>true</code> if it is constant bitrates
     */
    public boolean isCbr() {
        return cbr;
    }

    /**
     * @param cbr - set if it is a constant bitrate
     */
    public void setCbr(boolean cbr) {
        this.cbr = cbr;
    }

    /**
     * @param fixedFrameSize - fixedFrameSize to set only applicable when cbr is <code>true</code>
     */
    public void setFixedFrameSize(int fixedFrameSize) {
        this.fixedFrameSize = fixedFrameSize;
    }
    
    @Override
    public String toString() {
        return "Mp3Header[" + offset + "] {" + " MPEG: "
                + getMPEGVersion() + " Layer: "
                + getLayerDescription() + " Is CRC protected: "
                + isCRCProtected() + " Bitrate: " + getBitRate()
                + " Samplerate: " + getSampleRate() + " Is Padded: "
                + isPadded() + " Channel mode: " + getChannelMode()
                + " Mode Extension: " + getModeExtension() + " Copyright: "
                + isCopyrighted() + " Is original: " + isOriginal()
                + " Emphasis: " + getEmphasis() + " Frame size: "
                + getFrameSize() + "}";
    }
}
