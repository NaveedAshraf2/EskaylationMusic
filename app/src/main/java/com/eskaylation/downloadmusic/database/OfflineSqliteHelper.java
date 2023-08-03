package com.eskaylation.downloadmusic.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class OfflineSqliteHelper extends SQLiteOpenHelper {
    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
    }

    public OfflineSqliteHelper(Context context) {
        super(context, "TOOLMUSIC_EQULAZIER_DOWNLOAD", null, 1);
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS TABLE_MAME(CL_NAME NVARCHAR PRIMARY KEY NOT NULL,CL_SLIDER0 INTEGER,CL_SLIDER1 INTEGER,CL_SLIDER2 INTEGER,CL_SLIDER3 INTEGER,CL_SLIDER4 INTEGER)");
    }
}
