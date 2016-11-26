package tao.study.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.nio.ByteBuffer;

/**
 * Created by qinluo.ct on 15/8/27.
 */
public class SimpleCommondDecoder extends LengthFieldBasedFrameDecoder {


    public SimpleCommondDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
    }


    @Override
    public Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {


        SimpleCommond simpleCommond = new SimpleCommond();

        ByteBuf frame = null;
        try {
            frame = (ByteBuf) super.decode(ctx, in);
            if (null == frame) {
                return null;
            }

            ByteBuffer byteBuffer = frame.nioBuffer();

            int length = byteBuffer.getInt();

            simpleCommond.setCmd(byteBuffer.getInt());

            simpleCommond.setVersion(byteBuffer.getInt());

            int dataLength = length - 8;
            if(dataLength == 0){
                return simpleCommond;
            }


            byte[] data = new byte[dataLength];

            byteBuffer.get(data);

            simpleCommond.setData(data);

            return simpleCommond;

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (null != frame) {
                frame.release();
            }
        }

        return null;

    }

}
