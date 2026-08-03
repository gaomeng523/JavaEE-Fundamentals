package thread;
// 自己实现一个阻塞队列
class MyBlockingQueue {
    // 通过数组表示当前阻塞队列
    private String[] data;
    // [head , tail) 表示队列中有效元素的区间
    private int head;
    private int tail;
    private int size; // 表示有效的数量
    private Object locker = new Object();

    public MyBlockingQueue(int capacity) {
        data = new String[capacity];
    }

    public void put(String elem) throws InterruptedException {
        synchronized (locker) {
            if(size == data.length) {
                locker.wait();
            }
            data[tail] = elem;
            tail++;
            if(tail >= data.length) {
                tail = 0;
            }
            size++;
            locker.notify();
        }
    }
    public String take() throws InterruptedException {
        synchronized (locker) {
            if(size == 0){
                locker.wait();
            }
            String ret = data[head];
            head++;
            if(head >= data.length){
                head = 0;
            }
            size--;
            locker.notify();
            return ret;
        }
    }

}
public class Demo29 {
    public static void main(String[] args) {
        MyBlockingQueue queue = new MyBlockingQueue(1000);
        Thread t1= new Thread(() -> {
            long n = 0;
            while(true) {
                try {
                    queue.put(n + "");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("生产：" + n);
                n++;
            }
        });
        Thread t2 = new Thread(() -> {
            while(true) {
                try {
                    String n = queue.take();
                    System.out.println("消费：" + n);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        t1.start();
        t2.start();
    }
}
