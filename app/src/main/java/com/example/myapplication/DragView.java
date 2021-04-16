package com.example.myapplication;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

/**
 * 可拖拽View
 *
 * @author Jiangli
 * @see <a href="https://www.jianshu.com/p/19cd34e957e7">https://www.jianshu.com/p/19cd34e957e7</a>
 */
public class DragView extends View {
    private static final String TAG = DragView.class.getSimpleName();
    private float mDownX;
    private float mDownY;
    private int mRootMeasuredWidth = 0;
    private int mRootMeasuredHeight = 0;
    private int minTouchSlop;//系统可以辨别的最小滑动距离

    private Context mContext;


    public DragView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        minTouchSlop = ViewConfiguration.get(mContext).getScaledTouchSlop();
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //释放资源
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //解决parentView或者ancestorsView为ScrollView时,当前View不能自动滑动的问题
                getParent().requestDisallowInterceptTouchEvent(true);
                //测量按下位置
                mDownX = event.getX();
                mDownY = event.getY();
                //测量父类的位置和宽高
                ViewGroup mViewGroup = (ViewGroup) getParent();
                if (mViewGroup != null) {
                    //获取父布局的高度
                    mRootMeasuredHeight = mViewGroup.getMeasuredHeight();
                    mRootMeasuredWidth = mViewGroup.getMeasuredWidth();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                //Log.i(TAG, "ACTION_MOVE " + (mDownY >= mRootTopY) + (" mDownY = " + mDownY) + (" mRootTopY = " + mRootTopY));
                getParent().requestDisallowInterceptTouchEvent(true);
                //计算移动距离 判定是否滑动
                float dx = event.getX() - mDownX;
                float dy = event.getY() - mDownY;
                boolean interceptd = false;
                if (Math.abs(dx) > minTouchSlop || Math.abs(dy) > minTouchSlop) {
                    interceptd = true;
                } else {
                    interceptd = false;
                }
                if (interceptd && mDownX >= 0
                        && mDownX <= mRootMeasuredWidth
                        && mDownY <= (mRootMeasuredHeight)) {

                    float ownX = getX();
                    //获取手指按下的距离与控件本身Y轴的距离
                    float ownY = getY();
                    //理论中X轴拖动的距离
                    float endX = ownX + dx;
                    //理论中Y轴拖动的距离
                    float endY = ownY + dy;
                    //X轴可以拖动的最大距离
                    float maxX = mRootMeasuredWidth - getWidth();
                    //Y轴可以拖动的最大距离
                    float maxY = mRootMeasuredHeight - getHeight();
                    //X轴边界限制
                    endX = endX < 0 ? 0 : Math.min(endX, maxX);
                    //Y轴边界限制
                    endY = endY < 0 ? 0 : Math.min(endY, maxY);
                    //开始移动
                    setX(endX);
                    setY(endY);
                }

                break;
            case MotionEvent.ACTION_UP:
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }

        super.onTouchEvent(event);

        return true;
    }

}
