package common;

public class UserConnection {
    private String clientIP;
    private String hostName;
    private String connectionStatus;

    /**
     * @param clientIP
     * @param hostName
     * @param connectionStatus
     */
    public UserConnection(String clientIP, String hostName, String connectionStatus) {
        this.clientIP = clientIP;
        this.hostName = hostName;
        this.connectionStatus = connectionStatus;
    }

    /**
     * @return
     */
    public String getClientIP() {
        return clientIP;
    }

    /**
     * @param clientIP
     */
    public void setClientIP(String clientIP) {
        this.clientIP = clientIP;
    }

    /**
     * @return
     */
    public String getHostName() {
        return hostName;
    }

    /**
     * @param hostName
     */
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    /**
     * @return
     */
    public String getConnectionStatus() {
        return connectionStatus;
    }

    /**
     * @param connectionStatus
     */
    public void setConnectionStatus(String connectionStatus) {
        this.connectionStatus = connectionStatus;
    }
}
