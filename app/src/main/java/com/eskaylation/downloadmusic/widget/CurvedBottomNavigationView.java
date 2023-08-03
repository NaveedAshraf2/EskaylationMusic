package com.eskaylation.downloadmusic.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class CurvedBottomNavigationView extends LinearLayout {
    public int CURVE_CIRCLE_RADIUS;
    public Point mFirstCurveControlPoint1 = new Point();
    public Point mFirstCurveControlPoint2 = new Point();
    public Point mFirstCurveEndPoint = new Point();
    public Point mFirstCurveStartPoint = new Point();
    public int mNavigationBarHeight;
    public int mNavigationBarWidth;
    public Paint mPaint;
    public Path mPath;
    public Point mSecondCurveControlPoint1 = new Point();
    public Point mSecondCurveControlPoint2 = new Point();
    public Point mSecondCurveEndPoint = new Point();
    public Point mSecondCurveStartPoint = new Point();

    public CurvedBottomNavigationView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public CurvedBottomNavigationView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView();
    }

    public final void initView() {
        this.mPath = new Path();
        this.mPaint = new Paint(1);
        this.mPaint.setShadowLayer(10.0f, (float) getWidth(), (float) getHeight(), Color.parseColor("#33000000"));
        this.mPaint.setStyle(Style.FILL_AND_STROKE);
        this.mPaint.setColor(-1);
        setBackgroundColor(0);
        this.CURVE_CIRCLE_RADIUS = (int) convertDpToPixel(40.0f);
    }

    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.mNavigationBarWidth = getWidth();
        this.mNavigationBarHeight = getHeight();
        Point point = this.mFirstCurveStartPoint;
        int i5 = this.mNavigationBarWidth / 2;
        int i6 = this.CURVE_CIRCLE_RADIUS;
        point.set((i5 - (i6 * 2)) - (i6 / 3), 8);
        Point point2 = this.mFirstCurveEndPoint;
        int i7 = this.mNavigationBarWidth / 2;
        int i8 = this.CURVE_CIRCLE_RADIUS;
        point2.set(i7, i8 + (i8 / 4));
        Point point3 = this.mFirstCurveControlPoint1;
        Point point4 = this.mFirstCurveStartPoint;
        int i9 = point4.x;
        int i10 = this.CURVE_CIRCLE_RADIUS;
        point3.set(i9 + i10 + (i10 / 4), point4.y);
        Point point5 = this.mFirstCurveControlPoint2;
        Point point6 = this.mFirstCurveEndPoint;
        int i11 = point6.x;
        int i12 = this.CURVE_CIRCLE_RADIUS;
        point5.set((i11 - (i12 * 2)) + i12, point6.y);
        this.mSecondCurveStartPoint = this.mFirstCurveEndPoint;
        Point point7 = this.mSecondCurveEndPoint;
        int i13 = this.mNavigationBarWidth / 2;
        int i14 = this.CURVE_CIRCLE_RADIUS;
        point7.set(i13 + (i14 * 2) + (i14 / 3), 8);
        Point point8 = this.mSecondCurveControlPoint1;
        Point point9 = this.mSecondCurveStartPoint;
        int i15 = point9.x;
        int i16 = this.CURVE_CIRCLE_RADIUS;
        point8.set((i15 + (i16 * 2)) - i16, point9.y);
        Point point10 = this.mSecondCurveControlPoint2;
        Point point11 = this.mSecondCurveEndPoint;
        int i17 = point11.x;
        int i18 = this.CURVE_CIRCLE_RADIUS;
        point10.set(i17 - (i18 + (i18 / 4)), point11.y);
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.mPath.reset();
        this.mPath.moveTo(0.0f, 8.0f);
        Path path = this.mPath;
        Point point = this.mFirstCurveStartPoint;
        path.lineTo((float) point.x, (float) point.y);
        Path path2 = this.mPath;
        Point point2 = this.mFirstCurveControlPoint1;
        float f = (float) point2.x;
        float f2 = (float) point2.y;
        Point point3 = this.mFirstCurveControlPoint2;
        float f3 = (float) point3.x;
        float f4 = (float) point3.y;
        Point point4 = this.mFirstCurveEndPoint;
        path2.cubicTo(f, f2, f3, f4, (float) point4.x, (float) point4.y);
        Path path3 = this.mPath;
        Point point5 = this.mSecondCurveControlPoint1;
        float f5 = (float) point5.x;
        float f6 = (float) point5.y;
        Point point6 = this.mSecondCurveControlPoint2;
        float f7 = (float) point6.x;
        float f8 = (float) point6.y;
        Point point7 = this.mSecondCurveEndPoint;
        path3.cubicTo(f5, f6, f7, f8, (float) point7.x, (float) point7.y);
        this.mPath.lineTo((float) this.mNavigationBarWidth, 8.0f);
        this.mPath.lineTo((float) this.mNavigationBarWidth, (float) this.mNavigationBarHeight);
        this.mPath.lineTo(0.0f, (float) this.mNavigationBarHeight);
        this.mPath.close();
        canvas.drawPath(this.mPath, this.mPaint);
    }

    public static float convertDpToPixel(float f) {
        return (float) Math.round(f * (((float) Resources.getSystem().getDisplayMetrics().densityDpi) / 160.0f));
    }
}
