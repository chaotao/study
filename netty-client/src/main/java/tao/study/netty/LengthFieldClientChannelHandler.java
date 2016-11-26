package tao.study.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

import java.net.InetAddress;

/**
 * Created by qinluo.ct on 15/8/27.
 */
public class LengthFieldClientChannelHandler extends ChannelInitializer<SocketChannel> {


    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(10240,0,4))
                .addLast(new LengthFieldPrepender(4))
                .addLast(new SimpleChannelInboundHandler<Object>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {

                        print(msg.toString());

                        ByteBuf byteBuf = (ByteBuf)msg;

                        int length = byteBuf.getInt(0);
                        int content = byteBuf.getInt(4);

                        print("lengthField:"+length+";conent:"+content);

                    }

                });

    }




    private  void print(String msg){

        System.out.println("[client]"+msg);

    }
}
