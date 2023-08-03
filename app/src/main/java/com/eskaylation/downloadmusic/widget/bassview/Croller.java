package com.eskaylation.downloadmusic.widget.bassview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.eskaylation.downloadmusic.R;
import com.eskaylation.downloadmusic.widget.bassview.utilities.Utils;

public class Croller extends View {
    public int backCircleColor = Color.parseColor("#222222");
    public int backCircleDisabledColor;
    public float backCircleRadius;
    public Bitmap centerBitmap;
    public Paint circleCenter;
    public Paint circlePaint;
    public Paint circlePaint2;
    public float currdeg = 0.0f;
    public float deg = 3.0f;
    public float downdeg = 0.0f;
    public int indicatorColor;
    public int indicatorDisabledColor;
    public float indicatorWidth;
    public boolean isAntiClockwise;
    public boolean isContinuous = false;
    public boolean isEnabled;
    public String label;
    public int labelColor;
    public int labelDisabledColor;
    public String labelFont;
    public float labelSize;
    public int labelStyle;
    public Paint linePaint;
    public OnCrollerChangeListener mCrollerChangeListener;
    public onProgressChangedListener mProgressChangeListener;
    public int mainCircleColor = Color.parseColor("#000000");
    public int mainCircleDisabledColor;
    public float mainCircleRadius;
    public int max;
    public float midx;
    public float midy;
    public int min;
    public RectF oval;
    public float progressPrimaryCircleSize;
    public int progressPrimaryColor;
    public int progressPrimaryDisabledColor;
    public float progressPrimaryStrokeWidth;
    public float progressRadius;
    public float progressSecondaryCircleSize;
    public int progressSecondaryColor;
    public int progressSecondaryDisabledColor;
    public float progressSecondaryStrokeWidth;
    public boolean startEventSent;
    public int startOffset;
    public int startOffset2;
    public int sweepAngle;
    public Paint textPaint;

    public interface onProgressChangedListener {
        void onProgressChanged(int i);
    }

    public void setOnProgressChangedListener(onProgressChangedListener onprogresschangedlistener) {
        this.mProgressChangeListener = onprogresschangedlistener;
    }

    public void setOnCrollerChangeListener(OnCrollerChangeListener onCrollerChangeListener) {
        this.mCrollerChangeListener = onCrollerChangeListener;
    }

    public Croller(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        String str = "#FFA036";
        this.indicatorColor = Color.parseColor(str);
        this.progressPrimaryColor = Color.parseColor(str);
        this.progressSecondaryColor = Color.parseColor("#111111");
        this.backCircleDisabledColor = Color.parseColor("#82222222");
        this.mainCircleDisabledColor = Color.parseColor("#82000000");
        String str2 = "#82FFA036";
        this.indicatorDisabledColor = Color.parseColor(str2);
        this.progressPrimaryDisabledColor = Color.parseColor(str2);
        this.progressSecondaryDisabledColor = Color.parseColor("#82111111");
        this.progressPrimaryCircleSize = -1.0f;
        this.progressSecondaryCircleSize = -1.0f;
        this.progressPrimaryStrokeWidth = 25.0f;
        this.progressSecondaryStrokeWidth = 10.0f;
        this.mainCircleRadius = -1.0f;
        this.backCircleRadius = -1.0f;
        this.progressRadius = -1.0f;
        this.max = 25;
        this.min = 1;
        this.indicatorWidth = 7.0f;
        this.label = "Label";
        this.labelStyle = 0;
        this.labelSize = 14.0f;
        this.labelColor = -1;
        this.labelDisabledColor = -16777216;
        this.startOffset = 30;
        this.startOffset2 = 0;
        this.sweepAngle = -1;
        this.isEnabled = true;
        this.isAntiClockwise = false;
        this.startEventSent = false;
        initXMLAttrs(context, attributeSet);
        init();
    }

