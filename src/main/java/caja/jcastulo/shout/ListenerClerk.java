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
 * Clerk that attends a particular client socket providing frame bytes
 * 
 * @author Carlos Juarez
 */
public class ListenerClerk implements Runnable {

    /**
     * The logger
     */
    final org.slf4j.Logger logger = LoggerFactory.getLogger(ListenerClerk.class);
    
    /**
     * How many bytes before the client expects meta data 32768 is used by the
     * client example
     */
    private static final int METADATA_INTERVAL = 65536;
    
    /**
     * The client socket
     */
    private Socket socket;
    
    /**
     * The client specification
     */
    private ClientSpec clientSpec;

    /**
     * A hook that is called when closing the client connection
     */
    private DoneCallback doneCallback;
    
    /**
     * The client request
     */
    private Request request;
    
    /**
     * The stream provider
     */
    private StreamProvider streamProvider;
    
    /**
     * Constructs an instance of <code>ListenerClerk</code> class
     * 
     * @param streamProviderResolver - streamProviderResolver to set
     * @param clientSocket - clientSocket to set
     * @throws IOException
     * @throws IllegalRequestException 
     */
    public ListenerClerk(StreamProviderResolver streamProviderResolver, Socket clientSocket) throws IOException, IllegalRequestException {
        this(streamProviderResolver, clientSocket, null);
    }
    
    /**
     * Constructs an instance of <code>ListenerClerk</code> class
     * 
     * @param streamProviderResolver - streamProviderResolver to set
     * @param clientSocket - clientSocket to set
     * @param doneCallback - doneCallback to set
     * @throws IOException
     * @throws IllegalRequestException 
     */
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

    /**
     * Writes frame bytes into the client socket as well as meta data
     */
    @Override
    public void run() {
        logger.info("client " + clientSpec.ipPlusPort() + " has started with request : " + request);
        try {
            OutputStream out = socket.getOutputStream();
            //writes the suitable http message on out
            writeStartStreamResponse(streamProvider.getStreamName(), request, out);
            //stars reading the frames
            long time = System.currentTimeMillis();
            int bytesSent = 0;
            //stars reading frame by frame, every entry has a frame
            while (true) {
                try {
                    TimedFrame timedFrame = streamProvider.findTimedFrame(time);
                    time = timedFrame.getStopTime();
                    // grab the next frame to send
                    Frame frame = timedFrame.getFrame();
//                    logger.debug("processing frame : " + frame + " time :" + dateFormat.format(new Date(time)) );
                    // Check if we have to send metadata
                    //send possible the first part of the frame then metadata then the rest of the frame
                    if (request.isMetadataRequested() && (bytesSent + frame.getSize() >= METADATA_INTERVAL)) {
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
                            logger.info("stream provider : " + streamProvider.getMountPoint() + " is not longer running");
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

    /**
     * Performs cleanup such as close client socket and all stuff
     */
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
    
    /**
     * Writes error code
     * 
     * @param out - where the error code is written
     * @throws IOException 
     */
    protected void writeNegativeResponse(OutputStream out) throws IOException {
        out.write("404 Not found".getBytes());
    }

    /**
     * Writes the success initial stream response
     * 
     * Every name-value pair is separated by CRLF
     *
     * @param name which is used as the icy name
     * @param out - where the response is written
     * @throws IOException
     */
    protected void writeStartStreamResponse(String name, Request request, OutputStream out) throws IOException {
        StringBuilder response = new StringBuilder();
        if(request.isBrowserUserAgent()){
            response.append("HTTP/1.1 200 OK\r\n");
        }else{
            response.append("ICY 200 OK\r\n");
        }
        // add the stream name
        response.append("icy-notice1:<BR>This stream requires <a href=\"http://www.winamp.com/\">Winamp</a><BR>\r\n");
        response.append("icy-notice2:SHOUTcast Distributed Network Audio Server/win32 v1.8.2<BR>\r\n");
        response.append("icy-name:").append(name).append("\r\n");
        response.append("icy-genre:Grunge\r\n");
        response.append("icy-url:http://overfitti.ng-dis.co/\r\n");
        response.append("content-type:audio/mpeg\r\n");
        // add metadata information
        response.append("icy-pub:1\r\n");
        if(request.isMetadataRequested()){
            response.append("icy-metaint:").append(METADATA_INTERVAL).append("\r\n");
        }
        int bitrate = getBitrate();
        if(bitrate!=0){
            response.append("icy-br:").append(bitrate).append("\r\n");
        }
        response.append("\r\n");
        out.write(response.toString().getBytes());
    }
    
    /**
     * @return bitrate in kbps
     */
    private int getBitrate(){
        String property = System.getProperty("bitrate");
        int bitrate = 0;
        try{
            bitrate = Integer.parseInt(property)/1000;
        }catch(NumberFormatException ex){}
        finally{
            return bitrate;
        }
    }

    /**
     * Retrieves the bytes representing the header from the inputStream and creates a <code>Request</code>
     * 
     * @param in - where the request bytes are looked for
     * @return the request created
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

    /**
     * @return client specification
     */
    public ClientSpec getClientSpec() {
        return clientSpec;
    }
    
    /**
     * Call back to be called when closing client connection
     */
    public interface DoneCallback{
        public void done(ListenerClerk listenerClerk);
    }

}
