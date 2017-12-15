package com.wzf.slgtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.wzf.slgtest.netty.Config;
import com.wzf.slgtest.netty.NettyTCPClient;
import com.wzf.slgtest.utils.BaseActivity;
import com.wzf.slgtest.utils.PreferencesHelper;

import butterknife.Bind;
import butterknife.ButterKnife;
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

    @OnClick({R.id.btn_zhenfei, R.id.btn_sanshao, R.id.btn_pengyu})
    public void onClick(View view) {
        String account = etAccount.getText().toString();
        if(TextUtils.isEmpty(account)){
            Toast.makeText(this, "账号未输入", Toast.LENGTH_SHORT).show();
            return;
        }
        Config.ACCOUNT = account;
        helper.setValue("ACCOUNT", account);
        switch (view.getId()) {
            case R.id.btn_zhenfei:
                Config.IP = Config.IP_ZHENFEI;
                break;
            case R.id.btn_sanshao:
                Config.IP = Config.IP_SANSHAO;
                break;
            case R.id.btn_pengyu:
                Config.IP = Config.IP_PENGYU;
                break;
            default:
        }


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
