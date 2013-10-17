package caja.jcastulo.shout.http;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class RequestTest {

    @Test
    public void testRequest() throws Exception {
        String httpRequest = "GET /media.mp3 HTTP/1.0\n\rHost: localhost\n\rUser-Agent: xmms/1.2.10\n\rIcy-MetaData:1\n\r\n\r";
        Request request = new Request(httpRequest);
        assertEquals("/media.mp3", request.getPath());
        assertFalse(request.isBrowserUserAgent());
        assertTrue(request.isMetadataRequested());
        /////////////////
        String chromeRequest = "GET /folk.mp3 HTTP/1.1\n\rHost: localhost:8001\n\rConnection: keep-alive\n\r" +
            "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8\n\r" +
            "User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.69 Safari/537.36\n\r" +
            "Accept-Encoding: gzip,deflate,sdch\n\rAccept-Language: en-US,en;q=0.8";
        request = new Request(chromeRequest);
        assertEquals("/folk.mp3", request.getPath());
        assertTrue(request.isBrowserUserAgent());
        assertFalse(request.isMetadataRequested());
        /////////////////
        String winampRequest = "GET /folk.mp3 HTTP/1.0\n\rHost: localhost\n\rUser-Agent: WinampMPEG/5.63, Ultravox/2.1\n\r" +
                "Ultravox-transport-type: TCP\n\rAccept: */*\n\rIcy-MetaData:1";
        request = new Request(winampRequest);
        assertEquals("/folk.mp3", request.getPath());
        assertFalse(request.isBrowserUserAgent());
        assertTrue(request.isMetadataRequested());
    }
}
