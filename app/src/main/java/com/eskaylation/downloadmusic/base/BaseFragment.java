package com.eskaylation.downloadmusic.base;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.provider.MediaStore.Audio.Media;
import android.provider.Settings.System;
import android.view.View;
import androidx.fragment.app.Fragment;

import com.eskaylation.downloadmusic.R;
import com.eskaylation.downloadmusic.bus.MessageEvent;
import com.eskaylation.downloadmusic.model.Song;
import com.eskaylation.downloadmusic.utils.PreferenceUtils;
import com.eskaylation.downloadmusic.utils.ToastUtils;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public abstract class BaseFragment extends Fragment implements MvpView {
    public Song ringtone = null;

    public interface Callback {
    }

    @Subscribe(sticky = false, threadMode = ThreadMode.MAIN)
    public void subCribeBus(MessageEvent messageEvent) {
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setHasOptionsMenu(false);
        PreferenceUtils.getInstance(getContext());
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
    }

    public void onResume() {
        super.onResume();
        Song song = this.ringtone;
        if (song != null) {
            setRingtone(song);
        }
    }

    public void setRingtone(Song song) {
        if (VERSION.SDK_INT < 23) {
            return;
        }
        if (System.canWrite(getContext())) {
            setRingoneFunction(song);
            this.ringtone = null;
            return;
        }
        ToastUtils.error(getContext(), getString(R.string.txt_need_permission));
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            ((BaseActivity) context).onFragmentAttached();
        }
        EventBus.getDefault().register(this);
    }

    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void setRingoneFunction(Song song) {
        String str = song.mSongPath;
        if (str != null) {
            File file = new File(str);
            if (VERSION.SDK_INT >= 29) {
                SetAsRingtoneOrNotification(file);
            } else {
                ContentValues contentValues = new ContentValues();
                contentValues.put("_data", file.getAbsolutePath());
                contentValues.put("title", song.title);
                contentValues.put("_display_name", song.title);
                contentValues.put("mime_type", "audio/mp3");
                contentValues.put("_size", Long.valueOf(file.length()));
                String str2 = "is_ringtone";
                contentValues.put(str2, Boolean.valueOf(true));
                Uri contentUriForPath = Media.getContentUriForPath(song.mSongPath);
                Uri uri = contentUriForPath;
                Cursor query = getContext().getContentResolver().query(uri, null, "_data=?", new String[]{song.mSongPath}, null);
                if (query != null && query.moveToFirst() && query.getCount() > 0) {
                    String string = query.getString(0);
                    contentValues.put(str2, Boolean.valueOf(true));
                    getContext().getContentResolver().update(contentUriForPath, contentValues, "_data=?", new String[]{song.mSongPath});
                    try {
                        RingtoneManager.setActualDefaultRingtoneUri(getContext(), 1, ContentUris.withAppendedId(contentUriForPath, Long.valueOf(string).longValue()));
                        Context context = getContext();
                        StringBuilder sb = new StringBuilder();
                        sb.append(getString(R.string.set_ringtone_success));
                        sb.append("\n");
                        sb.append(getString(R.string.txt_songs));
                        sb.append(": ");
                        sb.append(song.title);
                        ToastUtils.success(context, sb.toString());
                    } catch (Throwable th) {
                        th.printStackTrace();
                        ToastUtils.error(getContext(), getString(R.string.set_ringtone_error));
                    }
                    query.close();
                }
            }
        }
    }


    public final void SetAsRingtoneOrNotification(File file) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", file.getName());
        contentValues.put("mime_type", "audio/mp3");
        contentValues.put("_display_name", file.getName());
        contentValues.put("is_ringtone", Boolean.valueOf(true));
        if (VERSION.SDK_INT >= 29) {
            Uri insert = getContext().getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, contentValues);
            OutputStream openOutputStream = null;
            try {
                openOutputStream = getContext().getContentResolver().openOutputStream(insert);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            byte[] bArr = new byte[((int) file.length())];
            try {
                BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
                bufferedInputStream.read(bArr, 0, bArr.length);
                bufferedInputStream.close();
                openOutputStream.write(bArr);
                openOutputStream.close();
                openOutputStream.flush();
            } catch (IOException unused) {
            }
            if (openOutputStream != null) {
                try {
                    openOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                RingtoneManager.setActualDefaultRingtoneUri(getContext(), 1, insert);
                Context context = getContext();
                StringBuilder sb = new StringBuilder();
                sb.append(getString(R.string.set_ringtone_success));
                sb.append("\n");
                sb.append(getString(R.string.txt_songs));
                sb.append(": ");
                sb.append(file.getName());
                ToastUtils.success(context, sb.toString());
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtils.error(getContext(), getString(R.string.set_ringtone_error));
            }
        }
    }
}
