package thread;

import java.util.Hashtable;
import java.util.concurrent.CountDownLatch;

public class Demo42 {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(8);
        for (int i = 0; i < 8; i++) {
            int id = i;
            Thread t = new Thread(() -> {
                System.out.println("运动员" + id +"开始");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("运动员" + id +"到达终点");
                latch.countDown();
            });
            t.start();
        }

        // 主线程中，如何判定所有运动员到达终点
        latch.await();
        System.out.println("比赛结束");

        Hashtable<String , String>  ht = new Hashtable<>();
        ht.put("1" , "1");
        ht.get("1");
    }
}
