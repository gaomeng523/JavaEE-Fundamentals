package thread;

//先创建一个类，让这个类继承标准库中的Thread类
class MyThread extends Thread {
    // 重写父类的 run 方法
    @Override
    public void run(){
        while(true){
            System.out.println("hello , world");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
public class Demo1 {
    public static void main(String[] args) {
        // 1. 创建一个Thread 的实例
        Thread t = new MyThread();

        // 2.启动线程
        t.start();

        while(true){
            System.out.println("hello , main");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
