package bioChatDemo;

import java.util.Scanner;

/**
 * @author shengqiang
 * @date 2020-08-12 16:13
 */
public class ChatClientTest {

    public static void main(String[] args) {
        ChatClient client1 = new ChatClient("张三");
        client1.online();
        while (true) {
            System.out.println("请输入你要说的话:");
            Scanner scanner = new Scanner(System.in);
            String content = scanner.nextLine();
            try {
                client1.send(content);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
