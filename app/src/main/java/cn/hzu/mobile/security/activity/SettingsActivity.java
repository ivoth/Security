package cn.hzu.mobile.security.activity;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.xutils.view.annotation.ContentView;
import org.xutils.x;

import cn.hzu.mobile.security.R;
import cn.hzu.mobile.security.utils.ConstantValue;
@ContentView(R.layout.activity_settings)
public class SettingsActivity extends BaseAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("设置中心");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if (savedInstanceState == null) {
            SettingFragment settingFragment = new SettingFragment();
            //R.id.pref_content防止与toolbar重叠
            getFragmentManager().beginTransaction()
                    .add(R.id.pref_content,settingFragment)
                    .commit();
        }
    }
    public static class SettingFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //指定配置文件名字
            getPreferenceManager().setSharedPreferencesName(ConstantValue.CONFIG);
            // 加载xml资源文件
            addPreferencesFromResource(R.xml.pref_setting);
        }
    }
}
