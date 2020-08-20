package moreReactor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author shengqiang
 * @date 2020-08-19 16:07
 */
public class SubReactor {

    private static final Logger logger = LoggerFactory.getLogger(SubReactor.class);


    private static final ExecutorService service =
            Executors.newFixedThreadPool(2 * Runtime.getRuntime().availableProcessors());

    private Selector selector;

    public SubReactor() throws IOException {
        this.selector = SelectorProvider.provider().openSelector();
        listen();
    }

    public void addChannel(SocketChannel socketChannel) throws ClosedChannelException {
        socketChannel.register(this.selector, SelectionKey.OP_READ);
    }

    public void wakeup() {
        this.selector.wakeup();
    }


    public void listen() {
        service.submit(() -> {
            while (true) {
                try {
                    if (selector.select(500) <= 0) {
                        continue;
                    }
                    logger.info("subReactor监听到了事件,Thread ID :" + Thread.currentThread().getName());
                    Set<SelectionKey> keys = selector.selectedKeys();
                    Iterator<SelectionKey> it = keys.iterator();
                    while (it.hasNext()) {
                        logger.info("subReactor开始工作");
                        SelectionKey key = it.next();
                        if (key.isReadable()) {
                            SocketChannel socketChannel = (SocketChannel) key.channel();
                            ByteBuffer buffer = ByteBuffer.allocate(50);
                            socketChannel.read(buffer);
                            buffer.flip();
                            System.out.println("子Reactor收到客户端消息：" + new String(buffer.array()).trim());
                        }
                    }
                    it.remove();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
