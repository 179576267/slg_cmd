package com.wzf.slgtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.douqu.game.core.protobuf.SGBagProto;
import com.douqu.game.core.protobuf.SGMainProto;
import com.douqu.game.core.protobuf.SGPlayerProto;
import com.douqu.game.core.protobuf.SGSystemProto;
import com.wzf.slgtest.utils.BaseActivity;
import com.wzf.slgtest.utils.InputTextDialog;
import com.wzf.slgtest.utils.SendUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.OnClick;

public class BagInfoActivity extends BaseActivity {

    @Bind(R.id.tv_object_index)
    TextView tvObjectIndex;
    @Bind(R.id.tv_result)
    TextView tvResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bag_info);
        tvObjectIndex.setText(MyApplication.getAppInstance().objecIndex);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fun2(SGBagProto.S2C_CardDetail msgInfo) {
        tvResult.setText("卡牌详情返回： \n" + msgInfo.toString());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fun2(SGBagProto.S2C_CardUpLv msgInfo) {
        tvResult.setText("卡牌升级： \n" + msgInfo);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fun2(SGBagProto.S2C_UseProp msgInfo) {
        tvResult.setText("道具使用返回： \n" + msgInfo);
    }


    @OnClick({R.id.btn_bg_detail, R.id.btn_bg_cardlv, R.id.btn_use_prop})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_bg_detail:
                new InputTextDialog(this, "carId") {
                    @Override
                    public void sendText(String text) {
                        SGBagProto.C2S_CardDetail.Builder request = SGBagProto.C2S_CardDetail.newBuilder();
                        request.setCardId(Integer.valueOf(text));
                        SendUtils.sendMsg(SGMainProto.E_MSG_ID.MsgID_Bag_CardDetail_VALUE, request.build().toByteArray());
                    }
                }.show();
                break;
            case R.id.btn_bg_cardlv:
                new InputTextDialog(this, "carId;level") {
                    @Override
                    public void sendText(String text) {
                        String[] str = text.split(";");
                        SGBagProto.C2S_CardUpLv.Builder request = SGBagProto.C2S_CardUpLv.newBuilder();
                        request.setCardId(Integer.valueOf(str[0]));
                        request.setNeedLv(Integer.valueOf(str[1]));
                        SendUtils.sendMsg(SGMainProto.E_MSG_ID.MsgID_Bag_CardUpLv_VALUE, request.build().toByteArray());
                    }
                }.show();
                break;
            case R.id.btn_use_prop:
                new InputTextDialog(this, "propId;count") {
                    @Override
                    public void sendText(String text) {
                        String[] str = text.split(";");
                        SGBagProto.C2S_UseProp.Builder request = SGBagProto.C2S_UseProp.newBuilder();
                        request.setPropId(Integer.valueOf(str[0]));
                        request.setCount(Integer.valueOf(str[1]));
                        SendUtils.sendMsg(SGMainProto.E_MSG_ID.MsgID_Bag_UseProp_VALUE, request.build().toByteArray());
                    }
                }.show();
                break;
            default:
                break;
        }
    }
}
