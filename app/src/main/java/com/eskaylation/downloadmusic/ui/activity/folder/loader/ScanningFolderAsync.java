package com.eskaylation.downloadmusic.ui.activity.folder.loader;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.MediaStore.Files;
import com.eskaylation.downloadmusic.model.Folder;

import com.kunkun.mp3player.ui.fragment.main.folder_selected.loader.ScaningFolderListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;

import kotlin.io.CloseableKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;




public final class ScanningFolderAsync extends AsyncTask<Void, Integer, ArrayList<Folder>> {
    public Context context;
    public ScaningFolderListener scaningFolderListener;

    public ScanningFolderAsync(Context context2, ScaningFolderListener scaningFolderListener2) {
        Intrinsics.checkParameterIsNotNull(context2, "context");
        Intrinsics.checkParameterIsNotNull(scaningFolderListener2, "scaningFolderListener");
        this.context = context2;
        this.scaningFolderListener = scaningFolderListener2;

    }


    public ArrayList<Folder> doInBackground(Void... voidArr) {
        Cursor cursor;
        Throwable th;
        Intrinsics.checkParameterIsNotNull(voidArr, "params");
        ArrayList<Folder> arrayList = new ArrayList<>();
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        try {
            Context context2 = this.context;
            if (context2 != null) {
                ContentResolver contentResolver = context2.getContentResolver();
                if (contentResolver != null) {
                    cursor = contentResolver.query(Files.getContentUri("external"), null, "is_music != 0 AND media_type = 2", null, null);
                    if (cursor != null) {
                        while (true) {
                            try {
                                String str = "null cannot be cast to non-null type java.lang.String";
                                String str2 = "(this as java.lang.Strin…ing(startIndex, endIndex)";
                                if (!cursor.moveToNext()) {
                                    break;
                                }
                                String string = cursor.getString(cursor.getColumnIndex("_data"));
                                Intrinsics.checkExpressionValueIsNotNull(string, "cursor\n                 ….Files.FileColumns.DATA))");
                                int i = cursor.getInt(cursor.getColumnIndex("parent"));
                                int lastIndexOf$default =
                                        StringsKt.indexOfLast((CharSequence)
                                        string, null);
                                if (string != null) {
                                    String substring = string.substring(0, lastIndexOf$default);
                                    Intrinsics.checkExpressionValueIsNotNull(substring, str2);
                                    if (!linkedHashMap.containsKey(Integer.valueOf(i))) {
                                        linkedHashMap.put(Integer.valueOf(i),
                                                new ArrayList(CollectionsKt.listOf(substring)));
                                    } else {
                                        Object obj = linkedHashMap.get(Integer.valueOf(i));
                                        if (obj != null) {
                                            ((List) obj).add(substring);
                                        } else {
                                            Intrinsics.throwNpe();
                                            throw null;
                                        }
                                    }
                                } else {
                                    throw new TypeCastException(str);
                                }
                            } catch (Throwable th2) {
                                Throwable th3 = th2;

                            }
                        }
                    }

                    return arrayList;
                }
            }
            cursor = null;
            if (cursor != null) {
            }

        } catch (Exception unused) {
        }
        return arrayList;
    }

    public void onPostExecute(ArrayList<Folder> arrayList) {
        Intrinsics.checkParameterIsNotNull(arrayList, "result");
        super.onPostExecute(arrayList);
        this.scaningFolderListener.onScanningSuccessFull(arrayList);
    }
}
