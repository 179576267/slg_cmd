package com.wzf.slgtest.netty;

import android.util.Log;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.nio.ByteOrder;


/**
*
 * @author Administrator
 * @date 2016/11/9
 */
public abstract class NettyTCPClient implements Runnable{
    private EventLoopGroup workerGroup;

    @Override
    public void run() {
        connect();
    }

    private  void connect()
    {

        workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {

                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(ByteOrder.LITTLE_ENDIAN,Integer.MAX_VALUE,0,4,2,4,true));
                    ch.pipeline().addLast(new NettyTCPClientHandler());

                }
            });
            // 发起异步连接操作
            ChannelFuture future = bootstrap.connect(Config.IP, 11111).sync();

            if(future.isSuccess())
            {
                Log.i("TCP Client","TCP Client Start Success------Port---【" + 11111 + "】");
                startSuccess();
            }
            else
            {
                Log.i("TCP Client","TCP Client Start Failed------Port---【" + 11111 + "】");
            }
        } catch (Exception e) {
            e.printStackTrace();
            stop();
        }
    }

    public synchronized void stop()
    {
        Log.i("TCP Client","Close TCPServer..................");
        workerGroup.shutdownGracefully();
    }



    public abstract void startSuccess();
}
