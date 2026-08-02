package thread;

import java.util.Scanner;

public class Demo19 {
    private static  int flag = 0;

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            while(flag == 0) {
                // 循环什么都不做
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("t1 结束");
        });
        Thread t2 = new Thread(() -> {
            Scanner sc = new Scanner(System.in);
            System.out.println("请输入 flag 的值");
            flag = sc.nextInt();
            System.out.println("t2 结束");
        });

        t1.start();
        t2.start();
    }
}
