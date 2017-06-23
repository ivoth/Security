package cn.hzu.mobile.security.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import cn.hzu.mobile.security.R;
import cn.hzu.mobile.security.utils.ConstantValue;

@ContentView(R.layout.activity_setup2)
public class Setup2Activity extends BaseSetupActivity {
    @ViewInject(R.id.ctv_text)
    private CheckedTextView mCtvText;
    private SharedPreferences mPreference;
    private boolean isCheck = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("绑定sim卡");
        initUI();
    }

    @Override
    protected void showPrePage() {
        startActivity(new Intent(this, Setup1Activity.class));
        overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_out_anim);
        finish();
    }

    @Override
    protected void showNextPage() {
        if (!isCheck) {
            Toast.makeText(this, "请绑定sim卡", Toast.LENGTH_SHORT).show();
            return;
        }
        startActivity(new Intent(this, Setup3Activity.class));
        overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
        finish();
    }

    private void initUI() {
        mPreference = getSharedPreferences(ConstantValue.CONFIG, Context.MODE_PRIVATE);
        String simNumber = mPreference.getString(ConstantValue.SIM_NUMBER, "");
        //判断是否空
        isCheck = !TextUtils.isEmpty(simNumber);
        mCtvText.setChecked(true);
        mCtvText.setChecked(isCheck);
        setText();
    }

    @Event(R.id.ctv_text)
    private void onCtvTextClick(View view) {
        mCtvText.setChecked(!isCheck);
        isCheck = mCtvText.isChecked();
        SharedPreferences.Editor edit = mPreference.edit();
//                点击绑定sim卡\n
        if (isCheck) {
            TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            String number = manager.getSimSerialNumber();
            Log.e("getSimSerialNumber", "onClick: " + number);
            edit.putString(ConstantValue.SIM_NUMBER, number).apply();
        } else {
            edit.remove(ConstantValue.SIM_NUMBER).commit();
        }
        setText();
    }

    private void setText() {
        mCtvText.setText(isCheck ? "点击绑定sim卡\n绑定sim卡已绑定" : "点击绑定sim卡\n绑定sim卡未绑定");
    }
}
