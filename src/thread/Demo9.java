package thread;

import java.util.Scanner;

public class Demo9 {
    public static void main(String[] args) {
        Thread t = new Thread(() -> {
            while(Thread.currentThread().isInterrupted()){
                System.out.println("hello thread");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        t.start();

        Scanner sc = new Scanner(System.in);
        System.out.println("请输入整数 ， 0表示让t 线程终止");
        int n = sc.nextInt();
        if(n == 0) {
            // 这个方法不光可以设置标志位，还能唤醒sleep 等导致线程阻塞的方法
            // 会使sleep 抛出异常
            t.interrupt();
        }
    }
}
