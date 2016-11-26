package tao.study.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetAddress;

/**
 * Created by qinluo.ct on 15/8/26.
 */
public class LineTextServerChannelHandler extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        ch.pipeline().addLast(new LineBasedFrameDecoder(1024))
                .addLast(new StringDecoder())
                .addLast(new StringEncoder())
                .addLast(new SimpleChannelInboundHandler<String>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
                        print("receive:" + msg);
                        ctx.writeAndFlush("this is client_" + System.currentTimeMillis() + "\r\n");
                    }

                    @Override
                    public void channelActive(ChannelHandlerContext ctx) throws Exception {

                        print("RamoteAddress : " + ctx.channel().remoteAddress() + " active !");

                        ctx.writeAndFlush("Welcome to " + InetAddress.getLocalHost().getHostName() + " service!\n");

                        super.channelActive(ctx);
                    }
                });

    }



    private  void print(String msg){

        System.out.println("[server]"+msg);

    }

}
