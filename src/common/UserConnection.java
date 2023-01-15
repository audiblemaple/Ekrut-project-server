package common;

/**
 * The UserConnection class represents a connection made by a user to the server. It holds information 
 * about the connection such as the client's IP address, host name, and connection status.\
 * @author Lior
 */
public class UserConnection {
    private String clientIP;
    private String hostName;
    private String connectionStatus;

    /**
     * Constructor for the UserConnection class that takes in the client IP, host name, and connection status as arguments.
     * @param clientIP The IP address of the client that made the connection.
     * @param hostName The host name of the client that made the connection.
     * @param connectionStatus The status of the connection (e.g. connected, disconnected).
     */
    public UserConnection(String clientIP, String hostName, String connectionStatus) {
        this.clientIP = clientIP;
        this.hostName = hostName;
        this.connectionStatus = connectionStatus;
    }

    /**
     * A set of getter and setter methods for each variable of the UserConnection class, 
     * allowing for the retrieval and modification of the information stored in each connection.
     */
    public String getClientIP() {
        return clientIP;
    }

    public void setClientIP(String clientIP) {
        this.clientIP = clientIP;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getConnectionStatus() {
        return connectionStatus;
    }

    public void setConnectionStatus(String connectionStatus) {
        this.connectionStatus = connectionStatus;
    }
}
