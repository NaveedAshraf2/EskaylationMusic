package com.eskaylation.downloadmusic.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.provider.MediaStore.Audio.Media;
import android.provider.Settings.System;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;

import com.eskaylation.downloadmusic.R;
import com.eskaylation.downloadmusic.base.BaseFragment.Callback;
import com.eskaylation.downloadmusic.bus.InternetConection;
import com.eskaylation.downloadmusic.bus.MessageEvent;
import com.eskaylation.downloadmusic.bus.StopService;
import com.eskaylation.downloadmusic.model.AdsManager;
import com.eskaylation.downloadmusic.model.BackgroundModel;
import com.eskaylation.downloadmusic.model.Song;
import com.eskaylation.downloadmusic.service.MusicPlayerService;
import com.eskaylation.downloadmusic.ui.activity.PlayerActivity;
import com.eskaylation.downloadmusic.ui.activity.Rating;
import com.eskaylation.downloadmusic.ui.activity.permission.PermissionActivity;
import com.eskaylation.downloadmusic.utils.AppConstants;
import com.eskaylation.downloadmusic.utils.AppUtils;
import com.eskaylation.downloadmusic.utils.ToastUtils;
import com.eskaylation.downloadmusic.utils.ViewUtils;
import com.eskaylation.downloadmusic.widget.PlayerView.OnViewPlayerListener;



