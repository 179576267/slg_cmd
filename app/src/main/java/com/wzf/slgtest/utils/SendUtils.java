package com.wzf.slgtest.utils;

import android.content.Intent;
import android.widget.Toast;

import com.douqu.game.core.protobuf.SGChallengeProto;
import com.douqu.game.core.protobuf.SGCommonProto;
import com.douqu.game.core.protobuf.SGPlayerProto;
import com.wzf.slgtest.MyApplication;
import com.wzf.slgtest.netty.Config;
import com.wzf.slgtest.netty.NettyTCPClient;
import com.wzf.slgtest.ui.ForceTestActivity;
import com.wzf.slgtest.ui.MultFunctionTestActivity;

import java.nio.ByteOrder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by bean
 * 2017-03-24 15:54.
 * @author Administrator
 */
public class SendUtils {

    public static void sendMsg(int msgID, byte[] data) {
        final ByteBuf byteBuf = Unpooled.buffer();
        int length = data == null ? 0 : data.length;
        if (ByteOrder.nativeOrder().toString().equals(ByteOrder.LITTLE_ENDIAN.toString())) {
            byteBuf.writeIntLE(length);//总包长
            byteBuf.writeShortLE(msgID);//长度为2
        } else {
            byteBuf.writeInt(length);//总包长
            byteBuf.writeShort(msgID);//长度为2
        }
        if (data != null)
            byteBuf.writeBytes(data);
        if (MyApplication.getAppInstance().getCtx().channel().isActive()) {
            MyApplication.getAppInstance().getCtx().channel().writeAndFlush(byteBuf);
        } else {
            new Thread(new NettyTCPClient() {
                @Override
                public void startSuccess() {
//                        MyApplication.getAppInstance().getCtx().channel().writeAndFlush(byteBuf);
                }
            }).start();
            Toast.makeText(MyApplication.getAppInstance(), "重连中...", Toast.LENGTH_LONG).show();
        }
    }


    public static void sendMsg(BaseActivity baseActivity, int msgID, byte[] data) {
        if(Config.multPlayerTest){
            Config.msgId = msgID;
            Config.data = data;
            baseActivity.startActivity(new Intent(baseActivity, MultFunctionTestActivity.class));
            baseActivity.finish();
            return;
        }

        sendMsg(msgID, data);

    }

    public static void sendMsg(ChannelHandlerContext context, int msgID, byte[] data)
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
        if(data != null){ byteBuf.writeBytes(data);}

        if(context.channel().isActive()){
            context.channel().writeAndFlush(byteBuf);
        }

    }

    public static int A= 0;
    public static void main(String[] args) {
        SGPlayerProto.S2C_RankList.Builder response = SGPlayerProto.S2C_RankList.newBuilder();

        SGCommonProto.RankInfo.Builder selfRank = SGCommonProto.RankInfo.newBuilder();
        selfRank.setName("1");
        selfRank.setObjectIndex("1");
        selfRank.setRank(100);
        selfRank.setCamp(SGCommonProto.E_CAMP_TYPE.forNumber(1));
        response.setSelfRank(selfRank.build());

        System.out.println(response);
        response.getSelfRank().newBuilderForType().setRank(333);
//        selfRank.setRank(333);

        System.out.println(response);
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
