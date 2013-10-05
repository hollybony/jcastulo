package caja.jcastulo.shout;

import caja.jcastulo.media.Frame;
import caja.jcastulo.shout.http.Request;
import caja.jcastulo.stream.EmptyFrameStorageException;
import caja.jcastulo.stream.NoLoadedFrameException;
import caja.jcastulo.stream.OldFrameException;
import caja.jcastulo.stream.StreamProvider;
import caja.jcastulo.stream.TimedFrame;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Carlos Juarez
 */
public class ListenerClerk implements Runnable {

    final org.slf4j.Logger logger = LoggerFactory.getLogger(ListenerClerk.class);
    
    /**
     * How many bytes before the client expects meta data 32768 is used by the
     * client example
     */
    private static final int METADATA_INTERVAL = 65536;
        
    private Socket socket;
        
    private ClientSpec clientSpec;
    
//    protected final SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm:ss a");

    private DoneCallback doneCallback;
    
    private Request request;
    
    private StreamProvider streamProvider;
    
    public ListenerClerk(StreamProviderResolver streamProviderResolver, Socket clientSocket) throws IOException, IllegalRequestException {
        this(streamProviderResolver, clientSocket, null);
    }
    
    public ListenerClerk(StreamProviderResolver streamProviderResolver, Socket clientSocket, DoneCallback doneCallback) throws IOException, IllegalRequestException {
        this.socket = clientSocket;
        this.doneCallback = doneCallback;
        String ip = socket.getInetAddress().toString();
        int port = socket.getPort();
        try {
            request = readRequest(socket.getInputStream());
        } catch (IllegalRequestException ex) {
            writeNegativeResponse(socket.getOutputStream());
            logger.error("client " + ip + ":" + port + " IllegalRequestException closing socket", ex);
            socket.close();
            throw ex;
        }
        streamProvider = streamProviderResolver.findStreamProvider(request);
        if (streamProvider == null) {
            writeNegativeResponse(socket.getOutputStream());
            logger.error("client " + ip + ":" + port + " the mountpoint : [" + request.getPath() + "] does not exist or is stopped");
            socket.close();
            throw new IllegalRequestException("mountpoint " + request.getPath() + " does not exist or is stopped");
        }
        clientSpec = new ClientSpec(ip, port, streamProvider.getMountPoint());
    }

    @Override
    public void run() {
        logger.info("client " + clientSpec.ipPlusPort() + " has started");
        try {
            OutputStream out = socket.getOutputStream();
            //writes the suitable http message on out
            sendStartStreamResponse(streamProvider.getStreamName(), request, out);
            //stars reading the frames
            TimedFrame timedFrame;
            long time = System.currentTimeMillis();
            int bytesSent = 0;
            //stars reading entry by entry, every entry has a frame
            while (true) {
                try {
                    timedFrame = streamProvider.findTimedFrame(time);
                    time = timedFrame.getStopTime();
                    // grab the next frame to send
                    Frame frame = timedFrame.getFrame();
//                    logger.debug("processing frame : " + frame + " time :" + dateFormat.format(new Date(time)) );
                    // Check if we have to send metadata
                    //send possible the first part of the frame then metadata then the rest of the frame
                    if (bytesSent + frame.getSize() >= METADATA_INTERVAL) {
                        int sendBefore = METADATA_INTERVAL - bytesSent;
                        if (sendBefore > 0) {
                            out.write(frame.getData(), 0, sendBefore);
                        }
                        byte[] metadata = streamProvider.getMetadata();
                        out.write(metadata);
                        out.write(frame.getData(), sendBefore, frame.getSize() - sendBefore);
                        bytesSent -= METADATA_INTERVAL;
                    } else {
                        // we don't need to send any meta data
                        out.write(frame.getData(), 0, frame.getSize());
                    }
                    bytesSent += frame.getSize();
                } catch (EmptyFrameStorageException ex) {
                    try {
                        if(!streamProvider.isRunning()){
                            logger.info("stream provider : " + streamProvider.getMountPoint() + " is not loner running");
                            cleanup();
                            return;
                        }
                        logger.debug("client " + clientSpec.ipPlusPort() + " sleep for a while and let the StreamProvider fill the storage", ex.getMessage());
                        Thread.sleep(250);
                    } catch (InterruptedException ex1) {
                        logger.info("client " + clientSpec.ipPlusPort() + " has been interrupted");
                        cleanup();
                        return;
                    }
                } catch (OldFrameException ex) {
                    logger.debug("client " + clientSpec.ipPlusPort() + " is lagging behind. Reset the time to the current system time", ex.getMessage());
                    time = System.currentTimeMillis();
                } catch (NoLoadedFrameException ex) {
                    try {
                        logger.trace("client " + clientSpec.ipPlusPort() + " is too far ahead it should take a nap 250 ms " + Thread.interrupted(), ex.getMessage());
                        Thread.sleep(250);
                    } catch (InterruptedException ex1) {
                        logger.info("client " + clientSpec.ipPlusPort() + " has been interrupted");
                        cleanup();
                        return;
                    }
                }
                if (Thread.interrupted()) {
                    logger.info("client " + clientSpec.ipPlusPort() + " has been interrupted");
                }
            }//end while
        }catch(java.net.SocketException ex){
            logger.info("client " + clientSpec.ipPlusPort() + " has closed the connection", ex);
            cleanup();
        }
        catch (IOException ex) {
            logger.error("client " + clientSpec.ipPlusPort() + " everything is lost", ex);
            cleanup();
        }
    }

