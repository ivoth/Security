package cn.hzu.mobile.security.receive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import cn.hzu.mobile.security.utils.ConstantValue;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String simSerialNumber = tm.getSimSerialNumber();
        SharedPreferences pf = context.getSharedPreferences("Preference", Context.MODE_PRIVATE);
        String simNumber = pf.getString(ConstantValue.SIM_NUMBER, "");

        if (!simNumber.equals(simSerialNumber)) {
            SmsManager sms = SmsManager.getDefault();
//            sms.sendTextMessage("5554", null, "sim change!!", null, null);
            Toast.makeText(context, "已向妖妖灵报警!!", Toast.LENGTH_LONG).show();
        }

    }
}
