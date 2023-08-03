package com.eskaylation.downloadmusic.ui.dialog;

import android.app.Dialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build.VERSION;
import android.provider.MediaStore.Audio.Media;
import android.provider.Settings.System;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eskaylation.downloadmusic.R;
import com.eskaylation.downloadmusic.base.BaseApplication;
import com.eskaylation.downloadmusic.base.GlideApp;
import com.eskaylation.downloadmusic.database.SongListDao;
import com.eskaylation.downloadmusic.database.SongListSqliteHelper;
import com.eskaylation.downloadmusic.model.Favorite;
import com.eskaylation.downloadmusic.model.Song;
import com.eskaylation.downloadmusic.utils.AppUtils;
import com.eskaylation.downloadmusic.utils.ArtworkUtils;
import com.eskaylation.downloadmusic.utils.FileProviderApp;
import com.eskaylation.downloadmusic.utils.ToastUtils;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

public class DialogMoreSongUtil {
    public Context context;
    public DialogFavorite dialogFavorite;
    public Dialog dialogMore;
    public boolean isPlayerActivity;
    public DialogMoreListener listener;
    public SongListDao songListDao;
    public SongListSqliteHelper songListSqliteHelperl;

    public DialogMoreSongUtil(Context context2, DialogMoreListener dialogMoreListener, boolean z) {
        this.listener = dialogMoreListener;
        this.context = context2;
        this.isPlayerActivity = z;
    }

    public void showDialogMore(Song song, boolean z, Favorite favorite, boolean z2) {
        this.dialogMore = new Dialog(this.context);
        this.dialogMore.requestWindowFeature(1);
        this.dialogMore.setContentView(R.layout.dialog_more_song);
        this.dialogMore.getWindow().setBackgroundDrawableResource(17170445);
        Window window = this.dialogMore.getWindow();
        LayoutParams attributes = window.getAttributes();
        attributes.gravity = 80;
        attributes.flags &= -5;
        window.setAttributes(attributes);
        this.dialogMore.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        this.dialogMore.getWindow().setLayout(-1, -2);
        TextView textView = (TextView) this.dialogMore.findViewById(R.id.tv_name);
        TextView textView2 = (TextView) this.dialogMore.findViewById(R.id.tv_artist);
        TextView textView3 = (TextView) this.dialogMore.findViewById(R.id.btn_detail);
        TextView textView4 = (TextView) this.dialogMore.findViewById(R.id.btn_set_ringtone);
        TextView textView5 = (TextView) this.dialogMore.findViewById(R.id.btn_share);
        TextView textView6 = (TextView) this.dialogMore.findViewById(R.id.btn_delete);
        TextView textView7 = (TextView) this.dialogMore.findViewById(R.id.btn_add_playlist);
        TextView textView8 = (TextView) this.dialogMore.findViewById(R.id.btn_play_latter);
        if (!z2) {
            textView8.setVisibility(View.GONE);
        } else {
            textView8.setVisibility(View.VISIBLE);
        }
        textView.setText(song.getTitle());
        textView2.setText(song.getArtist());
        textView3.setOnClickListener(new OnClickListener() {


            public final void onClick(View view) {
                DialogMoreSongUtil.this.DialogMoreSongUtil0(song, view);
            }
        });
        if (favorite != null) {
            textView7.setText(this.context.getString(R.string.delete_from_playlist));
            textView7.setOnClickListener(new OnClickListener() {

                public final void onClick(View view) {
                    DialogMoreSongUtil.this.DialogMoreSongUtil1(favorite, song, view);
                }
            });
        } else {
            textView7.setText(this.context.getString(R.string.txt_add_to_playlist));
            textView7.setOnClickListener(new OnClickListener() {


                public final void onClick(View view) {
                    DialogMoreSongUtil.this.DialogMoreSongUtil2(song, view);
                }
            });
        }
        textView8.setOnClickListener(new OnClickListener() {

            public final void onClick(View view) {
                DialogMoreSongUtil.this.DialogMoreSongUtil4(song, view);
            }
        });
        textView4.setOnClickListener(new OnClickListener() {


            public final void onClick(View view) {
                DialogMoreSongUtil.this.DialogMoreSongUtil43(song, view);
            }
        });
        textView5.setOnClickListener(new OnClickListener() {


            public final void onClick(View view) {
                DialogMoreSongUtil.this.DialogMoreSongUtil5(song, view);
            }
        });
        if (z) {
            textView6.setVisibility(View.GONE);
        } else {
            textView6.setVisibility(View.VISIBLE);
        }
        textView6.setOnClickListener(new OnClickListener() {
      

            public final void onClick(View view) {
                DialogMoreSongUtil.this.DialogMoreSongUtil6(song, view);
            }
        });
        this.dialogMore.show();
    }

