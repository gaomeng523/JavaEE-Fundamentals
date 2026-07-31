package thread;

public class Demo6 {
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            while(true) {
                System.out.println("t1");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "t1");

        Thread t2 = new Thread(() -> {
            while(true) {
                System.out.println("t2");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        } , "t2");

        Thread t3 = new Thread(() -> {
            while(true) {
                System.out.println("t3");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        } , "t3");

        Thread t4 = new Thread(() -> {
            while(true) {
                System.out.println("t4");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        } , "t4");

        t1.start();
        t2.start();
        t3.start();
        t4.start();

        while(true) {
            System.out.println("main");
            Thread.sleep(1000);
        }

    }
}
