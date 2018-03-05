package com.wzf.slgtest.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.douqu.game.core.protobuf.SGBagProto;
import com.douqu.game.core.protobuf.SGCommonProto;
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

public class BagCardInfoActivity extends BaseActivity {

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
    public void fun2(SGBagProto.S2C_UseProp msgInfo) {
        tvResult.setText("道具使用返回： \n" + msgInfo);
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
    public void fun2(SGBagProto.S2C_CardAddExp msgInfo) {
        tvResult.setText("根据经验卷轴升级： \n" + msgInfo);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fun2(SGBagProto.S2C_CardSyn msgInfo) {
        tvResult.setText("卡牌合称： \n" + msgInfo);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fun2(SGBagProto.S2C_EquipIntensify msgInfo) {
        tvResult.setText("装备强化： \n" + msgInfo);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fun2(SGBagProto.S2C_AccessoryUp msgInfo) {
        tvResult.setText("饰品升级： \n" + msgInfo);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fun2(SGBagProto.S2C_AccessoryIntensify msgInfo) {
        tvResult.setText("饰品强化： \n" + msgInfo);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fun2(SGBagProto.S2C_CardFate msgInfo) {
        tvResult.setText("宿命激活： \n" + msgInfo);
    }



    @OnClick({R.id.btn_bg_detail, R.id.btn_bg_cardlv, R.id.btn_use_prop, R.id.btn_card_add_exp_by_porp,
    R.id.btn_card_merge, R.id.btn_equip_strong, R.id.btn_accessory_lv_up, R.id.btn_accessory_strong, R.id.btn_fate_active})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_bg_detail:
                new InputTextDialog(this, "carId") {
                    @Override
                    public void sendText(String text) {
                        SGBagProto.C2S_CardDetail.Builder request = SGBagProto.C2S_CardDetail.newBuilder();
                        request.setCardId(Integer.valueOf(text));
                        SendUtils.sendMsg(BagCardInfoActivity.this, SGMainProto.E_MSG_ID.MsgID_Bag_CardDetail_VALUE, request.build().toByteArray());
                    }
                }.show();
                break;
            case R.id.btn_bg_cardlv:
                new InputTextDialog(this, "carId;needLevel") {
                    @Override
                    public void sendText(String text) {
                        String[] str = text.split(";");
                        SGBagProto.C2S_CardUpLv.Builder request = SGBagProto.C2S_CardUpLv.newBuilder();
                        request.setCardId(Integer.valueOf(str[0]));
                        request.setNeedLv(Integer.valueOf(str[1]));
                        SendUtils.sendMsg(BagCardInfoActivity.this, SGMainProto.E_MSG_ID.MsgID_Bag_CardUpLv_VALUE, request.build().toByteArray());
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
                        SendUtils.sendMsg(BagCardInfoActivity.this, SGMainProto.E_MSG_ID.MsgID_Bag_UseProp_VALUE, request.build().toByteArray());
                    }
                }.show();
                break;
            case R.id.btn_card_add_exp_by_porp:
                new InputTextDialog(this, "cardId(卡id);expPorpId(经验卷轴id);count(个数)") {
                    @Override
                    public void sendText(String text) {
                        String[] str = text.split(";");
                        SGBagProto.C2S_CardAddExp.Builder b = SGBagProto.C2S_CardAddExp.newBuilder();
                        b.setCardId(StringUtils.stringToInt(str[0])); //卡id
                        b.setExpId(StringUtils.stringToInt(str[1])); //经验卷轴id
                        b.setCount(StringUtils.stringToInt(str[2])); //个数
                        SendUtils.sendMsg(BagCardInfoActivity.this, SGMainProto.E_MSG_ID.MsgID_Bag_CardAddExp_VALUE, b.build().toByteArray());
                    }
                }.show();
                break;
            case R.id.btn_card_merge:
                new InputTextDialog(this, "synId") {
                    @Override
                    public void sendText(String text) {
                        //卡牌合称
                        SGBagProto.C2S_CardSyn.Builder b = SGBagProto.C2S_CardSyn.newBuilder();
                        b.setSynId(StringUtils.stringToInt(text));
                        SendUtils.sendMsg(BagCardInfoActivity.this, SGMainProto.E_MSG_ID.MsgID_Bag_CardSyn_VALUE, b.build().toByteArray());
                    }
                }.show();
                break;
            case R.id.btn_equip_strong:
                new InputTextDialog(this, "cardId(卡id);equipType(装备类型);count(强化次数)") {
                    @Override
                    public void sendText(String text) {
                        String[] str = text.split(";");
                        SGBagProto.C2S_EquipIntensify.Builder b = SGBagProto.C2S_EquipIntensify.newBuilder();
                        b.setCardId(StringUtils.stringToInt(str[0])); //卡id
                        b.setEquipType(SGCommonProto.E_EQUIP_TYPE.forNumber(StringUtils.stringToInt(str[1]))); //经验卷轴id
                        b.setCount(StringUtils.stringToInt(str[2])); //个数
                        SendUtils.sendMsg(BagCardInfoActivity.this, SGMainProto.E_MSG_ID.MsgID_Bag_EquipIntensify_VALUE, b.build().toByteArray());
                    }
                }.show();
                break;
            case R.id.btn_accessory_lv_up:
                new InputTextDialog(this, "cardId(卡id);accessoryType(饰品类型)") {
                    @Override
                    public void sendText(String text) {
                        String[] str = text.split(";");
                        SGBagProto.C2S_AccessoryUp.Builder b = SGBagProto.C2S_AccessoryUp.newBuilder();
                        b.setCardId(StringUtils.stringToInt(str[0])); //卡id
                        b.setAccessoryTypeValue(StringUtils.stringToInt(str[1])); //经验卷轴id
                        SendUtils.sendMsg(BagCardInfoActivity.this, SGMainProto.E_MSG_ID.MsgID_Bag_AccessoryUp_VALUE, b.build().toByteArray());
                    }
                }.show();
                break;
            case R.id.btn_accessory_strong:
                new InputTextDialog(this, "cardId(卡id);accessoryType(饰品类型)") {
                    @Override
                    public void sendText(String text) {
                        String[] str = text.split(";");
                        SGBagProto.C2S_AccessoryUp.Builder b = SGBagProto.C2S_AccessoryUp.newBuilder();
                        b.setCardId(StringUtils.stringToInt(str[0])); //卡id
                        b.setAccessoryTypeValue(StringUtils.stringToInt(str[1])); //经验卷轴id
                        SendUtils.sendMsg(BagCardInfoActivity.this, SGMainProto.E_MSG_ID.MsgID_Bag_AccessoryIntensify_VALUE, b.build().toByteArray());
                    }
                }.show();
                break;
            case R.id.btn_fate_active:
                new InputTextDialog(this, "cardId(卡id);fateId(宿命id)") {
                    @Override
                    public void sendText(String text) {
                        String[] str = text.split(";");
                        //宿命激活
                        SGBagProto.C2S_CardFate.Builder b = SGBagProto.C2S_CardFate.newBuilder();
                        b.setCardId(StringUtils.stringToInt(str[0])); //卡牌id
                        b.setFateId(StringUtils.stringToInt(str[1])); //宿命id
                        SendUtils.sendMsg(BagCardInfoActivity.this, SGMainProto.E_MSG_ID.MsgID_Bag_CardFate_VALUE, b.build().toByteArray());
                    }
                }.show();
                break;
            default:
                break;
        }
    }
}
