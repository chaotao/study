package tao.study.disk;

/**
 * Created by qinluo.ct on 16/11/24.
 */
public abstract class FlushCallback {

    protected long writePosition;

    public abstract void callback(boolean success, String code, String msg);

}
