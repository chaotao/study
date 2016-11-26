package tao.study.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;


/**
 * Created by qinluo.ct on 15/8/26.
 */
public class LineTextClientChannelHandler extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        ch.pipeline().addLast( new LineBasedFrameDecoder(1024))
                .addLast(new StringDecoder())
                .addLast( new StringEncoder())
                .addLast(new SimpleChannelInboundHandler<String>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
                        print("receive:" + msg);

                        //ctx.write("this is client_"+System.currentTimeMillis());
                    }
                });


    }

    private  void print(String msg){

        System.out.println("[client]"+msg);

    }
}
