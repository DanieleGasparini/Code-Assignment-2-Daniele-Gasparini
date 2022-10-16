package practical;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    private ServerSocket server = null;
    private int clientCount = 0;

    class SocketHandler implements Runnable {
        private Socket socket;

        private BufferedReader in = null;
        private BufferedWriter out = null;

        private int clientId;
        private String prefix;

        public SocketHandler(Socket socket, int clientId) throws IOException {
            this.socket = socket;
            this.clientId = clientId;
            this.prefix = "Client[" + clientId + "]: ";

            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        }

        @Override
        public void run() {
            try {
                String msg = "";
                while (!msg.equals("end")) {
                    try {
                        msg = in.readLine();
                        System.out.println(prefix + msg);

                        out.write("Received the message " + msg);
                        out.newLine();
                        out.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("Closing connection with " + prefix);
                clientCount -=1;
                in.close();
                out.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    class ServerThread extends Thread{
        public void run(){
            Scanner scanner = new Scanner(System.in);
            while(true){
                String message = scanner.nextLine();
                if (message.equalsIgnoreCase("num_users")) {
                    System.out.println(clientCount);
                }
            }
        }
    }
    public Server(int port) {
        try {
            server = new ServerSocket(port);
            System.out.println("Server started");
            ServerThread s = new ServerThread();
            s.start();
            while (true) {
                Socket socket = server.accept();
                System.out.println("Client accepted");
                SocketHandler handler = new SocketHandler(socket, ++clientCount);
                new Thread(handler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: server [port]");
            System.exit(0);
        }
        Server server = new Server(Integer.parseInt(args[0]));
    }
}