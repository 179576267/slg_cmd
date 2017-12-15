package com.wzf.slgtest;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.douqu.game.core.protobuf.SGAreanProto;
import com.douqu.game.core.protobuf.SGCommonProto;
import com.douqu.game.core.protobuf.SGMainProto;
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

public class ArenaActivity extends BaseActivity {

    @Bind(R.id.tv_result)
    TextView tvResult;
    @Bind(R.id.tv_object_index)
    TextView tvObjectIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arena);
        tvObjectIndex.setText(MyApplication.getAppInstance().objecIndex);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void arenaInfoWithMe(SGAreanProto.S2C_ArenaGetArenaInfo msgInfo) {
        tvResult.setText("查看我的竞技场排名： \n" + msgInfo.toString());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void previewRank(SGAreanProto.S2C_ArenaPreviewRank msgInfo) {
        tvResult.setText("竞技场前100排名： \n" + msgInfo.toString());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void arenaChallenge(SGAreanProto.S2C_ArenaChallenge msgInfo) {
        tvResult.setText("挑战结果： \n" + msgInfo.toString());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEveryDayReward(SGAreanProto.S2C_ArenaGetDailyReward msgInfo) {
        tvResult.setText("每日奖领取： \n" + msgInfo.toString());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void exchangeReward(SGAreanProto.S2C_ArenaExchangeReward msgInfo) {
        tvResult.setText("兑换结果： \n" + msgInfo.toString());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void rewardRecord(SGAreanProto.S2C_ArenaRewardRecord msgInfo) {
        tvResult.setText("兑换记录： \n" + msgInfo.toString());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void notifyAlert(SGAreanProto.S2C_ArenaSweep msgInfo) {
        tvResult.setText("扫荡玩家： \n" + msgInfo.toString());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void notifyAlert(SGPlayerProto.S2C_StoreBuyTimes msgInfo) {
        tvResult.setText("挑战次数购买： \n" + msgInfo.toString());
    }



    @OnClick({R.id.btn_getRank, R.id.btn_getTopRank, R.id.btn_arenaChallenge, R.id.btn_getEveryDayReward,
            R.id.btn_rewardRecord, R.id.btn_reward, R.id.btn_sweep, R.id.btn_buy_challenge_times})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_getRank:
                SendUtils.sendMsg(SGMainProto.E_MSG_ID.MsgID_Arena_GetArenaInfo_VALUE, null);
                break;
            case R.id.btn_getTopRank:
                SendUtils.sendMsg(SGMainProto.E_MSG_ID.MsgID_Arena_PreviewRank_VALUE, null);
                break;
            case R.id.btn_arenaChallenge:
                new InputTextDialog(this, "targetRand") {
                    @Override
                    public void sendText(String text) {
                        SGAreanProto.C2S_ArenaChallenge.Builder request = SGAreanProto.C2S_ArenaChallenge.newBuilder();
                        request.setTargetRank(StringUtils.stringToInt(text));
                        SendUtils.sendMsg(SGMainProto.E_MSG_ID.MsgID_Arena_Challenge_VALUE, request.build().toByteArray());
                    }
                }.show();
                break;
            case R.id.btn_getEveryDayReward:
                SendUtils.sendMsg(SGMainProto.E_MSG_ID.MsgID_Arena_GetDailyReward_VALUE, null);
                break;
            case R.id.btn_rewardRecord:
                SendUtils.sendMsg(SGMainProto.E_MSG_ID.MsgID_Arena_RewardRecord_VALUE, null);
                break;
            case R.id.btn_reward:
                new InputTextDialog(this, "rewardId") {
                    @Override
                    public void sendText(String text) {
                        SGAreanProto.C2S_ArenaExchangeReward.Builder request = SGAreanProto.C2S_ArenaExchangeReward.newBuilder();
                        request.setRewardId(StringUtils.stringToInt(text));
                        SendUtils.sendMsg(SGMainProto.E_MSG_ID.MsgID_Arena_ExchangeReward_VALUE, request.build().toByteArray());
                    }
                }.show();

                break;
            case R.id.btn_sweep:
                new InputTextDialog(this, "targetRand") {
                    @Override
                    public void sendText(String text) {
                        SGAreanProto.C2S_ArenaSweep.Builder request = SGAreanProto.C2S_ArenaSweep.newBuilder();
                        request.setTargetRank(StringUtils.stringToInt(text));
                        SendUtils.sendMsg(SGMainProto.E_MSG_ID.MsgID_Arena_Sweep_VALUE, request.build().toByteArray());
                    }
                }.show();
                break;
            case R.id.btn_buy_challenge_times:
                new InputTextDialog(this, "times") {
                    @Override
                    public void sendText(String text) {
                        SGPlayerProto.C2S_StoreBuyTimes.Builder request = SGPlayerProto.C2S_StoreBuyTimes.newBuilder();
                        request.setTimes(StringUtils.stringToInt(text));
                        request.setType(SGCommonProto.E_BUY_TIMES_TYPE.BUY_TIMES_TYPE_ARENA);
                        SendUtils.sendMsg(SGMainProto.E_MSG_ID.MsgID_Store_BuyTimes_VALUE, request.build().toByteArray());
                    }
                }.show();
                break;
            default:
                break;
        }
    }
}
