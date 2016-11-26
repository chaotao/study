package tao.study.algorithm.concurrent.sync;

import java.util.Random;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * Created by qinluo.ct on 16/3/3.
 * 周期循环定时器
 * 按照指定周期时间，不断触发新的周期, 可用于超时计数器。
 */
public class CycleTimer {


    private Sync sync = new Sync();

    private long timeout;

    private int randomMax;

    private Random random = new Random();

    public CycleTimer(long timeout, int random) {
        this.timeout = timeout;
        this.randomMax = random;

    }


    public void start() {

        int period = 0;
        for (; ; ) {

            try {
                period++;
                System.out.println("[CycleTimer]period[" + period + "] start");

                sleep(randomTimeOut());

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }


    public boolean sleep(long timeout) throws InterruptedException {
        long start = System.currentTimeMillis();
        if (sync.tryAcquireSharedNanos(1, timeout)) {

            System.out.println("period interrupted,dur = " + (System.currentTimeMillis() - start));

            /**
             * 可以处理中断逻辑
             */
            return false;
        }

        System.out.println("period interrupted,dur = " + (System.currentTimeMillis() - start));
        return true;

    }


    public void interrupt() {
        sync.release(1);
    }

    private long randomTimeOut() {
        return timeout + new Random().nextInt(randomMax);
    }

    public static class Sync extends AbstractQueuedSynchronizer {


        public Sync() {
            setState(1);
        }

        public boolean tryReleaseShared(int releases) {

            if (releases != 1) {
                throw new RuntimeException();
            }
            int p = getState();
            if (p == 1) {
                return false;
            }
            return compareAndSetState(getState(), releases);

        }

        public int tryAcquireShared(int acquires) {

            if (acquires != 1) {
                throw new RuntimeException();
            }

            for (; ; ) {
                int available = getState();
                int remaining = available - acquires;
                if (available < 0 ||
                        compareAndSetState(available, remaining))
                    return remaining;
            }
        }


    }


    public static void main(String[] args) {


        final CycleTimer cycleTimer = new CycleTimer(2000, 2000);

        new Thread(new Runnable() {
            public void run() {
                cycleTimer.start();
            }

        }).start();

        try {

            Thread.sleep(5000);

            cycleTimer.interrupt();

            Thread.sleep(5000);
            cycleTimer.interrupt();


            Thread.sleep(10000);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
