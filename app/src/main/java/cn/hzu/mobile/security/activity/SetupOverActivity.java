package cn.hzu.mobile.security.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import cn.hzu.mobile.security.R;
import cn.hzu.mobile.security.utils.ConstantValue;

@ContentView(R.layout.activity_setup_over)
public class SetupOverActivity extends BaseAppCompatActivity {
    @ViewInject(R.id.tv_phone)
    private TextView mTvPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp = getSharedPreferences(ConstantValue.CONFIG, Activity.MODE_PRIVATE);
        boolean setupOver = sp.getBoolean(ConstantValue.SETUP_OVER, false);

        if (!setupOver) {
            Intent intent = new Intent(this, Setup1Activity.class);
            startActivity(intent);
            finish();
        }
        setTitle("手机防盗");
        initUI();
    }

    private void initUI() {
        SharedPreferences pf = getSharedPreferences(ConstantValue.CONFIG, Context.MODE_PRIVATE);
        String phone = pf.getString(ConstantValue.CONTACT_PHONE, "");
        mTvPhone.setText(phone);
    }

    @Event(R.id.tv_reset_setup)
    private void onTvResetSetupClick(View view) {
        Intent intent = new Intent(SetupOverActivity.this, Setup1Activity.class);
        startActivity(intent);
        finish();
    }
}
