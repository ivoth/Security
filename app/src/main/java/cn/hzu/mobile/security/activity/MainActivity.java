package cn.hzu.mobile.security.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import cn.hzu.mobile.security.R;

public class MainActivity extends AppCompatActivity {

    private GridView mGvHome;
    private int[] mIcons;
    private String[] mTitles;
    private Context content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        content = this;
        initUI();
        initData();
    }

    private void initUI() {
        mGvHome = (GridView) findViewById(R.id.gv_home);
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

        mGvHome.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
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
        });
    }

    private void showDialog() {
        SharedPreferences preference = getSharedPreferences("Preference", Activity.MODE_PRIVATE);
        String pwd = preference.getString("pwd", "");
        if (TextUtils.isEmpty(pwd)) {
            showSetPwdDialog();
        } else {
            showConfirmPwdDialog(pwd);
        }
    }

    private void showConfirmPwdDialog(final String entrPwd) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View view = View.inflate(this, R.layout.dialog_set_pwd, null);
        final EditText cPwd = (EditText) view.findViewById(R.id.et_confirm_pwd);
        cPwd.setVisibility(View.GONE);
        builder.setView(view)
                .setTitle("设置密码")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText pwd = (EditText) view.findViewById(R.id.et_set_pwd);
                        String pwdStr = pwd.getText().toString();

                        if (TextUtils.isEmpty(pwdStr)) {
                            Toast.makeText(content, "请输入密码", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (!pwdStr.equals(entrPwd)) {
                            Toast.makeText(content, "确认密码错误", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Intent intent = new Intent();
                        intent.setClass(content, SetupOverActivity.class);
                        startActivity(intent);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).show();
    }

    private void showSetPwdDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View view = View.inflate(this, R.layout.dialog_set_pwd, null);
        builder.setView(view)
                .setTitle("设置密码")
                .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText pwd = (EditText) view.findViewById(R.id.et_set_pwd);
                        EditText cPwd = (EditText) view.findViewById(R.id.et_confirm_pwd);

                        String pwdStr = pwd.getText().toString();
                        String cPwdStr = cPwd.getText().toString();

                        if (TextUtils.isEmpty(pwdStr) || TextUtils.isEmpty(cPwdStr)) {
                            Toast.makeText(content, "请输入密码", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (!pwdStr.equals(cPwdStr)) {
                            Toast.makeText(content, "确认密码错误", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(content, "设置密码成", Toast.LENGTH_SHORT).show();
                        SharedPreferences preference = getSharedPreferences("Preference", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor edit = preference.edit();
                        edit.putString("pwd", pwdStr);
                        edit.apply();
                        showDialog();
                    }
                }).setNegativeButton("取消", null).show();
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
