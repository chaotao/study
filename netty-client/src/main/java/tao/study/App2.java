package tao.study;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import tao.study.netty.LengthFieldClientChannelHandler;
import tao.study.netty.NettyClient;

/**
 * Created by qinluo.ct on 15/8/27.
 */
public class App2 {

    public static void main(String[] args) {
        System.out.println("Hello World!");

        NettyClient nettyClient = new NettyClient();
        nettyClient.setHandler(new LengthFieldClientChannelHandler());
        try {
            nettyClient.connect("127.0.0.1", 8080);
        } catch (Exception e) {

            e.printStackTrace();
        }



        ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.buffer(4);

        byteBuf.writeInt(100);

        nettyClient.getChannel().writeAndFlush(byteBuf);


        System.out.println("client end");

        //nettyClient.close();

    }
}
