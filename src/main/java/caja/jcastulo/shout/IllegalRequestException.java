package caja.jcastulo.shout;

/**
 * 
 * @author Carlos Juarez
 */
public class IllegalRequestException extends Exception {

    private static final long serialVersionUID = -8418140559118657718L;

    public IllegalRequestException() {
        super();
    }

    public IllegalRequestException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public IllegalRequestException(String arg0) {
        super(arg0);
    }

    public IllegalRequestException(Throwable arg0) {
        super(arg0);
    }
}
