package cn.hzu.mobile.security.service;


import android.app.Service;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class LocationService extends Service{
    private static final String TAG = "LocationService";
    @Override
    public void onCreate() {
        super.onCreate();
        //获取手机经纬度
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setCostAllowed(true);
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        String bestProvider = lm.getBestProvider(criteria, true);
        lm.requestLocationUpdates(bestProvider, 0, 0, new MyLocationListener());
        Log.i(TAG, "onCreate: ");
    }

    class MyLocationListener implements LocationListener
    {
        private static final String TAG = "MyLocationListener";
        @Override
        public void onLocationChanged(Location location) {
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
//            SmsManager sms = SmsManager.getDefault();
//            sms.sendTextMessage("5554", null, "sim change!!", null, null);
            Toast.makeText(getApplicationContext(), "longitud:"+longitude +"latitude:"+latitude, Toast.LENGTH_LONG).show();
            Log.i(TAG, "onLocationChanged: " +"longitud:"+longitude +"latitude:"+latitude );
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
