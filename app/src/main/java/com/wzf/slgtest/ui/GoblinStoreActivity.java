package com.wzf.slgtest.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.douqu.game.core.protobuf.SGChallengeProto.*;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Administrator
 */
public class GoblinStoreActivity extends BaseActivity {

    @Bind(R.id.tv_result)
    TextView tvResult;
    @Bind(R.id.tv_object_index)
    TextView tvObjectIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goblin_store);
        tvObjectIndex.setText(MyApplication.getAppInstance().objecIndex);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fun(SGPlayerProto.S2C_StoreInitInfo msgInfo) {
        tvResult.setText("地精商店刷新结果： \n" + msgInfo.toString());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fun(SGPlayerProto.S2C_StoreBuyGoods msgInfo) {
        tvResult.setText(tvResult.getText() + "\n" + "商品购买返回结果： \n" + msgInfo.toString());
    }

    @OnClick({R.id.btn_init, R.id.btn_refresh, R.id.btn_exchange})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_init:
                new InputTextDialog(this, "storeType(1地精商店，2竞技场商店,4远征商店,5钻石商店)") {
                    @Override
                    public void sendText(String text) {
                        try {
                            SGPlayerProto.C2S_StoreInitInfo.Builder request1 = SGPlayerProto.C2S_StoreInitInfo.newBuilder();
                            request1.setIsFresh(false);
                            request1.setType(SGCommonProto.E_STORE_TYPE.forNumber(StringUtils.stringToInt(text)));
                            SendUtils.sendMsg(GoblinStoreActivity.this, SGMainProto.E_MSG_ID.MsgID_Store_InitInfo_VALUE, request1.build().toByteArray());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.show();

                break;
            case R.id.btn_refresh:
                new InputTextDialog(this, "storeType(1地精商店，2竞技场商店，4远征商店，5钻石商店)") {
                    @Override
                    public void sendText(String text) {
                        try {
                            SGPlayerProto.C2S_StoreInitInfo.Builder request2 = SGPlayerProto.C2S_StoreInitInfo.newBuilder();
                            request2.setIsFresh(true);
                            request2.setType(SGCommonProto.E_STORE_TYPE.forNumber(StringUtils.stringToInt(text)));
                            SendUtils.sendMsg(GoblinStoreActivity.this, SGMainProto.E_MSG_ID.MsgID_Store_InitInfo_VALUE, request2.build().toByteArray());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.show();
                break;
            case R.id.btn_exchange:
                new InputTextDialog(this, "storeType;goodsId;count") {
                    @Override
                    public void sendText(String text) {
                        try {
                            SGPlayerProto.C2S_StoreBuyGoods.Builder request = SGPlayerProto.C2S_StoreBuyGoods.newBuilder();
                            String [] strings = text.split(";");
                            request.setType(SGCommonProto.E_STORE_TYPE.forNumber(StringUtils.stringToInt(strings[0])));
                            request.setGoodsId(StringUtils.stringToInt(strings[1]));
                            request.setCount(StringUtils.stringToInt(strings[2]));
                            SendUtils.sendMsg(GoblinStoreActivity.this, SGMainProto.E_MSG_ID.MsgID_Store_BuyGoods_VALUE, request.build().toByteArray());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.show();
                break;
            default:
                break;
        }
    }
}
