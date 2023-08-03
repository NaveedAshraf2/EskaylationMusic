package com.eskaylation.downloadmusic.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FavoriteSqliteHelper extends SQLiteOpenHelper {
    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
    }

    public FavoriteSqliteHelper(Context context) {
        super(context, "TOOLMUSIC_FAVORITEDOWNLOAD", null, 1);
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS FAVORITE_TABLE(ID INTEGER PRIMARY KEY AUTOINCREMENT, FAVORITE_ID TEXT, FAVORITE_NAME NVARCHAR)");
    }
}
