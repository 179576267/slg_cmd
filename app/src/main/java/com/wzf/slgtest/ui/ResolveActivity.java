package com.wzf.slgtest.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.douqu.game.core.protobuf.SGBagProto;
import com.douqu.game.core.protobuf.SGMainProto;
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
public class ResolveActivity extends BaseActivity {
    @Bind(R.id.tv_object_index)
    TextView tvObjectIndex;
    @Bind(R.id.tv_result)
    TextView tvResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resolve);
        tvObjectIndex.setText(MyApplication.getAppInstance().objecIndex);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fun(SGBagProto.S2C_RebirthCard msgInfo) {
        tvResult.setText("卡片重生返回： \n" + msgInfo.toString());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fun(SGBagProto.S2C_ResolveCardPreview msgInfo) {
        tvResult.setText("可分解卡牌预览： \n" + msgInfo.toString());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fun(SGBagProto.S2C_SoulResolve msgInfo) {
        tvResult.setText("英魂分解成功： \n" + msgInfo.toString());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fun(SGBagProto.S2C_EquipResolve msgInfo) {
        tvResult.setText("装备分解成功： \n" + msgInfo.toString());
    }


    @OnClick({R.id.btn_card_rebirth, R.id.btn_preview_resolve_card, R.id.btn_card_fragment_resolve,R.id.btn_card_resolve,  R.id.btn_equip_resolve})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_card_rebirth:
                new InputTextDialog(this, "carId") {
                    @Override
                    public void sendText(String text) {
                        SGBagProto.C2S_RebirthCard.Builder request = SGBagProto.C2S_RebirthCard.newBuilder();
                        request.setCardId(Integer.valueOf(text));
                        SendUtils.sendMsg(ResolveActivity.this, SGMainProto.E_MSG_ID.MsgID_Bag_RebirthCard_VALUE, request.build().toByteArray());
                    }
                }.show();
                break;
            case R.id.btn_preview_resolve_card:
                SendUtils.sendMsg(ResolveActivity.this, SGMainProto.E_MSG_ID.MsgID_Bag_ResolveCardPreview_VALUE, null);
                break;
            case R.id.btn_card_fragment_resolve:
                new InputTextDialog(this, "cardFragmentId") {
                    @Override
                    public void sendText(String text) {
                        String[] idString = text.split(";");
                        SGBagProto.C2S_SoulResolve.Builder request = SGBagProto.C2S_SoulResolve.newBuilder();
                        for(String s : idString){
                            request.addDebrisIds(StringUtils.stringToInt(s));
                        }
                        SendUtils.sendMsg(ResolveActivity.this, SGMainProto.E_MSG_ID.MsgID_Bag_SoulResolve_VALUE, request.build().toByteArray());
                    }
                }.show();
                break;
            case R.id.btn_card_resolve:
                new InputTextDialog(this, "cardId") {
                    @Override
                    public void sendText(String text) {
                        String[] idString = text.split(";");
                        SGBagProto.C2S_SoulResolve.Builder request = SGBagProto.C2S_SoulResolve.newBuilder();
                        for(String s : idString){
                            request.addCardIds(StringUtils.stringToInt(s));
                        }
                        SendUtils.sendMsg(ResolveActivity.this, SGMainProto.E_MSG_ID.MsgID_Bag_SoulResolve_VALUE, request.build().toByteArray());
                    }
                }.show();
                break;
            case R.id.btn_equip_resolve:
                new InputTextDialog(this, "equipId") {
                    @Override
                    public void sendText(String text) {
                        String[] idString = text.split(";");
                        SGBagProto.C2S_EquipResolve.Builder request = SGBagProto.C2S_EquipResolve.newBuilder();
                        for(String s : idString){
                            request.addPropIds(StringUtils.stringToInt(s));
                        }
                        SendUtils.sendMsg(ResolveActivity.this, SGMainProto.E_MSG_ID.MsgID_Bag_EquipResolve_VALUE, request.build().toByteArray());
                    }
                }.show();
                break;
            default:
                break;
        }
    }
}
