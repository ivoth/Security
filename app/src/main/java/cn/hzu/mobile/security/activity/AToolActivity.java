package cn.hzu.mobile.security.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import cn.hzu.mobile.security.R;

public class AToolActivity extends AppCompatActivity {

    private TextView mQueryPhoneAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atool);
        initUI();
    }

    private void initUI() {
        mQueryPhoneAddress = (TextView) findViewById(R.id.tv_query_phone_address);
        mQueryPhoneAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AToolActivity.this,QueryAddressActivity.class));
            }
        });
    }
}
