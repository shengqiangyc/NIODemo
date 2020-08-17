package bioDemo;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author shengqiang
 * @date 2020-08-12 14:02
 */
public class Client {

    private static final int SERVER_PORT = 8082;

    private static String DEFAULT_IP = "127.0.0.1";

    public static void send(String expression){
        send(SERVER_PORT,expression);
    }

    private static void send(int serverPort, String expression) {
        System.out.println("算术表达式为：" + expression);
        Socket socket = null;
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            socket = new Socket(DEFAULT_IP,serverPort);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(),true);
            out.println(expression);
            System.out.println("结果为:" + in.readLine());
        }catch (Exception e){
            e.printStackTrace();
        }finally {
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

}
