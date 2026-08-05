package thread;

import java.util.concurrent.Semaphore;

public class Demo40 {
    public static void main(String[] args) throws InterruptedException {
        Semaphore semaphore = new Semaphore(4);

        // p操作
        semaphore.acquire();
        System.out.println(" 进行p操作");
        semaphore.acquire();
        System.out.println(" 进行p操作");
        semaphore.acquire();
        System.out.println(" 进行p操作");
        semaphore.acquire();
        System.out.println(" 进行p操作");

        // v操作
        semaphore.release();
        System.out.println("进行v操作");
        semaphore.acquire();
        System.out.println(" 进行p操作");
    }
}
