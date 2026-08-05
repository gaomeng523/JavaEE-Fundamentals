package thread;

import java.util.concurrent.Semaphore;

public class Demo41 {
    private static int count = 0;

    public static void main(String[] args) throws InterruptedException {
        Semaphore semaphore = new Semaphore(1);
        Thread t1 = new Thread(() -> {
            for(int i = 0 ;i < 50000 ;i++){
                try {
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                count++;
                semaphore.release();
            }
        });

        Thread t2 = new Thread(() -> {
            for(int i = 0 ;i < 50000 ;i++){
                try {
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                count++;
                semaphore.release();
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println(count);
    }
}
