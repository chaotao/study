package tao.study;

import tao.study.netty.NettyServer;
import tao.study.netty.SimpleCommondServerChannelHandler;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );

        NettyServer nettyServer = new NettyServer();

        nettyServer.setHandler(new SimpleCommondServerChannelHandler());

        try {
            nettyServer.listen(8080);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
