import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author shengqiang
 * @date 2020-08-18 11:12
 */
public class ClientDemo {

    private static SocketChannel socketChannel;

    public void initClient(String host,int port) throws IOException {
        InetSocketAddress address = new InetSocketAddress(host,port);
        this.socketChannel = SocketChannel.open(address);
        System.out.println("client acceptable") ;
    }

    public void sendMsg(String word){
        ByteBuffer byteBuffer = ByteBuffer.wrap(word.getBytes());
        System.out.println("send msg : '" + word);
        try {
            socketChannel.write(byteBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socketChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        ClientDemo clientDemo = new ClientDemo();
        clientDemo.initClient("127.0.0.1",7777);
        clientDemo.sendMsg("mhc good");
    }
}
