/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package caja.jcastulo.shout;

/**
 *
 * @author Carlos Juarez
 */
public class ClientSpec {
    
    private final String ip;
    
    private final int port;
    
    private final String mountpoint;
    
    public ClientSpec(String ip, int port){
        this(ip,port,null);
    }
    
    public ClientSpec(String ip, int port, String mountpoint){
        this.ip = ip;
        this.port = port;
        this.mountpoint = mountpoint;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public String getMountpoint() {
        return mountpoint;
    }
    
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
