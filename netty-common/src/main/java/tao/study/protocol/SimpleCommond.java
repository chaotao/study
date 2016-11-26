package tao.study.protocol;

import com.alibaba.fastjson.JSON;

/**
 * Created by qinluo.ct on 15/8/27.
 */
public class SimpleCommond {

    public static final int CMD_HEART_BEAT=0;

    private int cmd;

    private int version;

    private byte[] data;

    private Object dataObject;

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
