package com.eskaylation.nativeadsbig;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.eskaylation.downloadmusic.R;


public class TemplateView extends FrameLayout {
    public TextView callToActionView;
    public ImageView iconView;

    public TextView primaryView;
    public NativeTemplateStyle styles;
    public int templateType;
    public TextView tertiaryView;

    public TemplateView(Context context) {
        super(context);
    }

    public TemplateView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView(context, attributeSet);
    }

    public TemplateView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView(context, attributeSet);
    }

    public TemplateView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        initView(context, attributeSet);
    }

    public void setStyles(NativeTemplateStyle nativeTemplateStyle) {
        this.styles = nativeTemplateStyle;
        applyStyles();
        throw null;
    }
    public final void applyStyles() {
        this.styles.getMainBackgroundColor();
        throw null;
    }



    public final boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }




    public final void initView(Context context, AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(attributeSet, R.styleable.TemplateView, 0, 0);
        try {
            this.templateType = obtainStyledAttributes.getResourceId(R.styleable.
                    TemplateView_gnt_template_type, R.layout.gnt_medium_template_view);
            obtainStyledAttributes.recycle();
            ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(this.templateType, this);
        } catch (Throwable th) {
            obtainStyledAttributes.recycle();
            throw th;
        }
    }


}
