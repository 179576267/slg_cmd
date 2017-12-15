package com.wzf.slgtest;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.douqu.game.core.protobuf.SGBagProto;
import com.douqu.game.core.protobuf.SGMainProto;
import com.douqu.game.core.protobuf.SGPlayerProto;
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
public class AltarActivity extends BaseActivity {
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

    @OnClick({R.id.btn_init, R.id.btn_altar})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_init:
                SendUtils.sendMsg(SGMainProto.E_MSG_ID.MsgID_Altar_Init_VALUE, null);
                break;
            case R.id.btn_altar:
                new InputTextDialog(this, "type") {
                    @Override
                    public void sendText(String text) {
                        SGPlayerProto.C2S_Sacrifice.Builder request = SGPlayerProto.C2S_Sacrifice.newBuilder();
                        request.setId(StringUtils.stringToInt(text));
                        SendUtils.sendMsg(SGMainProto.E_MSG_ID.MsgID_Altar_Sacrifice_VALUE, request.build().toByteArray());
                    }
                }.show();
                break;
            default:
                break;
        }
    }
}
