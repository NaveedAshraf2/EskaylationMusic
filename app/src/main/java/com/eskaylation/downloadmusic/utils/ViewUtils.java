package com.eskaylation.downloadmusic.utils;

import android.app.Activity;
import android.content.res.Resources;
import com.eskaylation.downloadmusic.R;
public class ViewUtils {
    public static final int getHeightNavigationBar(Activity activity) {
        Resources resources = activity.getResources();
        int identifier = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (identifier > 0) {
            return resources.getDimensionPixelSize(identifier);
        }
        return 0;
    }
}