    public  void DialogMoreSongUtil0(Song song, View view) {
        showDialogDetail(song);
        this.dialogMore.dismiss();
    }

    public  void DialogMoreSongUtil1(Favorite favorite, Song song, View view) {
        this.dialogMore.dismiss();
        this.songListSqliteHelperl = new SongListSqliteHelper(this.context, favorite.favorite_id);
        this.songListDao = new SongListDao(this.songListSqliteHelperl);
        if (this.songListDao.deleteFavoriteSong(song) != -1) {
            this.listener.onDeleteSongFromPlaylist(song);
        } else {
            ToastUtils.error(this.context, (int) R.string.failed);
        }
    }

    public  void DialogMoreSongUtil2(Song song, View view) {
        this.dialogMore.dismiss();
        this.dialogFavorite = new DialogFavorite(this.context, new DialogFavoriteListener() {
            public void deletePlaylistDone(int i) {
            }

            public void onCreateNewPlaylist(Favorite favorite) {
            }

            public void onOpenAddSong(Favorite favorite) {
            }

            public void onUpdatePlaylist(int i, Favorite favorite) {
            }

            public void onCreatePlaylistFromDialog(Song song) {
                DialogMoreSongUtil.this.dialogFavorite.showDialogAddSong(song, DialogMoreSongUtil.this.isPlayerActivity);
            }
        });
        this.dialogFavorite.showDialogAddSong(song, this.isPlayerActivity);
    }

    public  void DialogMoreSongUtil4(Song song, View view) {
        this.listener.onAddToPlayNext(song);
        this.dialogMore.dismiss();
    }

    public  void DialogMoreSongUtil43(Song song, View view) {
        setRingtone(song);
        this.dialogMore.dismiss();
    }

    public  void DialogMoreSongUtil5(Song song, View view) {
        this.dialogMore.dismiss();
        FileProviderApp.share(this.context, song.getmSongPath());
    }

    public  void DialogMoreSongUtil6(Song song, View view) {
        deleteFile(song);
        this.dialogMore.dismiss();
    }

    public void closeDialog() {
        Dialog dialog = this.dialogMore;
        if (dialog != null && dialog.isShowing()) {
            this.dialogMore.dismiss();
        }
    }

