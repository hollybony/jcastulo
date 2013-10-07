package caja.jcastulo.stream;

/**
 * Exception to be thrown when a <code>FrameStorage</code> is found empty
 * 
 * @author Carlos Juarez
 */
public class EmptyFrameStorageException extends RuntimeException {

    private static final long serialVersionUID = -5393933470236337451L;

    /**
     * Constructs an instance of <code></code> class
     */
    public EmptyFrameStorageException() {
        super();
    }

    /**
     * Constructs an instance of <code></code> class
     * 
     * @param arg0 - message to set
     * @param arg1 - root exception
     */
    public EmptyFrameStorageException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    /**
     * Constructs an instance of <code></code> class
     * 
     * @param arg0 - message to set
     */
    public EmptyFrameStorageException(String arg0) {
        super(arg0);
    }

    /**
     * Constructs an instance of <code></code> class
     * 
     * @param arg0 - root exception
     */
    public EmptyFrameStorageException(Throwable arg0) {
        super(arg0);
    }
}
