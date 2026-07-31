package thread;

public class Demo7 {
    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(() -> {
            for(int i = 0 ; i < 5 ;i++){
                System.out.println("hello thread");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("t 线程结束");
        });

        // 在start 之前 ， 把t设定为后台线程
        // 设置后台线程
        t.isDaemon();
        t.start();

        // 主线程调用 start 之后 就没啥东西了，main紧接着也结束了
        Thread.sleep(1000);
        System.out.println("主线程结束");
    }
}
 