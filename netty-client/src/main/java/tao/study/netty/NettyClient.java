package tao.study.netty;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by qinluo.ct on 15/6/19.
 */
public class NettyClient {

    private final Bootstrap bootstrap = new Bootstrap();

    private Channel channel;

    EventLoopGroup group;

    private ChannelInitializer channelInitializer = null;

    public void setHandler(ChannelInitializer channelInitializer){
        this.channelInitializer = channelInitializer;
    }

    public void connect(String ip,int port)throws InterruptedException{


        group = new NioEventLoopGroup();

        Bootstrap client = bootstrap.group(group).channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                        //
//                .option(ChannelOption.SO_KEEPALIVE, false)
                        //
//                .option(ChannelOption.SO_SNDBUF, 65535)
                        //
//                .option(ChannelOption.SO_RCVBUF, 65535)
//                .handler(new LineTextClientChannelHandler());
//                  .handler(new LengthFieldClientChannelHandler());
                   .handler(channelInitializer);

             channel = client.connect(ip,port).sync().channel();


    }

    public void close(){
        channel.close();
        group.shutdownGracefully();
    }

    public void send(String msg){
        channel.write(msg + "\n");
    }

    public Channel getChannel(){
        return channel;
    }


    public void flush(){
        channel.flush();
    }



}
