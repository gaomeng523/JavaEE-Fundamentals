package thread;

import java.util.concurrent.*;

public class Demo27 {
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(100);
        BlockingQueue<String> blockingQueue1 = new LinkedBlockingQueue<>(100);
        BlockingQueue<String> blockingQueue2 = new PriorityBlockingQueue<>(100);
        // 入队列
        blockingQueue.put("aaa");
        String elem = blockingQueue.take();
    }
}
