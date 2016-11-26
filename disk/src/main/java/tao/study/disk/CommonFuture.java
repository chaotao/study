package tao.study.disk;

import com.google.common.util.concurrent.AbstractFuture;

/**
 * Created by qinluo.ct on 16/11/24.
 */
public class CommonFuture extends AbstractFuture {

    boolean value(Object value) {
        return set(value);
    }

    boolean exception(Throwable throwable) {
        return setException(throwable);
    }

}
