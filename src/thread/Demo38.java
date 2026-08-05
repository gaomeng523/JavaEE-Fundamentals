package thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class Demo38 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        // 1. Callable：描述一个「带返回值的任务」
        //    与 Runnable 不同，call() 可以有返回值，也能抛受检异常
        //    这里用匿名内部类实现，泛型 Integer 表示返回值的类型
        Callable<Integer> callable = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                int sum = 0;
                for (int i = 1; i <= 1000; i++) {
                    sum += i;   // 计算 1~1000 的累加和
                }
                return sum;     // 把结果返回给外部
            }
        };

        // 2. FutureTask：既是 Runnable（能被 Thread 执行），又是 Future（能取结果）
        //    它内部持有 Callable，负责在子线程跑任务并把返回值保存起来
        FutureTask<Integer> futureTask = new FutureTask<>(callable);

        // 3. 把 FutureTask 交给 Thread 执行（因为 FutureTask 实现了 Runnable）
        Thread t = new Thread(futureTask);
        t.start();  // 启动子线程，开始异步计算 1~1000 的和

        // 4. get()：获取任务结果
        //    ⚠️ 这是「阻塞方法」：如果子线程还没算完，主线程会在这里卡住等待，
        //       直到子线程执行完 call() 把结果写进 futureTask 才返回
        //    ⚠️ 需要 throws/捕获两个异常：
        //        InterruptedException（等待时被中断）
        //        ExecutionException（call() 内部抛了异常，这里包一层再抛出）
        System.out.println(futureTask.get());
    }
}
