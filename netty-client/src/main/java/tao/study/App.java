package tao.study;

import tao.study.netty.LineTextClientChannelHandler;
import tao.study.netty.NettyClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Hello world!
 */
public class App {


    public static void main(String[] args) {
        System.out.println("Hello World!");

        NettyClient nettyClient = new NettyClient();

        nettyClient.setHandler(new LineTextClientChannelHandler());
        try {
            nettyClient.connect("127.0.0.1", 8080);
        } catch (Exception e) {

            e.printStackTrace();
        }


        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            for (; ; ) {
                String line = in.readLine();
                if (line == null) {
                    continue;
                }

                if(line.equalsIgnoreCase("flush")){
                    nettyClient.flush();
                    continue;
                }

                if(line.equalsIgnoreCase("exit")){
                    break;
                }

                nettyClient.send(line);

            }
        }catch (Exception e){
            e.printStackTrace();
        }

        System.out.println("client exit!");

        nettyClient.close();

    }
}
