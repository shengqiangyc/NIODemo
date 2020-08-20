package moreReactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @author shengqiang
 * @date 2020-08-18 11:12
 */
public class ClientDemo {

    private SocketChannel socketChannel;

    private Selector selector;

    public void initClient(String host,int port) throws IOException {
        InetSocketAddress address = new InetSocketAddress(host,port);
        this.socketChannel = SocketChannel.open(address);
        socketChannel.configureBlocking(false);
        this.selector = Selector.open();
        socketChannel.register(this.selector, SelectionKey.OP_READ);
        System.out.println("客户端请求连接") ;
    }

    public void listen() throws IOException {
        // 轮询访问selector
        while (true) {
            int n = selector.select();
            if (n == 0) {
                continue;
            }
            // 获得selector中选中的项的迭代器
            Iterator ite = this.selector.selectedKeys().iterator();
            while (ite.hasNext()) {
                SelectionKey key = (SelectionKey) ite.next();
                // 删除已选的key,以防重复处理
                ite.remove();
                if (key.isReadable()) {
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(100);
                    socketChannel.read(buffer);
                    buffer.flip();
                    System.out.println("收到服务端回复 : " + new String(buffer.array()).trim());
                    String msg = "好的，我已经知道你收到我的消息了";
                    ByteBuffer outBuffer = ByteBuffer.wrap(msg.getBytes());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    socketChannel.write(outBuffer);
                    System.out.println("回复服务端：" + msg);
                }

            }

        }
    }

    public void sendMsg(String word) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(word.getBytes());
        System.out.println("发送消息'" + word);
        try {
            socketChannel.write(byteBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        final ClientDemo clientDemo = new ClientDemo();
        clientDemo.initClient("127.0.0.1", 7777);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        clientDemo.listen();
        while (true) {
            clientDemo.sendMsg("hello mhc");
        }
    }
}
