package caja.jcastulo.shout.http;

import caja.jcastulo.shout.IllegalRequestException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a client request
 * 
 * @author Carlos Juarez
 */
public class Request {

    /**
     * Regex to find the mount point in the request
     */
    private static final Pattern getRegexp = Pattern.compile("get\\s+([^\\s]+).*", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

    /**
     * Types of requests
     */
    public enum Type {
        HTTP
    }
    
    /**
     * The raw string request
     */
    private final String rawRequest;
    
    /**
     * Path that is used as the mount point
     */
    private String path;
    
    /**
     * Type of request
     */
    private Type type;

    /**
     * Constructs an instance of <code>Request</code> class
     * 
     * @param request - the raw request
     * @throws IllegalRequestException 
     */
    public Request(String request) throws IllegalRequestException {
        this.rawRequest = request.trim();
        processRequest();
    }

    /**
     * Parses and retrieve some properties from the raw request
     * 
     * @throws IllegalRequestException 
     */
    private void processRequest() throws IllegalRequestException {
        Matcher matcher = getRegexp.matcher(rawRequest);
        if (matcher.matches()) {
            path = matcher.group(1);
            type = Type.HTTP;
        }else{
            throw new IllegalRequestException("Illegal request [" + rawRequest + "]");
        }
    }

    /**
     * @return the raw request
     */
    public String getRawRequest() {
        return rawRequest;
    }

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @return the type
     */
    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return rawRequest;
    }
}
