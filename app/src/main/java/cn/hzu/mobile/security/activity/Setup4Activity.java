package cn.hzu.mobile.security.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import cn.hzu.mobile.security.R;
import cn.hzu.mobile.security.utils.ConstantValue;

@ContentView(R.layout.activity_setup4)
public class Setup4Activity extends BaseSetupActivity {
    @ViewInject(R.id.cb_box)
    private CheckBox mCheckBox;
    private SharedPreferences mPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("防盗模块");
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
        if (!mPreference.getBoolean(ConstantValue.OPEN_SECURITY, false)) {
            Toast.makeText(this, "请开启防盗模块", Toast.LENGTH_SHORT).show();
            return;
        }
        startActivity(new Intent(this, SetupOverActivity.class));
        mPreference.edit().putBoolean(ConstantValue.SETUP_OVER, true).apply();
        finish();
        overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
    }

    private void initUI() {
        mPreference = getSharedPreferences(ConstantValue.CONFIG, Context.MODE_PRIVATE);

        boolean openSecurity = mPreference.getBoolean(ConstantValue.OPEN_SECURITY, false);
        mCheckBox.setChecked(true);
        mCheckBox.setChecked(openSecurity);
    }

    @Event(value = R.id.cb_box, type = CompoundButton.OnCheckedChangeListener.class)
    private void checkBoxOnCheckedChange(CompoundButton compoundButton, boolean b) {
        compoundButton.setText(b ? "你已开启防盗模块" : "你未开启防盗模块");
        mPreference.edit().putBoolean(ConstantValue.OPEN_SECURITY, b).apply();
    }
}
