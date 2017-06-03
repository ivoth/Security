package cn.hzu.mobile.security.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import cn.hzu.mobile.security.R;
import cn.hzu.mobile.security.utils.ConstantValue;

public class Setup4Activity extends BaseSetupActivity {
    private CheckBox mCheckBox;
    private SharedPreferences mPreference;
    private boolean openSecurity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);
        initUI();
    }

    @Override
    protected void showPrePage() {
        startActivity(new Intent(this, Setup3Activity.class));
        finish();
        overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_out_anim);
    }

    @Override
    protected void showNextPage() {
        if (!openSecurity) {
            Toast.makeText(this,"请开启防盗模块",Toast.LENGTH_SHORT).show();
            return;
        }
        startActivity(new Intent(this, SetupOverActivity.class));
        mPreference.edit().putBoolean(ConstantValue.SETUP_OVER, true).apply();
        finish();
        overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
    }

    private void initUI() {
        mCheckBox = (CheckBox) findViewById(R.id.cb_box);

        mPreference = getSharedPreferences("Preference", Context.MODE_PRIVATE);

        openSecurity = mPreference.getBoolean(ConstantValue.OPEN_SECURITY, false);
        mCheckBox.setChecked(openSecurity);
        String text = openSecurity ? "你已开启防盗模块" : "您未开启防盗模块";
        mCheckBox.setText(text);

        mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSecurity = mCheckBox.isChecked();
                mPreference.edit().putBoolean(ConstantValue.OPEN_SECURITY, openSecurity).apply();
            }
        });
    }

}
