package tao.study.algorithm.concurrent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by qinluo.ct on 15/12/17.
 * 多生产单消费。 队列大小到阈值时唤醒消费者
 * 允许唤醒失败，消费者wait一定时间后自己可以唤醒。
 */
public class RingSingleConsumeBlcokingQueue<T> {

    private AtomicLong put = new AtomicLong(0);

    private AtomicLong take = new AtomicLong(0);

    private T[] dataArr;

    private int queueSize = 1024;

    private long mask = 1024;

    private ReentrantLock lock = new ReentrantLock();

    private Condition takeCondition = lock.newCondition();

    private int sizeForSignalTake = 10;

    private AtomicBoolean isTaking = new AtomicBoolean(false);


    public RingSingleConsumeBlcokingQueue(int size){
        this.queueSize = size;
        this.dataArr = (T[])new Object[size];
    }


    public boolean put(T data){


        for (;;) {

            long takeIndex = take.get();
            long putIndex = put.get();
            long size = putIndex - takeIndex;

            if(size >= queueSize){
                return false;
            }

            if(put.compareAndSet(putIndex,putIndex+1)){

                dataArr[(int)(mask&(putIndex+1))] = data;
                size++;
                if(size >= sizeForSignalTake && !isTaking.get() && lock.tryLock()){
                    try {
                        takeCondition.signal();
                    }finally {
                        lock.unlock();
                    }
                }
                return true;
            }

        }


    }


    public T take()throws InterruptedException{

        for (;;){

            isTaking.set(true);

            long takeIndex = take.get();
            long putIndex = put.get();
            long size = putIndex - takeIndex;

            if(size == 0){
                if(lock.tryLock()){
                    try {
                        isTaking.set(false);
                        takeCondition.await(1000, TimeUnit.SECONDS);
                    }finally {
                        lock.unlock();
                    }
                }
                continue;
            }

            if(!take.compareAndSet(takeIndex,takeIndex -1)){
                continue;
            }

            int arrIndex = (int) ((takeIndex -1) & mask);
            T data =null;
            while (data==null) {
                data = dataArr[arrIndex];
            }

            return data;

        }

    }







}
