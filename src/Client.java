
import javax.sound.midi.Soundbank;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client extends Thread {
    private int port;
    private String filename;

    public Client(int port, String filename) {
        this.port = port;
        this.filename = filename;
    }

    public void run() {
        try {
            InetAddress ip = InetAddress.getByName("localhost");
            Socket socket = new Socket(ip, port);
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

                if (!dis.readUTF().equals("0")) {
                    //no connection - close socket and exit
                    dis.close();
                    dos.close();
                    socket.close();
                } else {
                    //send filename, filesize and to server
                    dos.writeUTF(filename);
                    File toSendFile = new File(filename);
                    long filesize = toSendFile.length();
                    dos.writeLong(filesize);
                    //** end send filename, size
                    FileInputStream fis = new FileInputStream(filename);
                    byte[] buffer = new byte[1024 * 10];
                    int bytesRead = 0;
                    BufferedOutputStream bos = new BufferedOutputStream(dos);
                    BufferedInputStream bis = new BufferedInputStream(fis);

                    while ((bytesRead = bis.read(buffer)) > 0) {
                        dos.write(buffer, 0, bytesRead);
                    }
                    String returnStatus = dis.readUTF();
                    if (returnStatus.equals("OK")) {
                        System.out.println("-------------------");
                        System.out.println("Client get info OK");
                    } else {
                        System.out.println("Client don't know!");
                    }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}