package com.wzf.slgtest.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.douqu.game.core.protobuf.SGBagProto;
import com.douqu.game.core.protobuf.SGCommonProto;
import com.douqu.game.core.protobuf.SGMainProto;
import com.douqu.game.core.protobuf.SGMajorProto;
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
public class MajorActivity extends BaseActivity {
    @Bind(R.id.tv_object_index)
    TextView tvObjectIndex;
    @Bind(R.id.tv_result)
    TextView tvResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_major);
        tvObjectIndex.setText(MyApplication.getAppInstance().objecIndex);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fun(SGMajorProto.S2C_MajorBattle msgInfo) {
        tvResult.setText("请求战斗返回： \n" + msgInfo.toString());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fun(SGMajorProto.S2C_MajorCollectInit msgInfo) {
        tvResult.setText("采集初始化： \n" + msgInfo.toString());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fun(SGMajorProto.S2C_MajorCollectStart msgInfo) {
        tvResult.setText("采集开始： \n" + msgInfo.toString());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fun(SGBagProto.S2C_SoulResolve msgInfo) {
        tvResult.setText("英魂分解成功： \n" + msgInfo.toString());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fun(SGMajorProto.S2C_MajorMerge msgInfo) {
        tvResult.setText("合成的返回： \n" + msgInfo.toString());
    }


    @OnClick({R.id.btn_major_battle, R.id.btn_collect_init,R.id.btn_collect_start, R.id.btn_receiver_collect_production,
    R.id.btn_refining})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_major_battle:
                new InputTextDialog(this, "专业类型（1锻造，2挖掘，3采集，4炼制）;战斗类型（1抢夺，2复仇）;流水号") {
                    @Override
                    public void sendText(String text) {
                        SGMajorProto.C2S_MajorBattle.Builder request = SGMajorProto.C2S_MajorBattle.newBuilder();
                        String[] ss = text.split(";");
                        request.setMajorTypeValue(Integer.valueOf(ss[0]));
                        request.setBattleTypeValue(Integer.valueOf(ss[1]));
                        request.setObjectIndex(ss[2]);
                        SendUtils.sendMsg(MajorActivity.this, SGMainProto.E_MSG_ID.MsgID_Major_Battle_VALUE, request.build().toByteArray());
                    }
                }.show();
                break;
            case R.id.btn_collect_init:
                new InputTextDialog(this, "专业类型（1锻造，2挖掘，3采集，4炼制）") {
                    @Override
                    public void sendText(String text) {
                        SGMajorProto.C2S_MajorCollectInit.Builder request = SGMajorProto.C2S_MajorCollectInit.newBuilder();
                        request.setTypeValue(Integer.valueOf(text));
                        SendUtils.sendMsg(MajorActivity.this, SGMainProto.E_MSG_ID.MsgID_Major_CollectInit_VALUE, request.build().toByteArray());
                    }
                }.show();
                break;
            case R.id.btn_collect_start:
                new InputTextDialog(this, "专业类型（1锻造，2挖掘，3采集，4炼制）;时间; 采集类型（1普通，2高级）") {
                    @Override
                    public void sendText(String text) {
                        String[] idString = text.split(";");
                        SGMajorProto.C2S_MajorCollectStart.Builder request = SGMajorProto.C2S_MajorCollectStart.newBuilder();
                        request.setMajorTypeValue(StringUtils.stringToInt(idString[0]));
                        request.setHours(StringUtils.stringToInt(idString[1]));
                        request.setCollecTypeValue(StringUtils.stringToInt(idString[2]));
                        SendUtils.sendMsg(MajorActivity.this, SGMainProto.E_MSG_ID.MsgID_Major_CollectStart_VALUE, request.build().toByteArray());
                    }
                }.show();
                break;
            case R.id.btn_receiver_collect_production:
                new InputTextDialog(this, "专业类型（1锻造，2挖掘，3采集，4炼制）") {
                    @Override
                    public void sendText(String text) {
                        SGMajorProto.C2S_MajorCollectProductionReceiver.Builder request = SGMajorProto.C2S_MajorCollectProductionReceiver.newBuilder();
                        request.setMajorTypeValue(Integer.valueOf(text));
                        SendUtils.sendMsg(MajorActivity.this, SGMainProto.E_MSG_ID.MsgID_Major_CollectProductionReceiver_VALUE, request.build().toByteArray());
                    }
                }.show();
                break;
            case R.id.btn_refining:
                new InputTextDialog(this, "原材料1;原材料2") {
                    @Override
                    public void sendText(String text) {
                        String[] idString = text.split(";");
                        SGMajorProto.C2S_MajorMerge.Builder request = SGMajorProto.C2S_MajorMerge.newBuilder();
                        request.setMajorTypeValue(SGCommonProto.E_MAJOR_TYPE.MAJOR_TYPE_REFINING_VALUE);
                        for(String s : idString){
                            request.addSrcMaterial(SGCommonProto.GoodsObject.newBuilder().
                                    setType(SGCommonProto.E_GOODS_TYPE.GOODS_TYPE_PROPS_VALUE).setId(Integer.valueOf(s)).build());
                        }
                        SendUtils.sendMsg(MajorActivity.this, SGMainProto.E_MSG_ID.MsgID_Major_Merge_VALUE, request.build().toByteArray());
                    }
                }.show();
                break;
            default:
                break;
        }
    }
}
