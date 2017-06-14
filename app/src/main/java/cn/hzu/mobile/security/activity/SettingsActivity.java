package cn.hzu.mobile.security.activity;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;

import cn.hzu.mobile.security.R;
import cn.hzu.mobile.security.utils.ConstantValue;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            SettingFragment settingFragment = new SettingFragment();
            getFragmentManager().beginTransaction()
                    .add(android.R.id.content,settingFragment)
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
            addPreferencesFromResource(R.xml.pref_general);
        }
    }
}
