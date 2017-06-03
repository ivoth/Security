package cn.hzu.mobile.security.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import cn.hzu.mobile.security.R;
import cn.hzu.mobile.security.utils.ConstantValue;

public class SetupOverActivity extends AppCompatActivity {

    private TextView mTvPhone;
    private TextView mTvResetSetup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preference = getSharedPreferences("Preference", Activity.MODE_PRIVATE);
        boolean setupOver = preference.getBoolean(ConstantValue.SETUP_OVER, false);

        if (!setupOver) {
            Intent intent = new Intent(this, Setup1Activity.class);
            startActivity(intent);
            finish();
        }
        setContentView(R.layout.activity_setup_over);
        initUI();
    }

    private void initUI() {
        mTvPhone = (TextView) findViewById(R.id.tv_phone);
        SharedPreferences pf = getSharedPreferences("Preference", Context.MODE_PRIVATE);
        String phone = pf.getString(ConstantValue.CONTACT_PHONE, "");
        mTvPhone.setText(phone);

        mTvResetSetup = (TextView) findViewById(R.id.tv_reset_setup);
        mTvResetSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SetupOverActivity.this, Setup1Activity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
