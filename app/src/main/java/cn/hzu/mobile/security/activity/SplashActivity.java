package cn.hzu.mobile.security.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import cn.hzu.mobile.security.R;
import cn.hzu.mobile.security.utils.StreamUtil;


public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";
    private TextView mVersionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//        startActivity(new Intent(this, MainActivity.class));
        init();
    }

    private void init() {
        mVersionName = (TextView) findViewById(R.id.tv_version);
        mVersionName.setText("版本号: " + getVersionName());
        int code = getVersionCode();
        checkVersion();
    }

    private void checkVersion() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //设置url
                    //TODO 渣渣出错
                    URL url = new URL("http://192.168.172.99:8080/update.json");
                    //开启链接
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    //连接超时
                    urlConnection.setConnectTimeout(2000);
                    urlConnection.setReadTimeout(2000);
                    Log.i(TAG, "run: "+urlConnection.getResponseCode());
                    if (urlConnection.getResponseCode() == 200) {
                        InputStream inputStream = urlConnection.getInputStream();
                        String string = StreamUtil.streamToString(inputStream);
                        Log.i(TAG, "checkVersion: " + string);
                    } else {
//                        Toast.makeText(getApplicationContext(), "检查更新失败!", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private int getVersionCode() {
        PackageManager manager = getPackageManager();
        try {
            PackageInfo packageInfo = manager.getPackageInfo(getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取版本名称
     *
     * @return 返回null为异常
     */
    private String getVersionName() {
        PackageManager manager = getPackageManager();
        try {
            PackageInfo packageInfo = manager.getPackageInfo(getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
