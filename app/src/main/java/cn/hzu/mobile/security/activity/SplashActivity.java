package cn.hzu.mobile.security.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.x;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import cn.hzu.mobile.security.R;
import cn.hzu.mobile.security.utils.ConstantValue;
@ContentView(R.layout.activity_splash)
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
    private Bean bean;
    private int mLocalVersionCode;

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
                    Toast.makeText(SplashActivity.this, "检查更新失败!", Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case IO_ERROR:
                    Toast.makeText(SplashActivity.this, "读取异常!", Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        init();
        initDB();
    }

    private void initDB() {
        //1.归属地数据库拷贝
        initAddressDB("address.db");
    }

    /**
     * 从assets拷贝文件到用户目录
     *
     * @param dbName
     */
    private void initAddressDB(String dbName) {
        File filesDir = getFilesDir();
        File file = new File(filesDir, dbName);
        //文件存在就不在拷贝
        if (file.exists()) {
            return;
        }
        InputStream inputStream = null;
        FileOutputStream fos = null;
        //读取资产文件
        try {
            inputStream = getAssets().open(dbName);
            fos = new FileOutputStream(file);
            byte[] bs = new byte[1024];
            int temp = -1;
            while ((temp = inputStream.read(bs)) != -1) {
                fos.write(bs, 0, temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null && fos != null) {
                try {
                    inputStream.close();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void init() {
        TextView versionName = (TextView) findViewById(R.id.tv_version);
        versionName.setText("版本号: " + getVersionName());
        mLocalVersionCode = getVersionCode();
        //检查是否设置自动更新
        SharedPreferences sp = getSharedPreferences(ConstantValue.CONFIG, Context.MODE_PRIVATE);
        if (sp.getBoolean("auto_update", true)) {
            checkVersion();
        } else {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                        enterHome();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    /**
     * 获取版本代码
     *
     * @return
     */
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

    private void checkVersion() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                long startTime = System.currentTimeMillis();
                String uri = "https://raw.githubusercontent.com/ivoth/Security/master/other/update.json";
                RequestParams params = new RequestParams(uri);
                params.setConnectTimeout(2000);
                params.setReadTimeout(2000);
                x.http().get(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Log.i(TAG, "onSuccess: " + result);
                        Gson gson = new Gson();
                        bean = gson.fromJson(result, Bean.class);
                        if (mLocalVersionCode < bean.getVersionCode()) {
                            //提示用户更新,弹出对话框(UI),消息机制
                            mHandler.sendEmptyMessage(UPDATE_VERSION);
                        } else {
                            //进入应用程序主界面
                            mHandler.sendEmptyMessage(ENTER_HOME);
                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        mHandler.sendEmptyMessage(URL_ERROR);
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {
                    }

                    @Override
                    public void onFinished() {
                    }
                });
                long endTime = System.currentTimeMillis();
                if (endTime - startTime < 4000) {
                    try {
                        Thread.sleep(4000 - (endTime - startTime));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /**
     * 弹出对话框,提示用户更新
     */
    protected void showUpdateDialog() {
        //对话框,是依赖于activity存在的
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //设置左上角图标
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("发现新版本");
        //设置描述内容
        builder.setMessage(bean.getDesc());
        //积极按钮,立即更新
        builder.setPositiveButton(R.string.update_immediately, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //下载apk,apk链接地址,downloadUrl
                downloadApk();
            }
        });
        builder.setNegativeButton(R.string.later, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //取消对话框,进入主界面
                enterHome();
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                enterHome();
            }
        });
        builder.show();
    }

    private void downloadApk() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            final String path = Environment.getExternalStorageDirectory().getAbsolutePath()
                    + File.separator + "moblesafe.apk";
            RequestParams requestParams = new RequestParams(bean.getDownloadUrl());
            requestParams.setSaveFilePath(path);
            x.http().get(requestParams, new Callback.ProgressCallback<File>() {
                @Override
                public void onWaiting() {
                }

                @Override
                public void onStarted() {
                    Toast.makeText(SplashActivity.this, "开始下载...", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onLoading(long total, long current, boolean isDownloading) {

                }

                @Override
                public void onSuccess(File result) {
                    Toast.makeText(SplashActivity.this, R.string.download_successful, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    Uri file = Uri.fromFile(new File(path));
                    intent.setDataAndType(file, "application/vnd.android.package-archive");
                    startActivityForResult(intent,0);
//                    enterHome();
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    ex.printStackTrace();
                    Toast.makeText(SplashActivity.this, R.string.check_sd_card, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {

                }
            });
        } else {
            Toast.makeText(SplashActivity.this, R.string.check_sd_card, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        enterHome();
    }

    /**
     * 进入主界面
     */
    private void enterHome() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
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
