package com.eskaylation.downloadmusic.loader;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore.Audio.Media;
import android.provider.MediaStore.Files;
import com.eskaylation.downloadmusic.model.Song;

import java.util.ArrayList;
import java.util.List;
import kotlin.Unit;
import kotlin.io.CloseableKt;
import kotlin.jvm.internal.Intrinsics;



public final class GetSongFolder {
    public Context context;
    public String sort = "title_key";

    public GetSongFolder(Context context2) {
        Intrinsics.checkParameterIsNotNull(context2, "context");
        this.context = context2;
    }

    public final void setSort(String str) {
        Intrinsics.checkParameterIsNotNull(str, "<set-?>");
        this.sort = str;
    }

    public final ArrayList<Song> getSongsByParentId(int i) {
        List songIdsByParentId = getSongIdsByParentId(i);
        if (songIdsByParentId != null && songIdsByParentId.size() == 0) {
            return new ArrayList<>();
        }
        StringBuilder sb = new StringBuilder(127);
        sb.append("_id in (");
        if (songIdsByParentId != null) {
            int i2 = 0;
            int size = songIdsByParentId.size();
            while (i2 < size) {
                sb.append(((Number) songIdsByParentId.get(i2)).intValue());
                sb.append(i2 == songIdsByParentId.size() + -1 ? ") " : ",");
                i2++;
            }
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(sb.toString());
        sb2.append(" AND ");
        sb2.append("is_music");
        sb2.append(" != 0 ");
        return getSongs(sb2.toString(), null, this.sort);
    }

    public final List<Integer> getSongIdsByParentId(int i) {
        Cursor cursor;
        ArrayList arrayList = new ArrayList();
        Context context2 = this.context;
        if (context2 != null) {
            ContentResolver contentResolver = context2.getContentResolver();
            if (contentResolver != null) {
                Uri contentUri = Files.getContentUri("external");
                String[] strArr = {"_id"};
                StringBuilder sb = new StringBuilder();
                sb.append("parent = ");
                sb.append(i);
                cursor = contentResolver.query(contentUri, strArr, sb.toString(), null, null);
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        try {
                            arrayList.add(Integer.valueOf(cursor.getInt(0)));
                        } catch (Throwable th) {
                            CloseableKt.closeFinally(cursor, th);
                            throw th;
                        }
                    }
                }
                Unit unit = Unit.INSTANCE;
                CloseableKt.closeFinally(cursor, null);
                return arrayList;
            }
        }
        cursor = null;
        if (cursor != null) {
        }
        Unit unit2 = Unit.INSTANCE;
        CloseableKt.closeFinally(cursor, null);
        return arrayList;
    }


    public final ArrayList<Song> getSongs(String str, String[] strArr, String str2) {
        ArrayList<Song> arrayList = new ArrayList<>();
        try {
            Cursor makeSongCursor = makeSongCursor(str, strArr, str2);
            if (makeSongCursor != null) {
                if (makeSongCursor.getCount() > 0) {
                    while (makeSongCursor.moveToNext()) {
                        arrayList.add(getSongInfo(makeSongCursor));
                    }
                }
            }
            Unit unit = Unit.INSTANCE;
            CloseableKt.closeFinally(makeSongCursor, null);
        } catch (Exception unused) {
        }
        return arrayList;
    }

    public final Cursor makeSongCursor(String str, String[] strArr, String str2) {
        try {
            Context context2 = this.context;
            if (context2 == null) {
                return null;
            }
            ContentResolver contentResolver = context2.getContentResolver();
            if (contentResolver != null) {
                return contentResolver.query(Media.EXTERNAL_CONTENT_URI, null, str, strArr, str2);
            }
            return null;
        } catch (SecurityException unused) {
            return null;
        }
    }

    public final Song getSongInfo(Cursor cursor) {
        Cursor cursor2 = cursor;
        Intrinsics.checkParameterIsNotNull(cursor2, "cursor");
        int columnIndex = cursor2.getColumnIndex("_id");
        int columnIndex2 = cursor2.getColumnIndex("title");
        int columnIndex3 = cursor2.getColumnIndex("duration");
        int columnIndex4 = cursor2.getColumnIndex("artist");
        int columnIndex5 = cursor2.getColumnIndex("album");
        int columnIndex6 = cursor2.getColumnIndex("album_id");
        int columnIndex7 = cursor2.getColumnIndex("track");
        int columnIndex8 = cursor2.getColumnIndex("_data");
        int columnIndex9 = cursor2.getColumnIndex("year");
        int columnIndex10 = cursor2.getColumnIndex("artist_id");
        Song song = new Song(cursor2.getLong(columnIndex), cursor2.getString(columnIndex2), cursor2.getString(columnIndex4), cursor2.getString(columnIndex5), cursor2.getInt(columnIndex7), cursor2.getLong(columnIndex6), "", cursor2.getString(columnIndex8), false, cursor2.getString(columnIndex9), "", cursor2.getLong(columnIndex10), cursor2.getString(columnIndex3));
        return song;
    }
}
