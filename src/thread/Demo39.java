package thread;

import java.util.concurrent.locks.ReentrantLock;

public class Demo39 {
    private static int count = 0;
    public static void main(String[] args) throws InterruptedException {
        ReentrantLock locker = new ReentrantLock();
        Thread t1 = new Thread(() -> {
            for(int i = 1; i <= 5000 ;i++){
                try {
                    locker.lock();
                    count++;
                }finally {
                    locker.unlock();
                }
            }
        });

        Thread t2 = new Thread(() -> {
            for(int i = 1; i <= 5000 ;i++){
                try {
                    locker.lock();
                    count++;
                }finally {
                    locker.unlock();
                }
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println(count);
    }
}
