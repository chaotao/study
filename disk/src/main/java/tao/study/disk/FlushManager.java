package tao.study.disk;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by qinluo.ct on 16/11/24.
 */

public class FlushManager {

    private static ScheduledExecutorService flushExecutor = Executors.newSingleThreadScheduledExecutor();

    private List<FileQueue> fileList;

    private Runnable flushRunner = new FlushRunner();

    public FlushManager(List<FileQueue> fileList) {
        this.fileList = fileList;
    }

    public void init() {
        flushExecutor.scheduleAtFixedRate(flushRunner, 1000, 1000, TimeUnit.MILLISECONDS);
    }

    class FlushRunner implements Runnable {
        public void run() {
            for (FileQueue item : fileList) {
                item.flushAtOnce();
            }
        }
    }


}
