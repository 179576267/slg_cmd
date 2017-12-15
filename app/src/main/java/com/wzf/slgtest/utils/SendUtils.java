package com.wzf.slgtest.utils;

import android.widget.Toast;

import com.wzf.slgtest.MyApplication;
import com.wzf.slgtest.netty.NettyTCPClient;

import java.nio.ByteOrder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;

/**
 * Created by bean
 * 2017-03-24 15:54.
 */
public class SendUtils {

    public static void sendMsg(int msgID, byte[] data)
    {
        final ByteBuf byteBuf = Unpooled.buffer();
        int length = data == null ? 0 : data.length;
        if(ByteOrder.nativeOrder().toString().equals(ByteOrder.LITTLE_ENDIAN.toString())){
            byteBuf.writeIntLE(length);//总包长
            byteBuf.writeShortLE(msgID);//长度为2
        }else{
            byteBuf.writeInt(length);//总包长
            byteBuf.writeShort(msgID);//长度为2
        }
        if(data != null)
            byteBuf.writeBytes(data);
        if(MyApplication.getAppInstance().getCtx().channel().isActive()){
            MyApplication.getAppInstance().getCtx().channel().writeAndFlush(byteBuf);
        }else {
             new Thread(new NettyTCPClient() {
                    @Override
                    public void startSuccess() {
//                        MyApplication.getAppInstance().getCtx().channel().writeAndFlush(byteBuf);
                    }
                }).start();
            Toast.makeText(MyApplication.getAppInstance(), "重连中...", Toast.LENGTH_LONG).show();
        }

    }

    public static int A= 0;
    public static void main(String[] args) {
        int a = 54;
        int b = 514;
        A = 33;
        int max = max(a, b);
        int max2 = Math.max(a, b);
        String str = null;
        str.toString();
        System.out.println(max);
        
    }

    public static int max(int a, int b ){
        int c1 = 0;
        int c2 = 1;
        int c3 = 3;
        A = 45;
        int c4 = 4;
        for(int i = 0; i < 100; i++){
            System.out.println(i);
        }
        return a > b ? a : b;
    }


}
