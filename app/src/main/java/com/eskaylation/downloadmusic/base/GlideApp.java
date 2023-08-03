package com.eskaylation.downloadmusic.base;

import android.content.Context;
import com.bumptech.glide.Glide;

public final class GlideApp {
    public static GlideRequests with(Context context) {
        return (GlideRequests) Glide.with(context);
    }
}
