package cn.hzu.mobile.security.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public abstract class BaseSetupActivity extends AppCompatActivity {
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                //监听手势滑动
                if (e1.getX() - e2.getX() > 0) {
                    showNextPage();
                }
                if (e1.getX() - e2.getX() < 0) {
                    showPrePage();
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    protected abstract void showPrePage();

    protected abstract void showNextPage();

    public void prePage(View view){
        showPrePage();
    }

    public void nextPage(View view){
        showNextPage();
    }
}
