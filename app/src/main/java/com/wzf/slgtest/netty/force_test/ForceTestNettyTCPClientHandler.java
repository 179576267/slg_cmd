package com.wzf.slgtest.netty.force_test;


import android.util.Log;

import com.douqu.game.core.protobuf.SGCommonProto;
import com.douqu.game.core.protobuf.SGChallengeProto.*;
import com.douqu.game.core.protobuf.SGMainProto;
import com.douqu.game.core.protobuf.SGPlayerProto;
import com.douqu.game.core.protobuf.SGSystemProto;
import com.douqu.game.core.protobuf.SGWarProto;
import com.wzf.slgtest.model.BattleInfo;
import com.wzf.slgtest.model.PlayerStatus;
import com.wzf.slgtest.model.TcpHandlerParam;
import com.wzf.slgtest.utils.BufferUtils;
import com.wzf.slgtest.utils.SendUtils;

import org.greenrobot.eventbus.EventBus;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author Administrator
 * @date 2016/11/9
 */
public class ForceTestNettyTCPClientHandler extends ChannelInboundHandlerAdapter {
    public final static String TAG = "ForceTestHandler";
    private TcpHandlerParam param;
    private String objectIndex;

    public ForceTestNettyTCPClientHandler(TcpHandlerParam param) {
        this.param = param;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        cause.printStackTrace();
       Log.e("ForceTestHandler", cause.getLocalizedMessage());
        ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        int code = BufferUtils.readShort(byteBuf);

//        Log.i("ForceTestHandler","data length:" + byteBuf.readableBytes());
//        Log.i("ForceTestHandler","code:"+code);
        byte[] data = BufferUtils.byteBufToBytes(byteBuf);
        switch (code){
            /*************************系统****************************************/
            case SGMainProto.E_MSG_ID.MsgID_System_NotifyAlert_VALUE:
//                EventBus.getDefault().post("警告:\n" + SGSystemProto.S2C_NotifyAlert.parseFrom(data).getContent());
                break;
            case SGMainProto.E_MSG_ID.MsgID_Player_FlushData_VALUE:
                SGPlayerProto.S2C_FlushData s2C_flushData = SGPlayerProto.S2C_FlushData.parseFrom(data);
                Log.i("flushData-->>",s2C_flushData.toString());
//                EventBus.getDefault().post("刷新数据：\n" + s2C_flushData.toString());
                break;

            case SGMainProto.E_MSG_ID.MsgID_Player_FlushGoodsGet_VALUE:
//                SGPlayerProto.S2C_FlushGoodsGet s2C_flushGoodsGet = SGPlayerProto.S2C_FlushGoodsGet.parseFrom(data);
//                Log.i("sendGoodsGet-->>",s2C_flushGoodsGet.toString());
//                EventBus.getDefault().post("获得物品：\n" + s2C_flushGoodsGet.toString());
                break;
            case SGMainProto.E_MSG_ID.MsgID_System_Login_VALUE:
                SGSystemProto.S2C_Login s2C_login = SGSystemProto.S2C_Login.parseFrom(data);
                Log.i(TAG,"登录返回<<------------" + s2C_login.toString());
                if(s2C_login.getResult().getNumber() == SGCommonProto.E_LOGIN_RESULT.LOGIN_RESULT_NEED_WAIT_VALUE){
                    EventBus.getDefault().post(new PlayerStatus(param.account, "登录失败,需要排队" + s2C_login.getWaitTime() / 1000 + "秒",
                            objectIndex));
                    return;
                }else if(s2C_login.getResult().getNumber() == SGCommonProto.E_LOGIN_RESULT.LOGIN_RESULT_SERVER_FULL_VALUE){
                    EventBus.getDefault().post(new PlayerStatus(param.account, "登录失败,服务器爆满",
                            objectIndex));
                    return;
                }
                objectIndex = s2C_login.getPlayerInfo().getPlayerIndex();
                Thread.sleep(1000);
                EventBus.getDefault().post(new PlayerStatus(param.account, "登录成功,请求战斗中", objectIndex));
                requestBattle(ctx);
                break;
            case SGMainProto.E_MSG_ID.MsgID_System_Regist_VALUE:
                break;
            /*************************副本****************************************/
            case  SGMainProto.E_MSG_ID.MsgID_Instance_RequestLevelBattle_VALUE:
                Thread.sleep(1000);
                EventBus.getDefault().post(new PlayerStatus(param.account, "请求战斗成功，连接战斗服务器", objectIndex));
                Thread.sleep(1000);
                connectBattle(S2C_RequestLevelBattle.parseFrom(data));
                break;
            /*********************************战斗****************************************/
            case SGMainProto.E_MSG_ID.MsgID_War_Create_VALUE:
                EventBus.getDefault().post(new PlayerStatus(param.account, "战斗创建成功，请求ReadyStart", objectIndex));
                Thread.sleep(1000);
                readyStart(ctx);
                break;
            case SGMainProto.E_MSG_ID.MsgID_War_ReadyStart_VALUE:
                EventBus.getDefault().post(new PlayerStatus(param.account, "正在战斗...", objectIndex));
                autoBattle(ctx);
                Thread.sleep(1000);
                break;
            case SGMainProto.E_MSG_ID.MsgID_War_SynResult_VALUE:
                SGWarProto.S2C_SynResult result = SGWarProto.S2C_SynResult.parseFrom(data);
                EventBus.getDefault().post(new PlayerStatus(param.account, "【战斗结束】", objectIndex));
                break;
            default:
                break;
        }


    }

