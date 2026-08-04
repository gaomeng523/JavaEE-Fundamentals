package thread;

public class Demo35 {
//    此处线程级变量，就是一个Integer
//    ThreadLocal 通常会定义成 static final 形式
//    确保tl 指向的实例是一个“单例的”
    private static final ThreadLocal<Integer> tl = new ThreadLocal<>();

    public static void main(String[] args) throws InterruptedException {
        // t1 线程就可以使用 t1 中的变量的内容。
        // 当 t1 使用 t1 的时候，就会把 t1 里包裹的 Integer 的值，往 t1 线程中
        // 复制一个副本，t1 后续的操作，都是针对自己的副本进行的。
        Thread t1 = new Thread(() -> {
            for(int i = 0 ; i < 100 ;i++) {
                tl.set(i);
            }
            System.out.println("tl: " + tl.get());
        });

        // t2 线程使用 t1 的变量，也会把副本拷贝到 t2 内部。
        // 此时 t2 操作的 t1 的值和 t1 毫不相关
        Thread t2 = new Thread(() -> {
            for(int i = 0 ; i < 1000 ;i++) {
                tl.set(i);
            }
            System.out.println("t2: " + tl.get());
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("main: " + tl.get());
    }


}
