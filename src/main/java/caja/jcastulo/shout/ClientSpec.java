/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package caja.jcastulo.shout;

/**
 * Represents the info of client requesting a stream mount point
 * 
 * @author Carlos Juarez
 */
public class ClientSpec {
    
    /**
     * The client IP address
     */
    private final String ip;
    
    /**
     * The client port
     */
    private final int port;
    
    /**
     * The mount point requested
     */
    private final String mountpoint;
    
    /**
     * Constructs an instance of <code>ClientSpec</code> class
     * 
     * @param ip - ip to set
     * @param port - port to set
     */
    public ClientSpec(String ip, int port){
        this(ip,port,null);
    }
    
    /**
     * Constructs an instance of <code>ClientSpec</code> class
     * 
     * @param ip - ip to set
     * @param port - port to set
     * @param mountpoint - mountpoint to set
     */
    public ClientSpec(String ip, int port, String mountpoint){
        this.ip = ip;
        this.port = port;
        this.mountpoint = mountpoint;
    }

    /**
     * @return the ip address
     */
    public String getIp() {
        return ip;
    }

    /**
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * @return the mountpoint
     */
    public String getMountpoint() {
        return mountpoint;
    }
    
    /**
     * @return Ip and port in the format 0:0:0:0:0:0:0:1:50618
     */
    public String ipPlusPort(){
        return ip + ":" + port;
    }

    @Override
    public String toString() {
        return "ClientSpec{" + "ip=" + ip + ", port=" + port + ", mountpoint=" + mountpoint + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 31 * hash + (this.mountpoint != null ? this.mountpoint.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ClientSpec other = (ClientSpec) obj;
        if ((this.ip == null) ? (other.ip != null) : !this.ip.equals(other.ip)) {
            return false;
        }
        if (this.port != other.port) {
            return false;
        }
        return true;
    }
    
}