import com.google.android.material.snackbar.Snackbar;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import jp.wasabeef.blurry.Blurry;
import jp.wasabeef.blurry.Blurry.Composer;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public abstract class BaseActivity extends AppCompatActivity implements MvpView, Callback, OnViewPlayerListener{
    public ProgressDialog mProgressDialog;
    public Song ringtone = null;
    public Snackbar snackbarInternet;
    public AsyncTask updateLyricsAsyncTask;

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void event(MessageEvent messageEvent) {
    }

    public void onFragmentAttached() {
    }

    public void setBackgroundThemes(CoordinatorLayout coordinatorLayout, int i) {
        BackgroundModel backgroundModel = (BackgroundModel) AppConstants.listBackground(this).get(i);
        int i2 = backgroundModel.bgRoot;
        if (i2 != -1) {
            coordinatorLayout.setBackgroundResource(i2);
        } else {
            coordinatorLayout.setBackground(AppUtils.createDrawable(backgroundModel.startGradient, backgroundModel.endGradient));
        }
    }

    public void setBackgroundBlur(Context context, CoordinatorLayout coordinatorLayout, int i) {
        BackgroundModel backgroundModel = (BackgroundModel) AppConstants.listBackground(this).get(i);
        if (backgroundModel.bgRoot != -1) {
            setBackground(context, coordinatorLayout, i);
        } else {
            coordinatorLayout.setBackground(AppUtils.createDrawable(backgroundModel.startGradient, backgroundModel.endGradient));
        }
    }

    @SuppressLint({"StaticFieldLeak"})
    public void setBackground(final Context context, final CoordinatorLayout coordinatorLayout, final int i) {
        AsyncTask asyncTask = this.updateLyricsAsyncTask;
        if (asyncTask != null) {
            asyncTask.cancel(false);
        }
        this.updateLyricsAsyncTask = new AsyncTask<Void, Void, Bitmap>() {
            @SuppressLint({"StaticFieldLeak"})
            public void onPreExecute() {
                super.onPreExecute();
            }

            public Bitmap doInBackground(Void... voidArr) {
                return ((BitmapDrawable) BaseActivity.this.getResources().getDrawable(((BackgroundModel) AppConstants.listBackground(context).get(i)).bgNavigation)).getBitmap();
            }

            public void onPostExecute(Bitmap bitmap) {
                Composer with = Blurry.with(context);
                with.radius(25);
                with.async();
                with.animate();
                with.from(bitmap).into(coordinatorLayout);
            }

            public void onCancelled(Bitmap bitmap) {
                onPostExecute((Bitmap) null);
            }
        }.execute(new Void[0]);
    }


    public void loadBanner(Activity activity,FrameLayout frameLayout) {
        AdsManager.showBanner(BaseActivity.this,frameLayout);
    }

    public void onNext() {
        Intent intent = new Intent(this, MusicPlayerService.class);
        intent.setAction("com.eskaylation.downloadmusic.action.next");
        startService(intent);
    }

    public void onPrive() {
        Intent intent = new Intent(this, MusicPlayerService.class);
        intent.setAction("com.eskaylation.downloadmusic.action.prive");
        startService(intent);
    }

    public void onPlayPause() {
        Intent intent = new Intent(this, MusicPlayerService.class);
        intent.setAction("com.eskaylation.downloadmusic.action.playpause");
        startService(intent);
    }

    public void onClickPlayer() {
        startActivity(new Intent(this, PlayerActivity.class));
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (VERSION.SDK_INT >= 21) {
            getWindow().setNavigationBarColor(-16777216);
        }
    }

    public void marginNavigationBar(View view) {
        if (AppUtils.hasSoftKeys(this)) {
            view.setPadding(0, 0, 0, ViewUtils.getHeightNavigationBar(this));
            getWindow().setNavigationBarColor(-16777216);
        }
    }

    public boolean checkListPermission() {
        if (ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") != -1) {
            return true;
        }
        startActivity(new Intent(this, PermissionActivity.class));
        finish();
        Intent intent = new Intent(this, MusicPlayerService.class);
        intent.setAction("com.eskaylation.downloadmusic.action.stop_music");
        startService(intent);
        return false;
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

        if (System.canWrite(this)) {
            setRingoneFunction(song);
            this.ringtone = null;
            return;
        }
        ToastUtils.error(getApplicationContext(), (int) R.string.txt_need_permission);
        this.ringtone = null;
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
                Cursor query = getContentResolver().query(uri, null, "_data=?", new String[]{song.mSongPath}, null);
                if (query != null && query.moveToFirst() && query.getCount() > 0) {
                    String string = query.getString(0);
                    contentValues.put(str2, Boolean.valueOf(true));
                    getContentResolver().update(contentUriForPath, contentValues, "_data=?", new String[]{song.mSongPath});
                    try {
                        RingtoneManager.setActualDefaultRingtoneUri(this, 1, ContentUris.withAppendedId(contentUriForPath, Long.valueOf(string).longValue()));
                        StringBuilder sb = new StringBuilder();
                        sb.append(getString(R.string.set_ringtone_success));
                        sb.append("\n");
                        sb.append(getString(R.string.txt_songs));
                        sb.append(": ");
                        sb.append(song.title);
                        ToastUtils.success((Context) this, sb.toString());
                    } catch (Throwable th) {
                        th.printStackTrace();
                        ToastUtils.error((Context) this, getString(R.string.set_ringtone_error));
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
            Uri insert = getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, contentValues);
            OutputStream openOutputStream = null;
            try {
                openOutputStream = getContentResolver().openOutputStream(insert);
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
                RingtoneManager.setActualDefaultRingtoneUri(this, 1, insert);
                StringBuilder sb = new StringBuilder();
                sb.append(getString(R.string.set_ringtone_success));
                sb.append("\n");
                sb.append(getString(R.string.txt_songs));
                sb.append(": ");
                sb.append(file.getName());
                ToastUtils.success((Context) this, sb.toString());
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtils.error((Context) this, getString(R.string.set_ringtone_error));
            }
        }
    }

    public void hideLoading() {
        ProgressDialog progressDialog = this.mProgressDialog;
        if (progressDialog != null && progressDialog.isShowing()) {
            this.mProgressDialog.cancel();
        }
    }

    public void paddingStatusbar(View view) {
        view.setPadding(0, AppUtils.getHeightStatusBar(this), 0, 0);
    }

    public void transitionBG(View view) {
        AnimationDrawable animationDrawable = (AnimationDrawable) view.getBackground();
        animationDrawable.setEnterFadeDuration(7500);
        animationDrawable.setExitFadeDuration(10000);
        animationDrawable.start();
    }

    public void marginStb_trasitionBG(View view) {
        view.setPadding(8, AppUtils.getHeightStatusBar(this), 8, 8);
        transitionBG(view);
    }

    public void marginStatusbarNormalView(View view) {
        view.setPadding(AppUtils.dpToPixels(0), AppUtils.getHeightStatusBar(this) + AppUtils.dpToPixels(0), AppUtils.dpToPixels(0), AppUtils.dpToPixels(0));
    }

    @SuppressLint("ResourceType")
    public void showSnackBarInternet(String str) {
        this.snackbarInternet = Snackbar.make(findViewById(16908290), str, 2000);
        View view = this.snackbarInternet.getView();
        TextView textView = (TextView) view.findViewById(2131363933);
        textView.setMaxLines(1);
        textView.setTextColor(ContextCompat.getColor(this, R.color.white));
        textView.setTextSize(14.0f);
        if (VERSION.SDK_INT >= 23) {
            textView.setTextAlignment(4);
        } else {
            textView.setGravity(1);
        }
        view.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        this.snackbarInternet.show();
    }

    public void showMessage(String str) {
        if (str != null) {
            Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.txt_error), Toast.LENGTH_SHORT).show();
        }
    }

    public void hideKeyboard() {
        View currentFocus = getCurrentFocus();
        if (currentFocus != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out);
    }

    public void openUrl(String str) {
        startActivity(new Intent("android.intent.action.VIEW", Uri.parse(str)));
    }

    public void openAppInGooglePlay(String str) {
        String str2 = "android.intent.action.VIEW";
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("market://details?id=");
            sb.append(str);
            startActivity(new Intent(str2, Uri.parse(sb.toString())));
        } catch (ActivityNotFoundException unused) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("https://play.google.com/store/apps/details?id=");
            sb2.append(str);
            startActivity(new Intent(str2, Uri.parse(sb2.toString())));
        }
    }


    @SuppressLint("WrongConstant")
    public void rateInStore() {
        startActivity(new Intent(BaseActivity.this , Rating.class));
//        StringBuilder sb = new StringBuilder();
//        sb.append("market://details?id=");
//        sb.append(getPackageName());
//        String str = "android.intent.action.VIEW";
//        Intent intent = new Intent(str, Uri.parse(sb.toString()));
//        intent.addFlags(1208483840);
//        try {
//            startActivity(intent);
//        } catch (ActivityNotFoundException unused) {
//            StringBuilder sb2 = new StringBuilder();
//            sb2.append("http://play.google.com/store/apps/details?id=");
//            sb2.append(getPackageName());
//            startActivity(new Intent(str, Uri.parse(sb2.toString())));
//        }
    }

    @Subscribe(sticky = false, threadMode = ThreadMode.MAIN)
    public void event(InternetConection internetConection) {
        showSnackBarInternet(internetConection.mess);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onStopService(StopService stopService) {
        if (stopService.stop) {
            EventBus.getDefault().postSticky(new StopService(false));
        }
    }

}
