package bioChatDemo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author shengqiang
 * @date 2020-08-12 15:19
 */
public class ChatClient{

    private static int PORT = 7777;

    private static String ADDRESS = "127.0.0.1";

    private String name;

    private static Socket socket;

    private static PrintWriter out;

    private static BufferedReader in;

    public ChatClient(String name) {
        this.name = name;
    }

    public void online(){
        try {
            socket = new Socket(ADDRESS,PORT);
            out = new PrintWriter(socket.getOutputStream(),true);
            out.println(name + "已上线");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(String content) throws Exception{
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out.println(name + "：" + content);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
