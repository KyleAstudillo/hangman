package hangman.Networking;

public class NetworkHelper {
    private String serverIP;
    private boolean online;
    private String username;
    private int port;

    public NetworkHelper(String serverIP, boolean online, String username, int port){
        this.serverIP = serverIP;
        this.online = online;
        this.username = username;
        this.port = port;
    }

    public String getServerIP() {
        return serverIP;
    }

    public boolean getOnline() {
        return online;
    }

    public String getUsername() {
        return username;
    }

    public int getPort() {
        return port;
    }
}
