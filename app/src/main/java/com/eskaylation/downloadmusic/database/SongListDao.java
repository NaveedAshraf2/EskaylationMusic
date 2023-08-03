package com.eskaylation.downloadmusic.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.eskaylation.downloadmusic.model.Song;
import java.io.File;
import java.util.ArrayList;

public class SongListDao {
    public String SQL_QUERRY;
    public String TABLE_NAME;
    public SongListSqliteHelper database;
    public SQLiteDatabase sqLiteDatabase;

    public SongListDao(SongListSqliteHelper songListSqliteHelper) {
        this.database = songListSqliteHelper;
        this.TABLE_NAME = songListSqliteHelper.getTABLE_NAME();
        this.SQL_QUERRY = songListSqliteHelper.getSQL_QUERRY();
    }

    public ArrayList<Song> getAllFavoriteSong() {
        ArrayList<Song> arrayList = new ArrayList<>();
        this.sqLiteDatabase = this.database.getReadableDatabase();
        try {
            Cursor rawQuery = this.sqLiteDatabase.rawQuery(this.SQL_QUERRY, null);
            if (rawQuery != null) {
                if (rawQuery.getCount() > 0) {
                    while (rawQuery.moveToNext()) {
                        long j = (long) rawQuery.getInt(rawQuery.getColumnIndex("SONG_ID"));
                        String string = rawQuery.getString(rawQuery.getColumnIndex("TITLE"));
                        String string2 = rawQuery.getString(rawQuery.getColumnIndex("ARTIST"));
                        String string3 = rawQuery.getString(rawQuery.getColumnIndex("ALBULM"));
                        int i = rawQuery.getInt(rawQuery.getColumnIndex("TRACK_NUMBER"));
                        long j2 = (long) rawQuery.getInt(rawQuery.getColumnIndex("ALBUM_ID"));
                        String string4 = rawQuery.getString(rawQuery.getColumnIndex("GENRE"));
                        String string5 = rawQuery.getString(rawQuery.getColumnIndex("SONG_PATH"));
                        long j3 = (long) rawQuery.getInt(rawQuery.getColumnIndex("ARTIST_ID"));
                       // Song song = r4;
                        String str = string5;
                        Song song2 = new Song(j, string, string2, string3, i, j2, string4, str, false, rawQuery.getString(rawQuery.getColumnIndex("YEAR")), rawQuery.getString(rawQuery.getColumnIndex("LYRICS")), j3, rawQuery.getString(rawQuery.getColumnIndex("DURATION")));
                        if (new File(string5).exists()) {
                            arrayList.add(song2);
                        } else {
                            deleteFavoriteSong(song2);
                        }
                    }
                }
                rawQuery.close();
                this.database.close();
            }
            if (rawQuery != null) {
                rawQuery.close();
            }
            return arrayList;
        } catch (Throwable th) {

            throw th;
        }
    }


