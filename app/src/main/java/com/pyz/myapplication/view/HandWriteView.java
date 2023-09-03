package com.pyz.myapplication.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.pyz.myapplication.R;

public class HandWriteView extends View {

    private int weight,height;
    private Paint mPaint;
    private int paintColor;
    private int strokeWidth = 10;
    private Path path;
    private float x ;
    private float y ;
    private boolean mUseEraser = false;
    private Bitmap mBufferBitmap;//缓存的bitmap
    private Canvas mBufferCanvas;//缓存的Canvas


    public HandWriteView(Context context) {
        this(context,null);
    }

    public HandWriteView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public HandWriteView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.HandWriteView);
        paintColor = typedArray.getColor(R.styleable.HandWriteView_paintColor,Color.BLACK);
        initView();
    }

    private void initView() {
        mPaint = new Paint();
        path = new Path();
        mPaint.setColor(paintColor);
        mPaint.setStrokeWidth(strokeWidth);
        mPaint.setAntiAlias(true);//抗锯齿
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setXfermode(null);
    }

    /**
     * 视图的绘制工作
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBufferBitmap,0,0,null);
    }

    /**
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mBufferBitmap = Bitmap.createBitmap(getMeasuredWidth(),getMeasuredHeight(),Bitmap.Config.ARGB_8888);
        mBufferCanvas = new Canvas(mBufferBitmap);
    }

    /**
     * 处理触摸事件
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                touchDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(event);
                break;
            case MotionEvent.ACTION_UP:
                //画完一条之后清空路径
                path.reset();
                break;

        }
        invalidate();
        return true;
    }

    /**
     * 手指按下
     * @param event
     */
    private void touchDown(MotionEvent event) {
        float downX = event.getX();
        float downY = event.getY();
        x = downX;
        y = downY;
        path.moveTo(downX,downY);
    }

    /**
     * 手指移动
     * @param event
     */
    private void touchMove(MotionEvent event){
        float moveX = event.getX();
        float moveY = event.getY();
        float previousX = x;
        float previousY = y;
        float absX = Math.abs(moveX - previousX);
        float absY = Math.abs(moveY -previousY);
        if(absX >3 || absY >3 ){
            float cX = (moveX + previousX)/2;
            float cY = (moveY + previousY)/2;
            path.quadTo(previousX,previousY,cX,cY);
            x = moveX;
            y = moveY;
        }
        mBufferCanvas.drawPath(path,mPaint);
    }

    /**
     * 清除线
     */
    public void cleanPath(){
        mBufferCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();
    }

    /**
     * 设置画笔模式
     * @param useEraser
     */
    public void changeEraser(boolean useEraser){
        mUseEraser = useEraser;
        if(useEraser){
            //切换橡皮檫模式
            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        }else {
            mPaint.setXfermode(null);
        }
    }

    public Bitmap getMBufferBitmap(){
        return mBufferBitmap;
    }
}
