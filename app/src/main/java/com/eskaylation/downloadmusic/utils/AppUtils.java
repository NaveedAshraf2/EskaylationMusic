package com.eskaylation.downloadmusic.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import com.eskaylation.downloadmusic.R;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.ViewConfiguration;

import com.eskaylation.downloadmusic.model.AudioExtract;
import com.google.android.exoplayer2.source.hls.DefaultHlsExtractorFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public final class AppUtils {
    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public static double round(double d, int i) {
        if (i >= 0) {
            double pow = (double) ((long) Math.pow(10.0d, (double) i));
            return ((double) Math.round(d * pow)) / pow;
        }
        throw new IllegalArgumentException();
    }

    public static String convertDuration(long j) {
        String str;
        String str2;
        String str3;
        long j2 = j / 1000;
        long j3 = j2 % 60;
        long j4 = j2 / 60;
        long j5 = j4 % 60;
        long j6 = j4 / 60;
        String str4 = "0";
        String str5 = "";
        if (j3 < 10) {
            StringBuilder sb = new StringBuilder();
            sb.append(str4);
            sb.append(j3);
            str = sb.toString();
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(j3);
            sb2.append(str5);
            str = sb2.toString();
        }
        if (j5 < 10) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append(str4);
            sb3.append(j5);
            str2 = sb3.toString();
        } else {
            StringBuilder sb4 = new StringBuilder();
            sb4.append(j5);
            sb4.append(str5);
            str2 = sb4.toString();
        }
        if (j6 < 10) {
            StringBuilder sb5 = new StringBuilder();
            sb5.append(str4);
            sb5.append(j6);
            str3 = sb5.toString();
        } else {
            StringBuilder sb6 = new StringBuilder();
            sb6.append(j6);
            sb6.append(str5);
            str3 = sb6.toString();
        }
        String str6 = ":";
        if (j6 == 0) {
            StringBuilder sb7 = new StringBuilder();
            sb7.append(str2);
            sb7.append(str6);
            sb7.append(str);
            return sb7.toString();
        }
        StringBuilder sb8 = new StringBuilder();
        sb8.append(str3);
        sb8.append(str6);
        sb8.append(str2);
        sb8.append(str6);
        sb8.append(str);
        return sb8.toString();
    }

    public static int getHeightStatusBar(Context context) {
        int identifier = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (identifier > 0) {
            return context.getResources().getDimensionPixelSize(identifier);
        }
        return 0;
    }

    public static int dpToPixels(int i) {
        return Math.round(TypedValue.applyDimension(1, (float) i, Resources.getSystem().getDisplayMetrics()));
    }

    public static int getRandomNumber(int i) {
        return new Random().nextInt(i + 0 + 1) + 0;
    }

    public static String getCurrentMillisecond() {
        return String.valueOf(new Date().getTime());
    }

    public static void shareText(Context context, String str, String str2) {
        try {
            ArrayList arrayList = new ArrayList();
            List<ResolveInfo> queryIntentActivities = context.getPackageManager().queryIntentActivities(createShareIntent(str, str2), 0);
            if (!queryIntentActivities.isEmpty()) {
                for (ResolveInfo resolveInfo : queryIntentActivities) {
                    Intent createShareIntent = createShareIntent(str, str2);
                    if (!resolveInfo.activityInfo.packageName.equalsIgnoreCase("com.google.android.apps.docs")) {
                        createShareIntent.setPackage(resolveInfo.activityInfo.packageName);
                        arrayList.add(createShareIntent);
                    }
                }
                context.startActivity(Intent.createChooser((Intent) arrayList.remove(0), context.getString(R.string.share)).putExtra("android.intent.extra.INITIAL_INTENTS", (Parcelable[]) arrayList.toArray(new Parcelable[0])));
                return;
            }
            throw new Exception("No applications found");
        } catch (Exception unused) {
            context.startActivity(Intent.createChooser(createShareIntent(str, str2), context.getString(R.string.share)));
        }
    }

    public static Intent createShareIntent(String str, String str2) {
        return new Intent("android.intent.action.SEND").setType("text/plain").putExtra("android.intent.extra.SUBJECT", str).putExtra("android.intent.extra.TEXT", str2);
    }

    public static String outputPath(Context context) {
        StringBuilder sb = new StringBuilder();
        sb.append(Environment.getExternalStorageDirectory().toString());
        sb.append(File.separator);
        sb.append("MusicDownloader 2020");
        String sb2 = sb.toString();
//        File file = new File(sb2);
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "MusicDownloader 2022");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }

    public static boolean hasSoftKeys(Activity activity) {
        if (VERSION.SDK_INT >= 17) {
            Display defaultDisplay = activity.getWindowManager().getDefaultDisplay();
            DisplayMetrics displayMetrics = new DisplayMetrics();
            defaultDisplay.getRealMetrics(displayMetrics);
            int i = displayMetrics.heightPixels;
            int i2 = displayMetrics.widthPixels;
            DisplayMetrics displayMetrics2 = new DisplayMetrics();
            defaultDisplay.getMetrics(displayMetrics2);
            int i3 = displayMetrics2.heightPixels;
            if (i2 - displayMetrics2.widthPixels > 0 || i - i3 > 0) {
                return true;
            }
        } else {
            boolean hasPermanentMenuKey = ViewConfiguration.get(activity).hasPermanentMenuKey();
            boolean deviceHasKey = KeyCharacterMap.deviceHasKey(4);
            if (!hasPermanentMenuKey && !deviceHasKey) {
                return true;
            }
        }
        return false;
    }

    public static boolean isMusicDownloaded(Context context, AudioExtract audioExtract) {
        if (audioExtract == null) {
            return false;
        }
        String outputPath = outputPath(context);
        String str = "";
        String replaceAll = audioExtract.title.replaceAll("\\/", str).replaceAll("\\\\", str).replaceAll("\\*", str).replaceAll("\\?", str).replaceAll("\\\"", str).replaceAll("<", str).replaceAll("\\(", str).replaceAll("\\)", str).replaceAll(">", str).replaceAll("\\|", str).replaceAll("|", str).replaceAll("#", str).replaceAll("\\#", str).replaceAll("\\.", str).replaceAll("\\:", str);
        StringBuilder sb = new StringBuilder();
        sb.append(replaceAll.replaceAll("[\\\\><\"|*?%:#/]", str));
        sb.append(".mp3");
        String sb2 = sb.toString();
        StringBuilder sb3 = new StringBuilder();
        sb3.append(outputPath);
        sb3.append("/");
        sb3.append(sb2);
        return new File(sb3.toString()).exists();
    }

    public static Drawable createDrawable(int i, int i2) {
        return new GradientDrawable(Orientation.TOP_BOTTOM, new int[]{i2, i});
    }
}
