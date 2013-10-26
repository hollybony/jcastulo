package caja.jcastulo.media.audio;

import caja.jcastulo.media.MetadataType;
import java.util.EnumMap;
import java.util.Map;

/**
 * Holds some basic information about a media file.
 *
 * @author bysse
 *
 */
public class SongMetadata {

    /**
     * Map with the metadata fields
     */
    private Map<MetadataType, String> data = new EnumMap<MetadataType, String>(MetadataType.class);
    
    public SongMetadata(String artist, String album, String title){
        data.put(MetadataType.ARTIST, artist);
        data.put(MetadataType.ALBUM, album);
        data.put(MetadataType.TITLE, title);
    }
    
    /**
     * Sets a new metadata if this already exists then it'll replace the old one
     * 
     * @param metadataType - the metadataType
     * @param value - the new value
     */
    public void set(MetadataType metadataType, String value){
        data.put(metadataType, value);
    }

    /**
     * 
     * @param metadataType - to look for
     * @return <code>true</code> if metadata was found
     */
    public boolean has(MetadataType metadataType) {
        return data.containsKey(metadataType);
    }

    /**
     * @param metadataType - metadataType to look for
     * @return the metadataType found or null if it was not found
     */
    public String get(MetadataType metadataType) {
        return data.get(metadataType);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (MetadataType type : data.keySet()) {
            String value = data.get(type);
            if(value!=null){
                sb.append(" ").append(data.get(type)).append(" -");
            }
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 2);
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (this.data != null ? this.data.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SongMetadata other = (SongMetadata) obj;
        if (this.data != other.data && (this.data == null || !this.data.equals(other.data))) {
            return false;
        }
        return true;
    }
    
}
