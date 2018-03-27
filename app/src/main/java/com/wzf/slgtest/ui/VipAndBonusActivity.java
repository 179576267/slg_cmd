package com.wzf.slgtest.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.douqu.game.core.protobuf.SGCommonProto;
import com.douqu.game.core.protobuf.SGMainProto;
import com.douqu.game.core.protobuf.SGPlayerProto;
import com.douqu.game.core.protobuf.SGSystemProto;
import com.wzf.slgtest.MyApplication;
import com.wzf.slgtest.R;
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

public class VipAndBonusActivity extends BaseActivity {
    @Bind(R.id.tv_object_index)
    TextView tvObjectIndex;
    @Bind(R.id.tv_result)
    TextView tvResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip_bonus);
        tvObjectIndex.setText(MyApplication.getAppInstance().objecIndex);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fun2(SGPlayerProto.S2C_RechargeInit msgInfo) {
        tvResult.setText("充值面板初始化： \n" + msgInfo.toString());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fun2(SGPlayerProto.S2C_RechargeCheck msgInfo) {
        tvResult.setText("充值检测： \n" + msgInfo.toString());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fun2(SGPlayerProto.S2C_BuyVipGiftBag msgInfo) {
        tvResult.setText("vip礼包购买返回： \n" + msgInfo.toString());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fun2(SGPlayerProto.S2C_ReceiveMouthCardReward msgInfo) {
        tvResult.setText("月卡每日奖励领取： \n" + msgInfo.toString());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fun2(SGPlayerProto.S2C_DailySignReward msgInfo) {
        tvResult.setText("每日签到： \n" + msgInfo.toString());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fun2(SGPlayerProto.S2C_BuyFund msgInfo) {
        tvResult.setText("基金购买： \n" + msgInfo.toString());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fun2(SGPlayerProto.S2C_ReceiveFundReward msgInfo) {
        tvResult.setText("基金奖励领取： \n" + msgInfo.toString());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fun2(SGPlayerProto.S2C_ReceiveFirstRechargeReward msgInfo) {
        tvResult.setText("首冲礼包领取成功： \n" + msgInfo.toString());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fun2(SGPlayerProto.S2C_BonusBoardInit msgInfo) {
        tvResult.setText("福利面板返回： \n" + msgInfo.toString());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fun2(SGPlayerProto.S2C_LoginTimesRewardInit msgInfo) {
        tvResult.setText("累计登录初始化返回： \n" + msgInfo.toString());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fun2(SGPlayerProto.S2C_NewServerActivityInit msgInfo) {
        tvResult.setText("累计登录奖励领取返回： \n" + msgInfo.toString());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fun2(SGPlayerProto.S2C_ReceiveLoginTimesReward msgInfo) {
        tvResult.setText("新区活动返回： \n" + msgInfo.toString());
    }

    @OnClick({R.id.btn_recharge_init,R.id.btn_recharge, R.id.btn_vip_gift, R.id.btn_mouth_card_reward, R.id.btn_sign,
            R.id.btn_buy_open_fund, R.id.btn_open_fund_reward, R.id.btn_bonus_init, R.id.btn_receive_first_recharge_reward,
            R.id.btn_login_times_init, R.id.btn_receive_login_times, R.id.btn_new_server})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_recharge_init:
                SendUtils.sendMsg(VipAndBonusActivity.this, SGMainProto.E_MSG_ID.MsgID_Bonus_RechargeInit_VALUE, null);
                break;
            case R.id.btn_recharge:
                new InputTextDialog(this, "rechargeId") {
                    @Override
                    public void sendText(String text) {
                        SGPlayerProto.C2S_RechargeCheck.Builder request = SGPlayerProto.C2S_RechargeCheck.newBuilder();
                        request.setRecharId(StringUtils.stringToInt(text));
                        SendUtils.sendMsg(VipAndBonusActivity.this, SGMainProto.E_MSG_ID.MsgID_Bonus_RechargeCheck_VALUE, request.build().toByteArray());
                    }
                }.show();
                break;
            case R.id.btn_vip_gift:
                new InputTextDialog(this, "vipLv") {
                    @Override
                    public void sendText(String text) {
                        SGPlayerProto.C2S_BuyVipGiftBag.Builder request = SGPlayerProto.C2S_BuyVipGiftBag.newBuilder();
                        request.setVipLv(StringUtils.stringToInt(text));
                        SendUtils.sendMsg(VipAndBonusActivity.this, SGMainProto.E_MSG_ID.MsgID_Bonus_BuyVipGiftBag_VALUE, request.build().toByteArray());
                    }
                }.show();
                break;
            case R.id.btn_mouth_card_reward:
                new InputTextDialog(this, "rechargeId") {
                    @Override
                    public void sendText(String text) {
                        SGPlayerProto.C2S_ReceiveMouthCardReward.Builder request = SGPlayerProto.C2S_ReceiveMouthCardReward.newBuilder();
                        request.setRecharId(StringUtils.stringToInt(text));
                        SendUtils.sendMsg(VipAndBonusActivity.this, SGMainProto.E_MSG_ID.MsgID_Bonus_ReceiveMouthCardReward_VALUE, request.build().toByteArray());
                    }
                }.show();
                break;
            case R.id.btn_sign:
                SendUtils.sendMsg(VipAndBonusActivity.this, SGMainProto.E_MSG_ID.MsgID_Bonus_DailySignReward_VALUE, null);
                break;

            case R.id.btn_buy_open_fund:
                new InputTextDialog(this, "fundType（1.开服基金）") {
                    @Override
                    public void sendText(String text) {
                        SGPlayerProto.C2S_BuyFund.Builder request = SGPlayerProto.C2S_BuyFund.newBuilder();
                        request.setFundType(SGCommonProto.E_FUND_TYPE.forNumber(StringUtils.stringToInt(text)));
                        SendUtils.sendMsg(VipAndBonusActivity.this, SGMainProto.E_MSG_ID.MsgID_Bonus_BuyFund_VALUE, request.build().toByteArray());
                    }
                }.show();
                break;

            case R.id.btn_open_fund_reward:
                new InputTextDialog(this, "fundType（1.开服基金 2新区基金 3累计充值）;rewardId") {
                    @Override
                    public void sendText(String text) {
                        SGPlayerProto.C2S_ReceiveFundReward.Builder request = SGPlayerProto.C2S_ReceiveFundReward.newBuilder();
                        String[] ids = text.split(";");
                        request.setFundType(SGCommonProto.E_FUND_TYPE.forNumber(StringUtils.stringToInt(ids[0])));
                        request.setRewardId(StringUtils.stringToInt(ids[1]));
                        SendUtils.sendMsg(VipAndBonusActivity.this, SGMainProto.E_MSG_ID.MsgID_Bonus_ReceiveFundReward_VALUE, request.build().toByteArray());
                    }
                }.show();
                break;
            case R.id.btn_bonus_init:
                SendUtils.sendMsg(VipAndBonusActivity.this, SGMainProto.E_MSG_ID.MsgID_Bonus_BonusBoardInit_VALUE, null);
                break;
            case R.id.btn_receive_first_recharge_reward:
                SendUtils.sendMsg(VipAndBonusActivity.this, SGMainProto.E_MSG_ID.MsgID_Bonus_ReceiveFirstRechargeReward_VALUE, null);
                break;
            case R.id.btn_login_times_init:
                SendUtils.sendMsg(VipAndBonusActivity.this, SGMainProto.E_MSG_ID.MsgID_Bonus_LoginTimesRewardInit_VALUE, null);
                break;
            case R.id.btn_receive_login_times:
                new InputTextDialog(this, "累计登录奖励id") {
                    @Override
                    public void sendText(String text) {
                        SGPlayerProto.C2S_ReceiveLoginTimesReward.Builder request = SGPlayerProto.C2S_ReceiveLoginTimesReward.newBuilder();
                        request.setRewardId(StringUtils.stringToInt(text));
                        SendUtils.sendMsg(VipAndBonusActivity.this, SGMainProto.E_MSG_ID.MsgID_Bonus_ReceiveLoginTimesReward_VALUE, request.build().toByteArray());
                    }
                }.show();
                break;
            case R.id.btn_new_server:
                SendUtils.sendMsg(VipAndBonusActivity.this, SGMainProto.E_MSG_ID.MsgID_Bonus_NewServerActivityInit_VALUE, null);
                break;
            default:
                break;
        }
    }
}
