package thread;

public class Demo10 {
    static int result = 0;
    public static void main(String[] args) throws InterruptedException {
        // 创建一个线程， 让这个线程计算1+2 + ... + 100
        // 主线程在这个计算线程执行完毕，打印此处的结果
        Thread t = new Thread(() -> {
            for(int i = 1 ;i <= 100 ;i++)
                result += i;
            System.out.println("t 线程计算完毕");
        });

        t.start();
//        Thread.sleep(1000);
        // 可以使用join 来代替sleep
        t.join();
        // 这里的join是等待t的结果
        System.out.println(result);

    }
}
