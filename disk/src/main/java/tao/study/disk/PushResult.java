package tao.study.disk;

import java.util.concurrent.Future;

/**
 * Created by qinluo.ct on 16/11/24.
 */
public class PushResult {

    private long position;

    private Future<Boolean> flushResult;

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public Future<Boolean> getFlushResult() {
        return flushResult;
    }

    public void setFlushResult(Future<Boolean> flushResult) {
        this.flushResult = flushResult;
    }
}
