package thread;

class MyRunnable implements Runnable{
    @Override
    public void run() {
        while(true){
            System.out.println("hello , thread");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}


public class Demo2 {
    public static void main(String[] args) throws InterruptedException {
        MyRunnable r = new MyRunnable();
        Thread t = new Thread(r);
        t.start();
        while(true){
            System.out.println("hello , main");
            Thread.sleep(1000);
        }
    }
}
