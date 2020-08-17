package bioDemo;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * BIO服务端
 * @author shengqiang
 * @date 2020-08-11 17:47
 */
public class Server implements Runnable{

    private static final int SERVER_PORT = 8082;

    //单例serverSocket
    private static ServerSocket serverSocket;

    //
    public static void start() throws IOException {
        start(SERVER_PORT);
    }

    public static void start(int port) throws IOException{
        try {
            serverSocket = new ServerSocket(SERVER_PORT);
            System.out.println("服务d端已启动，端口号:" + SERVER_PORT);
            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(new ServerHandler(socket)).start();
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if (serverSocket != null){
                System.out.println("服务已关闭");
                serverSocket.close();
                serverSocket = null;
            }
        }
    }

    @Override
    public void run() {
        try {
            start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
