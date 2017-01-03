package com.example.teamalmanac.codealmanac;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.view.View;

import static android.content.Context.MODE_PRIVATE;

public class SensibleView extends View {
    private SensorManager sm;
    private SensorEventListener accelEventListener;
    private Paint p = new Paint();
    private Rect src, dst;
    private float x, y;
    private Bitmap img;

    public SensibleView(Context context) {
        super(context);
        initView(context);
    }

    public SensibleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SensibleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        dst = new Rect(0, 0, w, h);
    }

    private void initView(Context context) {
        img = retrieveImage(context);
        final int imgWidth = img.getWidth();
        final int imgHeight = img.getHeight();
        final int imgWidthMargin = (int) (imgWidth * 0.1);
        final int imgHeightMargin = (int) (imgWidth * 0.1);
        final int imgWidthLen = (int) (imgWidth * 0.9);
        final int imgHeightLen = (int) (imgHeight * 0.9);

        sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float[] acc = sensorEvent.values;
                x = amplify(acc[0], 2, 10);
                y = amplify(acc[1], 2, 10);
                int distanceX = (int) (imgWidthMargin + imgWidthMargin * (x / 10));
                int distanceY = (int) (imgHeightMargin + imgHeightMargin * (y / 10));
                src = new Rect(
                        distanceX,
                        distanceY,
                        imgWidthLen + distanceX,
                        imgHeightLen + distanceY
                );
                invalidate();
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };
        sm.registerListener(
                accelEventListener,
                sm.getDefaultSensor(Sensor.TYPE_GRAVITY),
                SensorManager.SENSOR_DELAY_UI);
    }

    private float amplify(float value, float factor, float limit) {
        int sign = value >= 0 ? 1 : -1;
        return Math.min(Math.abs(value * factor), limit) * sign;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(img, src, dst, p);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        sm.unregisterListener(accelEventListener);
    }

    private Bitmap retrieveImage(Context context) {
        SharedPreferences pref = context.getSharedPreferences("settings", MODE_PRIVATE);
        String s = "bg_" + pref.getInt("backround_image_index", 1);
        int id = context.getResources().getIdentifier(s, "drawable", context.getPackageName());

        return BitmapFactory.decodeResource(getResources(), id);
    }
}
