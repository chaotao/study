package tao.study.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

import java.net.InetAddress;

/**
 * Created by qinluo.ct on 15/8/26.
 */
public class LengthFieldServerChannelHandler extends ChannelInitializer<SocketChannel> {

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

                    @Override
                    public void channelActive(ChannelHandlerContext ctx) throws Exception {

                        print("RamoteAddress : " + ctx.channel().remoteAddress() + " active !");


                        ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.buffer(4);

                        byteBuf.writeInt(10);


                        ctx.writeAndFlush(byteBuf);


                        super.channelActive(ctx);
                    }
                });
    }



    private  void print(String msg){

        System.out.println("[server]"+msg);

    }
}
