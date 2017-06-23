package cn.hzu.mobile.security.activity;

import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import cn.hzu.mobile.security.R;
import cn.hzu.mobile.security.engine.AddressDao;

@ContentView(R.layout.activity_query_address)
public class QueryAddressActivity extends BaseAppCompatActivity {
    @ViewInject(R.id.et_phone)
    private EditText mEtPhone;
    @ViewInject(R.id.tv_query_result)
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
        setTitle("归属地查询");
        initUI();
    }

    @Event(value = R.id.et_phone)
    private void initUI() {
        mEtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                onBtnQueryClick(null);
            }
        });
    }

    /**
     * 获取归属地,耗时操作
     */
    @Event(R.id.btn_query)
    private void onBtnQueryClick(View view) {
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
