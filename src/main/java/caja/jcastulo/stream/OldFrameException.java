package caja.jcastulo.stream;

/**
 * Means that the frame requested is too old and has been removed
 * 
 * @author Carlos Juarez
 */
public class OldFrameException extends RuntimeException {

    private static final long serialVersionUID = -3486181476553301458L;

    /**
     * Constructs an instance of <code>OldFrameException</code> class
     */
    public OldFrameException() {
        super();
    }

    /**
     * Constructs an instance of <code>OldFrameException</code> class
     * 
     * @param message - message to set
     * @param cause - root exception
     */
    public OldFrameException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs an instance of <code>OldFrameException</code> class
     * 
     * @param message - message to set
     */
    public OldFrameException(String message) {
        super(message);
    }

    /**
     * Constructs an instance of <code>OldFrameException</code> class
     * 
     * @param cause - root exception
     */
    public OldFrameException(Throwable cause) {
        super(cause);
    }
}
