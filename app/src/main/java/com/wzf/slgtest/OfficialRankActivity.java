package com.wzf.slgtest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.douqu.game.core.protobuf.SGAreanProto;
import com.douqu.game.core.protobuf.SGCommonProto;
import com.douqu.game.core.protobuf.SGMainProto;
import com.douqu.game.core.protobuf.SGOfficialWarProto;
import com.douqu.game.core.protobuf.SGPlayerProto;
import com.douqu.game.core.protobuf.SGSystemProto;
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
public class OfficialRankActivity extends BaseActivity {

    @Bind(R.id.tv_object_index)
    TextView tvObjectIndex;
    @Bind(R.id.tv_result)
    TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official_rank);
        tvObjectIndex.setText(MyApplication.getAppInstance().objecIndex);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fun2(SGOfficialWarProto.S2C_InitInfo msgInfo) {
        tvResult.setText("初始化信息返回： \n" + msgInfo.toString());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fun2(SGOfficialWarProto.S2C_PreviewRank msgInfo) {
        tvResult.setText("查看官阶人员信息： \n" + msgInfo.toString());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fun2(SGOfficialWarProto.S2C_ChallengeRank msgInfo) {
        tvResult.setText("挑战官阶返回： \n" + msgInfo.toString());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fun3(SGOfficialWarProto.S2C_SweepRank msgInfo) {
        tvResult.setText("挑战官阶返回： \n" + msgInfo.toString());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fun3(SGOfficialWarProto.S2C_GetDailyReward msgInfo) {
        tvResult.setText("领取每日奖励返回： \n" + msgInfo.toString());
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fun3(SGOfficialWarProto.S2C_ExchangeReward msgInfo) {
        tvResult.setText("兑换奖励返回： \n" + msgInfo.toString());
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fun3(SGOfficialWarProto.S2C_RewardRecord msgInfo) {
        tvResult.setText("兑换奖励记录返回： \n" + msgInfo.toString());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fun3(SGOfficialWarProto.S2C_IntegralReward msgInfo) {
        tvResult.setText("积分兑换奖励记录返回： \n" + msgInfo.toString());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fun3(SGOfficialWarProto.S2C_IntegralRewardRecord msgInfo) {
        tvResult.setText("积分兑换奖励记录返回： \n" + msgInfo.toString());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void notifyAlert(SGPlayerProto.S2C_StoreBuyTimes msgInfo) {
        tvResult.setText("挑战次数购买： \n" + msgInfo.toString());
    }

    @OnClick({R.id.btn_init, R.id.btn_challenge, R.id.btn_preview_rank, R.id.btn_sweep, R.id.btn_daily,
            R.id.btn_reward, R.id.btn_reward_record, R.id.btn_integral_reward, R.id.btn_integral_reward_record,
            R.id.btn_buy_challenge_times})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_init:
                SendUtils.sendMsg(SGMainProto.E_MSG_ID.MsgID_OfficialWar_InitInfo_VALUE, null);
                break;
            case R.id.btn_preview_rank:
                new InputTextDialog(this, "targetRandId") {
                    @Override
                    public void sendText(String text) {
                        SGOfficialWarProto.C2S_PreviewRank.Builder request = SGOfficialWarProto.C2S_PreviewRank.newBuilder();
                        request.setOfficialRankId(StringUtils.stringToInt(text));
                        SendUtils.sendMsg(SGMainProto.E_MSG_ID.MsgID_OfficialWar_PreviewRank_VALUE, request.build().toByteArray());
                    }
                }.show();
                break;
            case R.id.btn_challenge:
                new InputTextDialog(this, "targetRandId;position") {
                    @Override
                    public void sendText(String text) {
                        String[] ids = text.split(";");
                        int targetRandId = StringUtils.stringToInt(ids[0]);
                        int position = StringUtils.stringToInt(ids[1]);
                        SGOfficialWarProto.C2S_ChallengeRank.Builder request = SGOfficialWarProto.C2S_ChallengeRank.newBuilder();
                        request.setOfficialRankId(targetRandId);
                        request.setPosition(position);
                        SendUtils.sendMsg(SGMainProto.E_MSG_ID.MsgID_OfficialWar_ChallengeRank_VALUE, request.build().toByteArray());
                    }
                }.show();
                break;
            case R.id.btn_sweep:
                new InputTextDialog(this, "targetRandId;position") {
                    @Override
                    public void sendText(String text) {
                        String[] ids = text.split(";");
                        int targetRandId = StringUtils.stringToInt(ids[0]);
                        int position = StringUtils.stringToInt(ids[1]);
                        SGOfficialWarProto.C2S_SweepRank.Builder request = SGOfficialWarProto.C2S_SweepRank.newBuilder();
                        request.setOfficialRankId(targetRandId);
                        request.setPosition(position);
                        SendUtils.sendMsg(SGMainProto.E_MSG_ID.MsgID_OfficialWar_SweepRank_VALUE, request.build().toByteArray());
                    }
                }.show();
                break;
            case R.id.btn_daily:
                SendUtils.sendMsg(SGMainProto.E_MSG_ID.MsgID_OfficialWar_GetDailyReward_VALUE, null);
                break;
            case R.id.btn_reward:
                new InputTextDialog(this, "rewardId") {
                    @Override
                    public void sendText(String text) {
                        SGOfficialWarProto.S2C_ExchangeReward.Builder request = SGOfficialWarProto.S2C_ExchangeReward.newBuilder();
                        request.setRewardId(StringUtils.stringToInt(text));
                        SendUtils.sendMsg(SGMainProto.E_MSG_ID.MsgID_OfficialWar_ExchangeReward_VALUE, request.build().toByteArray());
                    }
                }.show();
                break;
            case R.id.btn_reward_record:
                SendUtils.sendMsg(SGMainProto.E_MSG_ID.MsgID_OfficialWar_RewardRecord_VALUE, null);
                break;
            case R.id.btn_integral_reward:
                new InputTextDialog(this, "rewardId") {
                    @Override
                    public void sendText(String text) {
                        SGOfficialWarProto.S2C_IntegralReward.Builder request = SGOfficialWarProto.S2C_IntegralReward.newBuilder();
                        request.setRewardId(StringUtils.stringToInt(text));
                        SendUtils.sendMsg(SGMainProto.E_MSG_ID.MsgID_OfficialWar_IntegralReward_VALUE, request.build().toByteArray());
                    }
                }.show();
                break;
            case R.id.btn_integral_reward_record:
                SendUtils.sendMsg(SGMainProto.E_MSG_ID.MsgID_OfficialWar_IntegralRewardRecord_VALUE, null);
                break;

            case R.id.btn_buy_challenge_times:
                new InputTextDialog(this, "times") {
                    @Override
                    public void sendText(String text) {
                        SGPlayerProto.C2S_StoreBuyTimes.Builder request = SGPlayerProto.C2S_StoreBuyTimes.newBuilder();
                        request.setTimes(StringUtils.stringToInt(text));
                        request.setType(SGCommonProto.E_BUY_TIMES_TYPE.BUY_TIMES_TYPE_OFFICIAL_RANK);
                        SendUtils.sendMsg(SGMainProto.E_MSG_ID.MsgID_Store_BuyTimes_VALUE, request.build().toByteArray());
                    }
                }.show();
                break;

            default:
                break;

        }
    }
}
