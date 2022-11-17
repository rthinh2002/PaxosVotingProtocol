import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.stream.Collectors;

// This class will act Thread, starting Proposers and Acceptors

public class ServerThread extends Thread {
    private Member participants;
    private ServerSocket serverSocket;

    public ServerThread(Member participants) {
        this.participants = participants;
        int portNumber = Profile.getPortNumber(participants.getName());
        try {
            serverSocket = new ServerSocket(portNumber);
            System.out.println(this.participants.getName() + " running on port number: " + portNumber);
        } catch (IOException e) {
            System.out.println("Error at server thread: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                Socket socket = serverSocket.accept();
                socket.setSoTimeout(5000);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String message = in.lines().collect(Collectors.joining("\n"));
                if (message.contains("OFFLINE")) { // Check if a proposers being shut down
                    System.out.println(this.participants.getName() + " is offline, not participate in voting");
                    break;
                } else if (message.contains("PROMISE") || message.contains("NO_PROMISE") || message.contains("ACCEPTED")
                        || message.contains("REJECTED") || message.contains("DECIDED")) {
                    this.participants.receivedMessageFromAcceptor(message);
                } else if (message.contains("PREPARE") || message.contains("ACCEPT") || message.contains("VOTING")) {
                    this.participants.receivedMessageFromProposer(message);
                }
            }
        } catch (IOException e) {
            System.out.println("Error at server thread " + e.getMessage());
        }

    }

}
