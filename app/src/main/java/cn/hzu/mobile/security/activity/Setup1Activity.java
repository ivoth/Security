package cn.hzu.mobile.security.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import cn.hzu.mobile.security.R;

public class Setup1Activity extends BaseSetupActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);
        setTitle("欢迎使用手机防盗");
    }

    @Override
    protected void showPrePage() {

    }

    @Override
    protected void showNextPage() {
        startActivity(new Intent(this, Setup2Activity.class));
        overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
        finish();
    }
}
