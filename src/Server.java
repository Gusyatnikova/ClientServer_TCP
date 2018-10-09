import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {
    private int port;

    public Server(int port) {
        this.port = port;
    }

    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                Socket socket = null;
                socket = serverSocket.accept();
                System.out.println("new client is connected : " + socket);

                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

                System.out.println("Assigning new thread for this client");
                Thread thread = new ClientHandler(socket, dataInputStream, dataOutputStream);
                thread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}