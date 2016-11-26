package tao.study.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by qinluo.ct on 15/8/27.
 */
public class SimpleCommondEncoder extends MessageToByteEncoder<SimpleCommond> {
    @Override
    protected void encode(ChannelHandlerContext ctx, SimpleCommond msg, ByteBuf out) throws Exception {

        int length = 8;


        if(msg.getData()!=null){
            length += msg.getData().length;
        }

        out.writeInt(length);

        out.writeInt(msg.getCmd());
        out.writeInt(msg.getVersion());

        if(msg.getData()!=null) {
            out.writeBytes(msg.getData());
        }


    }
}
