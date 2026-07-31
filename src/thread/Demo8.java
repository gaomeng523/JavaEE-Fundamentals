package thread;

import java.util.Scanner;

public class Demo8 {
    private static boolean running = true;

    public static void main(String[] args) {
//        boolean running = true;
        Thread t = new Thread(() -> {
            while(running) {
                System.out.println("hello thread");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("线程退出");
        });

        t.start();
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入整数 ， 0表示让t 线程终止");
        int n = sc.nextInt();
        if(n == 0){
            running = false;
        }
    }
}
