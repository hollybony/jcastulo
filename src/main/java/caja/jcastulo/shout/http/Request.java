package caja.jcastulo.shout.http;

import caja.jcastulo.shout.IllegalRequestException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author Carlos
 */
public class Request {

    private static final Pattern getRegexp = Pattern.compile("get\\s+([^\\s]+).*", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

    public enum Type {
        HTTP
    }
    
    private final String rawRequest;
    
    private String path;
    
    private Type type;

    public Request(String request) throws IllegalRequestException {
        this.rawRequest = request.trim();
        processRequest();
    }

    public final void processRequest() throws IllegalRequestException {
        Matcher matcher = getRegexp.matcher(rawRequest);
        if (matcher.matches()) {
            path = matcher.group(1);
            type = Type.HTTP;
        }else{
            throw new IllegalRequestException("Illegal request [" + rawRequest + "]");
        }
    }

    public String getRawRequest() {
        return rawRequest;
    }

    public String getPath() {
        return path;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return rawRequest;
    }
}
