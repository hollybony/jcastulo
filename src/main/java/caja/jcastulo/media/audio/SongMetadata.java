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

    private Map<MetadataType, String> data = new EnumMap<MetadataType, String>(MetadataType.class);
    
    public SongMetadata(String artist, String album, String title){
        data.put(MetadataType.ARTIST, artist);
        data.put(MetadataType.ALBUM, album);
        data.put(MetadataType.TITLE, title);
    }
    
    public void set(MetadataType metadataType, String value){
        data.put(metadataType, value);
    }

    public boolean has(MetadataType type) {
        return data.containsKey(type);
    }

    public String get(MetadataType type) {
        return data.get(type);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (MetadataType type : data.keySet()) {
            String value = data.get(type);
            if(value!=null){
                builder.append(data.get(type)).append(", ");
            }
        }
        if (builder.length() > 0) {
            builder.setLength(builder.length() - 2);
        }
        return builder.toString();
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
