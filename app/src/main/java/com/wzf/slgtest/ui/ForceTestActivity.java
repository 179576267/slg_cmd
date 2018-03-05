package com.wzf.slgtest.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.GridView;
import android.widget.TextView;

import com.wzf.slgtest.R;
import com.wzf.slgtest.model.BattleInfo;
import com.wzf.slgtest.model.PlayerStatus;
import com.wzf.slgtest.model.TcpHandlerParam;
import com.wzf.slgtest.netty.Config;
import com.wzf.slgtest.netty.force_test.ForceTestNettyTCPClient;
import com.wzf.slgtest.utils.BaseActivity;
import com.wzf.slgtest.utils.CommonAdapter;
import com.wzf.slgtest.utils.InputTextDialog;
import com.wzf.slgtest.utils.StringUtils;
import com.wzf.slgtest.utils.ViewHolder;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Administrator
 */
public class ForceTestActivity extends BaseActivity {
    private final int MAX_TEST_ACCOUNT = 150;
    List<PlayerStatus> players = new ArrayList<>();
    Map<String, PlayerStatus> playerMap = new HashMap<>();
    ExecutorService threadPool;
    @Bind(R.id.gv)
    GridView gv;

    private CommonAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_force_test);
        ButterKnife.bind(this);
        initView();
        setDialog();
    }

    private void setDialog() {
        int times = 1000 / MAX_TEST_ACCOUNT;
        final String[] charSequences = new String[times + 1];
        charSequences[0] = "自定义数量";
        for(int i = 1 ; i < charSequences.length; i++){
            charSequences[i] = (10001 + (i - 1) * MAX_TEST_ACCOUNT) + "-" + (10000 + (i) * MAX_TEST_ACCOUNT);
        }
        AlertDialog.Builder builder= new AlertDialog.Builder(ForceTestActivity.this);

        builder.setTitle("请选择测试账号范围")
                .setIcon(R.mipmap.ic_launcher)
                .setItems(charSequences, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       if(which == 0){
                            new InputTextDialog(ForceTestActivity.this, "输入测试账户数量："){
                                @Override
                                public void sendText(String text) {
                                    int count = 0;
                                    count = StringUtils.stringToInt(text);
                                    count = count == 0? 1 : count;
                                    initData(10001, 10000 + count);
                                }
                            }.show();
                        }
                        else {
                            String[] split = charSequences[which].split("-");
                            initData(StringUtils.stringToInt(split[0]), StringUtils.stringToInt(split[1]));
                        }

                    }
                }).show();



    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fun2(PlayerStatus info) {
        PlayerStatus playerInfo = playerMap.get(info.account);
        playerInfo.status = info.status;
        if(TextUtils.isEmpty(info.objectIndex)){
            playerInfo.objectIndex = info.objectIndex;
        }
        adapter.notifyDataSetChanged();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fun2(BattleInfo info) {
        PlayerStatus playerInfo = playerMap.get(info.account);
        playerInfo.status = "正在连接战斗服务器";
        adapter.notifyDataSetChanged();
        threadPool.execute(getBattleRunnable(info.serverInfo.getHost(), info.serverInfo.getPort(), info.battleId, info.objectIndex, info.account));
    }


    private void initData(int start, int end) {
        threadPool = newCachedThreadPool();
        for(int i = start; i<= end; i++){
            final String account = String.valueOf(i);
            PlayerStatus info =  new PlayerStatus(account, "正在连接服务器", "");
            players.add(info);
            playerMap.put(account, info);
            threadPool.execute(getMainRunnable(account));
        }
        adapter.notifyDataSetChanged();
    }


    private void initView() {
        gv.setAdapter(adapter = new CommonAdapter<PlayerStatus>(players, this, R.layout.item_player) {
            @Override
            public void convert(ViewHolder viewHolder, PlayerStatus item) {
                TextView ip = viewHolder.getView(R.id.ip);
                TextView status = viewHolder.getView(R.id.status);
                ip.setText(item.account);
                status.setText(item.status);
            }
        });
    }

    public Runnable getMainRunnable(final String account){
        return new ForceTestNettyTCPClient(Config.IP, 11111, new TcpHandlerParam(account)) {
            @Override
            public void startSuccess() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        PlayerStatus playerInfo = playerMap.get(account);
                        playerInfo.status = "连接成功，正在准备登录";
                    }
                });
            }
        };
    }


    public Runnable getBattleRunnable(String host, int port, String battleId, final String objectIndex, final String account){
        return new ForceTestNettyTCPClient(host, port, new TcpHandlerParam(account, battleId, objectIndex)) {
            @Override
            public void startSuccess() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        PlayerStatus playerInfo = playerMap.get(account);
                        playerInfo.status = "连接战斗服成功，正在发起战斗";
                    }
                });
            }
        };
    }


    public  ExecutorService newCachedThreadPool() {
        return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>());
    }

}
