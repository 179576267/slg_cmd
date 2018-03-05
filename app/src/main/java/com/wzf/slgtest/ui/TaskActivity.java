package com.wzf.slgtest.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.douqu.game.core.protobuf.SGMainProto;
import com.douqu.game.core.protobuf.SGPlayerProto;
import com.douqu.game.core.protobuf.SGSystemProto;
import com.douqu.game.core.protobuf.SGTaskProto;
import com.wzf.slgtest.MyApplication;
import com.wzf.slgtest.R;
import com.wzf.slgtest.netty.Config;
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

public class TaskActivity extends BaseActivity {
    @Bind(R.id.tv_object_index)
    TextView tvObjectIndex;
    @Bind(R.id.tv_result)
    TextView tvResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        tvObjectIndex.setText(MyApplication.getAppInstance().objecIndex);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fun2(SGTaskProto.S2C_TaskList msgInfo) {
        tvResult.setText("任务列表： \n" + msgInfo.toString());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fun2(SGTaskProto.S2C_TreasureReward msgInfo) {
        tvResult.setText("领取任务宝箱返回： \n" + msgInfo.toString());
    }

    @OnClick({R.id.btn_get_task_list, R.id.btn_get_task_box_reward})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_get_task_list:
                new InputTextDialog(this, "taskType(1:主线，2：支线，3:日常；4:攻城)") {
                    @Override
                    public void sendText(String text) {
                        SGTaskProto.C2S_TaskList.Builder request = SGTaskProto.C2S_TaskList.newBuilder();
                        request.setTypeValue(StringUtils.stringToInt(text));
                        SendUtils.sendMsg(TaskActivity.this, SGMainProto.E_MSG_ID.MsgID_Task_TaskList_VALUE, request.build().toByteArray());
                    }
                }.show();
                break;
            case R.id.btn_get_task_box_reward:
                new InputTextDialog(this, "type(1:主线，2：支线，3:日常；4:攻城)；boxId（宝箱id）") {
                    @Override
                    public void sendText(String text) {
                        SGTaskProto.C2S_TreasureReward.Builder request = SGTaskProto.C2S_TreasureReward.newBuilder();
                        int type = 0;
                        int boxId = 0;
                        try {
                            type = Integer.valueOf(text.split(";")[0]);
                            boxId = Integer.valueOf(text.split(";")[1]);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        request.setTypeValue(type);
                        request.setTreasureId(boxId);
                        SendUtils.sendMsg(TaskActivity.this, SGMainProto.E_MSG_ID.MsgID_Task_TreasureReward_VALUE, request.build().toByteArray());
                    }
                }.show();
            default:
                break;
        }
    }
}
