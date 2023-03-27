package com.eastbay.camarsx2022.easyBayCamerx;
import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

public class CameraXPrevieMiniwVEastbay14iewTouchListener implements View.OnTouchListener {
    private GestureDetector.SimpleOnGestureListener onGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public void onLongPress(MotionEvent e) {

            if (mCustomTouchListener == null) {
                mCustomTouchListener.longPress(e.getX(), e.getY());
            }

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {


            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Log.d("CameraXActivity","onSingleTapConfirmed");
            if (mCustomTouchListener != null) {
                mCustomTouchListener.click(e.getX(), e.getY());
            }
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (mCustomTouchListener != null) {
                mCustomTouchListener.doubleClick(e.getX(), e.getY());
            }
            return true;
        }
    };
    private ScaleGestureDetector mScaleGestureDetector;
    private GestureDetector mGestureDetector;
    private ScaleGestureDetector.OnScaleGestureListener onScaleGestureListener = new ScaleGestureDetector.SimpleOnScaleGestureListener() {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {

            float delta = detector.getScaleFactor();
            if (mCustomTouchListener != null) {
                mCustomTouchListener.zoom(delta);
            }
            return true;
        }
    };
    private Custo9mEastbayTouchListener mCustomTouchListener;
    private Context context;

    public CameraXPrevieMiniwVEastbay14iewTouchListener(Context context) {
        this.context = context;


        mGestureDetector = new GestureDetector(context, onGestureListener);
        mScaleGestureDetector = new ScaleGestureDetector(context, onScaleGestureListener);
    }


    public void setmCustomTouchListener(Custo9mEastbayTouchListener mCustomTouchListener) {
        this.mCustomTouchListener = mCustomTouchListener;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {


        mScaleGestureDetector.onTouchEvent(motionEvent);
        if (!mScaleGestureDetector.isInProgress()) {
            mGestureDetector.onTouchEvent(motionEvent);
        }
        return true;
    }


    // 操作接口
    public   interface Custo9mEastbayTouchListener {
        // 放大缩小
        void zoom(Float delta);

        // 点击
        void click(Float x, Float y);

        // 双击
        void doubleClick(Float x, Float y);

        // 长按
        void longPress(Float x, Float y);
    }
}
