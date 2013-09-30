package caja.jcastulo.media;

import caja.jcastulo.media.Frame;
import caja.jcastulo.stream.FrameStorage;
import caja.jcastulo.stream.TimedFrame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;

public class FixedFrameSizeFrameStorageTest {

    @Test
    public void testInsertUpdate() {
        FrameStorage storage = new FrameStorage();

        // fill the storage with entries
        long time = 0;
        for (int i = 0; i < 100; i++) {
            storage.add(makeEntry(time));
            time += 26;
        }

        TimedFrame entry = storage.find(6 * 26 + 3);
        String value = new String(entry.getFrame().getData());
        assertEquals("10011100", value);

        storage.purgeUntil(10 * 26);
        try {
            storage.find(10 * 26 - 1);
            fail();
        } catch (RuntimeException e) {
        }
    }

    private TimedFrame makeEntry(long time) {
        byte[] data = Long.toBinaryString(time).getBytes();

        Frame frame = new Frame();
        frame.setLength(26);

        System.arraycopy(data, 0, frame.getData(), 0, data.length);
        frame.setSize(data.length);

        return new TimedFrame(time, frame);
    }
}
