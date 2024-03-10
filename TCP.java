import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TCP {

    public static void main(String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("server")) {
            runServer();
        } else {
            runClient();
        }
    }

    private static void runServer() {
        final int PORT_NUMBER = 12345;

        try {
            ServerSocket serverSocket = new ServerSocket(PORT_NUMBER);
            System.out.println("Server is waiting for a connection...");

            // Wait for a client to connect
            Socket clientSocket = serverSocket.accept();

            // Do something with the client connection, if needed

            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            String clientRequest;
            while ((clientRequest = in.readLine()) != null) {
                // Process client request and send response
                String response = processClientRequest(clientRequest);
                out.println(response);
            }

            // Close the sockets
            clientSocket.close();
            serverSocket.close();
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static String processClientRequest(String clientRequest) {
        if (clientRequest == null || clientRequest.trim().isEmpty()) {
            return "500\tRequest is empty";
        }

        String[] parts = clientRequest.split("\\s+");
        if (parts.length != 2) {
            return "300\tBad request\tMissing B or H";
        }

        String letter = parts[0];
        String numberStr = parts[1];

        if (!letter.equalsIgnoreCase("B") && !letter.equalsIgnoreCase("H")) {
            return "300\tBad request\tMissing B or H";
        }

        if (numberStr.isEmpty()) {
            return "400\tThe number is missing\tMissing the number";
        }

        try {
            int number = Integer.parseInt(numberStr);
            if (letter.equalsIgnoreCase("B")) {
                String binaryValue = Integer.toBinaryString(number);
                return "200\t" + binaryValue;
            } else if (letter.equalsIgnoreCase("H")) {
                String hexValue = Integer.toHexString(number).toUpperCase();
                return "200\t" + hexValue;
            } else {
                return "300\tBad request\tMissing B or H";
            }
        } catch (NumberFormatException e) {
            return "400\tThe number is missing\tMissing the number";
        }
    }

    private static void runClient() {
        // Client code remains unchanged
    }
}
