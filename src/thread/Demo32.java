package thread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

//固定线程数目的线程池
class MyThreadPool {
    private BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();

    public MyThreadPool(int n){
        for(int i = 0;i < n ;i++){
            Thread t = new Thread(() -> {
                try {
                    while(true) {
                        Runnable task = queue.take();
                        task.run();
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            t.setDaemon(true);
            t.start();
        }
    }

    // 往线程池中添加新的任务
    public void submit(Runnable task) throws InterruptedException {
        queue.put(task);
    }
}
public class Demo32 {
    public static void main(String[] args) {
        MyThreadPool pool = new MyThreadPool(4);
        for (int i = 0; i < 1000; i++) {
            int id = i;
            Thread cur = Thread.currentThread();
            System.out.println("hello" + cur + "," + id);
        }
    }
}
