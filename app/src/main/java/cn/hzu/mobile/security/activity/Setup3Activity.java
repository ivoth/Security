package cn.hzu.mobile.security.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import cn.hzu.mobile.security.R;
import cn.hzu.mobile.security.utils.ConstantValue;

@ContentView(R.layout.activity_setup3)
public class Setup3Activity extends BaseSetupActivity {
    @ViewInject(R.id.et_phone_number)
    private EditText mEtPhoneNumber;
    private Context mContext;
    private SharedPreferences mPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("设置安全号码");
        mContext = this;
        mPreference = getSharedPreferences(ConstantValue.CONFIG, Context.MODE_PRIVATE);
        mEtPhoneNumber.setText(mPreference.getString(ConstantValue.CONTACT_PHONE, ""));
    }

    @Override
    protected void showPrePage() {
        startActivity(new Intent(this, Setup2Activity.class));
        finish();
        overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_out_anim);
    }

    @Override
    protected void showNextPage() {
        String phone = mEtPhoneNumber.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "请选择电话号码", Toast.LENGTH_SHORT).show();
            return;
        }
        mPreference.edit().putString(ConstantValue.CONTACT_PHONE, phone).apply();
        startActivity(new Intent(this, Setup4Activity.class));
        finish();
        overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
    }

    @Event(R.id.btn_select_number)
    private void onBtnSelectNumberClick(View view) {
        Intent intent = new Intent(mContext, ContactListActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            String phone = data.getStringExtra("phone");
            phone = phone.replace("-", "").replace(" ", "").trim();
            mEtPhoneNumber.setText(phone);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
