package cn.hzu.mobile.security.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import cn.hzu.mobile.security.R;
import cn.hzu.mobile.security.utils.ConstantValue;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseAppCompatActivity {
    @ViewInject(R.id.gv_home)
    private GridView mGvHome;
    private int[] mIcons;
    private String[] mTitles;
    private Context mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("功能列表");
        mContent = this;
        initData();
    }

    private void initData() {
        mIcons = new int[]{R.drawable.home_safe,
                R.drawable.home_callmsgsafe, R.drawable.home_apps, R.drawable.home_taskmanager,
                R.drawable.home_netmanager, R.drawable.home_trojan, R.drawable.home_sysoptimize,
                R.drawable.home_tools, R.drawable.home_settings};
        // 将九个item的每一个标题都存入该数组中
        mTitles = new String[]{"手机防盗", "通讯卫士", "软件管理", "进程管理",
                "流量统计", "手机杀毒", "缓存清理", "高级工具", "设置中心"};
        mGvHome.setAdapter(new MyAdapter());

    }

    @Event(value = R.id.gv_home, type = AdapterView.OnItemClickListener.class)
    private void onGvHomeItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent();
        switch (i) {
            case 0:
                showDialog();
                break;
            case 7:
                intent.setClass(MainActivity.this, AToolActivity.class);
                startActivity(intent);
                break;
            case 8:
                intent.setClass(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                break;
            default:
                Toast.makeText(MainActivity.this, "未实现的功能", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 显示对话框
     */
    private void showDialog() {
        SharedPreferences sp = getSharedPreferences(ConstantValue.CONFIG, Activity.MODE_PRIVATE);
        String pwd = sp.getString(ConstantValue.PWD, "");
        //判断是否存有密码
        if (TextUtils.isEmpty(pwd)) {
            showSetPwdDialog();
        } else {
            showConfirmPwdDialog(pwd);
        }
    }

    private void showConfirmPwdDialog(final String enterPwd) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View view = View.inflate(this, R.layout.dialog_set_pwd, null);
        final EditText cPwd = (EditText) view.findViewById(R.id.et_confirm_pwd);
        //隐藏复用的布局文件
        cPwd.setVisibility(View.GONE);
        builder.setView(view).setTitle("确认密码")
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText pwd = (EditText) view.findViewById(R.id.et_set_pwd);
                        String pwdStr = pwd.getText().toString();

                        if (TextUtils.isEmpty(pwdStr)) {
                            Toast.makeText(mContent, "请输入密码", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (!pwdStr.equals(enterPwd)) {
                            Toast.makeText(mContent, "确认密码错误", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Intent intent = new Intent();
                        intent.setClass(mContent, SetupOverActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    /**
     * 显示设置dialog
     */
    private void showSetPwdDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View view = View.inflate(this, R.layout.dialog_set_pwd, null);
        builder.setView(view)
                .setTitle("设置密码")
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText pwd = (EditText) view.findViewById(R.id.et_set_pwd);
                        EditText cPwd = (EditText) view.findViewById(R.id.et_confirm_pwd);

                        String pwdStr = pwd.getText().toString();
                        String cPwdStr = cPwd.getText().toString();

                        if (TextUtils.isEmpty(pwdStr) || TextUtils.isEmpty(cPwdStr)) {
                            Toast.makeText(mContent, "请输入密码", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (!pwdStr.equals(cPwdStr)) {
                            Toast.makeText(mContent, "确认密码错误", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(mContent, "设置密码成功", Toast.LENGTH_SHORT).show();
                        SharedPreferences preference = getSharedPreferences(ConstantValue.CONFIG, Activity.MODE_PRIVATE);
                        preference.edit().putString(ConstantValue.PWD, pwdStr).apply();
                        showDialog();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mTitles.length;
        }

        @Override
        public Object getItem(int i) {
            return mTitles[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            //防止重复查找id
            if (view == null) {
                viewHolder = new ViewHolder();
                view = View.inflate(MainActivity.this, R.layout.gridview_item, null);
                viewHolder.imageView = (ImageView) view.findViewById(R.id.iv_icon);
                viewHolder.textView = (TextView) view.findViewById(R.id.tv_title);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.imageView.setBackgroundResource(mIcons[i]);
            viewHolder.textView.setText(mTitles[i]);

            return view;
        }
    }

    private class ViewHolder {
        private ImageView imageView;
        private TextView textView;
    }
}
