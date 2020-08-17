package bioDemo;

import java.io.IOException;
import java.util.Random;

/**
 * @author shengqiang
 * @date 2020-08-12 14:58
 */
public class Test {

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        Thread thread1 = new Thread(server);
        thread1.start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        final char[] op = {'+', '-' ,'*', '/'};
        final Random random = new Random(System.currentTimeMillis());
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    String expression = random.nextInt(10) + " " + op[random.nextInt(4)] + (random.nextInt(10) + 1);
                    Client.send(expression);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }
}
