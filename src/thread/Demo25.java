package thread;


// 懒汉的单例模式
class SingletionLazy {
    private volatile static SingletionLazy instance = null;
    // 懒汉模式的关键在于， 把实例的创建时机推迟了，推迟到第一次使用的时候，创建
    private static Object locker = new Object();
    public static SingletionLazy getInstance() {
        if(instance == null) {
            synchronized (locker) {
                if(instance == null) {
                    instance = new SingletionLazy();
                }
            }
        }
        return instance;
    }
    private SingletionLazy() {
    }
}
public class Demo25 {
    public static void main(String[] args) {
        SingletionLazy s1 = SingletionLazy.getInstance();
        SingletionLazy s2 = SingletionLazy.getInstance();
        System.out.println(s1 == s2);
    }
}