    /**
     * 自动战斗
     * @param ctx
     */
    private void autoBattle(ChannelHandlerContext ctx) {
        SendUtils.sendMsg(ctx, SGMainProto.E_MSG_ID.MsgID_War_AutoBattle_VALUE, SGWarProto.C2S_AutoBattle.newBuilder().setBegin(true).build().toByteArray());
    }

    private void readyStart(ChannelHandlerContext ctx) {
        SendUtils.sendMsg(ctx, SGMainProto.E_MSG_ID.MsgID_War_ReadyStart_VALUE, SGWarProto.C2S_ReadyStart.newBuilder().build().toByteArray());
    }

    private void connectBattle(S2C_RequestLevelBattle data) {
        Log.i(TAG,"获取战斗服务器信息 <<------------" + data.toString());
        EventBus.getDefault().post(new BattleInfo(param.account, data.getServerInfo(), objectIndex, data.getBattleId()));
    }

    private void requestBattle(ChannelHandlerContext ctx) {
        C2S_RequestLevelBattle.Builder request = C2S_RequestLevelBattle.newBuilder();
        request.setChapterId(1);
        request.setLevelId(1);
        SendUtils.sendMsg(ctx, SGMainProto.E_MSG_ID.MsgID_Instance_RequestLevelBattle_VALUE, request.build().toByteArray());
    }


    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();// 将消息发送队列中的消息写入到SocketChannel中发送给对方。
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        if(param.isMain()){
            login(ctx);
        }else{
            battleCreate(ctx);
        }

    }

    private void battleCreate(ChannelHandlerContext ctx) {
        SGWarProto.C2S_Create.Builder request = SGWarProto.C2S_Create.newBuilder();
        request.setBattleId(param.battleId);
        request.setPlayerIndex(param.objectIndex);
        Log.i(TAG,"战斗创建请求-------->>" + request.toString());
        SendUtils.sendMsg(ctx, SGMainProto.E_MSG_ID.MsgID_War_Create_VALUE, request.build().toByteArray());
    }

    private void login(ChannelHandlerContext ctx) {
        SGSystemProto.C2S_Login.Builder request = SGSystemProto.C2S_Login.newBuilder();
        request.setChannel(SGCommonProto.E_CHANNEL_TYPE.CHANNEL_TYPE_QUICK);
        request.setNormal(true);
        request.setAccount(param.account);
        request.setLoginType(SGCommonProto.E_LOGIN_TYPE.LOGIN_TYPE_DEFAULT);
        Log.i(TAG,"登录请求-------->>" + request.toString());
        SendUtils.sendMsg(ctx, SGMainProto.E_MSG_ID.MsgID_System_Login_VALUE, request.build().toByteArray());
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception
    {
//        EventBus.getDefault().post(new PlayerStatus(param.account, (param.isMain() ? "主服务器" : "战斗服务器") + "断开连接了", objectIndex));
        Log.e("NettyTCPClientHandler", "服务器已关闭，我也要关服了！");
        ctx.close();
    }


}