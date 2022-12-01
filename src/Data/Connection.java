package Data;

import javafx.beans.property.SimpleStringProperty;

public class Connection {
    private String clientIP;
    private String hostName;
    private String connectionStatus;


    public Connection(String clientIP, String hostName, String connectionStatus) {
        this.clientIP = clientIP;
        this.hostName = hostName;
        this.connectionStatus = connectionStatus;
    }

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
