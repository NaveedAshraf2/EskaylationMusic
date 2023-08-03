package com.eskaylation.downloadmusic.loader;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore.Audio.Media;
import androidx.core.content.PermissionChecker;
import com.eskaylation.downloadmusic.base.BaseAsyncTaskLoader;
import com.eskaylation.downloadmusic.listener.SearchListenner;
import com.eskaylation.downloadmusic.model.Song;

import java.util.ArrayList;


public class SearchLoader extends BaseAsyncTaskLoader<ArrayList<Song>> {
    public String[] datacol = {"_id", "title", "artist", "album", "album_id", "artist_id", "track",
            "_data","duration", "year", "composer"};
    public String name;
    public SearchListenner songListenner;
    public String sortorder = null;

    public SearchLoader(SearchListenner searchListenner, Context context) {
        super(context);
        this.songListenner = searchListenner;
    }

    public ArrayList<Song> loadInBackground() {
        ArrayList arrayList;
        ArrayList arrayList2 = new ArrayList();
        if (PermissionChecker.checkCallingOrSelfPermission(getContext(),
                "android.permission.READ_EXTERNAL_STORAGE") == 0) {
            ContentResolver contentResolver = getContext().getContentResolver();
            Uri uri = Media.EXTERNAL_CONTENT_URI;
            String[] strArr = this.datacol;
            StringBuilder sb = new StringBuilder();
            String str = "%";
            sb.append(str);
            sb.append(this.name);
            sb.append(str);
            String[] strArr2 = {sb.toString()};
            Cursor query = contentResolver.query(uri, strArr, "is_music != 0 AND title LIKE ?", strArr2, this.sortorder);
            if (query == null || !query.moveToFirst()) {
                arrayList = arrayList2;
            } else {
                int columnIndex = query.getColumnIndex("_id");
                int columnIndex2 = query.getColumnIndex("title");
                int columnIndex3 = query.getColumnIndex("duration");
                int columnIndex4 = query.getColumnIndex("artist");
                int columnIndex5 = query.getColumnIndex("album");
                int columnIndex6 = query.getColumnIndex("album_id");
                int columnIndex7 = query.getColumnIndex("track");
                int columnIndex8 = query.getColumnIndex("_data");
                int columnIndex9 = query.getColumnIndex("year");
                int columnIndex10 = query.getColumnIndex("artist_id");
                while (true) {
                    long j = query.getLong(columnIndex);
                    String string = query.getString(columnIndex2);
                    int i = columnIndex;
                    String string2 = query.getString(columnIndex4);
                    int i2 = columnIndex2;
                    String string3 = query.getString(columnIndex5);
                    int i3 = columnIndex4;
                    int i4 = columnIndex5;
                    long j2 = query.getLong(columnIndex6);
                    int i5 = columnIndex6;
                    int i6 = query.getInt(columnIndex7);
                    int i7 = columnIndex7;
                    String string4 = query.getString(columnIndex8);
                    int i8 = columnIndex8;
                    String string5 = query.getString(columnIndex9);
                    ArrayList arrayList3 = arrayList2;
                    long j3 = query.getLong(columnIndex10);
                    int i9 = columnIndex9;
                    String string6 = query.getString(columnIndex3);
                    int i10 = columnIndex3;
                    Song song = new Song();
                    song.setAlbum(string3);
                    song.setmSongPath(string4);
                    song.setArtist(string2);
                    song.setId(j);
                    song.setAlbumId(j2);
                    song.setTrackNumber(i6);
                    song.setTitle(string);
                    song.setYear(string5);
                    song.setArtistId(j3);
                    song.setDuration(string6);
                    arrayList = arrayList3;
                    arrayList.add(song);
                    if (!query.moveToNext()) {
                        break;
                    }
                    arrayList2 = arrayList;
                    columnIndex = i;
                    columnIndex2 = i2;
                    columnIndex4 = i3;
                    columnIndex5 = i4;
                    columnIndex6 = i5;
                    columnIndex7 = i7;
                    columnIndex8 = i8;
                    columnIndex9 = i9;
                    columnIndex3 = i10;
                }
                query.close();
            }
            this.songListenner.onAudioSearchSuccessful(arrayList);
            return arrayList;
        }
        return null;
    }

    public void setSearchName(String str) {
        this.name = str;
    }

    public void setSortOrder(String str) {
        this.sortorder = str;
    }
}
