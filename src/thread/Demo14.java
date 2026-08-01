package thread;

public class Demo14 {
    private static int count = 0;
    private static Object locker = new Object();
    public static void main(String[] args) throws InterruptedException {
        // 创建两个线程 ， 分别对同一个变量进行 5w 次的 ++ 操作
        // 最终在主线程打印结果

        Thread t1 = new Thread(() -> {
            for(int i = 0 ;i < 50000 ;i++){
                synchronized (locker) {
                    count++;
                }
            }
        });

        Thread t2 = new Thread(() -> {
            for(int i = 0 ;i < 50000 ;i++){
                synchronized (locker) {
                    count++;
                }
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("count = " + count);
    }
}
