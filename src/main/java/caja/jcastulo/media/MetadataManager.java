package caja.jcastulo.media;

import caja.jcastulo.media.audio.SongMetadata;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * Controls when a full meta data chunk should be rendered and in which format.
 * see http://sphere.sourceforge.net/flik/docs/streaming.html
 * 
 * @author bysse
 *
 */
public class MetadataManager{
    
    /**
     * The logger
     */
    final org.slf4j.Logger logger = LoggerFactory.getLogger(MetadataManager.class);
    
    /**
     * According to Winamp the max size of bytes is 4080 not including the first byte (the flag)
     * but other people says the limit is 4080 not including the first byte (the flag)
     */
    private final static int MAXIMUM_METADATA_LENGTH = 4080;
    
    private final static Pattern FIELD = Pattern.compile("(\\$\\{([^\\}\\$]+)\\})|(\\$([^\\s\\?\\$]+))");
    
    private final static Pattern CONDITION = Pattern.compile("\\?\\(([^,]+),([^\\)]+)\\)");
    
    /**
     * The song format
     */
    private final static String FORMAT = "$artist ?(album,- )$album ?(title,- )$title";
    
    /**
     * In case meta data info is requested too frequent (15 seconds)
     */
    private final static long SEND_METADATA_INTERVAL = 15000;
    
    /**
     * The current meta data
     */
    private SongMetadata currentMetadata;
    
    /**
     * The current meta data represented as String
     */
    private String currentMetadataString = "";
    
    /**
     * Holds the last time that the meta data was sent
     */
    private long lastMetadataChunk = 0;

    /**
     * Constructs an instance of <code>MetadataManager</code> class. Sets the meta data to "Nothing playing"
     */
    public MetadataManager() {
        setMetadata(new SongMetadata("Nothing playing", null, null));
    }

    /**
     * Sets and renders new meta data with the current format.Winamp complaints if there is no a ; at the end
     *
     * @param metadata
     */
    public final synchronized void setMetadata(final SongMetadata metadata) {
        if(currentMetadata!=null && currentMetadata.equals(metadata)){
            return;
        }
        this.currentMetadata = metadata;
        logger.debug("new metadata set [" + metadata + "]");
        lastMetadataChunk = 0;
        currentMetadataString = parseFormat("StreamTitle='" + FORMAT, metadata);
        if (currentMetadataString.length() > MAXIMUM_METADATA_LENGTH - 2) {
                currentMetadataString = currentMetadataString.substring(0, MAXIMUM_METADATA_LENGTH - 2);
        }
        currentMetadataString = currentMetadataString + "';";
    }
    
    /**
     * TODO probably the control with lastMetadataChunk is not suitable as different clients could require meta data in less than 15 sec.
     * 
     * Formats and returns a byte array containing meta data information
     * @return
     */
    public synchronized byte[] getBytesMetaData() {
        long time = System.currentTimeMillis();
        if (time - lastMetadataChunk > SEND_METADATA_INTERVAL) {
            logger.trace("send metadata string [" + currentMetadataString + "]");
            //.. return a full metadata string
            lastMetadataChunk = time;
            // restrict the length of the metadata
//            if (currentMetadataString.length() > MAXIMUM_METADATA_LENGTH) {
//                currentMetadataString = currentMetadataString.substring(0, MAXIMUM_METADATA_LENGTH);
//            }
            int metadataLenth = currentMetadataString.length();
            //how many 16 byte blocks has the current meta data
            int encodedLength = ((int) Math.ceil(metadataLenth / 16.0));
            //this if statement would fix the issue that happens when currentMetadataString lenght is exactly 16 multiple
            //and it makes no room for any padding character at the end of the bytes, it looks like for some reason 
            //Winamp requires at least 1 padding character at the end of the byte array
            if(currentMetadataString.length()%16==0){
                encodedLength++;
            }
            int blockLength = 16 * encodedLength;
            byte[] result = new byte[blockLength + 1];
            result[0] = (byte) encodedLength;
            System.arraycopy(currentMetadataString.getBytes(), 0, result, 1, metadataLenth);
            // add padding to the block
            for (int i = metadataLenth + 1; i < blockLength; i++) {
                result[i] = 0;
            }
            logger.debug("send metadata bytes: 16 byte length=" + result[0] + ", content=[" + new String(result) + "]");
            return result;
        } else {
//            logger.debug("Sending zero length metadata chunk");
            return new byte[]{0};
        }
    }

    /**
     * Renders the meta data string sent to the client.
     *
     * @param format
     * @param metadata
     * @return
     */
    protected String parseFormat(final String format, final SongMetadata metadata) {
        String result = format;
        //.. replace all fields in the format string
        Matcher fieldmatch = FIELD.matcher(format);
        while (fieldmatch.find()) {
            String fieldname = fieldmatch.group(2);
            if (fieldname == null) {
                fieldname = fieldmatch.group(4);
            }
            if (fieldname == null) {
                logger.warn("Metadata format uses invalid field syntax '" + fieldmatch.group(0) + "'");
                continue;
            }
            try {
                MetadataType type = MetadataType.valueOf(fieldname.toUpperCase());
                // if there is metadata for this field, insert the data into the string
                // otherwise just remove the expression from the string
                if (StringUtils.hasText(metadata.get(type))) {
                    result = result.replace(fieldmatch.group(0), metadata.get(type));
                } else {
                    result = result.replace(fieldmatch.group(0), "");
                }
            } catch (IllegalArgumentException ex) {
                logger.warn("metadata format uses invalid field name '" + fieldname + "' in expression '" + fieldmatch.group(0) + "'", ex);
            }
        }
        //.. replace all conditionals in the format string
        Matcher conditionmatch = CONDITION.matcher(format);
        while (conditionmatch.find()) {
            String fieldname = conditionmatch.group(1);
            String text = conditionmatch.group(2);
            if (fieldname == null) {
                logger.warn("Metadata format uses invalid field syntax '" + fieldmatch.group(0) + "'");
                continue;
            }
            try {
                MetadataType type = MetadataType.valueOf(fieldname.toUpperCase());
                if (StringUtils.hasText(metadata.get(type))) {
                    result = result.replace(conditionmatch.group(0), text);
                } else {
                    result = result.replace(conditionmatch.group(0), "");
                }
            } catch (IllegalArgumentException e) {
                logger.warn("Metadata format uses invalid field name '" + fieldname + "' in expression '" + conditionmatch.group(0) + "'");
            }
        }
        return result;
    }

    /**
     * Synchronized to avoid currentMetadata been updated when is retrieving and vice versa
     * 
     * @return the current meta data
     */
    public synchronized SongMetadata getCurrentMetadata() {
        return currentMetadata;
    }

}
