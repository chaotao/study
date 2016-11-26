package tao.study.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import tao.network.protocol.SimpleCommond;
import tao.network.protocol.SimpleCommondDecoder;
import tao.network.protocol.SimpleCommondEncoder;

import java.net.InetAddress;

/**
 * Created by qinluo.ct on 15/8/27.
 */
public class SimpleCommondServerChannelHandler extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        ch.pipeline().addLast(new SimpleCommondDecoder(102400,0,4))
            .addLast(new SimpleCommondEncoder())
            .addLast(new SimpleChannelInboundHandler<SimpleCommond>() {
                @Override
                protected void channelRead0(ChannelHandlerContext ctx, SimpleCommond msg) throws Exception {
                    print("cmd:"+msg.getCmd()+";version:"+msg.getVersion()+";data:"+new String(msg.getData()));

                    SimpleCommond simpleCommond = new SimpleCommond();
                    simpleCommond.setCmd(SimpleCommond.CMD_HEART_BEAT);
                    simpleCommond.setVersion(1);
                    simpleCommond.setData(("Welcome to " + InetAddress.getLocalHost().getHostName() + " service").getBytes());
                    ctx.writeAndFlush(simpleCommond);

                }

                @Override
                public void channelActive(ChannelHandlerContext ctx) throws Exception {

                    print("RamoteAddress : " + ctx.channel().remoteAddress() + " active !");

                    SimpleCommond simpleCommond = new SimpleCommond();
                    simpleCommond.setCmd(SimpleCommond.CMD_HEART_BEAT);
                    simpleCommond.setVersion(1);
                    simpleCommond.setData(("Welcome to " + InetAddress.getLocalHost().getHostName() + " service").getBytes());
                    ctx.writeAndFlush(simpleCommond);

                    super.channelActive(ctx);
                }

            });
    }




    private  void print(String msg){

        System.out.println("[server]"+msg);

    }
}
