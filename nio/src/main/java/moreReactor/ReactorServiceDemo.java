package moreReactor;

import simple.ServiceSocketChannelDemo;

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
 * @date 2020-08-19 16:01
 */
public class ReactorServiceDemo {

    //服务器地址
    private InetSocketAddress localAddress;

    public ReactorServiceDemo(int port){
        this.localAddress = new InetSocketAddress(port);
    }

    private void listen() throws IOException{
        ServerSocketChannel serverSocketChannel;
        Selector selector;
        int coreNum = Runtime.getRuntime().availableProcessors();
        SubReactor[] subReactors = new SubReactor[2 * coreNum];
        for (int i = 0; i < subReactors.length; i++){
            subReactors[i] = new SubReactor();
        }
        int index = 0;
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
                int select = selector.select(500);
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
                        String msg = "我收到了你的连接请求";
                        sc.write(ByteBuffer.wrap(msg.getBytes()));
                        System.out.println("客户端：" + sc.getRemoteAddress() + "连接成功,index = " + index);
                        //将其他事件交给子Reactor去处理
                        SubReactor subReactor = subReactors[(index % coreNum)];
                        subReactor.addChannel(sc);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        subReactor.wakeup();
                    }
                    //移除已经处理过的事件
                    it.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException{
        ReactorServiceDemo channelDemo = new ReactorServiceDemo(7777);
        channelDemo.listen();
    }
}
