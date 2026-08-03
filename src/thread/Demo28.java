package thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Demo28 {
    // 简单测生产者消费者模型代码
    // 使用一个线程表示生产者
    // 一个线程表示消费者
    // 搞一个阻塞队列  ，生产者往阻塞队列中放一个整数，消费者进行获取

    public static void main(String[] args) {
        BlockingQueue<Long> queue = new ArrayBlockingQueue<>(1000);
        // 生产
        Thread t1 = new Thread(() -> {
            long n  = 0;
            while(true) {
                try {
                    queue.put(n);
                    System.out.println("生产了 ：" + n);
                    n++;
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        Thread t2 = new Thread(() -> {
            while(true ){
                try {
                    long n = queue.take();
                    System.out.println("消费了：" + n);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        t1.start();
        t2.start();
    }
}
