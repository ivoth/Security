package cn.hzu.mobile.security.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.hzu.mobile.security.R;

public class ContactListActivity extends AppCompatActivity {

    private static final String TAG = "ContactListActivity";
    private ListView mLvContact;
    private List<HashMap<String, String>> mContactList = new ArrayList<>();

    private MyAdapter myAdapter;
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            myAdapter = new MyAdapter();
            mLvContact.setAdapter(myAdapter);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        setTitle("选择联系人");
        initUI();
        initData();
    }

    /**
     * 获取系统联系人
     */
    private void initData() {
        new Thread() {
            public void run() {
                ContentResolver contentResolver = getContentResolver();
                Cursor cursor = contentResolver.query(
                        Uri.parse("content://com.android.contacts/raw_contacts"),
                        new String[]{"contact_id"},
                        null, null, null
                );
                if (cursor == null) {
                    Log.w(TAG, "run: ");
                    return;
                }
                int count = 10;
                mContactList.clear();
                while (cursor.moveToNext() && count-- > 0) {
                    String id = cursor.getString(0);
                    Cursor indexCursor1 = contentResolver.query(
                            Uri.parse("content://com.android.contacts/data"),
                            new String[]{"data1", "mimetype"},
                            "raw_contact_id = ?", new String[]{id}, null
                    );

                    HashMap<String, String> hastMap = new HashMap<>();
                    while (indexCursor1.moveToNext()) {
                        String data = indexCursor1.getString(0);
                        String type = indexCursor1.getString(1);

                        if ("vnd.android.cursor.item/phone_v2".equals(type)) {
                            if (!TextUtils.isEmpty(data)) {
                                hastMap.put("phone", data);
                            }
                        } else if ("vnd.android.cursor.item/name".equals(type)) {
                            if (!TextUtils.isEmpty(data)) {
                                hastMap.put("name", data);
                            }
                        }
                    }

                    indexCursor1.close();
                    mContactList.add(hastMap);
                }
                cursor.close();
                mHandler.sendEmptyMessage(0);
            }
        }.start();
    }

    private void initUI() {
        mLvContact = (ListView) findViewById(R.id.lv_contact);
        mLvContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (myAdapter != null) {
                    HashMap<String, String> hashMap = myAdapter.getItem(i);
                    String phone = hashMap.get("phone");
                    Intent intent = new Intent();
                    intent.putExtra("phone", phone);
                    setResult(0, intent);
                    finish();
                }
            }
        });
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mContactList.size();
        }

        @Override
        public HashMap<String, String> getItem(int i) {
            return mContactList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                view = View.inflate(ContactListActivity.this, R.layout.listview_contact_item, null);
                viewHolder = new ViewHolder();
                viewHolder.name = (TextView) view.findViewById(R.id.tv_name);
                viewHolder.phone = (TextView) view.findViewById(R.id.tv_phone);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.name.setText(getItem(i).get("name"));
            viewHolder.phone.setText(getItem(i).get("phone"));
            return view;
        }
    }

    private class ViewHolder {
        private TextView name;
        private TextView phone;
    }
}
