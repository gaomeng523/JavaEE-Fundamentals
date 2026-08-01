package thread;
class  Counter{
    public int count = 0;
     public void add(){
         synchronized(this){
             count++;
         }
    }
}
public class Demo15 {
    public static void main(String[] args) throws InterruptedException {
        Counter c = new Counter();
        Thread t1 = new Thread(() -> {
            for(int i = 0 ;i < 50000 ;i++) {
                synchronized (c){
                    c.add();
                }
            }
        });
        Thread t2 = new Thread(() -> {
            for(int i = 0 ;i < 50000 ;i++) {
                synchronized (c){
                    c.add();
                }
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(c.count);
    }
}
