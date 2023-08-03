package com.eskaylation.downloadmusic.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.eskaylation.downloadmusic.model.CustomPreset;
import java.util.ArrayList;

public class EqualizerDao {
    public OfflineSqliteHelper database;
    public SQLiteDatabase sqLiteDatabase;

    public EqualizerDao(OfflineSqliteHelper offlineSqliteHelper) {
        this.database = offlineSqliteHelper;
    }

    public ArrayList<CustomPreset> getAllPreset() {
        ArrayList<CustomPreset> arrayList = new ArrayList<>();

        sqLiteDatabase = database.getReadableDatabase();

        try {
            Cursor rawQuery = this.sqLiteDatabase.rawQuery("SELECT * FROM TABLE_MAME", null);
            if (rawQuery != null) {
                if (rawQuery.getCount() > 0) {
                    while (rawQuery.moveToNext()) {
                        CustomPreset customPreset = new CustomPreset(rawQuery.getString(rawQuery.getColumnIndex("CL_NAME")), rawQuery.getInt(rawQuery.getColumnIndex("CL_SLIDER0")), rawQuery.getInt(rawQuery.getColumnIndex("CL_SLIDER1")), rawQuery.getInt(rawQuery.getColumnIndex("CL_SLIDER2")), rawQuery.getInt(rawQuery.getColumnIndex("CL_SLIDER3")), rawQuery.getInt(rawQuery.getColumnIndex("CL_SLIDER4")));
                        arrayList.add(customPreset);
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
}
