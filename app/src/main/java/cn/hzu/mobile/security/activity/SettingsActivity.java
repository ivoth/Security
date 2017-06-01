package cn.hzu.mobile.security.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.hzu.mobile.security.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        SharedPreferences sp = getSharedPreferences("session", Activity.MODE_PRIVATE);
        SharedPreferences settings = getSharedPreferences("avvvvv", Activity.MODE_PRIVATE);
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
            // 加载xml资源文件
            addPreferencesFromResource(R.xml.pref_general);
        }
    }
}