    public void showDialogDetail(Song song) {
        Dialog dialog = new Dialog(this.context);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawableResource(17170445);
        dialog.setContentView(R.layout.dialog_info_song);
        Window window = dialog.getWindow();
        LayoutParams attributes = window.getAttributes();
        attributes.gravity = 80;
        attributes.flags &= -5;
        window.setAttributes(attributes);
        dialog.getWindow().setLayout(-1, -2);
        TextView textView = (TextView) dialog.findViewById(R.id.tv_info);
        Glide.with(this.context).load(ArtworkUtils.uri(song.albumId)).error((int) R.drawable.song_not_found).into((ImageView) dialog.findViewById(R.id.img_thumb));
        String str = song.duration;
        String convertDuration = str != null ? AppUtils.convertDuration(Long.valueOf(str).longValue()) : this.context.getString(R.string.unknow);
        String str2 = song.artist;
        if (str2 == null) {
            str2 = this.context.getString(R.string.unknow);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<b>");
        sb.append(this.context.getString(R.string.txt_name));
        String str3 = ": </b>";
        sb.append(str3);
        sb.append(song.getTitle());
        String str4 = "<br/> <br/> <b>";
        sb.append(str4);
        sb.append(this.context.getString(R.string.txt_artist));
        sb.append(str3);
        sb.append(str2);
        sb.append(str4);
        sb.append(this.context.getString(R.string.txt_duration));
        sb.append(str3);
        sb.append(convertDuration);
        sb.append(str4);
        sb.append(this.context.getString(R.string.txt_location));
        sb.append(str3);
        sb.append(song.getmSongPath());
        textView.setText(Html.fromHtml(sb.toString()));
        dialog.show();
    }

    public final void setRingtone(Song song) {
        if (VERSION.SDK_INT < 23) {
            setRingoneFunction(song);
        } else if (System.canWrite(this.context)) {
            setRingoneFunction(song);
        } else {
            this.listener.onNeedPermisionWriteSetting(song);
        }
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
                Cursor query = this.context.getContentResolver().query(uri, null, "_data=?", new String[]{song.mSongPath}, null);
                if (query != null && query.moveToFirst() && query.getCount() > 0) {
                    String string = query.getString(0);
                    contentValues.put(str2, Boolean.valueOf(true));
                    this.context.getContentResolver().update(contentUriForPath, contentValues, "_data=?", new String[]{song.mSongPath});
                    try {
                        RingtoneManager.setActualDefaultRingtoneUri(this.context, 1, ContentUris.withAppendedId(contentUriForPath, Long.valueOf(string).longValue()));
                        Context context2 = this.context;
                        StringBuilder sb = new StringBuilder();
                        sb.append(this.context.getString(R.string.set_ringtone_success));
                        sb.append("\n");
                        sb.append(this.context.getString(R.string.txt_songs));
                        sb.append(": ");
                        sb.append(song.title);
                        ToastUtils.success(context2, sb.toString());
                    } catch (Throwable th) {
                        th.printStackTrace();
                        Context context3 = this.context;
                        ToastUtils.error(context3, context3.getString(R.string.set_ringtone_error));
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
            Uri insert = this.context.getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, contentValues);
            OutputStream openOutputStream = null;
            try {
                openOutputStream = this.context.getContentResolver().openOutputStream(insert);
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
                RingtoneManager.setActualDefaultRingtoneUri(this.context, 1, insert);
                Context context2 = this.context;
                StringBuilder sb = new StringBuilder();
                sb.append(this.context.getString(R.string.set_ringtone_success));
                sb.append("\n");
                sb.append(this.context.getString(R.string.txt_songs));
                sb.append(": ");
                sb.append(file.getName());
                ToastUtils.success(context2, sb.toString());
            } catch (Exception e) {
                e.printStackTrace();
                Context context3 = this.context;
                ToastUtils.error(context3, context3.getString(R.string.set_ringtone_error));
            }
        }
    }

    public void deleteFile(Song song) {
        File file = new File(song.getmSongPath());
        String str = "_data=?";
        if (!file.exists()) {
            this.listener.onDeleteSongSuccessFull(song);
            Context context2 = this.context;
            ToastUtils.error(context2, context2.getString(R.string.txt_file_not_found));
        } else if (file.delete()) {
            this.listener.onDeleteSongSuccessFull(song);
            Uri contentUriForPath = Media.getContentUriForPath(song.getmSongPath());
            this.context.getContentResolver().delete(contentUriForPath, str, new String[]{song.getmSongPath()});
            BaseApplication.notificationDeleteFile(song);
            this.context.deleteFile(file.getName());
            this.listener.onDeleteSongSuccessFull(song);
            Context context3 = this.context;
            ToastUtils.success(context3, context3.getString(R.string.txt_delete_file_successfull));
        } else {
            try {
                file.getCanonicalFile().delete();
                if (file.exists()) {
                    Uri contentUriForPath2 = Media.getContentUriForPath(song.getmSongPath());
                    this.context.getContentResolver().delete(contentUriForPath2, str, new String[]{song.getmSongPath()});
                    this.context.deleteFile(file.getName());
                    this.listener.onDeleteSongSuccessFull(song);
                    BaseApplication.notificationDeleteFile(song);
                    ToastUtils.success(this.context, this.context.getString(R.string.txt_delete_file_successfull));
                }
            } catch (IOException e) {
                Log.e("Exeption", e.getMessage());
                Context context4 = this.context;
                ToastUtils.error(context4, context4.getString(R.string.txt_cant_delete_file));
                e.printStackTrace();
            }
        }
        Uri contentUriForPath3 = Media.getContentUriForPath(song.getmSongPath());
        this.context.getContentResolver().delete(contentUriForPath3, str, new String[]{song.getmSongPath()});
    }
}
