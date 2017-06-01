package cn.hzu.mobile.security.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

public class FocusTextView extends android.support.v7.widget.AppCompatTextView {
    /**
     * 使用在通过java代码创建控件
     * @param context Context
     */
    public FocusTextView(Context context) {
        super(context);
    }

    /**
     * 由系统调用（带属性+上下文环境构造方法）
     * @param context Context
     * @param attrs AttributeSet
     */
    public FocusTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 由系统调用（带属性+上下文环境构造方法+布局文件中定义样式文件构造方法）
     * @param context Context
     * @param attrs AttributeSet
     * @param defStyleAttr int
     */
    public FocusTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 重写获取焦点的方法
     * @return bool
     */
    @Override
    public boolean isFocused() {
        return true;
    }
}
