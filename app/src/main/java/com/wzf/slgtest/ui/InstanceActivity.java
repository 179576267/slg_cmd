package com.wzf.slgtest.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.douqu.game.core.protobuf.SGChallengeProto.*;
import com.douqu.game.core.protobuf.SGMainProto;
import com.douqu.game.core.protobuf.SGSystemProto;
import com.wzf.slgtest.MyApplication;
import com.wzf.slgtest.R;
import com.wzf.slgtest.netty.Config;
import com.wzf.slgtest.utils.BaseActivity;
import com.wzf.slgtest.utils.InputTextDialog;
import com.wzf.slgtest.utils.SendUtils;
import com.wzf.slgtest.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Administrator
 */
public class InstanceActivity extends BaseActivity {

    @Bind(R.id.tv_object_index)
    TextView tvObjectIndex;
    @Bind(R.id.tv_result)
    TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instance);
        tvObjectIndex.setText(MyApplication.getAppInstance().objecIndex);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fun1(S2C_GetInstanceInfo msgInfo) {
        tvResult.setText("查看副本进度： \n" + msgInfo.toString());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fun2(S2C_PassLevel msgInfo) {
        tvResult.setText("通过某一关卡： \n" + msgInfo.toString());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fun3(S2C_ReceiveInstanceAward msgInfo) {
        tvResult.setText("兑换地图宝箱奖励： \n" + msgInfo.toString());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fun4(S2C_ReceiveLevelBoxReward msgInfo) {
        tvResult.setText("兑换副本关卡的宝箱： \n" + msgInfo.toString());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void exchangeReward(S2C_GetLastPassLevel msgInfo) {
        tvResult.setText("获取最近一次的挑战记录： \n" + msgInfo.toString());
    }




    @OnClick({R.id.btn_my_instance, R.id.btn_pass_level, R.id.btn_receive_award, R.id.btn_level_award,
            R.id.btn_last_pass_level, R.id.btn_battle_request})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_my_instance:
                C2S_GetInstanceInfo.Builder request = C2S_GetInstanceInfo.newBuilder();
                request.setChapterId(0);
                SendUtils.sendMsg(InstanceActivity.this, SGMainProto.E_MSG_ID.MsgID_Instance_GetInstanceInfo_VALUE, request.build().toByteArray());
                break;
            case R.id.btn_pass_level:
                new InputTextDialog(this, "mapId,levelId;stars") {
                    @Override
                    public void sendText(String text) {
                        String[] ids = text.split(";");
                        int mapId = StringUtils.stringToInt(ids[0]);
                        int levelId = StringUtils.stringToInt(ids[1]);
                        int stars = StringUtils.stringToInt(ids[2]);
                        C2S_PassLevel.Builder request = C2S_PassLevel.newBuilder().
                                setChapterId(mapId)
                                .setLevelId(levelId)
                                .setStarts(stars);
                        SendUtils.sendMsg(InstanceActivity.this, SGMainProto.E_MSG_ID.MsgID_Instance_PassLevel_VALUE, request.build().toByteArray());
                    }
                }.show();
                tvResult.setText("");
                break;
            case R.id.btn_battle_request:
                new InputTextDialog(this, "mapId,levelId;stars") {
                    @Override
                    public void sendText(String text) {
                        String[] ids = text.split(";");
                        int mapId = StringUtils.stringToInt(ids[0]);
                        int levelId = StringUtils.stringToInt(ids[1]);
                        int stars = StringUtils.stringToInt(ids[2]);
                        C2S_PassLevel.Builder request = C2S_PassLevel.newBuilder().
                                setChapterId(mapId)
                                .setLevelId(levelId)
                                .setStarts(stars);
                        SendUtils.sendMsg(InstanceActivity.this, SGMainProto.E_MSG_ID.MsgID_Instance_PassLevel_VALUE, request.build().toByteArray());
                    }
                }.show();
                tvResult.setText("");
                break;
            case R.id.btn_receive_award:
                new InputTextDialog(this, "mapId;rewardId") {
                    @Override
                    public void sendText(String text) {
                        String[] ids = text.split(";");
                        int mapId = StringUtils.stringToInt(ids[0]);
                        int rewardId = StringUtils.stringToInt(ids[1]);
                        C2S_ReceiveInstanceAward.Builder request =  C2S_ReceiveInstanceAward.newBuilder();
                        request.setChapterId(mapId);
                        request.setRewardId(rewardId);
                        SendUtils.sendMsg(InstanceActivity.this, SGMainProto.E_MSG_ID.MsgID_Instance_ReceiveAward_VALUE, request.build().toByteArray());
                    }
                }.show();
                break;
            case R.id.btn_level_award:
                new InputTextDialog(this, "mapId;levelId") {
                    @Override
                    public void sendText(String text) {
                        String[] ids = text.split(";");
                        int mapId = StringUtils.stringToInt(ids[0]);
                        int levelId = StringUtils.stringToInt(ids[1]);
                        C2S_ReceiveLevelBoxReward.Builder request =  C2S_ReceiveLevelBoxReward.newBuilder();
                        request.setChapterId(mapId);
                        request.setLevelId(levelId);
                        SendUtils.sendMsg(InstanceActivity.this, SGMainProto.E_MSG_ID.MsgID_Instance_ReceiveLevelBoxReward_VALUE, request.build().toByteArray());
                    }
                }.show();
                break;
            case R.id.btn_last_pass_level:
                SendUtils.sendMsg(InstanceActivity.this, SGMainProto.E_MSG_ID.MsgID_Instance_GetLastPassLevel_VALUE, null);
                break;
            default:
                break;
        }
    }
}
