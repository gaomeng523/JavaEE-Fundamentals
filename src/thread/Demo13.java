package thread;

public class Demo13 {
    public static void main(String[] args) {
        Thread t = new Thread(() -> {
            while(true) {
                System.out.println("hello thread");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        System.out.println(t.getState());
        t.start();
    }
}
