package tao.study.disk;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * Created by qinluo.ct on 16/11/24.
 */
public class HashThreadPool {

    private int threadCount;

    private List<HashThread> threads;

    public HashThreadPool(int threadCount) {
        this.threadCount = threadCount;
        threads = new ArrayList<HashThread>(threadCount);

        for (int i = 0; i < threadCount; i++) {

            HashThread thread = new HashThread("Hash-Thread-" + i);

            threads.add(thread);
        }
    }


    public <T> Future<T> submit(String hash, Callable<T> callable) {
        CommonFuture future = new CommonFuture();
        FutureCallable futureCallable = new FutureCallable(callable, future);
        if (inHashTread(hash)) {
            futureCallable.doTask();
        } else {
            getThread(hash).queue.add(futureCallable);
        }
        return future;

    }


    private HashThread getThread(String hash) {
        int idx = hash.hashCode() % threadCount;
        return threads.get(idx);
    }

    public boolean inHashTread(String hash) {
        return getThread(hash).getId() == Thread.currentThread().getId();
    }


    class HashThread extends Thread {

        private BlockingQueue<Callable> queue = new ArrayBlockingQueue<Callable>(1000);

        public HashThread(String name) {
            super(name);

        }

    }


    class FutureCallable implements Callable {

        private Callable callable;

        private CommonFuture future;

        public FutureCallable(Callable callable, CommonFuture future) {
            this.callable = callable;
            this.future = future;
        }

        public Object call() throws Exception {
            return doTask();
        }

        public Object doTask() {
            Object result = null;
            try {
                result = callable.call();
                future.value(result);
            } catch (Exception e) {
                future.exception(e);
            }
            return result;
        }
    }

}
