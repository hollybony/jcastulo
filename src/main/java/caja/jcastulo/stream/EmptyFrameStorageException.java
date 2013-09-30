package caja.jcastulo.stream;

/**
 * 
 * @author Carlos Juarez
 */
public class EmptyFrameStorageException extends RuntimeException {

    private static final long serialVersionUID = -5393933470236337451L;

    public EmptyFrameStorageException() {
        super();
    }

    public EmptyFrameStorageException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public EmptyFrameStorageException(String arg0) {
        super(arg0);
    }

    public EmptyFrameStorageException(Throwable arg0) {
        super(arg0);
    }
}
