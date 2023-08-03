package com.eskaylation.downloadmusic.utils;

import android.content.ContentUris;
import android.net.Uri;

public class ArtworkUtils {
    public static Uri getAlbumArtUri(int i) {
        Uri withAppendedId = ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), (long) i);
        if (withAppendedId == null) {
            return null;
        }
        return withAppendedId;
    }

    public static Uri uri(long j) {
        return ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), j);
    }
}
