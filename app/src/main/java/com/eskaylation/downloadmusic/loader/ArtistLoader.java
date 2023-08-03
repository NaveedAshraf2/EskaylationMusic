package com.eskaylation.downloadmusic.loader;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore.Audio.Artists;
import androidx.core.content.PermissionChecker;
import com.eskaylation.downloadmusic.base.BaseAsyncTaskLoader;
import com.eskaylation.downloadmusic.listener.ArtistListenner;
import com.eskaylation.downloadmusic.model.Artist;
import java.util.ArrayList;
import java.util.List;


public class ArtistLoader extends BaseAsyncTaskLoader<List<Artist>> {
    public ArtistListenner artistListenner;
    public String sortorder;

    public ArtistLoader(Context context, ArtistListenner artistListenner2) {
        super(context);
        this.artistListenner = artistListenner2;
    }

    public ArrayList<Artist> loadInBackground() {
        ArrayList<Artist> arrayList = new ArrayList<>();
        if (PermissionChecker.checkCallingOrSelfPermission(getContext(), "android.permission.READ_EXTERNAL_STORAGE") != 0) {
            return null;
        }
        Cursor query = getContext().getContentResolver().query(Artists.EXTERNAL_CONTENT_URI, null, null, null, this.sortorder);
        if (query != null && query.moveToFirst()) {
            int columnIndex = query.getColumnIndex("_id");
            int columnIndex2 = query.getColumnIndex("artist");
            int columnIndex3 = query.getColumnIndex("number_of_albums");
            int columnIndex4 = query.getColumnIndex("number_of_tracks");
            do {
                Artist artist = new Artist(query.getLong(columnIndex), query.getString(columnIndex2), query.getInt(columnIndex3), query.getInt(columnIndex4));
                arrayList.add(artist);
            } while (query.moveToNext());
            query.close();
        }
        this.artistListenner.onLoadArtistSuccessful(arrayList);
        return arrayList;
    }

    public void setSortOrder(String str) {
        this.sortorder = str;
    }
}
