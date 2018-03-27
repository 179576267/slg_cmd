package com.wzf.slgtest.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.wzf.slgtest.R;
import com.wzf.slgtest.utils.BaseActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Administrator
 */
public class MenuActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.instance, R.id.arena, R.id.official, R.id.goblin_store, R.id.playerinfo,
            R.id.goblin_bag, R.id.resolve, R.id.hero_temple, R.id.hero_altar, R.id.vip,
        R.id.hero_expedition,R.id.hero_task,R.id.major})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.instance:
                startActivity(new Intent(this, InstanceActivity.class));
                break;
            case R.id.arena:
                startActivity(new Intent(this, ArenaActivity.class));
                break;
            case R.id.official:
                startActivity(new Intent(this, OfficialRankActivity.class));
                break;
            case R.id.goblin_store:
                startActivity(new Intent(this, GoblinStoreActivity.class));
                break;
            case R.id.playerinfo:
                startActivity(new Intent(this, PlayerInfoActivity.class));
                break;
            case R.id.goblin_bag:
                startActivity(new Intent(this, BagCardInfoActivity.class));
                break;
            case  R.id.resolve:
                startActivity(new Intent(this, ResolveActivity.class));
                break;
            case  R.id.hero_temple:
                startActivity(new Intent(this, HeroTempleActivity.class));
                break;
            case  R.id.hero_altar:
                startActivity(new Intent(this, AltarLotteryActivity.class));
                break;
            case R.id.vip:
                startActivity(new Intent(this, VipAndBonusActivity.class));
                break;
            case R.id.hero_expedition:
                startActivity(new Intent(this, ExpeditionActivity.class));
                break;
            case R.id.hero_task:
                startActivity(new Intent(this, TaskActivity.class));
                break;
            case R.id.major:
                startActivity(new Intent(this, MajorActivity.class));
                break;
            default:
                break;
        }
    }
}
