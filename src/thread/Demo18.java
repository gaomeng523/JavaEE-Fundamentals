package thread;

import static java.lang.Thread.sleep;

public class Demo18 {
    public static void main(String[] args) {
        Object locker1 = new Object();
        Object locker2 = new Object();

        Thread t1 = new Thread(() -> {
            synchronized (locker1) {
                System.out.println("t1 获取到 locker1");
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                synchronized (locker2) {
                    System.out.println("t1 获取到 locker2");
                }
            }
        });
        Thread t2 = new Thread(() -> {
            synchronized (locker1) {
                System.out.println("t2 获取到 locker2");
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                synchronized (locker2) {
                    System.out.println("t2 获取到 locker1");
                }
            }
        });
        t1.start();
        t2.start();
    }
}
