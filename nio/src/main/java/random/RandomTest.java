package random;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author shengqiang
 * @date 2020-08-18 13:46
 */
public class RandomTest {

        public static void main(String[] args) throws Exception{
        List<String> names = Arrays.asList("断章","许乐","无朽","宫城","郭茂洋","妮娜");
        String result = "";
        int i = 1000;
        Random random = new Random();
        while (i > 0){
            result = names.get(random.nextInt(names.size() - 1));
            System.out.println(result);
            Thread.sleep(10);
            i --;
        }
        System.out.println("------------------------------------------");
        System.out.println("下一位幸(受)运(害)儿(者)是：" + result + " congratulations!");
    }
}
