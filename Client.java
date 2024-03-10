import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try (Socket socket = new Socket("127.0.0.1", 171)) {
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter outToServer = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);

            while (true) {
                display();
                String cd = getUserInput("Your command: ");

                if (cd.equals("Q")) {
                    break;
                } else if (isValidcd(cd)) {
                    String number = getUserInput("Please enter a number: ");

                    try {
                        if (!number.isEmpty())
                            Integer.parseInt(number);

                        outToServer.println(cd + " " + number);
                        String response = inFromServer.readLine();
                        callServer(response, number);

                    } catch (NumberFormatException e) {
                        System.out.println("Invalid number. Please try again.");
                    }
                } else {
                    System.out.println("Invalid command. Please try again.");
                }
            }
        } catch (IOException e) {
            System.out.println("Server is down, please try later");
        }
    }

    private static void display() {
        System.out.println("\nPlease enter one of the following commands:");
        System.out.println("B: to convert to binary");
        System.out.println("H: to convert to hexadecimal");
        System.out.println("Q: to quit the client program");
    }

    private static String getUserInput(String prompt) {
        System.out.print(prompt);
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    private static boolean isValidcd(String command) {
        return command.equals("B") || command.equals("H") || command.isEmpty();
    }

    private static void callServer(String response, String number) {
        String[] value = response.split(" ", 2);
        int code = Integer.parseInt(value[0]);

        if (code == 200) {
            System.out.println("200 " + value[1]);
        } else {
            System.out.println(  code +" " + value[1]);
        }
    }
}
