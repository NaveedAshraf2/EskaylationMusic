package com.eskaylation.downloadmusic.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SongListSqliteHelper extends SQLiteOpenHelper {
    public String CREATE_TABLE;
    public String SQL_QUERRY;
    public String TABLE_NAME;

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {

    }

    public SongListSqliteHelper(Context context, String str) {
        super(context, str, null, 1);
        String str2 = "";
        this.TABLE_NAME = str2;
        this.SQL_QUERRY = str2;
        this.TABLE_NAME = str;
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM ");
        sb.append(str);
        this.SQL_QUERRY = sb.toString();
        setCREATE_TABLE(str);
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL(this.CREATE_TABLE);
    }

    public void setCREATE_TABLE(String str) {
        this.TABLE_NAME = str;
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS ");
        sb.append(this.TABLE_NAME);
        sb.append("(");
        sb.append("SONG_ID");
        sb.append(" INTEGER PRIMARY KEY,");
        sb.append("TITLE");
        String str2 = " TEXT,";
        sb.append(str2);
        sb.append("SONG_PATH");
        sb.append(" TEXT  NOT NULL,");
        sb.append("ARTIST");
        sb.append(str2);
        sb.append("ALBULM");
        sb.append(str2);
        sb.append("TRACK_NUMBER");
        String str3 = " INTEGER,";
        sb.append(str3);
        sb.append("ALBUM_ID");
        sb.append(str3);
        sb.append("GENRE");
        sb.append(str2);
        sb.append("YEAR");
        sb.append(str2);
        sb.append("LYRICS");
        sb.append(str2);
        sb.append("ARTIST_ID");
        sb.append(str3);
        sb.append("DURATION");
        sb.append(" TEXT)");
        this.CREATE_TABLE = sb.toString();
    }

    public String getTABLE_NAME() {
        return this.TABLE_NAME;
    }

    public String getSQL_QUERRY() {
        return this.SQL_QUERRY;
    }
}
