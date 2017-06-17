package cn.hzu.mobile.security.receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import cn.hzu.mobile.security.R;
import cn.hzu.mobile.security.service.LocationService;
import cn.hzu.mobile.security.utils.ConstantValue;


public class SmsReceiver extends BroadcastReceiver {
    private static final String TAG = "SmsReceiver";
    private ComponentName mDeviceAdmin;
    private DevicePolicyManager mDPM;

    @Override
    public void onReceive(Context context, Intent intent) {
        //判断是否开启防盗
        SharedPreferences preference = context.getSharedPreferences("Preference", Context.MODE_PRIVATE);
        boolean openSecurity = preference.getBoolean(ConstantValue.OPEN_SECURITY, false);
        //获取短信内容
        if (!openSecurity) {
            return;
        }
        mDeviceAdmin = new ComponentName(context, DeviceAdminReceiver.class);
        mDPM = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        Object[] objects = (Object[]) intent.getExtras().get("pdus");
        //循环遍历短信
        for (Object obj : objects) {
            //获取基本信息
            SmsMessage sms = SmsMessage.createFromPdu((byte[]) obj);
            String originatingAddress = sms.getOriginatingAddress();
            String messageBody = sms.getMessageBody();
            //判断指令
            if (messageBody.contains("#*alarm*#")) {
                //播放
                MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.ylzs);
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
                Log.i(TAG, "onReceive: 报警了");
            }
            if (messageBody.contains("#*location*#")) {
                //开启服务
                context.startService(new Intent(context, LocationService.class));
            }
            if (messageBody.contains("#*lockScreen*#")) {
                if (mDPM.isAdminActive(mDeviceAdmin)) {
                    mDPM.lockNow();
//                    mDPM.resetPassword("", 0);
                } else {
                    Toast.makeText(context,"未激活设备管理器",Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "onReceive: 未激活设备管理器");
                }
            }
            if (messageBody.contains("#*wipeData*#")) {
                if (mDPM.isAdminActive(mDeviceAdmin)) {
                    mDPM.wipeData(0);
                } else {
                    Toast.makeText(context,"未激活设备管理器",Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "onReceive: 未激活设备管理器");
                }
            }
        }
    }
}
