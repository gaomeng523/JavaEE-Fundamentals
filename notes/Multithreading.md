# Java 多线程编程核心笔记（初阶 + 进阶）


---

## 目录

- [第一部分：多线程初阶](#第一部分多线程初阶)
  - [1. 认识线程（Thread）](#1-认识线程thread)
  - [2. 创建线程](#2-创建线程)
  - [3. Thread 类及常见方法](#3-thread-类及常见方法)
  - [4. 线程的状态](#4-线程的状态)
  - [5. 线程不安全（重点）](#5-线程不安全重点)
  - [6. synchronized 关键字](#6-synchronized-关键字)
  - [7. volatile 关键字](#7-volatile-关键字)
  - [8. wait 和 notify](#8-wait-和-notify)
  - [9. 多线程案例](#9-多线程案例)
  - [10. 保证线程安全的思路](#10-保证线程安全的思路)
  - [11. 线程与进程的对比](#11-线程与进程的对比)
- [第二部分：多线程进阶](#第二部分多线程进阶)
  - [1. 常见的锁策略](#1-常见的锁策略)
  - [2. CAS](#2-cas)
  - [3. synchronized 原理](#3-synchronized-原理)
  - [4. JUC（java.util.concurrent）常见类](#4-jucjavautilconcurrent常见类)
  - [5. 线程安全的集合类](#5-线程安全的集合类)
  - [6. 死锁](#6-死锁)
  - [7. 高频面试题汇总](#7-高频面试题汇总)
- [参考资料](#参考资料)

---

# 第一部分：多线程初阶

## 1. 认识线程（Thread）

### 1.1 概念

**1) 线程是什么**
- 线程是一个“执行流”。每个线程都可以按照顺序执行自己的代码，多个线程之间“同时”执行着多份代码。
- 类比银行场景：一家公司去办业务，要同时做财务转账、福利发放、缴社保。如果只有张三一个会计忙不过来，他叫来李四、王五分别负责一件事，三人分别排队办理——这就有了三个执行流共同完成任务，本质上都是为了办一家公司的业务。李四、王五是张三叫来的，张三一般称为**主线程（Main Thread）**。

**2) 为什么要有线程**
- **并发编程成为“刚需”**：单核 CPU 遇到瓶颈，要提高算力需要多核 CPU；并发编程能更充分利用多核 CPU 资源。
- 有些任务需要“等待 IO”，为了让等待 IO 的时间去做别的工作，也需要并发编程。
- **线程比进程更轻量**：创建、销毁、调度线程都比进程更快。
- 人们还不满足，于是又有了**线程池（ThreadPool）**和**协程（Coroutine）**。

**3) 进程和线程的区别**
| 对比项 | 进程 | 线程 |
|---|---|---|
| 包含关系 | 包含线程，至少一个主线程 | 被进程包含 |
| 内存空间 | 进程间不共享内存空间 | 同一进程内线程共享内存空间 |
| 资源分配 | 系统分配资源的最小单位 | 系统调度的最小单位 |
| 影响范围 | 一个进程挂了一般不影响其他进程 | 一个线程挂了可能带走同进程的其他线程（整个进程崩溃） |

**4) Java 线程和操作系统线程的关系**
- 线程是操作系统中的概念，内核实现了线程机制并提供 API（如 Linux 的 pthread 库）。
- Java 标准库的 `Thread` 类是对操作系统 API 的进一步抽象和封装。

---

## 2. 创建线程

### 2.1 第一个多线程程序

```java
import java.util.Random;

public class ThreadDemo {
    private static class MyThread extends Thread {
        @Override
        public void run() {
            Random random = new Random();
            while (true) {
                System.out.println(Thread.currentThread().getName());
                try {
                    Thread.sleep(random.nextInt(10));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        MyThread t1 = new MyThread();
        t1.start();
        Random random = new Random();
        while (true) {
            System.out.println(Thread.currentThread().getName());
            try {
                Thread.sleep(random.nextInt(10));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
```
- 每个线程都是独立的执行流，多个线程之间“并发”执行。
- 可用 `jconsole` 命令观察线程。

### 2.2 创建线程的 5 种方式

**方法 1：继承 Thread 类**
```java
class MyThread extends Thread {
    @Override
    public void run() {
        System.out.println("这里是线程运行的代码");
    }
}
MyThread t = new MyThread();
t.start();   // 线程开始运行
```

**方法 2：实现 Runnable 接口**
```java
class MyRunnable implements Runnable {
    @Override
    public void run() {
        System.out.println("这里是线程运行的代码");
    }
}
Thread t = new Thread(new MyRunnable());
t.start();
```
> 对比：继承 Thread 直接用 `this` 表示当前线程对象；实现 Runnable 时 `this` 表示 MyRunnable 引用，需用 `Thread.currentThread()`。

**方法 3：匿名内部类创建 Thread 子类对象**
```java
Thread t1 = new Thread() {
    @Override
    public void run() {
        System.out.println("使用匿名类创建 Thread 子类对象");
    }
};
```

**方法 4：匿名内部类创建 Runnable 子类对象**
```java
Thread t2 = new Thread(new Runnable() {
    @Override
    public void run() {
        System.out.println("使用匿名类创建 Runnable 子类对象");
    }
});
```

**方法 5：lambda 表达式（最常用、最简洁）**
```java
Thread t3 = new Thread(() -> System.out.println("lambda 创建线程"));
Thread t4 = new Thread(() -> {
    System.out.println("使用 lambda 创建线程");
});
```

### 2.3 多线程的优势——增加运行速度

```java
public class ThreadAdvantage {
    private static final long count = 10_0000_0000;

    public static void main(String[] args) throws InterruptedException {
        concurrency();   // 并发
        serial();        // 串行
    }

    private static void concurrency() throws InterruptedException {
        long begin = System.nanoTime();
        Thread thread = new Thread(() -> {
            int a = 0;
            for (long i = 0; i < count; i++) a--;
        });
        thread.start();
        int b = 0;
        for (long i = 0; i < count; i++) b--;
        thread.join();
        long end = System.nanoTime();
        System.out.printf("并发: %f 毫秒%n", (end - begin) * 1.0 / 1000 / 1000);
    }

    private static void serial() {
        long begin = System.nanoTime();
        int a = 0;
        for (long i = 0; i < count; i++) a--;
        int b = 0;
        for (long i = 0; i < count; i++) b--;
        long end = System.nanoTime();
        System.out.printf("串行: %f 毫秒%n", (end - begin) * 1.0 / 1000 / 1000);
    }
}
```
- 示例输出：并发 399ms，串行 720ms。
- **注意**：多线程不一定总能提速，取决于 `count` 与任务特性（线程创建/调度也有开销）。

---

## 3. Thread 类及常见方法

每个线程都有一个唯一的 `Thread` 对象与之关联，JVM 用它来组织、调度、管理线程。

### 3.1 常见构造方法
```java
Thread t1 = new Thread();
Thread t2 = new Thread(new MyRunnable());
Thread t3 = new Thread("这是我的名字");
Thread t4 = new Thread(new MyRunnable(), "这是我的名字");
```

### 3.2 常见属性
- **ID**：线程唯一标识，不重复。
- **名称（name）**：调试工具使用。
- **状态（state）**：线程当前所处情况。
- **优先级（priority）**：高的理论上更容易被调度。
- **后台线程（daemon）**：JVM 在所有非后台线程结束后才结束运行。
- **是否存活（isAlive）**：简单理解为 `run` 方法是否运行结束。
- **中断（isInterrupted）**：见 3.4。

```java
public class ThreadDemo {
    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    System.out.println(Thread.currentThread().getName() + ": 我还活着");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(Thread.currentThread().getName() + ": 我即将死去");
        });
        System.out.println(Thread.currentThread().getName() + ": ID: " + thread.getId());
        System.out.println(Thread.currentThread().getName() + ": 名称: " + thread.getName());
        System.out.println(Thread.currentThread().getName() + ": 状态: " + thread.getState());
        System.out.println(Thread.currentThread().getName() + ": 优先级: " + thread.getPriority());
        System.out.println(Thread.currentThread().getName() + ": 后台线程: " + thread.isDaemon());
        System.out.println(Thread.currentThread().getName() + ": 活着: " + thread.isAlive());
        System.out.println(Thread.currentThread().getName() + ": 被中断: " + thread.isInterrupted());
        thread.start();
        while (thread.isAlive()) {}
        System.out.println(Thread.currentThread().getName() + ": 状态: " + thread.getState());
    }
}
```

### 3.3 启动一个线程——start()
- 覆写 `run` 只是给线程准备“指令清单”，`start()` 才真正在操作系统的底层创建出一个线程。
- 多次 `start` 同一线程会抛 `IllegalThreadStateException`。

### 3.4 中断一个线程
两种常见方式：

**方式 1：自定义共享标记（加 volatile）**
```java
public class ThreadDemo {
    private static class MyRunnable implements Runnable {
        public volatile boolean isQuit = false;
        @Override
        public void run() {
            while (!isQuit) {
                System.out.println(Thread.currentThread().getName() + ": 别管我，我忙着转账呢!");
                try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
            }
            System.out.println(Thread.currentThread().getName() + ": 啊！险些误了大事");
        }
    }
    public static void main(String[] args) throws InterruptedException {
        MyRunnable target = new MyRunnable();
        Thread thread = new Thread(target, "李四");
        thread.start();
        Thread.sleep(10 * 1000);
        target.isQuit = true;
    }
}
```

**方式 2：使用 interrupt() 通知（推荐）**
```java
public class ThreadDemo {
    private static class MyRunnable implements Runnable {
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                System.out.println(Thread.currentThread().getName() + ": 别管我，我忙着转账呢!");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println(Thread.currentThread().getName() + ": 有内鬼，终止交易！");
                    break;   // 注意此处 break，才能真正结束
                }
            }
            System.out.println(Thread.currentThread().getName() + ": 啊！险些误了大事");
        }
    }
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new MyRunnable(), "李四");
        thread.start();
        Thread.sleep(10 * 1000);
        thread.interrupt();
    }
}
```
> `Thread.interrupted()` 与 `Thread.currentThread().isInterrupted()` 区别：前者会**清除**中断标志，后者**不清除**。

**收到通知的两种方式：**
1. 线程因 `wait/join/sleep` 阻塞挂起 → 以 `InterruptedException` 形式通知，**清除中断标志**。是否结束线程取决于 catch 中写法（忽略或 break 跳出循环）。
2. 否则，只是内部中断标志被设置，可通过 `isInterrupted()` 判断，不清除标志。这种方式即使线程正在 sleep 也能马上被感知。

### 3.5 等待一个线程——join()
```java
public class ThreadDemo {
    public static void main(String[] args) throws InterruptedException {
        Runnable target = () -> {
            for (int i = 0; i < 10; i++) {
                System.out.println(Thread.currentThread().getName() + ": 我还在工作！");
                try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
            }
            System.out.println(Thread.currentThread().getName() + ": 我结束了！");
        };
        Thread thread1 = new Thread(target, "李四");
        Thread thread2 = new Thread(target, "王五");
        thread1.start();
        thread1.join();     // 等李四结束
        thread2.start();
        thread2.join();     // 等王五结束
    }
}
```
> 把两个 join 注释掉，两个线程会并发执行，顺序不可控。

### 3.6 获取当前线程引用
```java
Thread thread = Thread.currentThread();
System.out.println(thread.getName());
```

### 3.7 休眠当前线程——sleep()
```java
System.out.println(System.currentTimeMillis());
Thread.sleep(3 * 1000);
System.out.println(System.currentTimeMillis());
```
- 因为线程调度不可控，`sleep` 实际休眠时间 **≥** 参数设置时间。

---

## 4. 线程的状态

### 4.1 观察所有状态
```java
public class ThreadState {
    public static void main(String[] args) {
        for (Thread.State state : Thread.State.values()) {
            System.out.println(state);
        }
    }
}
```
六种状态（`Thread.State` 枚举）：
- **NEW**：安排了工作，还未开始行动（已创建，未 start）。
- **RUNNABLE**：可工作的，可分为正在工作中 / 即将开始工作（已在 CPU 运行或等待运行）。
- **BLOCKED**：等待获取锁（排队）。
- **WAITING**：等待其他线程发来通知（排队）。
- **TIMED_WAITING**：带超时时限的等待（排队）。
- **TERMINATED**：工作完成了。

### 4.2 状态转移
```
        start()
  NEW ----------> RUNNABLE <---------- (获取锁/sleep结束/被notify)
                     |
         wait()/sleep()/锁竞争
                     v
              WAITING / TIMED_WAITING / BLOCKED
                     |
            (被唤醒 + 重新获取锁)
                     v
                  RUNNABLE
                     |
                run() 结束
                     v
                TERMINATED
```
- `isAlive()` ≈ 处于非 NEW 且非 TERMINATED 的状态。

### 4.3 观察状态转移示例
```java
// 观察 NEW -> RUNNABLE -> TERMINATED
Thread t = new Thread(() -> {
    for (int i = 0; i < 1000_0000; i++) {}
}, "李四");
System.out.println(t.getName() + ": " + t.getState());   // NEW
t.start();
while (t.isAlive()) {
    System.out.println(t.getName() + ": " + t.getState()); // RUNNABLE
}
System.out.println(t.getName() + ": " + t.getState());   // TERMINATED
```

```java
// 观察 BLOCKED / TIMED_WAITING / WAITING
final Object object = new Object();
Thread t1 = new Thread(() -> {
    synchronized (object) {
        while (true) {
            try { Thread.sleep(1000); } catch (InterruptedException e) {}
        }
    }
}, "t1");
Thread t2 = new Thread(() -> {
    synchronized (object) {
        System.out.println("hehe");
    }
}, "t2");
t1.start(); t2.start();
// 用 jconsole 看：t1 = TIMED_WAITING，t2 = BLOCKED
// 把 t1 中 sleep 换成 object.wait()，t1 = WAITING
```
> 结论：BLOCKED 表示等待获取锁；WAITING/TIMED_WAITING 表示等待其他线程发来通知；TIMED_WAITING 是带时限的等待。

---

## 5. 线程不安全（重点）

### 5.1 观察线程不安全
```java
private static int count = 0;
public static void main(String[] args) throws InterruptedException {
    Thread t1 = new Thread(() -> {
        for (int i = 0; i < 50000; i++) count++;
    });
    Thread t2 = new Thread(() -> {
        for (int i = 0; i < 50000; i++) count++;
    });
    t1.start(); t2.start();
    t1.join(); t2.join();
    System.out.println("count: " + count);   // 预期 100000，实际远小于
}
```

### 5.2 线程安全的定义
如果多线程环境下代码运行的结果符合预期（即单线程环境应得的结果），则称程序是**线程安全的**。

### 5.3 线程不安全的原因
1. **线程调度是随机的（罪魁祸首）**
   - 程序必须保证在任意执行顺序下都正确。
2. **修改共享数据**
   - 多个线程修改同一个变量（`count` 是共享数据）。
3. **原子性缺失**
   - `count++` 实际分三步：① 从内存读数据到 CPU；② 更新数据；③ 写回内存。
   - 一条 Java 语句不一定是原子的，也不一定只是一条指令。如果一个线程正在操作，中途被插入，结果就可能错误。
   - 这也和线程的抢占式调度密切相关。
4. **内存可见性**
   - 可见性：一个线程对共享变量值的修改，能及时被其他线程看到。
   - **Java 内存模型（JMM）**：线程共享变量存在**主内存（Main Memory）**；每个线程有自己的**工作内存（Working Memory，可理解为 CPU 寄存器/高速缓存）**。
   - 读：先把变量从主内存拷贝到工作内存再读；写：先改工作内存副本，再同步回主内存。
   - 于是线程1改了工作内存，线程2的工作内存不一定及时变化。
5. **指令重排序**
   - 单线程下 JVM/CPU 会优化执行顺序（前提：保持逻辑不变）。例如“取U盘 → 写作业 → 取快递”可优化为“取U盘 → 取快递 → 写作业”。
   - 多线程下编译器难以预测执行效果，激进重排序容易破坏逻辑等价性。

---

## 6. synchronized 关键字（监视器锁 monitor lock）

### 6.1 特性

**（1）互斥**
- 某个线程执行到某对象的 `synchronized` 时，其他线程执行到同一对象 `synchronized` 会阻塞等待。
- 进入 = 加锁，退出 = 解锁。锁信息存在 **Java 对象头**里（类似厕所“有人/无人”标识）。
- 每个锁在操作系统内部有等待队列。解锁后下一个线程不是立即获取，而是由 OS 唤醒后重新竞争，**不遵守先来后到**。
- 底层使用操作系统的 **mutex lock** 实现。

**（2）可重入**
- 同步块对同一条线程可重入，不会把自己锁死。
- 内部包含“线程持有者”和“计数器”：同一线程重复加锁计数器自增，解锁时计数器递减到 0 才真正释放锁。

```java
for (int i = 0; i < 50000; i++) {
    synchronized (locker) {
        synchronized (locker) {   // 可重入，不会死锁
            count++;
        }
    }
}
```

### 6.2 使用示例
`synchronized` 本质修改指定对象的对象头，必须搭配一个具体对象使用：

```java
// 1) 修饰代码块——锁任意对象
public class SynchronizedDemo {
    private Object locker = new Object();
    public void method() {
        synchronized (locker) { }
    }
}
// 1) 修饰代码块——锁当前对象(this)
public class SynchronizedDemo {
    public void method() {
        synchronized (this) { }
    }
}
// 2) 修饰普通方法——锁 SynchronizedDemo 对象
public class SynchronizedDemo {
    public synchronized void method() { }
}
// 3) 修饰静态方法——锁 SynchronizedDemo 类的对象(Class对象)
public class SynchronizedDemo {
    public synchronized static void method() { }
}
```
> 重点：**两个线程竞争同一把锁才产生阻塞等待；竞争不同锁，不竞争。**

### 6.3 标准库中的线程安全类
- **线程不安全**（无加锁）：ArrayList、LinkedList、HashMap、TreeMap、HashSet、TreeSet、StringBuilder。
- **线程安全**（加锁或不可变）：
  - Vector、HashTable（不推荐使用，性能差）；
  - ConcurrentHashMap、StringBuffer（方法带 synchronized）；
- **天然安全**（不涉及修改）：String。

### 6.4 用 synchronized 解决 5.1 的不安全
```java
private static int count = 0;
public static void main(String[] args) throws InterruptedException {
    Object locker = new Object();
    Thread t1 = new Thread(() -> {
        for (int i = 0; i < 50000; i++) {
            synchronized (locker) { count++; }
        }
    });
    Thread t2 = new Thread(() -> {
        for (int i = 0; i < 50000; i++) {
            synchronized (locker) { count++; }
        }
    });
    t1.start(); t2.start();
    t1.join(); t2.join();
    System.out.println("count: " + count);   // 100000
}
```

---

## 7. volatile 关键字

### 7.1 保证内存可见性
- `volatile` 修饰的变量，写入时：改工作内存副本 → 刷新到主内存；读取时：从主内存读最新值 → 读工作内存副本。
- 强制读写内存，速度慢一点但数据更准确。

```java
static class Counter {
    public volatile int flag = 0;
}
public static void main(String[] args) {
    Counter counter = new Counter();
    Thread t1 = new Thread(() -> {
        while (counter.flag == 0) { /* do nothing */ }
        System.out.println("循环结束!");
    });
    Thread t2 = new Thread(() -> {
        Scanner scanner = new Scanner(System.in);
        counter.flag = scanner.nextInt();
    });
    t1.start(); t2.start();
}
// 若 flag 不加 volatile，t2 修改后 t1 可能感知不到，循环不结束（bug）
// 加上 volatile 后，输入非 0 值 t1 立即结束
```

### 7.2 不保证原子性
```java
static class Counter {
    volatile public int count = 0;
    void increase() { count++; }
}
// 两个线程各自增 5w 次，最终 count 仍无法保证是 100000
```
> `volatile` 保证内存可见性，`synchronized` 保证原子性，二者有本质区别。

---

## 8. wait 和 notify

- 线程抢占式执行，顺序难预知；有时需要协调多个线程的先后次序。
- 三个方法：`wait()` / `wait(long timeout)`（进入等待）、`notify()` / `notifyAll()`（唤醒等待线程）。
- 注意：`wait`、`notify`、`notifyAll` 都是 **Object 类**的方法。

### 8.1 wait() 方法
- 做的事：使当前线程等待（放入等待队列）→ 释放当前锁 → 被唤醒后重新尝试获取锁。
- 必须搭配 `synchronized` 使用，脱离会直接抛异常。
- 结束等待的条件：
  1. 其他线程调用该对象的 `notify()`；
  2. 等待超时（`wait(timeout)`）；
  3. 其他线程调用该等待线程的 `interrupt()`，抛出 `InterruptedException`。

```java
public static void main(String[] args) throws InterruptedException {
    Object object = new Object();
    synchronized (object) {
        System.out.println("等待中");
        object.wait();
        System.out.println("等待结束");
    }
}
```

### 8.2 notify() 方法
- 在同步方法/块中调用，通知等待该对象锁的线程，令其重新获取锁。
- 多个线程等待时，由调度器**随机**挑一个（无先来后到）。
- `notify()` 后，当前线程不会马上释放锁，要等退出同步块才释放。

```java
static class WaitTask implements Runnable {
    private Object locker;
    public WaitTask(Object locker) { this.locker = locker; }
    @Override
    public void run() {
        synchronized (locker) {
            while (true) {
                try {
                    System.out.println("wait 开始");
                    locker.wait();
                    System.out.println("wait 结束");
                } catch (InterruptedException e) { e.printStackTrace(); }
            }
        }
    }
}
static class NotifyTask implements Runnable {
    private Object locker;
    public NotifyTask(Object locker) { this.locker = locker; }
    @Override
    public void run() {
        synchronized (locker) {
            System.out.println("notify 开始");
            locker.notify();
            System.out.println("notify 结束");
        }
    }
}
public static void main(String[] args) throws InterruptedException {
    Object locker = new Object();
    Thread t1 = new Thread(new WaitTask(locker));
    Thread t2 = new Thread(new NotifyTask(locker));
    t1.start();
    Thread.sleep(1000);
    t2.start();
}
```

### 8.3 notifyAll() 方法
- `notify` 只唤醒一个等待线程；`notifyAll` 一次唤醒所有等待线程。
- 被唤醒的线程需要**重新竞争锁**，所以仍是“有先有后”地执行。

### 8.4 wait 和 sleep 的对比（面试题）
1. `wait` 需要搭配 `synchronized` 使用，`sleep` 不需要。
2. `wait` 是 `Object` 的方法，`sleep` 是 `Thread` 的静态方法。
3. 唯一共同点：都能让线程放弃执行一段时间。

---

## 9. 多线程案例

### 9.1 单例模式
> 设计模式好比象棋“棋谱”，针对常见问题场景总结出固定套路。单例模式保证某个类在程序中只存在唯一一份实例（如 JDBC 的 DataSource）。

**饿汉模式**（类加载时创建）
```java
class Singleton {
    private static Singleton instance = new Singleton();
    private Singleton() {}
    public static Singleton getInstance() { return instance; }
}
```

**懒汉模式——单线程版**（首次使用时创建）
```java
class Singleton {
    private static Singleton instance = null;
    private Singleton() {}
    public static Singleton getInstance() {
        if (instance == null) instance = new Singleton();
        return instance;
    }
}
```

**懒汉模式——多线程版**（加 synchronized 改善安全）
```java
class Singleton {
    private static Singleton instance = null;
    private Singleton() {}
    public synchronized static Singleton getInstance() {
        if (instance == null) instance = new Singleton();
        return instance;
    }
}
```

**懒汉模式——双重检查（DCL，改进版，推荐）**
```java
class Singleton {
    private static volatile Singleton instance = null;
    private Singleton() {}
    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
```
> 双重 if + volatile 的作用：
> - 外层 if：实例已创建则不必再加锁，降低开销；
> - 内层 if：首个获取锁的线程创建实例，后续线程被挡住，不再重复创建；
> - volatile：避免“内存可见性”导致读取 instance 出现偏差，同时禁止指令重排序。

### 9.2 阻塞队列
**概念**：特殊队列，遵守 FIFO；是线程安全的数据结构。
- 队列满 → 继续入队阻塞，直到有线程取走元素；
- 队列空 → 继续出队阻塞，直到有线程插入元素。
- 典型应用：**生产者消费者模型**（削峰填谷 + 解耦）。

**标准库阻塞队列**
```java
BlockingQueue<String> queue = new LinkedBlockingQueue<>();
queue.put("abc");           // 阻塞入队
String elem = queue.take(); // 阻塞出队（没有就阻塞）
```

**生产者消费者模型**
```java
BlockingQueue<Integer> blockingQueue = new LinkedBlockingQueue<>();
Thread customer = new Thread(() -> {
    while (true) {
        try {
            int value = blockingQueue.take();
            System.out.println("消费元素: " + value);
        } catch (InterruptedException e) { e.printStackTrace(); }
    }
}, "消费者");
Thread producer = new Thread(() -> {
    Random random = new Random();
    while (true) {
        try {
            int num = random.nextInt(1000);
            System.out.println("生产元素: " + num);
            blockingQueue.put(num);
            Thread.sleep(1000);
        } catch (InterruptedException e) { e.printStackTrace(); }
    }
}, "生产者");
customer.start(); producer.start();
```

**手写阻塞队列（循环数组 + synchronized + wait/notifyAll）**
```java
public class BlockingQueue {
    private int[] items = new int[1000];
    private volatile int size = 0;
    private volatile int head = 0;
    private volatile int tail = 0;

    public void put(int value) throws InterruptedException {
        synchronized (this) {
            // 必须用 while：被 notifyAll 唤醒时队列可能又被填满
            while (size == items.length) {
                wait();
            }
            items[tail] = value;
            tail = (tail + 1) % items.length;
            size++;
            notifyAll();
        }
    }

    public int take() throws InterruptedException {
        int ret = 0;
        synchronized (this) {
            while (size == 0) {
                wait();
            }
            ret = items[head];
            head = (head + 1) % items.length;
            size--;
            notifyAll();
        }
        return ret;
    }

    public synchronized int size() { return size; }
}
```

### 9.3 定时器
类似“闹钟”，到设定时间执行指定代码。标准库 `Timer` 类的核心方法是 `schedule`。

```java
Timer timer = new Timer();
timer.schedule(new TimerTask() {
    @Override
    public void run() {
        System.out.println("hello");
    }
}, 3000);   // 3 秒后执行
```

**手写定时器**（优先级队列 + worker 线程）
```java
class MyTask implements Comparable<MyTask> {
    public Runnable runnable;
    public long time;   // 绝对时间戳
    public MyTask(Runnable runnable, long delay) {
        this.runnable = runnable;
        this.time = System.currentTimeMillis() + delay;
    }
    @Override
    public int compareTo(MyTask o) {
        return (int)(this.time - o.time);   // 时间小的优先
    }
}

class MyTimer {
    private PriorityQueue<MyTask> queue = new PriorityQueue<>();
    private Object locker = new Object();

    public void schedule(Runnable command, long after) {
        synchronized (locker) {
            queue.offer(new MyTask(command, after));
            locker.notify();
        }
    }

    public MyTimer() {
        Thread t = new Thread(() -> {
            while (true) {
                try {
                    synchronized (locker) {
                        while (queue.isEmpty()) {
                            locker.wait();
                        }
                        MyTask myTask = queue.peek();
                        long curTime = System.currentTimeMillis();
                        if (curTime >= myTask.time) {
                            queue.poll();
                            myTask.runnable.run();
                        } else {
                            locker.wait(myTask.time - curTime);
                        }
                    }
                } catch (InterruptedException e) { e.printStackTrace(); }
            }
        });
        t.start();
    }
}
```
> 注意：不要使用 `PriorityBlockingQueue`（容易死锁），用普通 `PriorityQueue` + 自己加锁。

### 9.4 线程池
> 减少每次启动、销毁线程的损耗。核心思想：线程不再用完即毁，而是放进“池子”复用。

**标准库线程池**
```java
ExecutorService pool = Executors.newFixedThreadPool(10);
pool.submit(new Runnable() {
    @Override
    public void run() { System.out.println("hello"); }
});
```
- `Executors` 创建方式：`newFixedThreadPool`（固定线程数）、`newCachedThreadPool`（线程数动态增长）、`newSingleThreadExecutor`（单线程）、`newScheduledThreadPool`（延迟/周期执行，进阶版 Timer）。
- `Executors` 本质是 `ThreadPoolExecutor` 的封装。

**ThreadPoolExecutor 七大参数**（进阶章节详细讲）
- `corePoolSize`：核心线程数（正式员工，不辞退）
- `maximumPoolSize`：最大线程数（正式 + 临时）
- `keepAliveTime` / `unit`：临时工空闲存活时间
- `workQueue`：传递任务的阻塞队列
- `threadFactory`：创建线程的工厂
- `handler`：拒绝策略（AbortPolicy / CallerRunsPolicy / DiscardOldestPolicy / DiscardPolicy）

**手写简单线程池**
```java
class MyThreadPool {
    private BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();
    public void submit(Runnable runnable) throws InterruptedException {
        queue.put(runnable);
    }
    public MyThreadPool(int n) {
        for (int i = 0; i < n; i++) {
            Thread t = new Thread(() -> {
                while (true) {
                    try {
                        Runnable runnable = queue.take();
                        runnable.run();
                    } catch (InterruptedException e) { e.printStackTrace(); }
                }
            });
            t.start();
        }
    }
}
```

---

## 10. 保证线程安全的思路
1. 使用没有共享资源的模型。
2. 使用共享资源只读、不写的模型：
   - 不需要写共享资源；
   - 使用不可变对象。
3. 直面线程安全（重点）：
   - 保证原子性；
   - 保证顺序性；
   - 保证可见性。

---

## 11. 线程与进程的对比

**11.1 线程的优点**
1. 创建新线程代价远小于创建新进程。
2. 线程切换比进程切换做的操作系统工作少得多。
3. 线程占用资源比进程少。
4. 能充分利用多处理器的并行能力。
5. 等待慢速 IO 的同时可执行其他计算。
6. 计算密集型应用可把计算分解到多线程。
7. IO 密集型应用可重叠多个 IO 操作。

**11.2 进程与线程的区别**
1. 进程是资源分配和调度独立单位，线程是程序执行最小单位。
2. 进程有独立内存地址空间，线程只独享指令流必要资源（寄存器、栈）。
3. 同一进程各线程共享内存和文件资源，可不通过内核直接通信。

---

# 第二部分：多线程进阶

## 1. 常见的锁策略

> 以下策略不局限于 Java，任何“锁”相关话题都涉及，主要给锁的实现者参考，使用者了解也能更好地用锁。

### 1.1 乐观锁 vs 悲观锁
- **悲观锁**：假设最坏情况，每次拿数据都认为别人会改，于是先上锁（如数据库 `select for update`、synchronized 竞争激烈时）。
- **乐观锁**：假设一般不会冲突，提交更新时才检测是否冲突，冲突则返回错误让用户决定。常见实现：**版本号机制**。
- 类比：同学 A 先问老师“忙不忙”（悲观），同学 B 直接去问（乐观）。
- `synchronized` 初始用**乐观锁**策略，锁竞争频繁时自动切换成**悲观锁**。

### 1.2 重量级锁 vs 轻量级锁
- 原子性机制根源是 CPU 硬件指令 → OS 用 mutex 实现互斥锁 → JVM 实现 synchronized/ReentrantLock。
- **重量级锁**：重度依赖 OS 的 mutex，涉及大量用户态/内核态切换与线程调度，开销大。
- **轻量级锁**：尽量在用户态完成，实在不行才用 mutex。
- `synchronized` 开始是轻量级锁，锁冲突严重时变成重量级锁。

### 1.3 自旋锁（Spin Lock）
- 抢锁失败后不立即放弃 CPU（不阻塞），而是循环重试：`while (抢锁(lock) == 失败) {}`
- 优点：不放弃 CPU、不涉及阻塞调度，锁一释放就能第一时间获取。
- 缺点：锁持有久则持续消耗 CPU。
- `synchronized` 的轻量级锁大概率通过**自旋锁**实现。

### 1.4 公平锁 vs 非公平锁
- **公平锁**：遵守“先来后到”，A 释放后 B 先于 C 获取。
- **非公平锁**：B、C 都有可能获取到，不遵守先来后到（OS 调度本身是随机的）。
- `synchronized` 是**非公平锁**；`ReentrantLock` 默认非公平，可传 `true` 开启公平锁。

### 1.5 可重入锁 vs 不可重入锁
- **可重入锁**：同一线程可多次获取同一把锁（递归锁）。Java 里 `Reentrant` 开头的锁、synchronized 都是可重入锁。
- **不可重入锁**（如 Linux 的 mutex）：未释放就再次加锁会死锁（自己锁死自己）。

### 1.6 读写锁
- 读-读不互斥，写-写互斥，读-写互斥。
- Java 提供 `ReentrantReadWriteLock`：`ReadLock`（读锁）、`WriteLock`（写锁）。
- 适合“频繁读、不频繁写”的场景（如教务系统查名单多、改名单少）。
- `synchronized` 不是读写锁。

---

## 2. CAS

### 2.1 什么是 CAS
CAS（Compare And Swap，比较并交换）：假设内存原数据 V，旧预期值 A，新值 B。
1. 比较 A 与 V 是否相等；
2. 相等则将 B 写入 V；
3. 返回操作是否成功。

```
boolean CAS(address, expectValue, swapValue) {
    if (&address == expectValue) {
        &address = swapValue;
        return true;
    }
    return false;
}
```
> 上面是伪代码，真实 CAS 是**一条原子硬件指令**完成。当多个线程同时 CAS，只有一个成功，其他不会阻塞，只收到失败信号。CAS 可视为一种乐观锁。

### 2.2 CAS 的应用
**（1）实现原子类**（java.util.concurrent.atomic）
```java
AtomicInteger atomicInteger = new AtomicInteger(0);
atomicInteger.getAndIncrement();   // 相当于 i++
```
伪代码：
```java
class AtomicInteger {
    private int value;
    public int getAndIncrement() {
        int oldValue = value;
        while (CAS(value, oldValue, oldValue + 1) != true) {
            oldValue = value;
        }
        return oldValue;
    }
}
```
> 不需要重量级锁即可高效完成自增；CAS 直接读写内存且是一条硬件指令，原子。

**（2）实现自旋锁**
```java
public class SpinLock {
    private Thread owner = null;
    public void lock() {
        // 通过 CAS 看锁是否被持有
        while (!CAS(this.owner, null, Thread.currentThread())) { }
    }
    public void unlock() {
        this.owner = null;
    }
}
```

### 2.3 CAS 的 ABA 问题
- 线程 t1 想把 num 从 A 改成 Z，先读 num=A；其间 t2 把 num 从 A→B→A。t1 的 CAS 发现 num 仍是 A，于是改成了 Z——但 num 其实已被改动过。
- **危害**：纯数值场景（如 AtomicInteger）通常无影响；但对象/引用/状态机场景会导致逻辑错误（如重复扣款）。
- **转账 Bug 示例**：余额 100，两个 ATM 线程各扣 50。线程1 成功扣到 50；线程2 阻塞期间，朋友转账 +50 变回 100；线程2 发现当前 100 == 之前读的 100，于是又扣 50，导致扣了两次。
- **解决**：给数据引入**版本号**，CAS 同时比较值与版本号。Java 提供 `AtomicStampedReference<E>`。

```
ABA 解决过程（带版本号）：
存款100/版本1 → 线程1扣到50/版本2 → 朋友转+50变100/版本3
线程2：当前100==读到100，但版本3 > 读到1 → 操作失败
```

---

## 3. synchronized 原理

### 3.1 基本特点（JDK 1.8）
1. 开始时是乐观锁，锁冲突频繁则转悲观锁。
2. 开始是轻量级锁，持有时间长则转重量级锁。
3. 轻量级锁大概率用自旋锁策略。
4. 是非公平锁。
5. 是可重入锁。
6. 不是读写锁。

### 3.2 加锁工作过程（锁升级）
JVM 将 synchronized 锁分为：**无锁 → 偏向锁 → 轻量级锁 → 重量级锁**，只能升级不能降级。

```
无锁
  │ （第一个线程加锁）
  ▼
偏向锁  （只给对象头做标记，记录线程ID，不真加锁）
  │ （有其他线程竞争）
  ▼
轻量级锁（CAS + 自适应自旋，用户态，不阻塞）
  │ （自旋久/竞争激烈）
  ▼
重量级锁（OS mutex，内核态，阻塞等待队列）
```

- **偏向锁**：第一个线程优先进入，只在对象头标记“属于哪个线程”。无竞争就不做真正同步；有竞争则撤销偏向，升级轻量级锁。相当于“延迟加锁”。
- **轻量级锁**：通过 CAS 检查并更新内存（null → 线程引用）。成功则加锁；失败则自旋等待（不放弃 CPU）。自旋到一定次数/时间就停止（自适应）。
- **重量级锁**：用到内核 mutex，加锁失败进入等待队列挂起，被 OS 唤醒后重试。

> 补充（参考 CSDN 资料）：字节码层面 synchronized 编译成 `monitorenter` / `monitorexit`；每个对象对应一个 ObjectMonitor（管程），含 `_owner`（持有线程）、`_EntryList`（阻塞池）、`_WaitSet`（等待池）、`_count`（可重入计数）。monitor 还是 `wait/notify` 的底层支撑。

### 3.3 其他优化
- **锁消除**：编译器 + JVM 判断锁可消除（如单线程下的 StringBuffer），直接消除加锁解锁。
- **锁粗化**：一段逻辑多次加锁解锁，自动合并成一次，避免频繁申请释放。

---

## 4. JUC（java.util.concurrent）常见类

### 4.1 Callable 接口
`Callable` 相当于带返回值的“任务”，配合 `FutureTask` 使用。

**不使用 Callable（麻烦版）**
```java
static class Result {
    public int sum = 0;
    public Object lock = new Object();
}
public static void main(String[] args) throws InterruptedException {
    Result result = new Result();
    Thread t = new Thread(() -> {
        int sum = 0;
        for (int i = 1; i <= 1000; i++) sum += i;
        synchronized (result.lock) {
            result.sum = sum;
            result.lock.notify();
        }
    });
    t.start();
    synchronized (result.lock) {
        while (result.sum == 0) result.lock.wait();
        System.out.println(result.sum);
    }
}
```

**使用 Callable（简洁版）**
```java
Callable<Integer> callable = new Callable<Integer>() {
    @Override
    public Integer call() throws Exception {
        int sum = 0;
        for (int i = 1; i <= 1000; i++) sum += i;
        return sum;
    }
};
FutureTask<Integer> futureTask = new FutureTask<>(callable);
Thread t = new Thread(futureTask);
t.start();
int result = futureTask.get();   // 阻塞等待结果
System.out.println(result);
```
> `FutureTask` 相当于麻辣烫的“小票”，可随时凭它查结果是否出来。

### 4.2 ReentrantLock
可重入互斥锁，与 synchronized 定位类似但更灵活。
```java
ReentrantLock lock = new ReentrantLock();
lock.lock();
try {
    // working
} finally {
    lock.unlock();   // 必须手动释放
}
```
**与 synchronized 的区别：**
| 对比 | synchronized | ReentrantLock |
|---|---|---|
| 实现 | JVM 内部（C++）关键字 | 标准库类（Java） |
| 释放 | 自动释放 | 手动 `unlock`（易遗漏） |
| 抢锁失败 | 死等 | `tryLock(timeout)` 可超时放弃 |
| 公平性 | 非公平 | 默认非公平，构造传 `true` 可公平 |
| 唤醒 | `wait/notify` 随机唤醒一个 | `Condition` 可精确唤醒指定线程 |

> 选择：竞争不激烈用 synchronized（方便）；竞争激烈用 ReentrantLock（tryLock 灵活）；需要公平锁用 ReentrantLock。

### 4.3 原子类
内部用 CAS，性能远高于加锁 `i++`。常见：`AtomicBoolean`、`AtomicInteger`、`AtomicIntegerArray`、`AtomicLong`、`AtomicReference`、`AtomicStampedReference`。
方法：`addAndGet`、`decrementAndGet`、`getAndDecrement`、`incrementAndGet`、`getAndIncrement` 等。

### 4.4 线程池（进阶版）
- `ExecutorService` 表示线程池实例；`Executors` 是工厂类；`submit` 提交任务。
- `Executors` 本质封装 `ThreadPoolExecutor`。
- **为什么不推荐直接用 Executors**：`newFixedThreadPool`/`newSingleThreadExecutor` 用无界队列可能 OOM；`newCachedThreadPool` 最大线程数 `Integer.MAX_VALUE` 可能创建过多线程导致 OOM。生产建议手动 `new ThreadPoolExecutor(...)`。

**ThreadPoolExecutor 七大参数 + 工作流程**
```
提交任务
  │
  ├─ 线程数 < corePoolSize → 创建核心线程执行
  ├─ 核心线程满 → 任务入 workQueue 排队
  ├─ 队列满 → 线程数 < maximumPoolSize → 创建非核心线程执行
  └─ 达到最大线程且队列满 → 执行拒绝策略(handler)
```
```java
ExecutorService pool = new ThreadPoolExecutor(
        1, 2, 1000, TimeUnit.MILLISECONDS,
        new SynchronousQueue<Runnable>(),
        Executors.defaultThreadFactory(),
        new ThreadPoolExecutor.AbortPolicy());
```
- 队列选择：`ArrayBlockingQueue`（有界数组）、`LinkedBlockingQueue`（链表）、`SynchronousQueue`（不存储，直接创建线程）、`PriorityBlockingQueue`（优先级）。
- 拒绝策略：`AbortPolicy`（抛异常）、`CallerRunsPolicy`（调用者执行）、`DiscardOldestPolicy`（丢最老）、`DiscardPolicy`（丢新任务）。

### 4.5 信号量 Semaphore
表示“可用资源的个数”，本质是一个原子计数器。P 操作（acquire）申请资源 -1，V 操作（release）释放资源 +1；为 0 时申请会阻塞。可实现“共享锁”（允许 N 个线程同时访问）。
```java
Semaphore semaphore = new Semaphore(4);
Runnable runnable = () -> {
    try {
        System.out.println("申请资源");
        semaphore.acquire();
        System.out.println("我获取到资源了");
        Thread.sleep(1000);
        System.out.println("我释放资源了");
        semaphore.release();
    } catch (InterruptedException e) { e.printStackTrace(); }
};
for (int i = 0; i < 20; i++) {
    new Thread(runnable).start();
}
```

### 4.6 CountDownLatch
同时等待 N 个任务执行结束（类似跑步比赛，全部到终点才公布成绩）。
```java
public class Demo {
    public static void main(String[] args) throws Exception {
        CountDownLatch latch = new CountDownLatch(10);
        Runnable r = () -> {
            try {
                Thread.sleep((long)(Math.random() * 10000));
                latch.countDown();   // 任务完成，计数 -1
            } catch (Exception e) { e.printStackTrace(); }
        };
        for (int i = 0; i < 10; i++) new Thread(r).start();
        latch.await();              // 阻塞至计数为 0
        System.out.println("比赛结束");
    }
}
```

---

## 5. 线程安全的集合类

大部分原生集合（ArrayList/HashMap 等）不是线程安全的。线程安全的有 Vector、Stack、HashTable（不推荐）。

### 5.1 多线程环境使用 ArrayList
1. 自己加同步（synchronized / ReentrantLock）；
2. `Collections.synchronizedList(new ArrayList<>())`；
3. `CopyOnWriteArrayList`（写时复制）：
   - 添加元素时先 copy 出新容器，新容器添加，再把原引用指向新容器；
   - 读不加锁，写加锁，读写分离；
   - 优点：读多写少场景性能高；缺点：占内存多，新写数据不能第一时间读到。

### 5.2 多线程环境使用队列
- `ArrayBlockingQueue`：基于数组的阻塞队列；
- `LinkedBlockingQueue`：基于链表的阻塞队列；
- `PriorityBlockingQueue`：基于堆的带优先级阻塞队列；
- `TransferQueue`：最多只含一个元素的阻塞队列。

### 5.3 多线程环境使用哈希表
`HashMap` 本身线程不安全，多线程可用 `Hashtable` 或 `ConcurrentHashMap`。

**Hashtable**
- 简单把关键方法加 `synchronized`（锁整个对象），多线程访问同一 Hashtable 直接锁冲突；
- size 也用 synchronized，慢；扩容时一个线程完成全部拷贝，效率低。

**ConcurrentHashMap（以 JDK 1.8 为例，重点）**
做了大量改进优化：
- **读操作不加锁**，用 volatile 保证读到最新值；只对写操作加锁，且锁的是“桶”（每个链表头结点），大幅降低锁冲突概率。
- 充分利用 **CAS**（如 size 用 CAS 更新，避免重量级锁）。
- **扩容优化——化整为零**：发现需扩容的线程创建新数组，只搬几个元素；扩容期间新老数组共存；后续每个操作 ConcurrentHashMap 的线程都会参与搬运一小部分；搬完最后一个元素再删老数组。期间插入只往新数组加，查找同时查新/老数组。
- 结构：数组 + 链表 / 红黑树（链表 ≥ 8 且数组 ≥ 64 时转红黑树）。
- key 不允许为 null。

| 对比 | HashMap | Hashtable | ConcurrentHashMap |
|---|---|---|---|
| 线程安全 | 否 | 是（锁对象，低效） | 是（锁桶头 + CAS） |
| key 为 null | 允许 | 不允许 | 不允许 |
| 性能 | — | 低 | 高 |

> JDK 1.7 用“分段锁 Segment”，1.8 已废弃，改为更细的桶级锁。

---

## 6. 死锁

### 6.1 什么是死锁
多个线程同时被阻塞，都在等待某个资源被释放，导致程序无法终止。例：滑稽老哥拿了酱油瓶、女神拿了醋瓶，互相等对方先给，构成死锁。经典案例还有**哲学家就餐问题**（5 个哲学家各拿左筷，再拿右筷，同时拿 → 死锁）。

### 6.2 死锁的四个必要条件
1. **互斥使用**：资源被一个线程占用时，别的不能用。
2. **不可抢占**：资源只能由占有者主动释放。
3. **请求和保持**：请求其他资源的同时保持原有资源。
4. **循环等待**：存在等待环路（P1 占有 P2 的资源，P2 占有 P3 的，P3 占有 P1 的）。

四个条件同时成立才形成死锁；打破任意一个即可消除死锁。**最容易破坏的是“循环等待”**。

### 6.3 避免死锁——锁排序
对所有锁编号（1..M），所有线程都按编号从小到大的顺序获取锁，避免环路等待。

```java
// 可能产生环路等待（危险）
Object lock1 = new Object(), lock2 = new Object();
// 线程1: lock1 -> lock2 ; 线程2: lock2 -> lock1  => 可能死锁

// 不会产生环路等待（安全）
// 所有线程统一: 先 lock1 再 lock2
synchronized (lock1) {
    synchronized (lock2) { /* do something */ }
}
```

---

## 7. 高频面试题汇总

1. **乐观锁 vs 悲观锁？怎么实现？**
   悲观锁认为冲突概率大，访问前真加锁（OS mutex）；乐观锁认为冲突概率小，直接访问并识别冲突，常引入版本号。

2. **读写锁？**
   读锁间不互斥，写锁间互斥，读写互斥；适合“频繁读、不频繁写”。

3. **自旋锁？**
   获取失败立即重试循环；优点是不放弃 CPU、第一时间获取，缺点持有久则浪费 CPU。

4. **synchronized 是可重入锁吗？**
   是；内部记录持有线程 + 计数器，重复加锁计数自增，归零才释放。

5. **CAS 机制？**
   Compare And Swap，一条原子硬件指令完成“读内存、比较、写内存”，需 CPU 指令支撑。

6. **ABA 怎么解决？**
   加版本号，CAS 同时比较值与版本号，版本不对则失败。

7. **synchronized 实现原理？**
   无锁 → 偏向锁 → 轻量级锁（CAS 自旋）→ 重量级锁（mutex）；含锁消除、锁粗化。

8. **Callable 与 Runnable 区别？**
   Runnable 不带返回值，Callable 带返回值，需配合 FutureTask。

9. **为什么有了 synchronized 还需要 JUC 的 Lock？**
   ReentrantLock 可手动释放、tryLock 超时放弃、可公平锁、Condition 精确唤醒。

10. **线程同步方式有哪些？**
    synchronized、ReentrantLock、Semaphore、CountDownLatch 等。

11. **volatile 用法？**
    保证内存可见性，强制从主内存读；不保证原子性。

12. **Java 多线程如何实现数据共享？**
    JVM 堆区是线程共享的，把数据放堆内存即可被多线程访问。

13. **线程池接口？LinkedBlockingQueue 作用？**
    Executors 工厂类 / ThreadPoolExecutor；LinkedBlockingQueue 是任务队列，submit/execute 往里加任务，工作线程取任务执行。

14. **Java 线程几种状态？怎么切换？**
    NEW、RUNNABLE、BLOCKED、WAITING、TIMED_WAITING、TERMINATED（见第 4 章）。

15. **多线程下对一个数叠加怎么做？**
    synchronized/ReentrantLock 加锁，或 AtomicInteger 原子操作。

16. **Servlet 线程安全吗？**
    本身工作在多线程下，若创建成员变量并被多线程操作，可能不安全。

17. **Thread 和 Runnable 的区别和联系？**
    Thread 描述线程，Runnable 描述任务；创建线程可重写 Thread.run 或用 Runnable 描述任务。

18. **多次 start 一个线程？**
    第一次成功，后续抛 `IllegalThreadStateException`。

19. **synchronized 两个方法，两线程同时调用？**
    非静态方法锁当前对象：同一实例则串行；不同实例则并发互不干扰。

20. **死锁是什么？怎么避免？**
    见第 6 章；破坏循环等待（锁排序）最常用。

21. **ConcurrentHashMap 读是否加锁？为什么？**
    读不加锁，用 volatile 保证可见性，降低锁冲突。

22. **ConcurrentHashMap 在 JDK 1.8 做了哪些优化？**
    取消分段锁改用桶级 synchronized + CAS；数组+链表/红黑树；化整为零扩容。

23. **ConcurrentHashMap vs Hashtable vs HashMap？**
    见 5.3 表格。

---


