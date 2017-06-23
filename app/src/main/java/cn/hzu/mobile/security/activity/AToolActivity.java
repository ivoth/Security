package cn.hzu.mobile.security.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.x;

import cn.hzu.mobile.security.R;
@ContentView(R.layout.activity_atool)
public class AToolActivity extends BaseAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("高级工具");
//        x.view().inject(this);
    }

    @Event(R.id.tv_query_phone_address)
    private void onmQueryPhoneAddressClick(View view) {
        startActivity(new Intent(this,QueryAddressActivity.class));
    }
}
