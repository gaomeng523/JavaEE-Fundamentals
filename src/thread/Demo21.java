package thread;

public class Demo21 {
    public static void main(String[] args) throws InterruptedException {
        Object obj = new Object();
        synchronized (obj) {
            System.out.println("wait 之前");
            obj.wait();
            System.out.println("wait 之后");
        }
    }
}
