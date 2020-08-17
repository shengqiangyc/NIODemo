package bioChatDemo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author shengqiang
 * @date 2020-08-12 15:17
 */
public class ChatServerHandler implements Runnable{

    private Socket socket;

    public ChatServerHandler(Socket socket) {
        this.socket = socket;
    }


    public void print() throws IOException{
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
            String content;
            while (true){
                if ((content = in.readLine()) == null) break;
                System.out.println(content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                in = null;
            }
            if (out != null){
                out.close();
                out = null;
            }if (socket != null){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                socket = null;
            }
        }
    }

    @Override
    public void run() {
        try {
            print();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
