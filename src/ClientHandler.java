import javax.xml.crypto.Data;
import java.io.*;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ClientHandler extends Thread {
    final DataInputStream dataInputStream;
    final DataOutputStream dataOutputStream;
    final Socket socket;
    static int bytesRead = 0;
    static long totalBytes = 0;
    static Date whileTime = new Date();
    static long timeForWhile = 0;
    static int i = 0;
    boolean success = false;



    public ClientHandler(Socket socket, DataInputStream dis, DataOutputStream dos) {
        this.socket = socket;
        this.dataInputStream = dis;
        this.dataOutputStream = dos;
    }

    @Override
    public void run() {
        String filename = null;
        long filesize = 0;
        try {
            dataOutputStream.writeUTF("0");
            filename = dataInputStream.readUTF();
            filesize = dataInputStream.readLong();
            Path filePath = Paths.get("D:\\education\\Java\\MyProtocol\\uploads");
            File recvFile = new File(filePath.toString() + "\\" + filename);
            recvFile.createNewFile();

            BufferedInputStream bis = new BufferedInputStream(dataInputStream);
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(recvFile));
            byte[] buffer = new byte[1024 * 10];
            Date startSessionTime = new Date();

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if(!success) {

                        double MB = totalBytes / 1024 / 1024;
                        double SEC = (whileTime.getTime() - startSessionTime.getTime()) / 1000;
                        double aSpeed = MB / SEC;
                        //System.out.println("total MB " + MB);
                        System.out.println("total time (SEC) " + SEC);
                        System.out.println("Average speed: " + aSpeed + " MB/SEC");
                        //сколько за раз я прочитала байт. Время одного цикла while примерно миллисекунда
                        System.out.println("Instant speed: " + (double)((bytesRead)) + " BYTE/MS");
                    }
                }
            },500, 3000);

            while ((bytesRead = bis.read(buffer)) > 0) {
                totalBytes = totalBytes + bytesRead;
                whileTime = new Date();

                bos.write(buffer, 0, bytesRead);
                if (filesize == totalBytes) {
                    success = true;
                    System.out.println("file received by Server!");
                    dataOutputStream.writeUTF("OK");
                    bis.close();
                    bos.close();
                    socket.close();
                    this.stop();
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


