package cn.hzu.mobile.security.engine;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AddressDao {
    public static String path = "data/data/cn.hzu.mobile.security/files/address.db";
    private static final String TAG = "AddressDao";
    private static String mAddress = "未知号码";

    /**
     * 查询归属地
     *
     * @param phone 电话号码
     */
    public static String getAddress(Context context, String phone) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = null;
//        String path = context.getFilesDir().toString() + File.separator + "address.db";
//        Cursor cursor = db.query("numinfo", new String[]{"outkey"}, "mobileprefix = ?", new String[]{phone}, null, null, null);

        int length = phone.length();
        switch (length) {
            case 3:
                mAddress = "报警电话";
                break;
            case 4:
                mAddress = "模拟器";
                break;
            case 5:
                mAddress = "服务电话";
                break;
            case 7:
            case 8:
                mAddress = "本地电话";
                break;
            case 11:
                if (phone.matches("^1[3-8]\\d{9}$")) {
                    phone = phone.substring(0, 7);
                    String sql = "SELECT location  FROM data2 WHERE id IN (SELECT outkey FROM data1 WHERE id = ?);";
                    cursor = db.rawQuery(sql, new String[]{phone});
                } else {
                    String area = phone.substring(1, 3);
                    cursor = db.query("data2", new String[]{"location"}, "area = ?", new String[]{area}, null, null, null);
                }
                break;
            case 12:
                String area = phone.substring(1, 4);
                cursor = db.query("data2", new String[]{"location"}, "area = ?", new String[]{area}, null, null, null);
                break;
            default:
                mAddress = "未知号码";
        }
        if (cursor != null) {
            if (cursor.moveToNext()) {
                mAddress = cursor.getString(0);
            } else {
                mAddress = "未知号码";
            }
            cursor.close();
        }
        db.close();
        return mAddress;
    }
}
