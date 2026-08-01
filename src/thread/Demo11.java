package thread;

public class Demo11 {
    static int result = 0;

    // 获取到指向 main 线程的引用
    static Thread mainThread = Thread.currentThread();
    public static void main(String[] args) {
        Thread t = new Thread(() -> {
            try {
                mainThread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(result);
        });

        t.start();

        for(int i = 1 ;i <= 100 ;i++)
            result += i;
    }
}
