package caja.jcastulo.stream;

/**
 * 
 * @author Carlos Juarez
 */
public class NoLoadedFrameException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 8666136563915734945L;

    public NoLoadedFrameException() {
        super();
    }

    public NoLoadedFrameException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public NoLoadedFrameException(String arg0) {
        super(arg0);
    }

    public NoLoadedFrameException(Throwable arg0) {
        super(arg0);
    }
    
}
