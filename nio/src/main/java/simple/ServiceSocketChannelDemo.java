package simple;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author shengqiang
 * @date 2020-08-17 10:53
 */
public class ServiceSocketChannelDemo {

    //服务器地址
    private InetSocketAddress localAddress;

    public ServiceSocketChannelDemo(int port){
        this.localAddress = new InetSocketAddress(port);
    }

    private void listen() {
        ServerSocketChannel serverSocketChannel;
        Selector selector;
        try {
            //创建选择器
            selector = Selector.open();

            //创建服务器通道
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);

            //设置监听服务器的端口，设置最大连接缓冲数为100
            serverSocketChannel.bind(localAddress, 100);

            //注册到selector上，只对连接事件感兴趣
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("服务端已启动，端口为:" + localAddress.getPort());

            while (true) {
                //如果没有事件的话，这里会阻塞
                int select = selector.select();
                if (select == 0) {
                    continue;
                }
                //获取所有连接的key，通过这些key可以获取到所有的事件
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> it = selectionKeys.iterator();
                while (it.hasNext()) {
                    SelectionKey key = it.next();
                    //如果是连接事件
                    if (key.isAcceptable()) {
                        //把连接接进来
                        SocketChannel sc = serverSocketChannel.accept();
                        sc.configureBlocking(false);
                        sc.register(selector, SelectionKey.OP_READ);
                        String msg = "我收到了你的连接请求";
                        sc.write(ByteBuffer.wrap(msg.getBytes()));
                        System.out.println("客户端：" + sc.getRemoteAddress() + "连接成功");
                    }
                    //判断读事件
                    if (key.isReadable()) {
                        //处理读事件,服务端收到消息并打印
                        SocketChannel socketChannel = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(50);
                        socketChannel.read(buffer);
                        buffer.flip();
                        System.out.println("收到客户端消息：" + new String(buffer.array()).trim());
                       /* String msg = "我收到了" + new String(buffer.array()).trim();
                        ByteBuffer outBuffer = ByteBuffer.wrap(msg.getBytes());
                        socketChannel.write(outBuffer);
                        System.out.println("回复客户端：" + msg);*/
                    }
                    //移除已经处理过的事件
                    it.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        ServiceSocketChannelDemo channelDemo = new ServiceSocketChannelDemo(7777);
        channelDemo.listen();
    }
}
