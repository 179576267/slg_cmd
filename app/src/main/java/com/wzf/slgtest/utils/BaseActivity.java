package com.wzf.slgtest.utils;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.wzf.slgtest.netty.Config;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;

/**
 * @Description:
 * @author: wangzhenfei
 * @date: 2017-10-31 17:43
 */

public class BaseActivity extends AppCompatActivity {
    private boolean isShow;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isShow = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isShow = false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void notifyAlert(String msgInfo) {
        if(isShow){
            new AlertDialog.Builder(this).setMessage(msgInfo).show();
        }
    }

    protected boolean isMultAccount(){
        return Config.multPlayerTest = false;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }
}
