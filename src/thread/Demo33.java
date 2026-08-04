package thread;

import java.util.Timer;
import java.util.TimerTask;

public class Demo33 {
    public static void main(String[] args) throws InterruptedException {
        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("hello , timer3000");
            }
        },3000);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("hello , timer2000");
            }
        },2000);


        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("hello , timer1000");
            }
        },1000);
        Thread.sleep(4000);
        timer.cancel();
    }
}
