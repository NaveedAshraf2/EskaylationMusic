package com.eskaylation.downloadmusic.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build.VERSION;
import androidx.core.content.FileProvider;

import com.google.android.exoplayer2.util.MimeTypes;
import java.io.File;
import com.eskaylation.downloadmusic.R;
public class FileProviderApp extends FileProvider {
    public static Uri getUriForFile(Context context, String str) {
        if (VERSION.SDK_INT >= 24) {
            return FileProvider.getUriForFile(context, "mp3juices.myfreemp3", new File(str));
        }
        StringBuilder sb = new StringBuilder();
        sb.append("file://");
        sb.append(str);
        return Uri.parse(sb.toString());
    }

    public static void share(Context context, String str) {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.putExtra("android.intent.extra.STREAM", getUriForFile(context, str));
        intent.putExtra("android.intent.extra.SUBJECT", str.substring(str.lastIndexOf("/") + 1, str.lastIndexOf(".")));
        intent.setType(MimeTypes.AUDIO_MPEG);
        if (VERSION.SDK_INT >= 24) {
            intent.addFlags(1);
        }
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.share)));
    }
}