    public Croller(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        String str = "#FFA036";
        this.indicatorColor = Color.parseColor(str);
        this.progressPrimaryColor = Color.parseColor(str);
        this.progressSecondaryColor = Color.parseColor("#111111");
        this.backCircleDisabledColor = Color.parseColor("#82222222");
        this.mainCircleDisabledColor = Color.parseColor("#82000000");
        String str2 = "#82FFA036";
        this.indicatorDisabledColor = Color.parseColor(str2);
        this.progressPrimaryDisabledColor = Color.parseColor(str2);
        this.progressSecondaryDisabledColor = Color.parseColor("#82111111");
        this.progressPrimaryCircleSize = -1.0f;
        this.progressSecondaryCircleSize = -1.0f;
        this.progressPrimaryStrokeWidth = 25.0f;
        this.progressSecondaryStrokeWidth = 10.0f;
        this.mainCircleRadius = -1.0f;
        this.backCircleRadius = -1.0f;
        this.progressRadius = -1.0f;
        this.max = 25;
        this.min = 1;
        this.indicatorWidth = 7.0f;
        this.label = "Label";
        this.labelStyle = 0;
        this.labelSize = 14.0f;
        this.labelColor = -1;
        this.labelDisabledColor = -16777216;
        this.startOffset = 30;
        this.startOffset2 = 0;
        this.sweepAngle = -1;
        this.isEnabled = true;
        this.isAntiClockwise = false;
        this.startEventSent = false;
        initXMLAttrs(context, attributeSet);
        init();
    }

    public final void init() {
        this.textPaint = new Paint();
        this.textPaint.setAntiAlias(true);
        this.textPaint.setStyle(Style.FILL);
        this.textPaint.setFakeBoldText(false);
        this.textPaint.setTextAlign(Align.CENTER);
        this.textPaint.setTextSize(this.labelSize);
        generateTypeface();
        this.circlePaint = new Paint(1);
        this.circlePaint.setAntiAlias(true);
        this.circlePaint.setStrokeWidth(this.progressSecondaryStrokeWidth);
        this.circlePaint.setStyle(Style.FILL);
        this.circlePaint2 = new Paint(1);
        this.circlePaint2.setAntiAlias(true);
        this.circlePaint2.setStrokeWidth(this.progressPrimaryStrokeWidth);
        this.circlePaint2.setStyle(Style.FILL);
        this.circleCenter = new Paint(1);
        this.circleCenter.setAntiAlias(true);
        this.circleCenter.setStrokeWidth(10.0f);
        this.circleCenter.setStyle(Style.STROKE);
        this.linePaint = new Paint(1);
        this.linePaint.setAntiAlias(true);
        this.linePaint.setStrokeWidth(this.indicatorWidth);
        if (this.isEnabled) {
            this.circlePaint2.setColor(this.progressPrimaryColor);
            this.circlePaint.setColor(this.progressSecondaryColor);
            this.linePaint.setColor(this.indicatorColor);
            this.textPaint.setColor(this.labelColor);
        } else {
            this.circlePaint2.setColor(this.progressPrimaryDisabledColor);
            this.circlePaint.setColor(this.progressSecondaryDisabledColor);
            this.linePaint.setColor(this.indicatorDisabledColor);
            this.textPaint.setColor(this.labelDisabledColor);
        }
        this.oval = new RectF();
    }

    public final void generateTypeface() {
        Typeface typeface = Typeface.DEFAULT;
        if (getLabelFont() != null && !getLabelFont().isEmpty()) {
            typeface = Typeface.createFromAsset(getContext().getAssets(), getLabelFont());
        }
        int labelStyle2 = getLabelStyle();
        if (labelStyle2 == 0) {
            this.textPaint.setTypeface(typeface);
        } else if (labelStyle2 == 1) {
            this.textPaint.setTypeface(Typeface.create(typeface, 1));
        } else if (labelStyle2 == 2) {
            this.textPaint.setTypeface(Typeface.create(typeface, 2));
        } else if (labelStyle2 == 3) {
            this.textPaint.setTypeface(Typeface.create(typeface, 3));
        }
    }

