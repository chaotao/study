package tao.study;

import tao.study.netty.NettyClient;
import tao.study.netty.SimpleCommondClientChannelHandler;
import tao.network.protocol.SimpleCommond;

/**
 * Created by qinluo.ct on 15/8/27.
 */
public class App3 {


    public static void main(String[] args) {
        System.out.println("Hello World!");

        NettyClient nettyClient = new NettyClient();
        nettyClient.setHandler(new SimpleCommondClientChannelHandler());
        try {
            nettyClient.connect("127.0.0.1", 8080);
        } catch (Exception e) {

            e.printStackTrace();
        }

        SimpleCommond simpleCommond = new SimpleCommond();
        simpleCommond.setCmd(2);
        simpleCommond.setVersion(2);
        simpleCommond.setData("this is client".getBytes());

        nettyClient.getChannel().writeAndFlush(simpleCommond);


        System.out.println("client end");

        //nettyClient.close();

    }
}
