package com.eskaylation.downloadmusic.utils;

import android.graphics.Bitmap;
import androidx.palette.graphics.Palette;

public class BitmapUtils {
    public static Palette createPaletteSync(Bitmap bitmap) {
        return Palette.from(bitmap).generate();
    }
}
