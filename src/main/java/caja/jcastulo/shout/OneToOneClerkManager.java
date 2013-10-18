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
 * <code>ListenerClerkManager</code> implementation that uses one to one
 * strategy which means there will be a dedicated thread for every listener
 *
 * @author Carlos Juarez
 */
public class OneToOneClerkManager implements ListenerClerkManager {

    /**
     * The logger
     */
    final org.slf4j.Logger logger = LoggerFactory.getLogger(OneToOneClerkManager.class);
    /**
     * The streamProviderResolver
     */
    protected StreamProviderResolver streamProviderResolver;
    /**
     * The task holders to keep track of all the tasks
     */
    private List<TaskHolder> taskHolders;
    /**
     * The doneCallback to remove the taskHolder and notify listeners
     */
    private ListenerClerk.DoneCallback doneCallback;
    /**
     * The executorService
     */
    private ExecutorService executorService;
    /**
     * The listeners that will be notified when updates come
     */
    private List<ListenerUpdatesListener> listeners;
    /**
     * The number of listener that can be at the same time
     */
    private int numConcurrentThreads = 5;

    /**
     * Constructs an instance of
     * <code>OneToOneClerkManager</code> class
     */
    public OneToOneClerkManager() {
        doneCallback = new ListenerClerk.DoneCallback() {
            @Override
            public void done(ListenerClerk listenerClerk) {
                ListIterator<TaskHolder> iterator = taskHolders.listIterator();
                while (iterator.hasNext()) {
                    TaskHolder next = iterator.next();
                    if (next.clientSpec.equals(listenerClerk.getClientSpec())) {
                        taskHolders.remove(next);
                        for (ListenerUpdatesListener listener : listeners) {
                            listener.listenerHasGone(listenerClerk.getClientSpec());
                        }
                    }
                }
            }
        };
        listeners = new ArrayList<ListenerUpdatesListener>();
        initExecutorService();
    }
    
    private void initExecutorService(){
        taskHolders = Collections.synchronizedList(new ArrayList<TaskHolder>());
        executorService = java.util.concurrent.Executors.newFixedThreadPool(numConcurrentThreads);
    }

    /**
     * A new ListenerClerk is created and send to the thread pool
     *
     * @param clientSocket
     */
    @Override
    public void attendListener(Socket clientSocket) {
        ListenerClerk clerk;
        try {
            clerk = new ListenerClerk(streamProviderResolver, clientSocket, doneCallback);
            FutureTask<?> futureTask = new FutureTask<Object>(clerk, null);
            taskHolders.add(new TaskHolder(futureTask, clerk.getClientSpec()));
            logger.debug("asking executor service to execute the new listener clerk");
            executorService.execute(futureTask);
            for (ListenerUpdatesListener listener : listeners) {
                listener.listenerHasArrived(clerk.getClientSpec());
            }
        } catch (IOException ex) {
            cleanup(clientSocket);
        } catch (IllegalRequestException ex) {
            cleanup(clientSocket);
        } catch (Exception ex) {
            logger.error("Exception when client was been attended ", ex);
            cleanup(clientSocket);
        }
    }

    private void cleanup(Socket clientSocket) {
        if (clientSocket != null && !clientSocket.isClosed()) {
            try {
                clientSocket.close();
            } catch (IOException ex1) {
            }
        }
    }

    /**
     * The executor service is shutdown
     *
     * @return
     * @throws InterruptedException
     */
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
                initExecutorService();
                return true;
            }
        }
        return false;
    }

    /**
     * The future task of the given clientSpec is canceled
     *
     * @param clientSpec
     */
    @Override
    public void shutdownListener(ClientSpec clientSpec) {
        ListIterator<TaskHolder> iterator = taskHolders.listIterator();
        while (iterator.hasNext()) {
            TaskHolder taskHolder = iterator.next();
            if (taskHolder.clientSpec.equals(clientSpec)) {
                taskHolder.futureTask.cancel(true);
            }
        }
    }

    /**
     * @return the streamProviderResolver
     */
    public StreamProviderResolver getStreamProviderResolver() {
        return streamProviderResolver;
    }

    /**
     * @param streamProviderResolver - the streamProviderResolver to set
     */
    public void setStreamProviderResolver(StreamProviderResolver streamProviderResolver) {
        this.streamProviderResolver = streamProviderResolver;
    }

    @Override
    public List<ClientSpec> getClientSpecsByMountpoint(String mountpoint) {
        List<ClientSpec> listenersFound = new ArrayList<ClientSpec>();
        for (TaskHolder taskHolder : taskHolders) {
            if (taskHolder.clientSpec.getMountpoint().equals(mountpoint)) {
                listenersFound.add(taskHolder.clientSpec);
            }
        }
        return listenersFound;
    }

    /**
     * @param listenerUpdatesListener a new listenerUpdatesListener to add
     */
    @Override
    public void addListenerUpdatesListener(ListenerUpdatesListener listenerUpdatesListener) {
        listeners.add(listenerUpdatesListener);
    }

    /**
     * @param listenerUpdatesListener - the listenerUpdatesListener to be
     * removed
     */
    @Override
    public void removeListenerUpdatesListener(ListenerUpdatesListener listenerUpdatesListener) {
        listeners.remove(listenerUpdatesListener);
    }

    /**
     * @param numConcurrentThreads - numConcurrentThreads to set
     */
    public void setNumConcurrentThreads(int numConcurrentThreads) {
        this.numConcurrentThreads = numConcurrentThreads;
    }

    /**
     * Holder of the future task for a give client
     */
    class TaskHolder {

        /**
         * The futureTask
         */
        final FutureTask<?> futureTask;
        /**
         * The clientSpec
         */
        final ClientSpec clientSpec;

        /**
         * Constructs an instance of
         * <code>TaskHolder</code> class
         *
         * @param futureTask - futureTask to set
         * @param clientSpec - clientSpec to set
         */
        TaskHolder(FutureTask<?> futureTask, ClientSpec clientSpec) {
            this.futureTask = futureTask;
            this.clientSpec = clientSpec;
        }
    }
}
