package com.eskaylation.downloadmusic.loader;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore.Audio.Albums;
import androidx.core.content.PermissionChecker;
import com.eskaylation.downloadmusic.base.BaseAsyncTaskLoader;
import com.eskaylation.downloadmusic.listener.AlbumListenner;
import com.eskaylation.downloadmusic.model.Album;
import com.eskaylation.downloadmusic.utils.ArtworkUtils;
import java.util.ArrayList;


public class AlbumLoader extends BaseAsyncTaskLoader<ArrayList<Album>> {
    public String Where;
    public AlbumListenner albumListenner;
    public String[] datacol = {"_id", "album", "artist", "numsongs", "minyear"};
    public String[] selectionargs = null;
    public String sortorder;

    public AlbumLoader(Context context, AlbumListenner albumListenner2) {
        super(context);
        this.albumListenner = albumListenner2;
    }

    public ArrayList<Album> loadInBackground() {
        ArrayList<Album> arrayList = new ArrayList<>();
        if (PermissionChecker.checkCallingOrSelfPermission(getContext(), "android.permission.READ_EXTERNAL_STORAGE") != 0) {
            return null;
        }
        Cursor query = getContext().getContentResolver().query(Albums.EXTERNAL_CONTENT_URI, this.datacol, this.Where, this.selectionargs, this.sortorder);
        if (query != null && query.moveToFirst()) {
            int columnIndex = query.getColumnIndex("album");
            int columnIndex2 = query.getColumnIndex("_id");
            int columnIndex3 = query.getColumnIndex("artist");
            int columnIndex4 = query.getColumnIndex("numsongs");
            int columnIndex5 = query.getColumnIndex("minyear");
            do {
                String string = query.getString(columnIndex);
                long j = query.getLong(columnIndex2);
                String string2 = query.getString(columnIndex3);
                int i = query.getInt(columnIndex5);
                int i2 = query.getInt(columnIndex4);
                Uri albumArtUri = ArtworkUtils.getAlbumArtUri((int) j);
                Album album = new Album();
                album.setArtistName(string2);
                album.setAlbumName(string);
                album.setId(j);
                album.setTrackCount(i2);
                album.setYear(i);
                album.setAlbumThumb(albumArtUri);
                arrayList.add(album);
            } while (query.moveToNext());
            query.close();
        }
        this.albumListenner.onAlbumLoadedSuccessful(arrayList);
        return arrayList;
    }

    public void setSortOrder(String str) {
        this.sortorder = str;
    }

    public void filterartistsong(String str, String[] strArr) {
        this.Where = str;
        this.selectionargs = strArr;
    }
}
