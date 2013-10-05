package caja.jcastulo.media;

/**
 * Exception that states a frameIterator was not found
 * 
 * @author Carlos Juarez
 */
public class NotSupportedFrameIteratorException extends RuntimeException {

    /**
     * Constructs an instance of <code>NotSupportedFrameIteratorException</code> class
     */
    public NotSupportedFrameIteratorException(Throwable ex) {
        super(ex);
    }

    /**
     * Constructs an instance of <code>NotSupportedFrameIteratorException</code> class
     * 
     * @param arg0 - message
     * @param arg1 - root exception
     */
    public NotSupportedFrameIteratorException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    /**
     * Constructs an instance of <code>NotSupportedFrameIteratorException</code> class
     * 
     * @param arg0 - message
     */
    public NotSupportedFrameIteratorException(String arg0) {
        super(arg0);
    }

}
