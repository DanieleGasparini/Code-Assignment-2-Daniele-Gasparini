package practical;

import java.io.*;
import java.net.Socket;

public class Client {
    private Socket socket = null;

    private BufferedReader in = null;
    private BufferedWriter out = null;

    private BufferedReader userInput = null;

    public Client(String address, int port) {
        try {
            socket = new Socket(address, port);
            System.out.println("Client started");

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            userInput = new BufferedReader(new InputStreamReader(System.in));

            String msg = "";
            while (!msg.equals("end")) {
                msg = userInput.readLine();
                out.write(msg);
                out.newLine();
                out.flush();

                System.out.println("Server response: " + in.readLine());
            }

            userInput.close();
            out.close();
            in.close();

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        Client client = new Client("127.0.0.1", 8080);
    }
}