package com.wzf.slgtest.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.douqu.game.core.protobuf.SGMainProto;
import com.douqu.game.core.protobuf.SGPlayerProto;
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
 * @author Administrator
 */
public class AltarLotteryActivity extends BaseActivity {
    @Bind(R.id.tv_object_index)
    TextView tvObjectIndex;
    @Bind(R.id.tv_result)
    TextView tvResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_altar);
        tvObjectIndex.setText(MyApplication.getAppInstance().objecIndex);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fun(SGPlayerProto.S2C_AltarInit msgInfo) {
        tvResult.setText("祭祀初始化： \n" + msgInfo.toString());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fun(SGPlayerProto.S2C_Sacrifice msgInfo) {
        tvResult.setText("祭祀结果： \n" + msgInfo.toString());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fun(SGPlayerProto.S2C_LotteryInit msgInfo) {
        tvResult.setText("抽奖初始化： \n" + msgInfo.toString());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fun(SGPlayerProto.S2C_LotteryClick msgInfo) {
        tvResult.setText("抽奖结果： \n" + msgInfo.toString());
    }

    @OnClick({R.id.btn_init, R.id.btn_altar, R.id.btn_lottery_init, R.id.btn_lottery_get})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_init:
                SendUtils.sendMsg(this, SGMainProto.E_MSG_ID.MsgID_Altar_Init_VALUE, null);
                break;
            case R.id.btn_altar:
                new InputTextDialog(this, "type") {
                    @Override
                    public void sendText(String text) {
                        SGPlayerProto.C2S_Sacrifice.Builder request = SGPlayerProto.C2S_Sacrifice.newBuilder();
                        request.setId(StringUtils.stringToInt(text));
                        SendUtils.sendMsg(AltarLotteryActivity.this, SGMainProto.E_MSG_ID.MsgID_Altar_Sacrifice_VALUE, request.build().toByteArray());
                    }
                }.show();
                break;
            case R.id.btn_lottery_init:
                SendUtils.sendMsg(AltarLotteryActivity.this, SGMainProto.E_MSG_ID.MsgID_Pub_LotteryInit_VALUE, null);
                break;
            case R.id.btn_lottery_get:
                new InputTextDialog(this, "type(1初级抽,2高级抽));buyType(1单次抽,2十连抽)") {
                    @Override
                    public void sendText(String text) {
                        String[] ids = text.split(";");
                        int type = StringUtils.stringToInt(ids[0]);
                        int buyType = StringUtils.stringToInt(ids[1]);
                        SGPlayerProto.C2S_LotteryClick.Builder c = SGPlayerProto.C2S_LotteryClick.newBuilder();
                        c.setType(type); //抽奖类型1：普通抽2：10连抽（）
                        c.setBuyType(buyType); //购买单:1 多次:2
                        SendUtils.sendMsg(AltarLotteryActivity.this, SGMainProto.E_MSG_ID.MsgID_Pub_LotteryClick_VALUE, c.build().toByteArray());
                    }
                }.show();
                break;
            default:
                break;
        }
    }
}
