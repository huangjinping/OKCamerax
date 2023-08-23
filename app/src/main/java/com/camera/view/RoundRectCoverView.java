package com.camera.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.eastbay.camarsx2022.R;

import java.lang.reflect.Type;

//https://www.freesion.com/article/72511146922/
//https://github.com/jhbxyz/CustomViewCollection/tree/master
public class RoundRectCoverView extends View {

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float mPadding = 40; //间距
    private float mRoundCorner = 10;//圆角矩形的角度
    private int mCoverColor = 0x99000000;//遮罩的颜色

    private RectF bounds = new RectF();
    private PorterDuffXfermode porterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT);
    private Path clipPath = new Path();
    private Context mContext;

    public RoundRectCoverView(Context context) {
        super(context);
        init(context, null);

    }

    public RoundRectCoverView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);

    }

    public RoundRectCoverView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        //开启View级别的离屏缓冲,并关闭硬件加速，使用软件绘制
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RoundRectCoverView);
        mPadding = ta.getDimension(R.styleable.RoundRectCoverView_roundPadding, 0f);
        mRoundCorner = ta.getDimension(R.styleable.RoundRectCoverView_roundCorner, 10f);
        mCoverColor = ta.getColor(R.styleable.RoundRectCoverView_roundCoverColor, 0x99000000);
        ta.recycle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        bounds.set(0f, 0f, getWidth(), getHeight());
        clipPath.addRoundRect(mPadding, mPadding, getWidth() - mPadding, getHeight() - mPadding, mRoundCorner, mRoundCorner, Path.Direction.CW);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        fun1(canvas);
//        fun2(canvas);
//        fun3(canvas);
    }

    /**
     * 方法一通过 paint 的 xfermode 绘制遮罩
     */
    public void fun1(Canvas canvas) {
        //先画一个圆角矩形,也就是透明区域

        RectF rel = new RectF(mPadding, mPadding, getWidth() - mPadding, getHeight() - mPadding);
        canvas.drawOval(rel, paint);
//        canvas.drawRoundRect(mPadding, mPadding, getWidth() - mPadding, getHeight() - mPadding, mRoundCorner, mRoundCorner, paint);
        //设置遮罩的颜色

        paint.setColor(mCoverColor);
        //设置paint的 xfermode 为PorterDuff.Mode.SRC_OUT

        paint.setXfermode(porterDuffXfermode);
        //画遮罩的矩形

        canvas.drawRect(0f, 0f, getWidth(), getHeight(), paint);
        //清空paint 的 xfermode

        paint.setXfermode(null);
    }

    /**
     * 方法二通过 canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.SRC)绘制遮罩
     */
    private void fun2(Canvas canvas) {
        //Canvas的离屏缓冲
        int count = canvas.saveLayer(bounds, paint);
        //KTX的扩展函数相当于对Canvas的 save 和 restore 操作
        canvas.save();
        //画遮罩的颜色
        canvas.drawColor(mCoverColor);
        //按Path来裁切
        canvas.clipPath(clipPath);

        //画镂空的范围
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.SRC);
        canvas.restore();

        //把离屏缓冲的内容,绘制到View上去
        canvas.restoreToCount(count);

    }


    public void fun3(Canvas canvas) {
        //先画一个圆角矩形,也就是透明区域
        float radius = getWidth() / 4;
        canvas.drawCircle(getWidth() / 2, radius + mPadding, radius, paint);
        //设置遮罩的颜色
        paint.setColor(mCoverColor);
        //设置paint的 xfermode 为PorterDuff.Mode.SRC_OUT

        paint.setXfermode(porterDuffXfermode);
        //画遮罩的矩形

        canvas.drawRect(0f, 0f, getWidth(), getHeight(), paint);
        //清空paint 的 xfermode

        paint.setXfermode(null);
    }
}
