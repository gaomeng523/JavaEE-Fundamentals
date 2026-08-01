package thread;

public class Demo16 {
    private static int count = 0;

    private static void add() {
        synchronized (Demo16.class){
            count++;
        }
    }
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            for(int i = 1 ;i <= 50000 ;i++)
                add();
        });

        Thread t2 = new Thread(() -> {
            for(int i = 1 ;i <= 50000 ;i++)
                add();
        });


        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(count);
    }
}
