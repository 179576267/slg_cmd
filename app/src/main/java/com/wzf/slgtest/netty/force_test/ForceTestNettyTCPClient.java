package com.wzf.slgtest.netty.force_test;

import android.util.Log;

import com.wzf.slgtest.model.TcpHandlerParam;

import java.nio.ByteOrder;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;


/**
*
 * @author Administrator
 * @date 2016/11/9
 */
public abstract class ForceTestNettyTCPClient implements Runnable{
    private static List<Nio> nios = new CopyOnWriteArrayList<>();
    public TcpHandlerParam param;
    private String ip;
    private int port;
    public ForceTestNettyTCPClient(String ip, int port, TcpHandlerParam param) {
        this.param = param;
        this.ip = ip;
        this.port = port;
    }


    @Override
    public void run() {
        connect();
    }

    private  void connect()
    {
        NioEventLoopGroup  workerGroup = null;
            synchronized (ForceTestNettyTCPClient.class){
                if(nios.size() > 0){
                    Nio nio  = nios.get(nios.size() - 1);
                    if(nio.isFull()){
                        workerGroup = new NioEventLoopGroup();
                        nios.add(new Nio(workerGroup, 1));
                    }else {
                        workerGroup = nio.group;
                        nio.addCount();
                    }
                }else {
                    workerGroup = new NioEventLoopGroup();
                    nios.add(new Nio(workerGroup, 1));
                }
            }
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {

                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(ByteOrder.LITTLE_ENDIAN,Integer.MAX_VALUE,0,4,2,4,true));
                    ch.pipeline().addLast(new ForceTestNettyTCPClientHandler(param));

                }
            });
            // 发起异步连接操作
            ChannelFuture future = bootstrap.connect(ip, port).sync();

            if(future.isSuccess())
            {
                Log.i("MultFunctionNettyTCPClient","TCP Client Start Success------Port---【" + 11111 + "】");
                startSuccess();
            }
            else
            {
                Log.i("MultFunctionNettyTCPClient","TCP Client Start Failed------Port---【" + 11111 + "】");
            }
        } catch (Exception e) {
            e.printStackTrace();
            stop();
        }
    }

    public synchronized void stop()
    {
        Log.i("MultFunctionNettyTCPClient","Close TCPServer..................");
        for(Nio nio : nios){
            nio.group.shutdownGracefully();
        }

    }



    public abstract void startSuccess();


    class Nio{
        int poolCount;
        NioEventLoopGroup group;

        public Nio(NioEventLoopGroup group, int poolCount) {
            this.group = group;
            this.poolCount = poolCount;
        }

        public boolean isFull(){
            return poolCount >= 4;
        }

        public void addCount(){
            poolCount++;
        }
    }
}
