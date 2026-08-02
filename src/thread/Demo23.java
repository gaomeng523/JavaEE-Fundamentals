package thread;

import java.util.Scanner;

public class Demo23 {
    public static void main(String[] args) {
        Object locker = new Object();

        Thread t1 = new Thread(() -> {
            synchronized (locker) {
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
            synchronized (locker) {
                System.out.println("t2 wait 之前");
                try {
                    locker.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("t2 wait 之后");
            }
        });

        Thread t3 = new Thread(() -> {
            synchronized (locker) {
                System.out.println("t3 wait 之前");
                try {
                    locker.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("t3 wait 之后");
            }
        });
        Thread t4 = new Thread(() -> {
            synchronized (locker) {
                System.out.println("t4 wait 之前");
                try {
                    locker.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("t4 wait 之后");
            }
        });
        t1.start();
        t2.start();
        t3.start();
        t4.start();

        Thread t5 = new Thread(() -> {
            synchronized(locker) {
                System.out.println("t5 notify 之前");
                Scanner sc = new Scanner(System.in);
                System.out.println("请输入任意内容，触发notify");
                sc.next();

                locker.notifyAll();
                System.out.println("t5 notify 之后");
            }
        });
        t5.start();
    }
}
