package thread;

public class Demo37  {
    private static int result = 0;
    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                int sum = 0;
                for (int i = 1; i <= 10000 ; i++) {
                    sum += i;
                }
                result = sum;
            }
        });

        t.start();
        t.join();

        System.out.println(result);
    }
}
