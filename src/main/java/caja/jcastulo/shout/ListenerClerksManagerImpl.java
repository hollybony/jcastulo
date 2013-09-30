/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package caja.jcastulo.shout;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Carlos Juarez
 */
public class ListenerClerksManagerImpl implements ListenerClerksManager {

    final org.slf4j.Logger logger = LoggerFactory.getLogger(ListenerClerksManagerImpl.class);
    
    protected StreamProviderResolver streamProviderResolver;
    
    private List<TaskHolder> taskHolders;
    
    private ListenerClerk.DoneCallback doneCallback;
    
    private ExecutorService executorService;
    
    private List<ListenerUpdatesListener> listeners;
    
    private int numConcurrentThreads = 5;

    public ListenerClerksManagerImpl() {
        taskHolders = Collections.synchronizedList(new ArrayList<TaskHolder>());
        doneCallback = new ListenerClerk.DoneCallback() {
            @Override
            public void done(ListenerClerk shoutRunnable) {
                ListIterator<TaskHolder> iterator = taskHolders.listIterator();
                while (iterator.hasNext()) {
                    TaskHolder next = iterator.next();
                    if (next.clientSpec.equals(shoutRunnable.getClientSpec())) {
                        taskHolders.remove(next);
                        for(ListenerUpdatesListener listener : listeners){
                            listener.listenerHasGone(shoutRunnable.getClientSpec());
                        }
                    }
                }
            }
        };
        executorService = java.util.concurrent.Executors.newFixedThreadPool(numConcurrentThreads);
        listeners = new ArrayList<ListenerUpdatesListener>();
    }

    @Override
    public void createAndRunListenerClerk(Socket clientSocket) {
        ListenerClerk shoutRunnable;
        try {
            shoutRunnable = new ListenerClerk(streamProviderResolver, clientSocket, doneCallback);
            FutureTask<?> futureTask = new FutureTask<Object>(shoutRunnable, null);
            taskHolders.add(new TaskHolder(futureTask, shoutRunnable.getClientSpec()));
            executorService.execute(futureTask);
            for(ListenerUpdatesListener listener : listeners){
                listener.listenerHasArrived(shoutRunnable.getClientSpec());
            }
        } catch (IOException ex) {
            if (!clientSocket.isClosed()) {
                try {
                    clientSocket.close();
                } catch (IOException ex1) {
                }
            }
        } catch (IllegalRequestException ex) {
        }
    }

    @Override
    public boolean shutdown() throws InterruptedException {
        executorService.shutdown();//disable new tasks from being submitted
        executorService.shutdownNow();
        int attempt = 1;
        while (attempt <= 3) {
            if (!executorService.awaitTermination(333, TimeUnit.MILLISECONDS)) {
                logger.debug("executor service did not terminate int the " + attempt + " attempt");
                attempt++;
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public void shutdownListenerClerk(ClientSpec clientSpec) {
        ListIterator<TaskHolder> iterator = taskHolders.listIterator();
        while (iterator.hasNext()) {
            TaskHolder taskHolder = iterator.next();
            if (taskHolder.clientSpec.equals(clientSpec)) {
                taskHolder.futureTask.cancel(true);
            }
        }
    }

    public StreamProviderResolver getStreamProviderResolver() {
        return streamProviderResolver;
    }

    public void setStreamProviderResolver(StreamProviderResolver streamProviderResolver) {
        this.streamProviderResolver = streamProviderResolver;
    }

    @Override
    public List<ClientSpec> getListenerClerksByMountpoint(String mountpoint) {
        List<ClientSpec> listenersFound = new ArrayList<ClientSpec>();
        for (TaskHolder taskHolder : taskHolders) {
            if (taskHolder.clientSpec.getMountpoint().equals(mountpoint)) {
                listenersFound.add(taskHolder.clientSpec);
            }
        }
        return listenersFound;
    }

    @Override
    public void addListenerUpdatesListener(ListenerUpdatesListener listenerUpdatesListener) {
        listeners.add(listenerUpdatesListener);
    }

    @Override
    public void removeListenerUpdatesListener(ListenerUpdatesListener listenerUpdatesListener) {
        listeners.remove(listenerUpdatesListener);
    }

    public void setNumConcurrentThreads(int numConcurrentThreads) {
        this.numConcurrentThreads = numConcurrentThreads;
    }

    class TaskHolder {

        final FutureTask<?> futureTask;
        
        final ClientSpec clientSpec;

        TaskHolder(FutureTask<?> futureTask, ClientSpec clientSpec) {
            this.futureTask = futureTask;
            this.clientSpec = clientSpec;
        }
    }
}
