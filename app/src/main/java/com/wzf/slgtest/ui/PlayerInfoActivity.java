package com.wzf.slgtest.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.douqu.game.core.protobuf.SGCommonProto;
import com.douqu.game.core.protobuf.SGMainProto;
import com.douqu.game.core.protobuf.SGChallengeProto.*;
import com.douqu.game.core.protobuf.SGPlayerProto;
import com.douqu.game.core.protobuf.SGSystemProto;
import com.douqu.game.core.protobuf.SGTaskProto;
import com.wzf.slgtest.MyApplication;
import com.wzf.slgtest.R;
import com.wzf.slgtest.netty.Config;
import com.wzf.slgtest.utils.BaseActivity;
import com.wzf.slgtest.utils.InputTextDialog;
import com.wzf.slgtest.utils.SendUtils;
import com.wzf.slgtest.utils.StringUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @Description:
 * @author: wangzhenfei
 * @date: 2017-11-03 14:14
 */

public class PlayerInfoActivity extends BaseActivity {
    @Bind(R.id.tv_object_index)
    TextView tvObjectIndex;
    @Bind(R.id.tv_result)
    TextView tvResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_info);
        tvObjectIndex.setText(MyApplication.getAppInstance().objecIndex);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fun2(SGPlayerProto.S2C_EditPlayerName msgInfo) {
        tvResult.setText("修改姓名返回： \n" + msgInfo.getPlayerName());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fun2(SGPlayerProto.S2C_EditPlayerAvatar msgInfo) {
        tvResult.setText("修改头像返回： \n" + msgInfo.getPlayerAvatar());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fun2(SGPlayerProto.S2C_ChangeEquippedSkill msgInfo) {
        tvResult.setText("改变上阵技能返回： \n" + msgInfo.toString());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fun2(SGPlayerProto.S2C_SynBaseData msgInfo) {
        tvResult.setText("BaseInfo返回： \n" + msgInfo.toString());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fun2(SGPlayerProto.S2C_SettingBoardInit msgInfo) {
        tvResult.setText("设置面板返回： \n" + msgInfo.toString());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fun2(SGPlayerProto.S2C_GetInstanceRemainChallengeTime msgInfo) {
        tvResult.setText("获取各个副本剩余挑战次数返回： \n" + msgInfo.toString());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fun2(SGTaskProto.S2C_TaskList msgInfo) {
        tvResult.setText("任务列表： \n" + msgInfo.toString());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fun2(SGPlayerProto.S2C_CdkUse msgInfo) {
        tvResult.setText("CDK使用成功： \n" + msgInfo.toString());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fun2(SGPlayerProto.S2C_RankList msgInfo) {
        tvResult.setText("获取排行榜返回： \n" + msgInfo.toString());
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fun2( SGPlayerProto.S2C_MasterTrain msgInfo) {
        tvResult.setText("主将培养返回： \n" + msgInfo.toString());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fun2( SGPlayerProto.S2C_MasterTrainAccept msgInfo) {
        tvResult.setText("主将培养接受： \n" + msgInfo.toString());
    }

    @OnClick({R.id.btn_gm, R.id.btn_red, R.id.btn_edit_name, R.id.btn_edit_avatar, R.id.btn_change_consume_remind,
            R.id.btn_change_equip_skill, R.id.btn_base_info, R.id.btn_setting_board, R.id.btn_get_instance_challenge_times,
    R.id.btn_get_task_list, R.id.btn_cdk, R.id.btn_rank_list, R.id.btn_master_train, R.id.btn_master_train_accept})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_gm:
                new InputTextDialog(this, "type;id;num") {
                    @Override
                    public void sendText(String text) {
                        SGSystemProto.C2S_GMCmd.Builder request = SGSystemProto.C2S_GMCmd.newBuilder();
                        if("full".equals(text)){
                            request.setCmd(Config.fullAccount);
                        }else {
                            request.setCmd(text);
                        }
                        SendUtils.sendMsg(PlayerInfoActivity.this, SGMainProto.E_MSG_ID.MsgID_System_GMCmd_VALUE, request.build().toByteArray());
                    }
                }.show();
                break;
            case R.id.btn_red:
                SendUtils.sendMsg(PlayerInfoActivity.this, SGMainProto.E_MSG_ID.MsgID_Player_RedPointRemind_VALUE, null);
                break;
            case R.id.btn_edit_name:
                new InputTextDialog(this, "name") {
                    @Override
                    public void sendText(String text) {
                        SGPlayerProto.C2S_EditPlayerName.Builder request = SGPlayerProto.C2S_EditPlayerName.newBuilder();
                        request.setPlayerName(text);
                        SendUtils.sendMsg(PlayerInfoActivity.this, SGMainProto.E_MSG_ID.MsgID_Player_EditPlayerName_VALUE, request.build().toByteArray());
                    }
                }.show();
                break;
            case R.id.btn_edit_avatar:
                new InputTextDialog(this, "avatar") {
                    @Override
                    public void sendText(String text) {
                        SGPlayerProto.C2S_EditPlayerAvatar.Builder request = SGPlayerProto.C2S_EditPlayerAvatar.newBuilder();
                        request.setPlayerAvatar(text);
                        SendUtils.sendMsg(PlayerInfoActivity.this, SGMainProto.E_MSG_ID.MsgID_Player_EditPlayerAvatar_VALUE, request.build().toByteArray());
                    }
                }.show();
                break;
            case R.id.btn_change_consume_remind:
                new InputTextDialog(this, "key;isShow") {
                    @Override
                    public void sendText(String text) {
                        SGPlayerProto.C2S_ChangeConsumeRemindStatus.Builder request = SGPlayerProto.C2S_ChangeConsumeRemindStatus.newBuilder();
                        int key = 0;
                        boolean isShow = false;
                        try {
                            key = Integer.valueOf(text.split(";")[0]);
                            isShow = "0".equals(text.split(";")[1]) ? false : true;
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                        request.setKey(key);
                        request.setIsShowRemind(isShow);
                        SendUtils.sendMsg(PlayerInfoActivity.this, SGMainProto.E_MSG_ID.MsgID_Player_ChangeConsumeRemindStatus_VALUE, request.build().toByteArray());
                    }
                }.show();
                break;
            case R.id.btn_change_equip_skill:
                new InputTextDialog(this, "removeSkillId;addSkillId") {
                    @Override
                    public void sendText(String text) {
                        SGPlayerProto.S2C_ChangeEquippedSkill.Builder request = SGPlayerProto.S2C_ChangeEquippedSkill.newBuilder();

                        try {
                            request.setRemoveSkillId(Integer.valueOf(text.split(";")[0]));
                            request.setAddSkillId(Integer.valueOf(text.split(";")[1]));
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                        SendUtils.sendMsg(PlayerInfoActivity.this, SGMainProto.E_MSG_ID.MsgID_Player_ChangeEquippedSkill_VALUE, request.build().toByteArray());
                    }
                }.show();
                break;
            case R.id.btn_base_info:
                SendUtils.sendMsg(PlayerInfoActivity.this, SGMainProto.E_MSG_ID.MsgID_Player_SynBaseData_VALUE, null);
                break;
            case R.id.btn_setting_board:
                SendUtils.sendMsg(PlayerInfoActivity.this, SGMainProto.E_MSG_ID.MsgID_Player_SettingBoardInit_VALUE, null);
                break;
            case R.id.btn_get_instance_challenge_times:
                SendUtils.sendMsg(PlayerInfoActivity.this, SGMainProto.E_MSG_ID.MsgID_Player_GetInstanceRemainChallengeTime_VALUE, null);
                break;
            case R.id.btn_get_task_list:
                new InputTextDialog(this, "taskType(1:主线，2：支线，3:日常；4:攻城)") {
                    @Override
                    public void sendText(String text) {
                        SGTaskProto.C2S_TaskList.Builder request = SGTaskProto.C2S_TaskList.newBuilder();
                        request.setTypeValue(StringUtils.stringToInt(text));
                        SendUtils.sendMsg(PlayerInfoActivity.this, SGMainProto.E_MSG_ID.MsgID_Task_TaskList_VALUE, request.build().toByteArray());
                    }
                }.show();
                break;
            case R.id.btn_cdk:
                new InputTextDialog(this, "cdk") {
                    @Override
                    public void sendText(String text) {
                        SGPlayerProto.C2S_CdkUse.Builder request = SGPlayerProto.C2S_CdkUse.newBuilder();
                        request.setCdkCode(text);
                        SendUtils.sendMsg(PlayerInfoActivity.this, SGMainProto.E_MSG_ID.MsgID_Player_CdkUse_VALUE, request.build().toByteArray());
                    }
                }.show();
            case R.id.btn_rank_list:
                new InputTextDialog(this, "1竞技场，2战力，3充值，4副本，5远征") {
                    @Override
                    public void sendText(String text) {
                        SGPlayerProto.C2S_RankList.Builder request = SGPlayerProto.C2S_RankList.newBuilder();
                        request.setRankTypeValue(StringUtils.stringToInt(text));
                        SendUtils.sendMsg(PlayerInfoActivity.this, SGMainProto.E_MSG_ID.MsgID_Player_RankList_VALUE, request.build().toByteArray());
                    }
                }.show();
                break;
            case R.id.btn_master_train:
                new InputTextDialog(this, "0获取上次记录， 1普通培养， 2高级培养") {
                    @Override
                    public void sendText(String text) {
                        SGPlayerProto.C2S_MasterTrain.Builder request = SGPlayerProto.C2S_MasterTrain.newBuilder();
                        request.setTrainTypeValue(Integer.valueOf(text));
                        SendUtils.sendMsg(PlayerInfoActivity.this, SGMainProto.E_MSG_ID.MsgID_Player_MasterTrain_VALUE, request.build().toByteArray());
                    }
                }.show();
                break;
            case R.id.btn_master_train_accept:
                SendUtils.sendMsg(PlayerInfoActivity.this, SGMainProto.E_MSG_ID.MsgID_Player_MasterTrainAccept_VALUE, null);
                break;
            default:
                break;
        }
    }
}
