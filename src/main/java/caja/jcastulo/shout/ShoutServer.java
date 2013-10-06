package caja.jcastulo.shout;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Carlos Juarez
 */
public class ShoutServer implements Runnable, ServerObservable {

    final org.slf4j.Logger logger = LoggerFactory.getLogger(ShoutServer.class);
    
    private ServerSocket serverSocket;
        
    protected ListenerClerkManager shoutRunnableManager;
    
    protected int port;
    
    private Thread thread;
    
    private List<ServerListener> serverListeners;

    public ShoutServer(ListenerClerkManager shoutRunnableManager, int serverPort) {
        this.shoutRunnableManager = shoutRunnableManager;
        this.port = serverPort;
        serverListeners = new ArrayList<ServerListener>();
    }

    public synchronized void start() {
        if (!isRunning()) {
            thread = new Thread(this, "ShoutServer");
            thread.start();
        }
    }

    public synchronized boolean isRunning() {
        return thread != null && thread.isAlive();
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            logger.info("Server started at port : " + port);
        } catch (IOException ex) {
            logger.error("IO Exception", ex);
            logger.info("server could not started due to IO exception");
            return;
        }
        for(ServerListener sl : serverListeners){
            sl.notifyNewServerStatus(ServerStatus.RUNNING);
        }
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                shoutRunnableManager.attendListener(clientSocket);
            } catch (SocketException ex) {
                if (Thread.interrupted()) {
                    cleanup();
                    return;
                } else {
                    try {
                        logger.error("SocketException go to sleep for a minute and try again", ex);
                        Thread.sleep(1000);
                    } catch (InterruptedException ex1) {
                        cleanup();
                        return;
                    }
                }
            } catch (IOException ex) {
                logger.error("IO Exception go to sleep for a minute and try again", ex);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex1) {
                    cleanup();
                    return;
                }
            }
        }
    }

    public synchronized void shutdown() {
        if (isRunning()) {
            logger.info("interrupting server thread...");
            thread.interrupt();
            try {
                serverSocket.close();
                try {
                    int attempt = 1;
                    while(attempt++<=3&&thread.isAlive()){
                        Thread.sleep(333);
                    }
                    if(!thread.isAlive()){
                        if(!shoutRunnableManager.shutdown()){
                            logger.warn("execution service could not be terminated");
                        }
                        logger.info("server has been stopped");
                        for(ServerListener serverListener : serverListeners){
                            serverListener.notifyNewServerStatus(ServerStatus.STOPPED);
                        }
                        thread = null;
                    }else{
                        throw new RuntimeException("server thread could not be terminated");
                    }
                } catch (InterruptedException ex) {
                    throw new RuntimeException("exception while shutting down the server", ex);
                }
            } catch (IOException ex) {
                throw new RuntimeException("exception while shutting down the server", ex);
            }
        } else {
            logger.debug("server cannot stop cause is not running");
        }
    }

    private void cleanup() {
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (Exception ex) {
                logger.error("error while trying to close the socket", ex);
            }
        }
    }
    
    /**
     * The port is will take into account next time the server starts
     *
     * @param port
     */
    public void setPort(final int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    @Override
    public void addServerListener(ServerListener serverListener) {
        serverListeners.add(serverListener);
    }

    @Override
    public void removeServerListener(ServerListener serverListener) {
        serverListeners.remove(serverListener);
    }

}
