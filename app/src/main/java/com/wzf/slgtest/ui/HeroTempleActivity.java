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
public class HeroTempleActivity extends BaseActivity {

    @Bind(R.id.tv_result)
    TextView tvResult;
    @Bind(R.id.tv_object_index)
    TextView tvObjectIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hero_temple);
        tvObjectIndex.setText(MyApplication.getAppInstance().objecIndex);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fun(SGChallengeProto.S2C_HeroTempleInitInfo msgInfo) {
        tvResult.setText("英雄圣殿初始化： \n" + msgInfo.toString());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fun(SGChallengeProto.S2C_HeroTempleChallenge msgInfo) {
        tvResult.setText("英雄圣殿挑战发起返回:" + msgInfo.toString());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fun(SGChallengeProto.S2C_HeroTempleSweep msgInfo) {
        tvResult.setText("英雄圣殿扫荡返回:" + msgInfo.toString());
    }

    @OnClick({R.id.btn_init, R.id.btn_challenge, R.id.btn_sweep})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_init:
                SendUtils.sendMsg(HeroTempleActivity.this, SGMainProto.E_MSG_ID.MsgID_HeroTemple_InitInfo_VALUE, null);
                break;
            case R.id.btn_challenge:
                new InputTextDialog(this, "targetLevelId") {
                    @Override
                    public void sendText(String text) {
                        SGChallengeProto.C2S_HeroTempleChallenge.Builder request = SGChallengeProto.C2S_HeroTempleChallenge.newBuilder();
                        request.setTargetLevelId(StringUtils.stringToInt(text));
                        SendUtils.sendMsg(HeroTempleActivity.this, SGMainProto.E_MSG_ID.MsgID_HeroTemple_Challenge_VALUE, request.build().toByteArray());
                    }
                }.show();
                break;
            case R.id.btn_sweep:
                new InputTextDialog(this, "targetLevelId") {
                    @Override
                    public void sendText(String text) {
                        SGChallengeProto.C2S_HeroTempleSweep.Builder request = SGChallengeProto.C2S_HeroTempleSweep.newBuilder();
                        request.setTargetLevelId(StringUtils.stringToInt(text));
                        SendUtils.sendMsg(HeroTempleActivity.this,SGMainProto.E_MSG_ID.MsgID_HeroTemple_Sweep_VALUE, request.build().toByteArray());
                    }
                }.show();
                break;
            default:
                break;
        }
    }
}
