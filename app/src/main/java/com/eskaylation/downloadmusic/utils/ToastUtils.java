package com.eskaylation.downloadmusic.utils;

import android.content.Context;
import android.widget.Toast;
import com.eskaylation.downloadmusic.R;

public class ToastUtils {
    public static void error(Context context, String str) {
        showMessage(context, str);
    }

    public static void error(Context context, int i) {
        showMessage(context, i);
    }

    public static void success(Context context, String str) {
        showMessage(context, str);
    }

    public static void success(Context context, int i) {
        showMessage(context, i);
    }

    public static void warning(Context context, String str) {
        showMessage(context, str);
    }

    public static void warning(Context context, int i) {
        showMessage(context, i);
    }

    public static void showMessage(Context context, String str) {
        if (str != null) {
            Toast.makeText(context, str, 0).show();
        } else {
            Toast.makeText(context, context.getString(R.string.txt_error), 0).show();
        }
    }

    public static void showMessage(Context context, int i) {
        showMessage(context, context.getString(i));
    }
}
