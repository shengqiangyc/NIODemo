package bioChatDemo;

/**
 * @author shengqiang
 * @date 2020-08-12 15:55
 */
public class ChatServerTest {

    public static void main(String[] args) {
        ChatServer server = new ChatServer();
        Thread thread = new Thread(server);
        thread.start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
