package com.wzf.slgtest.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.wzf.slgtest.R;
import com.wzf.slgtest.netty.Config;
import com.wzf.slgtest.netty.NettyTCPClient;
import com.wzf.slgtest.utils.BaseActivity;
import com.wzf.slgtest.utils.PreferencesHelper;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author Administrator
 */
public class MainActivity extends BaseActivity {

    @Bind(R.id.et_account)
    EditText etAccount;
    PreferencesHelper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        helper = new PreferencesHelper(this, PreferencesHelper.TB_USER);
        etAccount.setText(helper.getValue("ACCOUNT"));
    }

    @OnClick({R.id.btn_single_login,  R.id.btn_force_test, R.id.btn_mult_login})
    public void onClick(View view) {
        String account = etAccount.getText().toString();
        if(!TextUtils.isEmpty(account)){
            Config.ACCOUNT = account;
            helper.setValue("ACCOUNT", account);
        }
        showServerSelectDialog(view.getId());
    }


    private void showServerSelectDialog(final int viewId) {
        final String[] charSequences = new String[Config.servers.length];
        for(int i = 0; i < Config.servers.length ;i++){
            charSequences[i] = Config.servers[i].serverName;
        }
        AlertDialog.Builder builder= new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("请选择测试服务器:")
                .setItems(charSequences, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Config.IP = Config.servers[which].ip;
                        if(viewId == R.id.btn_single_login){
                            singleLogin();
                        }else if(viewId == R.id.btn_mult_login){
                            multLogin();
                        }else if(viewId == R.id.btn_force_test){
                            startActivity(new Intent(MainActivity.this, ForceTestActivity.class));
                        }
                    }
                }).show();



    }

    private void multLogin() {
        Config.multPlayerTest = true;
        startActivity(new Intent(MainActivity.this, MultFunctionTestActivity.class));
    }

    private void singleLogin() {
        Config.multPlayerTest = false;
        String account = etAccount.getText().toString();
        if(TextUtils.isEmpty(account)){
            Toast.makeText(this, "账号未输入", Toast.LENGTH_SHORT).show();
            return;
        }
        Config.ACCOUNT = account;
        helper.setValue("ACCOUNT", account);

        new Thread(new NettyTCPClient() {
            @Override
            public void startSuccess() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(MainActivity.this, MenuActivity.class));
                    }
                });
            }
        }).start();
    }
}
