package tao.study.netty;



import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Created by qinluo.ct on 15/6/19.
 */
public class NettyServer {


    private final ServerBootstrap bootstrap = new ServerBootstrap();




    private ChannelInitializer channelInitializer = null;

    public void setHandler(ChannelInitializer channelInitializer){
        this.channelInitializer = channelInitializer;
    }

    public void listen(int port)throws InterruptedException{


        EventLoopGroup bossGroup = new NioEventLoopGroup();

        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap server = bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                    //
//                .option(ChannelOption.SO_KEEPALIVE, false)
                    //
//                .option(ChannelOption.SO_SNDBUF, 65535)
                    //
//                .option(ChannelOption.SO_RCVBUF, 65535)
//                    .childHandler(new LineTextServerChannelHandler());
                    .childHandler(channelInitializer);

            ChannelFuture channelFuture = server.bind(port).sync();
            channelFuture.channel().closeFuture().sync();

        }finally {

            System.out.println("server close");
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }


    }


}
