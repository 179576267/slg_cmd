package com.wzf.slgtest.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.wzf.slgtest.R;
import com.wzf.slgtest.model.BattleInfo;
import com.wzf.slgtest.model.MultFunctionResInfo;
import com.wzf.slgtest.model.MultPlayerStatus;
import com.wzf.slgtest.model.PlayerStatus;
import com.wzf.slgtest.model.TcpHandlerParam;
import com.wzf.slgtest.netty.Config;
import com.wzf.slgtest.netty.force_test.ForceTestNettyTCPClient;
import com.wzf.slgtest.netty.mult_function.MultFunctionNettyTCPClient;
import com.wzf.slgtest.utils.BaseActivity;
import com.wzf.slgtest.utils.CommonAdapter;
import com.wzf.slgtest.utils.InputTextDialog;
import com.wzf.slgtest.utils.SendUtils;
import com.wzf.slgtest.utils.StringUtils;
import com.wzf.slgtest.utils.ViewHolder;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.R.attr.entries;

/**
 * 多功能测试点
 * @author Administrator
 */
public class MultFunctionTestActivity extends BaseActivity {
    private final int MAX_TEST_ACCOUNT = 200;
    List<MultPlayerStatus> players = new ArrayList<>();
    Map<String, MultPlayerStatus> playerMap = new HashMap<>();
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

    @Override
    protected void onResume() {
        super.onResume();
        if(Config.msgId != 0 && playerMap.size() > 0){
            Iterator<Map.Entry<String, MultPlayerStatus>> iterator = playerMap.entrySet().iterator();
            Map.Entry<String, MultPlayerStatus> next;
            MultPlayerStatus value;
            while (iterator.hasNext()){
                value =  iterator.next().getValue();
                if(value.ctx != null){
                    SendUtils.sendMsg(value.ctx, Config.msgId, Config.data);
                    value.start = System.currentTimeMillis();
                }
            }
            Config.data = null;
            Config.msgId = 0;
        }
    }

    private void setDialog() {
        int times = 1000 / MAX_TEST_ACCOUNT;
        int offset = 2;
        final String[] charSequences = new String[times + offset];
        charSequences[0] = "自定义数量";
        charSequences[1] = "特定账户";
        for(int i = offset ; i < charSequences.length; i++){
            charSequences[i] = (10001 + (i - offset) * MAX_TEST_ACCOUNT) + "-" + (10000 + (i - offset + 1) * MAX_TEST_ACCOUNT);
        }
        AlertDialog.Builder builder= new AlertDialog.Builder(MultFunctionTestActivity.this);

        builder.setTitle("请选择测试账号类型：")
                .setIcon(R.mipmap.ic_launcher)
                .setItems(charSequences, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       if(which == 0){
                            new InputTextDialog(MultFunctionTestActivity.this, "输入测试账户数量："){
                                @Override
                                public void sendText(String text) {
                                    int count = 0;
                                    count = StringUtils.stringToInt(text);
                                    count = count == 0? 1 : count;
                                    initData(10001, 10000 + count);
                                }
                            }.show();
                        }
                        else if(which == 1){
                           new InputTextDialog(MultFunctionTestActivity.this, "输入测试账户："){
                               @Override
                               public void sendText(String text) {
                                   threadPool = newCachedThreadPool();
                                   MultPlayerStatus info =  new MultPlayerStatus(text, "正在连接服务器", "", null);
                                   players.add(info);
                                   playerMap.put(text, info);
                                   threadPool.execute(getMainRunnable(text));
                                   adapter.notifyDataSetChanged();
                               }
                           }.show();
                        }else {
                           String[] split = charSequences[which].split("-");
                           initData(StringUtils.stringToInt(split[0]), StringUtils.stringToInt(split[1]));
                       }

                    }
                }).show();



    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fun2(MultPlayerStatus info) {
        MultPlayerStatus playerInfo = playerMap.get(info.account);
        playerInfo.status = info.status;
        if(TextUtils.isEmpty(info.objectIndex)){
            playerInfo.objectIndex = info.objectIndex;
        }
        if(info.ctx != null){
            playerInfo.ctx = info.ctx;
        }
        if("请求登录中...".equals(info.status)){
            playerInfo.start = System.currentTimeMillis();
        }else{
            playerInfo.end = System.currentTimeMillis();
        }
        adapter.notifyDataSetChanged();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fun2(MultFunctionResInfo info) {
        MultPlayerStatus playerInfo = playerMap.get(info.account);
        playerInfo.status = info.status;
        playerInfo.end = System.currentTimeMillis();
        adapter.notifyDataSetChanged();
    }

    private void initData(int start, int end) {
        threadPool = newCachedThreadPool();
        for(int i = start; i<= end; i++){
            final String account = String.valueOf(i);
            MultPlayerStatus info =  new MultPlayerStatus(account, "正在连接服务器", "", null);
            players.add(info);
            playerMap.put(account, info);
            threadPool.execute(getMainRunnable(account));
        }
        adapter.notifyDataSetChanged();
    }


    private void initView() {
        gv.setAdapter(adapter = new CommonAdapter<MultPlayerStatus>(players, this, R.layout.item_player) {
            @Override
            public void convert(ViewHolder viewHolder, MultPlayerStatus item) {
                TextView ip = viewHolder.getView(R.id.ip);
                TextView status = viewHolder.getView(R.id.status);
                ip.setText(item.account+ "【"+ item.getOffsetTime() + "】");
                status.setText(item.status );
            }
        });

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Config.multPlayerTest = true;
                startActivity(new Intent(MultFunctionTestActivity.this, MenuActivity.class));
            }
        });

        gv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Iterator<Map.Entry<String, MultPlayerStatus>> iterator = playerMap.entrySet().iterator();
                Map.Entry<String, MultPlayerStatus> next;
                long min = 0;
                long max = 0;
                long sum = 0;
                MultPlayerStatus value;
                while (iterator.hasNext()){
                    value =  iterator.next().getValue();
                    sum += value.getOffsetTime();
                    if(value.getOffsetTime() < min || min == 0){
                        min = value.getOffsetTime();
                    }
                    if(value.getOffsetTime() > max){
                        max = value.getOffsetTime();
                    }
                }
                AlertDialog.Builder builder= new AlertDialog.Builder(MultFunctionTestActivity.this);
                builder.setMessage("最大：" + max + "\n最小：" + min + "\n平均：" + sum / playerMap.size());
                builder.show();
                return true;
            }
        });
    }

    public Runnable getMainRunnable(final String account){
        return new MultFunctionNettyTCPClient(Config.IP, 11111, new TcpHandlerParam(account)) {
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

    public  ExecutorService newCachedThreadPool() {
        return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>());
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "进来了就别想走", Toast.LENGTH_SHORT).show();
    }
}
