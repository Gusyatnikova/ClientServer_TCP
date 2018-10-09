import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        if(args.length < 2) {
            System.out.println("Check count of arguments!");
            System.exit(1);
        }
            int serverPort = Integer.parseInt(args[0]);
            String fileToSend = args[1];
            Server server = new Server(serverPort);
            server.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Client client1 = new Client(serverPort, fileToSend);
            client1.start();
    }
}