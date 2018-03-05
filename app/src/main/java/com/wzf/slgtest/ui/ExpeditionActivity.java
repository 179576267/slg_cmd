package com.wzf.slgtest.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.douqu.game.core.protobuf.SGChallengeProto;
import com.douqu.game.core.protobuf.SGCommonProto;
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
public class ExpeditionActivity extends BaseActivity {

    @Bind(R.id.tv_result)
    TextView tvResult;
    @Bind(R.id.tv_object_index)
    TextView tvObjectIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expedition);
        tvObjectIndex.setText(MyApplication.getAppInstance().objecIndex);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fun(SGChallengeProto.S2C_ExpeditionInit msgInfo) {
        tvResult.setText("远征初始化： \n" + msgInfo.toString());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fun(SGChallengeProto.S2C_ExpeditionChallengeRequset msgInfo) {
        tvResult.setText("远征挑战发起返回:" + msgInfo.toString());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fun(SGChallengeProto.S2C_ExpeditionFreshBoss msgInfo) {
        tvResult.setText("远征换BOSS:" + msgInfo.toString());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void notifyAlert(SGPlayerProto.S2C_StoreBuyTimes msgInfo) {
        tvResult.setText("挑战次数购买： \n" + msgInfo.toString());
    }

  @Subscribe(threadMode = ThreadMode.MAIN)
    public void notifyAlert(SGChallengeProto.S2C_ExpeditionCallBoss msgInfo) {
        tvResult.setText("召唤boss返回： \n" + msgInfo.toString());
    }

    @OnClick({R.id.btn_init, R.id.btn_challenge, R.id.btn_fresh, R.id.btn_buy_challenge_times, R.id.btn_call})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_init:
                new InputTextDialog(this, "0(不重置)") {
                    @Override
                    public void sendText(String text) {
                        SGChallengeProto.C2S_ExpeditionInit.Builder request = SGChallengeProto.C2S_ExpeditionInit.newBuilder();
                        request.setIsReset(!"0".equals(text));
                        SendUtils.sendMsg(ExpeditionActivity.this, SGMainProto.E_MSG_ID.MsgID_Expedition_Init_VALUE, request.build().toByteArray());
                    }
                }.show();
                break;
            case R.id.btn_challenge:
                SendUtils.sendMsg(ExpeditionActivity.this, SGMainProto.E_MSG_ID.MsgID_Expedition_ChallengeRequest_VALUE, null);
                break;
            case R.id.btn_fresh:
                SendUtils.sendMsg(ExpeditionActivity.this, SGMainProto.E_MSG_ID.MsgID_Expedition_FreshBoss_VALUE, null);
                break;
            case R.id.btn_buy_challenge_times:
                SGPlayerProto.C2S_StoreBuyTimes.Builder request = SGPlayerProto.C2S_StoreBuyTimes.newBuilder();
                request.setTimes(1);
                request.setType(SGCommonProto.E_BUY_TIMES_TYPE.BUY_TIMES_TYPE_EXPEDITION);
                SendUtils.sendMsg(ExpeditionActivity.this, SGMainProto.E_MSG_ID.MsgID_Store_BuyTimes_VALUE, request.build().toByteArray());
                break;
            case R.id.btn_call:
                SendUtils.sendMsg(ExpeditionActivity.this, SGMainProto.E_MSG_ID.MsgID_Expedition_CallBoss_VALUE, null);
                break;
            default:
                break;
        }
    }
}
