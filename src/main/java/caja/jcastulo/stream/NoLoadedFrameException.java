package caja.jcastulo.stream;

/**
 * Means that the frame requested has not been loaded yet
 * 
 * @author Carlos Juarez
 */
public class NoLoadedFrameException extends RuntimeException {

    private static final long serialVersionUID = 8666136563915734945L;

    /**
     * Constructs an instance of <code>NoLoadedFrameException</code> class
     */
    public NoLoadedFrameException() {
        super();
    }

    /**
     * Constructs an instance of <code>NoLoadedFrameException</code> class
     * 
     * @param arg0 - message to set
     * @param arg1 - root exception
     */
    public NoLoadedFrameException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    /**
     * Constructs an instance of <code>NoLoadedFrameException</code> class
     * 
     * @param arg0 - message to set
     */
    public NoLoadedFrameException(String arg0) {
        super(arg0);
    }

    /**
     * Constructs an instance of <code>NoLoadedFrameException</code> class
     * 
     * @param arg0 - root exception
     */
    public NoLoadedFrameException(Throwable arg0) {
        super(arg0);
    }
    
}
