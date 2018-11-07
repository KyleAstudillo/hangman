package hangman.Networking;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import static java.lang.Thread.sleep;

public class BoringClient implements Runnable{

    private static final Logger logger = LogManager.getLogger("BoringClient");

	BufferedReader in;
	PrintWriter out;
	private String serverIP;
	private String name;
	private int port; //9003
	private World world;
	public boolean shouldRun = true;

	public BoringClient(NetworkHelper networkHelper, World world) {
		this.serverIP = networkHelper.getServerIP();
		this.name = networkHelper.getUsername();
		this.port = networkHelper.getPort();
		this.world = world;
	}

	private String getServerAddress() {
		return this.serverIP;
	}

	public String getName() {
		return this.name;
	}

	public void communicateActionOut(Action action){
        logger.info("CommunicatingActionOut");
		String message = action.getUser() + "," +
				action.getActionTag().toString() + "," +
				action.getExtra();
		out.println(message);
	}

	public void communicateActionIn(String action){
        //logger.info("CommunicateActionIn: " + action);
        Action actionIn = new Action(action);
        if(!name.equalsIgnoreCase(actionIn.getUser())){
            logger.info("CommunicateActionIn " + name + " got message from " + actionIn.getUser());
            world.addAction(actionIn);
        }
	}
	
	@SuppressWarnings("resource")
	public void run() {

		// Make connection and initialize streams
		String serverAddress = getServerAddress();
		Socket socket = new Socket();
		try {
			socket = new Socket(serverAddress, this.port);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			out = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Process all messages from server, according to the protocol.
		while (shouldRun) {
			String line;
			try {
				line = in.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				line  = "";
				e.printStackTrace();
			}
			if (line.startsWith("SUBMITNAME")) {
				System.out.println("attempting to submit name");
				out.println(getName());
			} else if (line.startsWith("NAMEACCEPTED")) {
				System.out.println("name accepted");
				System.out.println("Connected");
			} else if (line.startsWith("ACTION")) {
				communicateActionIn(line);
			}
		}
	}

	/**
	 * Runs the client as an application with a closeable frame.
	 */
	public static void main(String[] args) throws Exception {
	    NetworkHelper networkHelper = new NetworkHelper("127.0.0.1", true, "kyle", 9003);
		BoringClient client1 = new BoringClient(networkHelper, new World());
		Thread a = new Thread(client1);
        NetworkHelper networkHelper1 = new NetworkHelper("127.0.0.1", true, "ayla", 9003);
        BoringClient client2 = new BoringClient(networkHelper1, new World());
        Thread b = new Thread(client2);
        NetworkHelper networkHelper2 = new NetworkHelper("127.0.0.1", true, "justin", 9003);
        BoringClient client3 = new BoringClient(networkHelper2, new World());
        Thread c = new Thread(client3);
        a.start();
        b.start();
        c.start();
        sleep(30);
        client3.communicateActionOut(new Action(networkHelper2.getUsername(), ActionTag.SEND, "b"));
    }
}
