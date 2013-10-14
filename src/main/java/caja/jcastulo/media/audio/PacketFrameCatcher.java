/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package caja.jcastulo.media.audio;

import caja.jcastulo.media.Frame;
import caja.jcastulo.media.SilentMediaReader;
import com.xuggle.mediatool.MediaListenerAdapter;
import com.xuggle.mediatool.event.IWritePacketEvent;
import com.xuggle.xuggler.IPacket;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Carlos Juarez
 */
public class PacketFrameCatcher extends MediaListenerAdapter {
    
    final org.slf4j.Logger logger = LoggerFactory.getLogger(Mp3FrameHeaderFinderImpl.class);
    
    private Mp3FrameHeader currentHeader = new Mp3FrameHeader();
    
    private Frame currentFrame;
    
    private long offset;
    
    private boolean newFrame = false;

    @Override
    public void onWritePacket(IWritePacketEvent event) {
        newFrame = true;
        currentFrame = null;
        currentFrame = new Frame();
        IPacket packet = event.getPacket();
        byte[] bytes = new byte[packet.getSize()];
        packet.get(0, bytes, 0, packet.getSize());
        currentHeader.setData(bytes[0], bytes[1], bytes[2], bytes[3]);
        currentHeader.setOffset(offset);
        offset += bytes.length;
        if(currentHeader.getBitRate()==-1000){
            logger.debug("bitrate not valid");
            currentFrame = SilentMediaReader.frame;
        }
        currentFrame.setSize(currentHeader.getFrameSize());
        //the header info is copied
        System.arraycopy(currentHeader.getData(), 0, currentFrame.getData(), 0, 4);
        //since we already have the size of the frame we want we are going to take them from the reader
        //we skip 4 bytes that represent the header
        System.arraycopy(bytes, 4, currentFrame.getData(), 4, bytes.length - 4);
    }

    public Frame getCurrentFrame(){
        newFrame = false;
        return currentFrame;
    }

    public boolean isNewFrame() {
        return newFrame;
    }
}
