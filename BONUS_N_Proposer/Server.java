import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

// This class will act as the server,which will only receive messeges from the
// proposer/acceptors and direct them back

public class Server { // Responsible for redirecting message
    public static void sendMessages(String hostName, int portNumber, String messages) {
        boolean running = true;
        while (running) {
            try {
                Socket socket = new Socket(hostName, portNumber);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                out.println(messages);
                out.println("\r");
                out.close();
                socket.close();
                running = false;
            } catch (IOException e) {
                System.out.println("Error when connecting! " + e.getMessage());
            }
        }
    }
}
