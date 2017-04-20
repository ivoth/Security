package cn.hzu.mobile.security.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.hzu.mobile.security.MainActivity;
import cn.hzu.mobile.security.R;
import cn.hzu.mobile.security.utils.StreamUtil;


public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";
    /**
     * 更新新版本的状态码
     */
    protected static final int UPDATE_VERSION = 100;
    /**
     * 进入应用程序主界面状态码
     */
    protected static final int ENTER_HOME = 101;

    /**
     * url地址出错状态码
     */
    protected static final int URL_ERROR = 102;
    //以下是其他各种出错状态码
    protected static final int IO_ERROR = 103;
    protected static final int JSON_ERROR = 104;

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case UPDATE_VERSION:
                    showUpdateDialog();
                    break;
                case ENTER_HOME:
                    enterHome();
                    break;
                case URL_ERROR:
                    Toast.makeText(getApplicationContext(), "检查更新失败!", Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case IO_ERROR:
                    Toast.makeText(getApplicationContext(), "读取异常!", Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case JSON_ERROR:

                    break;
            }
        }
    };

    private void enterHome() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    private Bean bean;
    private int mLocalVersionCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//        startActivity(new Intent(this, MainActivity.class));
        init();
    }

    private void init() {
        TextView versionName = (TextView) findViewById(R.id.tv_version);
        versionName.setText("版本号: " + getVersionName());
        mLocalVersionCode = getVersionCode();
        checkVersion();
    }

    private void checkVersion() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                long startTime = System.currentTimeMillis();
                try {
                    //设置url
                    URL url = new URL("http://192.168.172.99:8080/update.json");
                    //开启链接
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    //连接超时
                    urlConnection.setConnectTimeout(2000);
                    urlConnection.setReadTimeout(2000);
                    Log.i(TAG, "run: " + urlConnection.getResponseCode());
                    if (urlConnection.getResponseCode() == 200) {
                        InputStream inputStream = urlConnection.getInputStream();
                        String string = StreamUtil.streamToString(inputStream);
                        Gson gson = new Gson();
                        bean = gson.fromJson(string, Bean.class);
                        if (mLocalVersionCode < bean.getVersionCode()) {
                            //提示用户更新,弹出对话框(UI),消息机制
                            msg.what = UPDATE_VERSION;
                        } else {
                            //进入应用程序主界面
                            msg.what = ENTER_HOME;
                        }
                        Log.i(TAG, "checkVersion: " + string);
                    } else {
                        msg.what = URL_ERROR;
                    }
                } catch (IOException e) {
                    msg.what = IO_ERROR;
                    e.printStackTrace();
                } finally {
                    long endTime = System.currentTimeMillis();
                    if(endTime - startTime < 4000) {
                        try {
                            Thread.sleep(4000 - (endTime - startTime));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                mHandler.sendMessage(msg);
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
    /**
     * 弹出对话框,提示用户更新
     */
    protected void showUpdateDialog() {
        //对话框,是依赖于activity存在的
        AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getApplicationContext());
        //设置左上角图标
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("发现新版本");
        //设置描述内容
        builder.setMessage(bean.getDesc());
        //积极按钮,立即更新
        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //下载apk,apk链接地址,downloadUrl
//                downloadApk();
            }
        });
        builder.setNegativeButton("稍后再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //取消对话框,进入主界面
                enterHome();
            }
        });
        builder.show();
    }
    private class Bean {

        /**
         * versionName : 3
         * versionCode : 10
         * desc : 这是新版本,欢迎下载使用
         * downloadUrl : http://www.baidu.com
         */

        private String versionName;
        private int versionCode;
        private String desc;
        private String downloadUrl;
        public String getVersionName() {
            return versionName;
        }

        public int getVersionCode() {
            return versionCode;
        }

        public String getDesc() {
            return desc;
        }

        public String getDownloadUrl() {
            return downloadUrl;
        }
    }
}