    public final void initXMLAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Croller);

        setEnabled(a.getBoolean(R.styleable.Croller_enabled, true));
        setProgress(a.getInt(R.styleable.Croller_start_progress, 1));
        setLabel(a.getString(R.styleable.Croller_label));

        setBackCircleColor(a.getColor(R.styleable.Croller_back_circle_color, backCircleColor));
        setMainCircleColor(a.getColor(R.styleable.Croller_main_circle_color, mainCircleColor));
        setIndicatorColor(a.getColor(R.styleable.Croller_indicator_color, indicatorColor));
        setProgressPrimaryColor(a.getColor(R.styleable.Croller_progress_primary_color, progressPrimaryColor));
        setProgressSecondaryColor(a.getColor(R.styleable.Croller_progress_secondary_color, progressSecondaryColor));

        setBackCircleDisabledColor(a.getColor(R.styleable.Croller_back_circle_disable_color, backCircleDisabledColor));
        setMainCircleDisabledColor(a.getColor(R.styleable.Croller_main_circle_disable_color, mainCircleDisabledColor));
        setIndicatorDisabledColor(a.getColor(R.styleable.Croller_indicator_disable_color, indicatorDisabledColor));
        setProgressPrimaryDisabledColor(a.getColor(R.styleable.Croller_progress_primary_disable_color, progressPrimaryDisabledColor));
        setProgressSecondaryDisabledColor(a.getColor(R.styleable.Croller_progress_secondary_disable_color, progressSecondaryDisabledColor));

        setLabelSize(a.getDimension(R.styleable.Croller_label_size, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                labelSize, getResources().getDisplayMetrics())));
        setLabelColor(a.getColor(R.styleable.Croller_label_color, labelColor));
        setlabelDisabledColor(a.getColor(R.styleable.Croller_label_disabled_color, labelDisabledColor));
        setLabelFont(a.getString(R.styleable.Croller_label_font));
        setLabelStyle(a.getInt(R.styleable.Croller_label_style, 0));
        setIndicatorWidth(a.getFloat(R.styleable.Croller_indicator_width, 7));
        setIsContinuous(a.getBoolean(R.styleable.Croller_is_continuous, false));
        setProgressPrimaryCircleSize(a.getFloat(R.styleable.Croller_progress_primary_circle_size, -1));
        setProgressSecondaryCircleSize(a.getFloat(R.styleable.Croller_progress_secondary_circle_size, -1));
        setProgressPrimaryStrokeWidth(a.getFloat(R.styleable.Croller_progress_primary_stroke_width, 25));
        setProgressSecondaryStrokeWidth(a.getFloat(R.styleable.Croller_progress_secondary_stroke_width, 10));
        setSweepAngle(a.getInt(R.styleable.Croller_sweep_angle, -1));
        setStartOffset(a.getInt(R.styleable.Croller_start_offset, 30));
        setMax(a.getInt(R.styleable.Croller_max, 25));
        setMin(a.getInt(R.styleable.Croller_min, 1));
        deg = min + 2;
        setBackCircleRadius(a.getFloat(R.styleable.Croller_back_circle_radius, -1));
        setProgressRadius(a.getFloat(R.styleable.Croller_progress_radius, -1));
        setAntiClockwise(a.getBoolean(R.styleable.Croller_anticlockwise, false));
        a.recycle();
    }

    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        int convertDpToPixel = (int) Utils.convertDpToPixel(180.0f, getContext());
        int convertDpToPixel2 = (int) Utils.convertDpToPixel(180.0f, getContext());
        int mode = MeasureSpec.getMode(i);
        int size = MeasureSpec.getSize(i);
        int mode2 = MeasureSpec.getMode(i2);
        int size2 = MeasureSpec.getSize(i2);
        int i3 = mode == 1073741824 ? size : mode == Integer.MIN_VALUE ? Math.min(convertDpToPixel, size) : size2;
        if (mode2 == 1073741824) {
            size = size2;
        } else if (mode2 == Integer.MIN_VALUE) {
            size = Math.min(convertDpToPixel2, size2);
        }
        if (mode == 0 && mode2 == 0) {
            size = convertDpToPixel2;
        } else {
            convertDpToPixel = i3;
        }
        this.centerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg_arc);
        setMeasuredDimension(convertDpToPixel, size);
    }

    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        this.midx = (float) (getWidth() / 2);
        this.midy = (float) (getHeight() / 2);
    }

    public void onDraw(Canvas canvas) {
        Canvas canvas2 = canvas;
        super.onDraw(canvas);
        onProgressChangedListener onprogresschangedlistener = this.mProgressChangeListener;
        if (onprogresschangedlistener != null) {
            onprogresschangedlistener.onProgressChanged((int) (this.deg - 2.0f));
        }
        OnCrollerChangeListener onCrollerChangeListener = this.mCrollerChangeListener;
        if (onCrollerChangeListener != null) {
            onCrollerChangeListener.onProgressChanged(this, (int) (this.deg - 2.0f));
        }
        if (this.isEnabled) {
            this.circlePaint2.setColor(this.progressPrimaryColor);
            this.circlePaint.setColor(this.progressSecondaryColor);
            this.linePaint.setColor(this.indicatorColor);
            this.textPaint.setColor(this.labelColor);
        } else {
            this.circlePaint2.setColor(this.progressPrimaryDisabledColor);
            this.circlePaint.setColor(this.progressSecondaryDisabledColor);
            this.linePaint.setColor(this.indicatorDisabledColor);
            this.textPaint.setColor(this.labelDisabledColor);
        }
        double d = 1.0d;
        float f = 360.0f;
        if (!this.isContinuous) {
            this.startOffset2 = this.startOffset - 15;
            this.linePaint.setStrokeWidth(this.indicatorWidth);
            this.textPaint.setTextSize(this.labelSize);
            int min2 = (int) (Math.min(this.midx, this.midy) * 0.90625f);
            if (this.sweepAngle == -1) {
                this.sweepAngle = 360 - (this.startOffset2 * 2);
            }
            if (this.mainCircleRadius == -1.0f) {
                this.mainCircleRadius = ((float) min2) * 0.73333335f;
            }
            if (this.backCircleRadius == -1.0f) {
                this.backCircleRadius = ((float) min2) * 0.8666667f;
            }
            if (this.progressRadius == -1.0f) {
                this.progressRadius = (float) min2;
            }
            Bitmap bitmap = this.centerBitmap;
            float f2 = this.backCircleRadius;
            Bitmap createScaledBitmap = Bitmap.createScaledBitmap(bitmap, (int) (f2 * 2.0f), (int) (f2 * 2.0f), false);
            float max2 = Math.max(3.0f, this.deg);
            float min3 = Math.min(this.deg, (float) (this.max + 2));
            int i = (int) max2;
            while (true) {
                int i2 = this.max;
                if (i >= i2 + 3) {
                    break;
                }
                float f3 = (((float) this.startOffset2) / f) + (((((float) this.sweepAngle) / f) * ((float) i)) / ((float) (i2 + 5)));
                if (this.isAntiClockwise) {
                    f3 = 1.0f - f3;
                }
                double d2 = (d - ((double) f3)) * 6.283185307179586d;
                float sin = this.midx + ((float) (((((double) this.progressRadius) * Math.sin(d2)) / 3.0d) + ((((double) this.progressRadius) * Math.sin(d2)) / 2.0d)));
                float cos = this.midy + ((float) (((((double) this.progressRadius) * Math.cos(d2)) / 3.0d) + ((((double) this.progressRadius) * Math.cos(d2)) / 2.0d)));
                float f4 = this.progressSecondaryCircleSize;
                if (f4 == -1.0f) {
                    canvas2.drawCircle(sin, cos, (((float) min2) / 30.0f) * (20.0f / ((float) this.max)) * (((float) this.sweepAngle) / 270.0f), this.circlePaint);
                } else {
                    canvas2.drawCircle(sin, cos, f4, this.circlePaint);
                }
                i++;
                d = 1.0d;
                f = 360.0f;
            }
            int i3 = 3;
            while (true) {
                float f5 = (float) i3;
                if (f5 > min3) {
                    break;
                }
                float f6 = (((float) this.startOffset2) / 360.0f) + (((((float) this.sweepAngle) / 360.0f) * f5) / ((float) (this.max + 5)));
                if (this.isAntiClockwise) {
                    f6 = 1.0f - f6;
                }
                double d3 = (1.0d - ((double) f6)) * 6.283185307179586d;
                float sin2 = this.midx + ((float) (((((double) this.progressRadius) * Math.sin(d3)) / 3.0d) + ((((double) this.progressRadius) * Math.sin(d3)) / 2.0d)));
                float cos2 = this.midy + ((float) (((((double) this.progressRadius) * Math.cos(d3)) / 3.0d) + ((((double) this.progressRadius) * Math.cos(d3)) / 2.0d)));
                float f7 = this.progressPrimaryCircleSize;
                if (f7 == -1.0f) {
                    canvas2.drawCircle(sin2, cos2, (this.progressRadius / 15.0f) * (20.0f / ((float) this.max)) * (((float) this.sweepAngle) / 270.0f), this.circlePaint2);
                } else {
                    canvas2.drawCircle(sin2, cos2, f7, this.circlePaint2);
                }
                i3++;
            }
            float f8 = (((float) this.startOffset2) / 360.0f) + (((((float) this.sweepAngle) / 360.0f) * this.deg) / ((float) (this.max + 5)));
            if (this.isAntiClockwise) {
                f8 = 1.0f - f8;
            }
            float f9 = (float) min2;
            double d4 = (double) (0.4f * f9);
            double d5 = (1.0d - ((double) f8)) * 6.283185307179586d;
            float sin3 = this.midx + ((float) (Math.sin(d5) * d4));
            double d6 = (double) (f9 * 0.6f);
            float cos3 = ((float) (d4 * Math.cos(d5))) + this.midy;
            float sin4 = this.midx + ((float) (Math.sin(d5) * d6));
            float cos4 = this.midy + ((float) (d6 * Math.cos(d5)));
            int i4 = min2;
            canvas.drawLine(sin3, cos3, sin4, cos4, this.linePaint);
            if (this.isEnabled) {
                this.circleCenter.setColor(this.progressPrimaryColor);
            } else {
                this.circleCenter.setColor(this.progressSecondaryColor);
            }
            canvas2.drawText("min", (float) ((getWidth() - (getWidth() / 2)) - (createScaledBitmap.getWidth() / 2)), (float) ((getHeight() / 2) + 70), this.textPaint);
            canvas2.drawText("max", (float) (getWidth() - (getWidth() - ((getWidth() / 2) + (createScaledBitmap.getWidth() / 2)))), (float) ((getHeight() / 2) + 70), this.textPaint);
            try {
                canvas2.drawText(this.label, this.midx, (this.midy + ((float) (((double) i4) * 1.1d))) - this.textPaint.getFontMetrics().descent, this.textPaint);
            } catch (Exception unused) {
            }
        } else {
            int min4 = (int) (Math.min(this.midx, this.midy) * 0.90625f);
            if (this.sweepAngle == -1) {
                this.sweepAngle = 360 - (this.startOffset * 2);
            }
            if (this.mainCircleRadius == -1.0f) {
                this.mainCircleRadius = ((float) min4) * 0.73333335f;
            }
            if (this.backCircleRadius == -1.0f) {
                this.backCircleRadius = ((float) min4) * 0.8666667f;
            }
            if (this.progressRadius == -1.0f) {
                this.progressRadius = (float) min4;
            }
            this.circlePaint.setStrokeWidth(this.progressSecondaryStrokeWidth);
            this.circlePaint.setStyle(Style.STROKE);
            this.circlePaint2.setStrokeWidth(this.progressPrimaryStrokeWidth);
            this.circlePaint2.setStyle(Style.STROKE);
            this.linePaint.setStrokeWidth(this.indicatorWidth);
            this.textPaint.setTextSize(this.labelSize);
            float min5 = Math.min(this.deg, (float) (this.max + 2));
            RectF rectF = this.oval;
            float f10 = this.midx;
            float f11 = this.progressRadius;
            float f12 = f10 - f11;
            float f13 = this.midy;
            rectF.set(f12, f13 - f11, f10 + f11, f13 + f11);
            canvas.drawArc(this.oval, ((float) this.startOffset) + 90.0f, (float) this.sweepAngle, false, this.circlePaint);
            if (this.isAntiClockwise) {
                canvas.drawArc(this.oval, 90.0f - ((float) this.startOffset), (min5 - 2.0f) * (((float) this.sweepAngle) / ((float) this.max)) * -1.0f, false, this.circlePaint2);
            } else {
                canvas.drawArc(this.oval, ((float) this.startOffset) + 90.0f, (min5 - 2.0f) * (((float) this.sweepAngle) / ((float) this.max)), false, this.circlePaint2);
            }
            float f14 = (((float) this.startOffset) / 360.0f) + ((((float) this.sweepAngle) / 360.0f) * ((this.deg - 2.0f) / ((float) this.max)));
            if (this.isAntiClockwise) {
                f14 = 1.0f - f14;
            }
            float f15 = (float) min4;
            double d7 = (double) (0.4f * f15);
            double d8 = (1.0d - ((double) f14)) * 6.283185307179586d;
            float sin5 = this.midx + ((float) (Math.sin(d8) * d7));
            float cos5 = ((float) (d7 * Math.cos(d8))) + this.midy;
            double d9 = (double) (f15 * 0.6f);
            float sin6 = this.midx + ((float) (Math.sin(d8) * d9));
            float cos6 = this.midy + ((float) (d9 * Math.cos(d8)));
            try {
                canvas2.drawText(this.label, this.midx, (this.midy + ((float) (((double) min4) * 1.1d))) - this.textPaint.getFontMetrics().descent, this.textPaint);
            } catch (Exception unused2) {
            }
            canvas.drawLine(sin5, cos5, sin6, cos6, this.linePaint);
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!this.isEnabled) {
            return false;
        }
        if (Utils.getDistance(motionEvent.getX(), motionEvent.getY(), this.midx, this.midy) > Math.max(this.mainCircleRadius, Math.max(this.backCircleRadius, this.progressRadius))) {
            if (this.startEventSent) {
                OnCrollerChangeListener onCrollerChangeListener = this.mCrollerChangeListener;
                if (onCrollerChangeListener != null) {
                    onCrollerChangeListener.onStopTrackingTouch(this);
                    this.startEventSent = false;
                }
            }
            return super.onTouchEvent(motionEvent);
        } else if (motionEvent.getAction() == 0) {
            this.downdeg = (float) ((Math.atan2((double) (motionEvent.getY() - this.midy), (double) (motionEvent.getX() - this.midx)) * 180.0d) / 3.141592653589793d);
            this.downdeg -= 90.0f;
            float f = this.downdeg;
            if (f < 0.0f) {
                this.downdeg = f + 360.0f;
            }
            this.downdeg = (float) Math.floor((double) ((this.downdeg / 360.0f) * ((float) (this.max + 5))));
            OnCrollerChangeListener onCrollerChangeListener2 = this.mCrollerChangeListener;
            if (onCrollerChangeListener2 != null) {
                onCrollerChangeListener2.onStartTrackingTouch(this);
                this.startEventSent = true;
            }
            return true;
        } else if (motionEvent.getAction() == 2) {
            this.currdeg = (float) ((Math.atan2((double) (motionEvent.getY() - this.midy), (double) (motionEvent.getX() - this.midx)) * 180.0d) / 3.141592653589793d);
            this.currdeg -= 90.0f;
            float f2 = this.currdeg;
            if (f2 < 0.0f) {
                this.currdeg = f2 + 360.0f;
            }
            this.currdeg = (float) Math.floor((double) ((this.currdeg / 360.0f) * ((float) (this.max + 5))));
            float f3 = this.currdeg;
            int i = this.max;
            if (f3 / ((float) (i + 4)) <= 0.75f || (this.downdeg - 0.0f) / ((float) (i + 4)) >= 0.25f) {
                float f4 = this.downdeg;
                int i2 = this.max;
                if (f4 / ((float) (i2 + 4)) <= 0.75f || (this.currdeg - 0.0f) / ((float) (i2 + 4)) >= 0.25f) {
                    if (this.isAntiClockwise) {
                        this.deg -= this.currdeg - this.downdeg;
                    } else {
                        this.deg += this.currdeg - this.downdeg;
                    }
                    float f5 = this.deg;
                    int i3 = this.max;
                    if (f5 > ((float) (i3 + 2))) {
                        this.deg = (float) (i3 + 2);
                    }
                    float f6 = this.deg;
                    int i4 = this.min;
                    if (f6 < ((float) (i4 + 2))) {
                        this.deg = (float) (i4 + 2);
                    }
                } else if (this.isAntiClockwise) {
                    this.deg -= 1.0f;
                    float f7 = this.deg;
                    int i5 = this.min;
                    if (f7 < ((float) (i5 + 2))) {
                        this.deg = (float) (i5 + 2);
                    }
                } else {
                    this.deg += 1.0f;
                    if (this.deg > ((float) (i2 + 2))) {
                        this.deg = (float) (i2 + 2);
                    }
                }
            } else if (this.isAntiClockwise) {
                this.deg += 1.0f;
                if (this.deg > ((float) (i + 2))) {
                    this.deg = (float) (i + 2);
                }
            } else {
                this.deg -= 1.0f;
                float f8 = this.deg;
                int i6 = this.min;
                if (f8 < ((float) (i6 + 2))) {
                    this.deg = (float) (i6 + 2);
                }
            }
            this.downdeg = this.currdeg;
            invalidate();
            return true;
        } else if (motionEvent.getAction() != 1) {
            return super.onTouchEvent(motionEvent);
        } else {
            OnCrollerChangeListener onCrollerChangeListener3 = this.mCrollerChangeListener;
            if (onCrollerChangeListener3 != null) {
                onCrollerChangeListener3.onStopTrackingTouch(this);
                this.startEventSent = false;
            }
            return true;
        }
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (getParent() != null && motionEvent.getAction() == 0) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    public boolean isEnabled() {
        return this.isEnabled;
    }

    public void setEnabled(boolean z) {
        this.isEnabled = z;
        invalidate();
    }

    public int getProgress() {
        return (int) (this.deg - 2.0f);
    }

    public void setProgress(int i) {
        this.deg = (float) (i + 2);
        invalidate();
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String str) {
        this.label = str;
        invalidate();
    }

    public int getBackCircleColor() {
        return this.backCircleColor;
    }

    public void setBackCircleColor(int i) {
        this.backCircleColor = i;
        invalidate();
    }

    public int getMainCircleColor() {
        return this.mainCircleColor;
    }

    public void setMainCircleColor(int i) {
        this.mainCircleColor = i;
        invalidate();
    }

    public int getIndicatorColor() {
        return this.indicatorColor;
    }

    public void setIndicatorColor(int i) {
        this.indicatorColor = i;
        invalidate();
    }

    public int getProgressPrimaryColor() {
        return this.progressPrimaryColor;
    }

    public void setProgressPrimaryColor(int i) {
        this.progressPrimaryColor = i;
        invalidate();
    }

    public int getProgressSecondaryColor() {
        return this.progressSecondaryColor;
    }

    public void setProgressSecondaryColor(int i) {
        this.progressSecondaryColor = i;
        invalidate();
    }

    public int getBackCircleDisabledColor() {
        return this.backCircleDisabledColor;
    }

    public void setBackCircleDisabledColor(int i) {
        this.backCircleDisabledColor = i;
        invalidate();
    }

    public int getMainCircleDisabledColor() {
        return this.mainCircleDisabledColor;
    }

    public void setMainCircleDisabledColor(int i) {
        this.mainCircleDisabledColor = i;
        invalidate();
    }

    public int getIndicatorDisabledColor() {
        return this.indicatorDisabledColor;
    }

    public void setIndicatorDisabledColor(int i) {
        this.indicatorDisabledColor = i;
        invalidate();
    }

    public int getProgressPrimaryDisabledColor() {
        return this.progressPrimaryDisabledColor;
    }

    public void setProgressPrimaryDisabledColor(int i) {
        this.progressPrimaryDisabledColor = i;
        invalidate();
    }

    public int getProgressSecondaryDisabledColor() {
        return this.progressSecondaryDisabledColor;
    }

    public void setProgressSecondaryDisabledColor(int i) {
        this.progressSecondaryDisabledColor = i;
        invalidate();
    }

    public float getLabelSize() {
        return this.labelSize;
    }

    public void setLabelSize(float f) {
        this.labelSize = f;
        invalidate();
    }

    public int getLabelColor() {
        return this.labelColor;
    }

    public void setLabelColor(int i) {
        this.labelColor = i;
        invalidate();
    }

    public int getlabelDisabledColor() {
        return this.labelDisabledColor;
    }

    public void setlabelDisabledColor(int i) {
        this.labelDisabledColor = i;
        invalidate();
    }

    public String getLabelFont() {
        return this.labelFont;
    }

    public void setLabelFont(String str) {
        this.labelFont = str;
        if (this.textPaint != null) {
            generateTypeface();
        }
        invalidate();
    }

    public int getLabelStyle() {
        return this.labelStyle;
    }

    public void setLabelStyle(int i) {
        this.labelStyle = i;
        invalidate();
    }

    public float getIndicatorWidth() {
        return this.indicatorWidth;
    }

    public void setIndicatorWidth(float f) {
        this.indicatorWidth = f;
        invalidate();
    }

    public void setIsContinuous(boolean z) {
        this.isContinuous = z;
        invalidate();
    }

    public float getProgressPrimaryCircleSize() {
        return this.progressPrimaryCircleSize;
    }

    public void setProgressPrimaryCircleSize(float f) {
        this.progressPrimaryCircleSize = f;
        invalidate();
    }

    public float getProgressSecondaryCircleSize() {
        return this.progressSecondaryCircleSize;
    }

    public void setProgressSecondaryCircleSize(float f) {
        this.progressSecondaryCircleSize = f;
        invalidate();
    }

    public float getProgressPrimaryStrokeWidth() {
        return this.progressPrimaryStrokeWidth;
    }

    public void setProgressPrimaryStrokeWidth(float f) {
        this.progressPrimaryStrokeWidth = f;
        invalidate();
    }

    public float getProgressSecondaryStrokeWidth() {
        return this.progressSecondaryStrokeWidth;
    }

    public void setProgressSecondaryStrokeWidth(float f) {
        this.progressSecondaryStrokeWidth = f;
        invalidate();
    }

    public int getSweepAngle() {
        return this.sweepAngle;
    }

    public void setSweepAngle(int i) {
        this.sweepAngle = i;
        invalidate();
    }

    public int getStartOffset() {
        return this.startOffset;
    }

    public void setStartOffset(int i) {
        this.startOffset = i;
        invalidate();
    }

    public int getMax() {
        return this.max;
    }

    public void setMax(int i) {
        int i2 = this.min;
        if (i < i2) {
            this.max = i2;
        } else {
            this.max = i;
        }
        invalidate();
    }

    public int getMin() {
        return this.min;
    }

    public void setMin(int i) {
        if (i < 0) {
            this.min = 0;
        } else {
            int i2 = this.max;
            if (i > i2) {
                this.min = i2;
            } else {
                this.min = i;
            }
        }
        invalidate();
    }

    public float getMainCircleRadius() {
        return this.mainCircleRadius;
    }

    public void setMainCircleRadius(float f) {
        this.mainCircleRadius = f;
        invalidate();
    }

    public float getBackCircleRadius() {
        return this.backCircleRadius;
    }

    public void setBackCircleRadius(float f) {
        this.backCircleRadius = f;
        invalidate();
    }

    public float getProgressRadius() {
        return this.progressRadius;
    }

    public void setProgressRadius(float f) {
        this.progressRadius = f;
        invalidate();
    }

    public void setAntiClockwise(boolean z) {
        this.isAntiClockwise = z;
        invalidate();
    }
}
