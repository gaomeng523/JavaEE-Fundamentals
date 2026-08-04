package thread;

import java.util.PriorityQueue;

class MyTimerTask implements Comparable<MyTimerTask> {
    private Runnable runnable;
    private long time;
    public MyTimerTask(Runnable runnable, long delay) {
        this.runnable = runnable;
        this.time = System.currentTimeMillis() + delay;
    }
    public long getTime() { return time; }
    public void run() { runnable.run(); }

    @Override
    public int compareTo(MyTimerTask o) {
        return Long.compare(this.time, o.time); // 修复溢出
    }
}

class MyTimer {
    private PriorityQueue<MyTimerTask> queue = new PriorityQueue<>();
    private final Object locker = new Object();

    public MyTimer() {
        Thread t = new Thread(() -> {
            while (true) {
                synchronized (locker) {
                    // 队列空就等，直到有新任务来 notify
                    while (queue.isEmpty()) {
                        try { locker.wait(); }
                        catch (InterruptedException e) { return; }
                    }
                    MyTimerTask task = queue.peek();
                    long curTime = System.currentTimeMillis();
                    if (curTime >= task.getTime()) {
                        task.run();
                        queue.poll();
                    } else {
                        // 还没到点：睡到该执行的时刻（不再忙等）
                        try { locker.wait(task.getTime() - curTime); }
                        catch (InterruptedException e) { return; }
                    }
                    // 循环回去重新 peek（可能有更早的新任务被加入）
                }
            }
        });
        t.setDaemon(true);  // 主线程结束，定时器线程自动退出
        t.start();
    }

    public void schedule(Runnable runnable, long delay) {
        synchronized (locker) {
            queue.add(new MyTimerTask(runnable, delay));
            locker.notify(); // 唤醒可能在 wait 的线程，重新检查队首
        }
    }
}
class Demo34 {
    public static void main(String[] args) throws InterruptedException {
        MyTimer timer = new MyTimer();
        timer.schedule(() ->{
            System.out.println("hello , 3000");
        } , 3000);
        timer.schedule(() ->{
            System.out.println("hello , 2000");
        } , 2000);
        timer.schedule(() ->{
            System.out.println("hello , 1000");
        } , 1000);
        Thread.sleep(4000);
    }
}


