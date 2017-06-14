package cn.hzu.mobile.security.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import cn.hzu.mobile.security.R;
import cn.hzu.mobile.security.engine.AddressDao;

public class QueryAddressActivity extends AppCompatActivity {

    private EditText mEtPhone;
    private Button mBtnQuery;
    private TextView mTvQueryResult;
    private String mAddress;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mTvQueryResult.setText(mAddress);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_address);
        initUI();
//        AddressDao.getAddress(this, "15507845578");
    }

    private void initUI() {
        mEtPhone = (EditText) findViewById(R.id.et_phone);
        mBtnQuery = (Button) findViewById(R.id.btn_query);
        mTvQueryResult = (TextView) findViewById(R.id.tv_query_result);

        mBtnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                query();
            }
        });
        mEtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                query();
            }
        });
    }

    /**
     * 获取归属地,耗时操作
     */
    private void query() {
        final String phone = mEtPhone.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            mEtPhone.setAnimation(shake);
            mTvQueryResult.setText("请输入号码");
            return;
        }
        new Thread() {
            @Override
            public void run() {
                mAddress = AddressDao.getAddress(QueryAddressActivity.this, phone);
                mHandler.sendEmptyMessage(0);
            }
        }.start();
    }
}
