package tao.study.disk;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by qinluo.ct on 16/11/18.
 * 单线程执行模型，
 */
public class FileQueue {

    private HashThreadPool hashThreadPool;

    Queue<FlushCallback> callbackQueue = new LinkedList<FlushCallback>();

    private volatile long lastFlushPosition;

    private String fileName;

    private long maxSize;

    private MappedByteBuffer mappedByteBuffer = null;

    private FileChannel fileChannel = null;


    public FileQueue(String fileName, long maxSize,HashThreadPool hashThreadPool) {
        this.fileName = fileName;
        this.maxSize = maxSize;
        this.hashThreadPool = hashThreadPool;
    }




    public void init() {

        File file = new File(fileName);
        try {

            if (!file.exists()) {
                file.createNewFile();
            }

            fileChannel = new RandomAccessFile(file, "rw").getChannel();
            mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, maxSize);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }

    }


    /**
     * 必须外部线程执行flush
     * 因为flush是IO操作，比较耗时，不能放在线程池中执行，否则会阻塞文件队列的正常操作
     *
     * @return
     */
    public long flushAtOnce() {
        long curPosition = mappedByteBuffer.position();

        if (curPosition == lastFlushPosition) {
            return lastFlushPosition;
        }

        lastFlushPosition = curPosition;
        mappedByteBuffer.force();

        /**
         * 异步通知
         */
        doInHashThreadPool(new Callable<Object>() {
            public Object call() throws Exception {
                notifyFlushCallback();
                return null;
            }
        });

        return lastFlushPosition;
    }


    private void notifyFlushCallback() {

        long callbackSize = callbackQueue.size();
        for (int i = 0; i < callbackSize; i++) {
            FlushCallback flushCallback = callbackQueue.poll();
            if (flushCallback.writePosition <= lastFlushPosition) {
                flushCallback.callback(true, null, null);
            } else {
                break;
            }
        }
    }


    public Long push(final byte[] data, final FlushCallback flushCallback) {
        return doInHashThreadPoolBlock(new Callable<Long>() {
            public Long call() {
                long postion = mappedByteBuffer.position();
                mappedByteBuffer.put(data);
                if (flushCallback != null) {
                    flushCallback.writePosition = mappedByteBuffer.position();
                    callbackQueue.add(flushCallback);
                }

                return postion;
            }
        });

    }

    public PushResult pushAndFlush(final byte[] data) {
        PushResult pushResult = new PushResult();
        CommonFuture commonFuture = new CommonFuture();
        pushResult.setFlushResult(commonFuture);
        pushResult.setPosition(push(data, new FutureFlushCallback(commonFuture)));
        return pushResult;
    }


    public Long push(final byte[] data) {
        return push(data, null);
    }


    public void push(final byte[] data, final PushCallback pushCallback, final FlushCallback flushCallback) {

        doInHashThreadPool(new Callable<Long>() {
            public Long call() {
                long postion = mappedByteBuffer.position();
                mappedByteBuffer.put(data);
                if (flushCallback != null) {
                    flushCallback.writePosition = mappedByteBuffer.position();
                    callbackQueue.add(flushCallback);
                }
                if (pushCallback != null) {
                    pushCallback.callback(postion, true, null);
                }
                return postion;
            }
        });

    }


    /**
     * 线程安全操作
     *
     * @param postion
     * @param size
     * @return
     */
    public ByteBuffer get(int postion, int size) {

        if (postion >= mappedByteBuffer.position()) {
            return null;
        }

        // copy 位置数据
        ByteBuffer byteBuffer = mappedByteBuffer.slice();
        byteBuffer.position(postion);
        byteBuffer.limit(postion + size);
        return byteBuffer;

    }

    private <T> Future<T> doInHashThreadPool(Callable<T> callable) {
        return hashThreadPool.submit(fileName, callable);
    }

    private <T> T doInHashThreadPoolBlock(Callable<T> callable) {
        try {
            return hashThreadPool.submit(fileName, callable).get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e.getCause());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    public interface PushCallback {

        void callback(long postion, boolean success, String code);

    }


    class FutureFlushCallback extends FlushCallback {

        private CommonFuture future;

        public FutureFlushCallback(CommonFuture future) {
            this.future = future;
        }

        public void callback(boolean success, String code, String msg) {
            future.value(success);
        }
    }
}