    private void cleanup(){
        logger.info("client " + clientSpec.ipPlusPort() + " cleanning up");
        try {
            socket.close();
        } catch (IOException ex1) {
            logger.error("client " + clientSpec.ipPlusPort() + " exception while closing the socket", ex1.getMessage());
        }finally{
            if(doneCallback!=null){
                doneCallback.done(this);
            }
        }
    }
    
    protected void writeNegativeResponse(OutputStream out) throws IOException {
        out.write("404 Not found".getBytes());
    }

    /**
     * Every name-value pair is separated by CRLF
     *
     * @param name
     * @param out
     * @throws IOException
     */
    protected void sendStartStreamResponse(String name, Request request, OutputStream out) throws IOException {
        StringBuilder response = new StringBuilder();
        response.append("ICY 200 OK\r\n");
        // add the stream name
        response.append("icy-notice1:<BR>This stream requires <a href=\"http://www.winamp.com/\">Winamp</a><BR>\r\n");
        response.append("icy-notice2:SHOUTcast Distributed Network Audio Server/win32 v1.8.2<BR>\r\n");
        response.append("icy-name:").append(name).append("\r\n");
        response.append("icy-genre:Grunge\r\n");
        response.append("icy-url:http://overfitti.ng-dis.co/\r\n");
        response.append("content-type:audio/mpeg\r\n");
        // add metadata information
        response.append("icy-pub:1\r\n");
        response.append("icy-metaint:").append(METADATA_INTERVAL).append("\r\n");
        response.append("icy-br:128\r\n");
        response.append("\r\n");
        out.write(response.toString().getBytes());
    }

    /**
     *
     * @param in
     * @return
     * @throws IOException
     */
    private static Request readRequest(InputStream in) throws IOException, IllegalRequestException {
        StringBuilder sb = new StringBuilder();
        byte[] buffer = new byte[4096];
        int bytes;
        byte endSequence[] = new byte[]{13, 10, 13, 10};
        while ((bytes = in.read(buffer)) > 0) {
            sb.append(new String(buffer, 0, bytes));
            if (bytes > endSequence.length) {
                boolean foundSequence = true;
                for (int i = 0; i < endSequence.length; i++) {
                    foundSequence |= (endSequence[i] == buffer[bytes - i - 1]);
                }
                if (foundSequence) {
                    break;
                }
            }
        }
        return new Request(sb.toString());
    }

    public ClientSpec getClientSpec() {
        return clientSpec;
    }
    
    public interface DoneCallback{
        public void done(ListenerClerk shoutRunnableDone);
    }

}
