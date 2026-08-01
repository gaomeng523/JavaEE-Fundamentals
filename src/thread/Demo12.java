package thread;

public class Demo12 {
    public static void main(String[] args) {
        Thread t = new Thread(){
            public void run() {
                System.out.println(this.getName());
            }
        };
        t.start();
    }
}
