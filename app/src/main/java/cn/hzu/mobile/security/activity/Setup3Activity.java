package cn.hzu.mobile.security.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.hzu.mobile.security.R;
import cn.hzu.mobile.security.utils.ConstantValue;

public class Setup3Activity extends BaseSetupActivity {
    private EditText mEtPhoneNumber;
    private Button mBtnSelectNumber;
    private Context mContext;
    private SharedPreferences mPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);
        setTitle("设置安全号码");
        initUI();
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

    private void initUI() {
        mContext = this;
        mPreference = getSharedPreferences(ConstantValue.CONFIG, Context.MODE_PRIVATE);

        mEtPhoneNumber = (EditText) findViewById(R.id.et_phone_number);
        mEtPhoneNumber.setText(mPreference.getString(ConstantValue.CONTACT_PHONE, ""));

        mBtnSelectNumber = (Button) findViewById(R.id.btn_select_number);
        mBtnSelectNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ContactListActivity.class);
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            String phone = data.getStringExtra("phone");
            phone = phone.replace("/", "").replace(" ", "").trim();
            mEtPhoneNumber.setText(phone);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
