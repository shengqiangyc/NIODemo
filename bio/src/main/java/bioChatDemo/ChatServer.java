package bioChatDemo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author shengqiang
 * @date 2020-08-12 15:17
 */
public class ChatServer implements Runnable{

    private static int PORT = 7777;

    private static volatile ServerSocket serverSocket = null;


    @Override
    public void run() {
        if (serverSocket == null){
            try {
                serverSocket = new ServerSocket(PORT);
                System.out.println("服务端已启动，端口号为 ：" + PORT);
                while (true){
                    Socket socket = serverSocket.accept();
                    new Thread(new ChatServerHandler(socket)).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (serverSocket != null){
                    System.out.println("服务已关闭");
                    try {
                        serverSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    serverSocket = null;
                }
            }
        }

    }
}
