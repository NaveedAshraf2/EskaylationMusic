package com.eskaylation.downloadmusic.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.eskaylation.downloadmusic.model.Favorite;
import com.eskaylation.downloadmusic.utils.PreferenceUtils;
import java.util.ArrayList;

public class FavoriteDao {
    public Context context;
    public FavoriteSqliteHelper database;
    public SQLiteDatabase sqLiteDatabase;

    public FavoriteDao(Context context2, FavoriteSqliteHelper favoriteSqliteHelper) {
        this.database = favoriteSqliteHelper;
        this.context = context2;
    }


    public ArrayList<Favorite> getAllFavorite() {
        ArrayList<Favorite> arrayList = new ArrayList<>();
      //  this.sqLiteDatabase = this.database.getReadableDatabase();
        sqLiteDatabase = database.getReadableDatabase();
        try {
            Cursor rawQuery = this.sqLiteDatabase.rawQuery("SELECT * FROM FAVORITE_TABLE", null);
            if (rawQuery != null) {
                if (rawQuery.getCount() > 0) {
                    while (rawQuery.moveToNext()) {
                        String string = rawQuery.getString(rawQuery.getColumnIndex("FAVORITE_NAME"));
                        arrayList.add(new Favorite(rawQuery.getInt(rawQuery.getColumnIndex("ID")), rawQuery.getString(rawQuery.getColumnIndex("FAVORITE_ID")), string));
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

    public ArrayList<String> getAllNameFavorite() {
        ArrayList allFavorite = getAllFavorite();
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < allFavorite.size(); i++) {
            arrayList.add(((Favorite) allFavorite.get(i)).name);
        }
        return arrayList;
    }

    public ArrayList<String> getAllFavoriteID() {
        ArrayList allFavorite = getAllFavorite();
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < allFavorite.size(); i++) {
            arrayList.add(((Favorite) allFavorite.get(i)).favorite_id);
        }
        return arrayList;
    }

    public long insertFavorite(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("MP3_");
        sb.append(PreferenceUtils.getInstance(this.context).getIDPlaylist());
        String sb2 = sb.toString();
        PreferenceUtils.getInstance(this.context).setIDPlaylist();
        if (getAllNameFavorite().contains(str)) {
            return -1;
        }
        String str2 = "DEFAULT_FAVORITEDOWNLOAD";
        String str3 = "FAVORITE_TABLE";
        String str4 = "FAVORITE_ID";
        String str5 = "FAVORITE_NAME";
        if (str.equals(str2)) {
            this.sqLiteDatabase = this.database.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(str5, str2);
            contentValues.put(str4, str2);
            return this.sqLiteDatabase.insert(str3, null, contentValues);
        }
        this.sqLiteDatabase = this.database.getWritableDatabase();
        ContentValues contentValues2 = new ContentValues();
        contentValues2.put(str5, str);
        contentValues2.put(str4, sb2);
        return this.sqLiteDatabase.insert(str3, null, contentValues2);
    }

    public long updateFavoriteName(String str, String str2) {
        this.sqLiteDatabase = this.database.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("FAVORITE_NAME", str2);
        return (long) this.sqLiteDatabase.update("FAVORITE_TABLE", contentValues, "FAVORITE_NAME = ?", new String[]{str});
    }

    public long deleteFavorite(String str) {
        this.sqLiteDatabase = this.database.getWritableDatabase();
        return (long) this.sqLiteDatabase.delete("FAVORITE_TABLE", "FAVORITE_NAME = ?", new String[]{str});
    }
}
