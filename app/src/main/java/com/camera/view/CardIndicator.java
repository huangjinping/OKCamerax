package com.camera.view;


import android.content.Context;
import android.graphics.*;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import android.app.Activity;

import com.eastbay.camarsx2022.R;


public class CardIndicator extends View {
    private Rect mShowRect = null;
    private Rect mDrawRect = null;
    private Paint mDrawPaint = null;
    private float IDCARD_RATIO = 856.0f / 540.0f;
    private float CONTENT_RATIO = 1f;
    private float SHOW_CONTENT_RATIO = CONTENT_RATIO * 13.0f / 16.0f;
    private Rect mTmpRect = null;
    private int backColor = 0x00000000;

    private void init() {
        mShowRect = new Rect();
        mDrawRect = new Rect();
        mTmpRect = new Rect();
        mDrawPaint = new Paint();
        mDrawPaint.setDither(true);
        mDrawPaint.setAntiAlias(true);
        mDrawPaint.setStrokeWidth(10);
        mDrawPaint.setStyle(Paint.Style.STROKE);
        mDrawPaint.setColor(0xff0000ff);
    }

    public void setBackColor(Activity activity, int backColor) {
        if (this.backColor != backColor) {
            this.backColor = backColor;
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    CardIndicator.this.invalidate();
                }
            });
        }
    }

    public CardIndicator(Context context) {
        super(context);
        init();
    }

    public CardIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CardIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int centerX = width >> 1;
        int centerY = height >> 1;
        int content_width = 0;
        int content_height = 0;
        if (width / (float) (height) < IDCARD_RATIO) // the view is to high
        {
            content_width = (int) (width * SHOW_CONTENT_RATIO);
            content_height = (int) (content_width / IDCARD_RATIO);
        } else { // the view is too wide
            content_height = (int) (height * SHOW_CONTENT_RATIO);
            content_width = (int) (content_height * IDCARD_RATIO);
        }

        mShowRect.left = centerX - content_width / 2;
        mShowRect.top = centerY - content_height / 2;
        mShowRect.right = centerX + content_width / 2;
        mShowRect.bottom = centerY + content_height / 2;
        getActualRect(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

    }

    private boolean mIsVertical;

    public void setCardSideAndOrientation(boolean mIsVertical) {
        this.mIsVertical = mIsVertical;
    }


    public void setContentRatio(boolean mIsVertical) {
        this.mIsVertical = mIsVertical;
        if (mIsVertical) CONTENT_RATIO = 1.0f;
        else CONTENT_RATIO = 0.8f;

        SHOW_CONTENT_RATIO = CONTENT_RATIO * 13.0f / 16.0f;
        invalidate();
    }

    /**
     * 实际给算法的大小
     */
    private void getActualRect(int width, int height) {

        int content_width = 0;
        int content_height = 0;
        int centerX = width >> 1;
        int centerY = height >> 1;

        if (width / (float) (height) < IDCARD_RATIO) // the view is to high
        {
            content_width = (int) (width * CONTENT_RATIO);
            content_height = (int) (content_width / IDCARD_RATIO);
        } else { // the view is too wide
            content_height = (int) (height * CONTENT_RATIO);
            content_width = (int) (content_height * IDCARD_RATIO);
        }

        mDrawRect.left = centerX - content_width / 2;
        mDrawRect.top = centerY - content_height / 2;
        mDrawRect.right = centerX + content_width / 2;
        mDrawRect.bottom = centerY + content_height / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // background
        mDrawPaint.setStyle(Paint.Style.FILL);
        mDrawPaint.setColor(backColor);

        // top
        mTmpRect.set(0, 0, getWidth(), mShowRect.top);
        canvas.drawRect(mTmpRect, mDrawPaint);
        // bottom
        mTmpRect.set(0, mShowRect.bottom, getWidth(), getHeight());
        canvas.drawRect(mTmpRect, mDrawPaint);
        // left
        mTmpRect.set(0, mShowRect.top, mShowRect.left, mShowRect.bottom);
        canvas.drawRect(mTmpRect, mDrawPaint);
        // right
        mTmpRect.set(mShowRect.right, mShowRect.top, getWidth(), mShowRect.bottom);
        canvas.drawRect(mTmpRect, mDrawPaint);

//        drawViewfinder(canvas);
        super.onDraw(canvas);
    }


    private void drawViewfinder(Canvas canvas) {
        int finderColor = 0XFF00D3FF;
        mDrawPaint.setStyle(Paint.Style.STROKE);
        mDrawPaint.setColor(finderColor);
        mDrawPaint.setStrokeWidth(4);
        int length = mShowRect.height() / 16;
        // 四个蓝色的角
        // left top
        canvas.drawLine(mShowRect.left, mShowRect.top, mShowRect.left + length, mShowRect.top, mDrawPaint);
        canvas.drawLine(mShowRect.left, mShowRect.top, mShowRect.left, mShowRect.top + length, mDrawPaint);

        // right top
        canvas.drawLine(mShowRect.right, mShowRect.top, mShowRect.right - length, mShowRect.top, mDrawPaint);
        canvas.drawLine(mShowRect.right, mShowRect.top, mShowRect.right, mShowRect.top + length, mDrawPaint);

        // left bottom
        canvas.drawLine(mShowRect.left, mShowRect.bottom, mShowRect.left + length, mShowRect.bottom, mDrawPaint);
        canvas.drawLine(mShowRect.left, mShowRect.bottom, mShowRect.left, mShowRect.bottom - length, mDrawPaint);

        // right bottom
        canvas.drawLine(mShowRect.right, mShowRect.bottom, mShowRect.right - length, mShowRect.bottom, mDrawPaint);
        canvas.drawLine(mShowRect.right, mShowRect.bottom, mShowRect.right, mShowRect.bottom - length, mDrawPaint);

        // 两个角中间的线
        mDrawPaint.setColor(0XBBFFFFFF);
        mDrawPaint.setStrokeWidth(2);
        // top
        canvas.drawLine(mShowRect.left + length, mShowRect.top, mShowRect.right - length, mShowRect.top, mDrawPaint);
        // left
        canvas.drawLine(mShowRect.left, mShowRect.top + length, mShowRect.left, mShowRect.bottom - length, mDrawPaint);
        // right
        canvas.drawLine(mShowRect.right, mShowRect.top + length, mShowRect.right, mShowRect.bottom - length, mDrawPaint);
        // bottom
        canvas.drawLine(mShowRect.left + length, mShowRect.bottom, mShowRect.right - length, mShowRect.bottom, mDrawPaint);
        int bitmapId = 0;
        bitmapId = R.mipmap.sfz_back;
        Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), bitmapId);
        Rect mSrcRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        Rect mDesRect = new Rect(mShowRect.left, mShowRect.top, mShowRect.left + mShowRect.width(), mShowRect.top + mShowRect.height());
        canvas.drawBitmap(bitmap, mSrcRect, mDesRect, null);
    }

    public RectF getShowPosition() {
        RectF rectF = new RectF();
        rectF.left = mShowRect.left / (float) getWidth();
        rectF.top = mShowRect.top / (float) getHeight();
        rectF.right = mShowRect.right / (float) getWidth();
        rectF.bottom = mShowRect.bottom / (float) getHeight();
        return rectF;
    }

    public RectF getPosition() {
        RectF rectF = new RectF();
        rectF.left = mDrawRect.left / (float) getWidth();
        rectF.top = mDrawRect.top / (float) getHeight();
        rectF.right = mDrawRect.right / (float) getWidth();
        rectF.bottom = mDrawRect.bottom / (float) getHeight();
        return rectF;
    }

    public Rect getMargin() {
        Rect rect = new Rect();
        rect.left = mDrawRect.left;
        rect.top = mDrawRect.top;
        rect.right = getWidth() - mDrawRect.right;
        rect.bottom = getHeight() - mDrawRect.bottom;
        return rect;
    }
}