    public ArrayList<Song> getAllFavoriteSong(String str) {
        ArrayList<Song> arrayList = new ArrayList<>();
        this.sqLiteDatabase = this.database.getReadableDatabase();
        try {
            SQLiteDatabase sQLiteDatabase = this.sqLiteDatabase;
            StringBuilder sb = new StringBuilder();
            sb.append(this.SQL_QUERRY);
            sb.append(" ORDER BY ");
            sb.append(str);
            Cursor rawQuery = sQLiteDatabase.rawQuery(sb.toString(), null);
            if (rawQuery != null) {
                if (rawQuery.getCount() > 0) {
                    while (rawQuery.moveToNext()) {
                        long j = (long) rawQuery.getInt(rawQuery.getColumnIndex("SONG_ID"));
                        String string = rawQuery.getString(rawQuery.getColumnIndex("TITLE"));
                        String string2 = rawQuery.getString(rawQuery.getColumnIndex("ARTIST"));
                        String string3 = rawQuery.getString(rawQuery.getColumnIndex("ALBULM"));
                        int i = rawQuery.getInt(rawQuery.getColumnIndex("TRACK_NUMBER"));
                        long j2 = (long) rawQuery.getInt(rawQuery.getColumnIndex("ALBUM_ID"));
                        String string4 = rawQuery.getString(rawQuery.getColumnIndex("GENRE"));
                        String string5 = rawQuery.getString(rawQuery.getColumnIndex("SONG_PATH"));
                        long j3 = (long) rawQuery.getInt(rawQuery.getColumnIndex("ARTIST_ID"));

                        String str2 = string5;
                        Song song2 = new Song(j, string, string2, string3, i, j2, string4, str2, true, rawQuery.getString(rawQuery.getColumnIndex("YEAR")), rawQuery.getString(rawQuery.getColumnIndex("LYRICS")), j3, rawQuery.getString(rawQuery.getColumnIndex("DURATION")));
                        if (new File(string5).exists()) {
                            arrayList.add(song2);
                        } else {
                            deleteFavoriteSong(song2);
                        }
                    }
                }
                rawQuery.close();
                this.database.close();
            }
            if (rawQuery != null) {
                rawQuery.close();
            }
            return arrayList;
        } catch (Throwable th) {

            throw th;
        }
    }

    public long insertFavoriteSong(Song song) {
        this.sqLiteDatabase = this.database.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("TITLE", song.title.isEmpty() ? "song" : song.title);
        contentValues.put("SONG_ID", Long.valueOf(song.id));
        contentValues.put("SONG_PATH", song.mSongPath);
        contentValues.put("ARTIST", song.artist);
        contentValues.put("ALBULM", song.album);
        contentValues.put("TRACK_NUMBER", Integer.valueOf(song.trackNumber));
        contentValues.put("ALBUM_ID", Long.valueOf(song.albumId));
        contentValues.put("GENRE", song.genre);
        contentValues.put("YEAR", song.year);
        contentValues.put("LYRICS", song.lyrics);
        contentValues.put("ARTIST_ID", Long.valueOf(song.artistId));
        contentValues.put("DURATION", song.duration);
        return this.sqLiteDatabase.insert(this.TABLE_NAME, null, contentValues);
    }

    public void insertListFavoriteSong(ArrayList<Song> arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            Song song = (Song) arrayList.get(i);
            this.sqLiteDatabase = this.database.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("TITLE", song.title.isEmpty() ? "song" : song.title);
            contentValues.put("SONG_ID", Long.valueOf(song.id));
            contentValues.put("SONG_PATH", song.mSongPath);
            contentValues.put("ARTIST", song.artist);
            contentValues.put("ALBULM", song.album);
            contentValues.put("TRACK_NUMBER", Integer.valueOf(song.trackNumber));
            contentValues.put("ALBUM_ID", Long.valueOf(song.albumId));
            contentValues.put("GENRE", song.genre);
            contentValues.put("YEAR", song.year);
            contentValues.put("LYRICS", song.lyrics);
            contentValues.put("ARTIST_ID", Long.valueOf(song.artistId));
            contentValues.put("DURATION", song.duration);
            this.sqLiteDatabase.insert(this.TABLE_NAME, null, contentValues);
        }
    }

    public long deleteFavoriteSong(Song song) {
        this.sqLiteDatabase = this.database.getWritableDatabase();
        return (long) this.sqLiteDatabase.delete(this.TABLE_NAME, "SONG_PATH = ?", new String[]{song.mSongPath});
    }

    public void deleteFavoriteDB(String str) {
        this.sqLiteDatabase = this.database.getWritableDatabase();
        SQLiteDatabase sQLiteDatabase = this.sqLiteDatabase;
        StringBuilder sb = new StringBuilder();
        sb.append("delete from ");
        sb.append(str);
        sQLiteDatabase.execSQL(sb.toString());
    }
}
