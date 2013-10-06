package caja.jcastulo.shout;

/**
 * To be thrown when there is an corrupt request
 * 
 * @author Carlos Juarez
 */
public class IllegalRequestException extends Exception {

    private static final long serialVersionUID = -8418140559118657718L;

    /**
     * Constructs an instance of <code>IllegalRequestException</code> class
     */
    public IllegalRequestException() {
        super();
    }

    /**
     * Constructs an instance of <code>IllegalRequestException</code> class
     * 
     * @param arg0 - message to set
     * @param arg1 - root exception
     */
    public IllegalRequestException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    /**
     * Constructs an instance of <code>IllegalRequestException</code> class
     * 
     * @param arg0 - message
     */
    public IllegalRequestException(String arg0) {
        super(arg0);
    }

    /**
     * Constructs an instance of <code>IllegalRequestException</code> class
     * 
     * @param arg0 - root exception
     */
    public IllegalRequestException(Throwable arg0) {
        super(arg0);
    }
}
