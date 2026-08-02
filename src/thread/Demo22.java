package thread;

import java.util.Scanner;

public class Demo22 {
    public static void main(String[] args) throws InterruptedException {
        Object locker = new Object();

        Thread t1 = new Thread(() -> {
            synchronized(locker) {
                System.out.println("t1 wait 之前");
                try {
                    locker.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("t1 wait 之后");
            }
        });

        Thread t2 = new Thread(() -> {
            synchronized(locker) {
                System.out.println("t2 notify 之前");
                Scanner sc = new Scanner(System.in);
                System.out.println("请输入任意内容，触发notify");
                sc.next();

                locker.notify();
                System.out.println("t2 notify 之后");
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }
}
