package com.eskaylation.downloadmusic.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Process;
import android.util.Log;
import android.view.WindowManager;
import android.webkit.WebView;
import com.eskaylation.downloadmusic.bus.NotifyDeleteMusic;
import com.eskaylation.downloadmusic.database.EqualizerDao;
import com.eskaylation.downloadmusic.database.FavoriteDao;
import com.eskaylation.downloadmusic.database.FavoriteSqliteHelper;
import com.eskaylation.downloadmusic.database.OfflineSqliteHelper;
import com.eskaylation.downloadmusic.model.AdsApplication;
import com.eskaylation.downloadmusic.model.BackgroundModel;
import com.eskaylation.downloadmusic.model.Song;
import com.eskaylation.downloadmusic.utils.AppConstants;

import com.eskaylation.downloadmusic.utils.PreferenceUtils;

import com.google.android.exoplayer2.upstream.DataSource.Factory;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.FileDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.upstream.cache.Cache;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.NoOpCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.google.android.exoplayer2.util.Util;

import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.connection.FileDownloadUrlConnection.Configuration;
import com.liulishuo.filedownloader.connection.FileDownloadUrlConnection.Creator;
import com.liulishuo.filedownloader.services.DownloadMgrInitialParams.InitCustomMaker;
import com.onesignal.OneSignal;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.orhanobut.logger.PrettyFormatStrategy.Builder;
import java.io.File;
import java.util.ArrayList;
import org.greenrobot.eventbus.EventBus;
import org.schabi.newpipe.extractor.NewPipe;
import org.schabi.newpipe.extractor.downloader.Downloader;
import org.schabi.newpipe.extractor.localization.Localization;

public class BaseApplication extends AdsApplication implements OnSharedPreferenceChangeListener{

    public Cache downloadCache;
    public File downloadDirectory;
    public FavoriteDao favoriteDao;
    public FavoriteSqliteHelper favoriteSqliteHelper;
    public ArrayList<BackgroundModel> lstBackground = new ArrayList<>();
    public PreferenceUtils mPreferenceUtils;
    public OfflineSqliteHelper sqliteHelper;
    public String userAgent;

    public static void setContext(Context context) {
    }

    public void attachBaseContext(Context context) {
        super.attachBaseContext(context);
    }

    public void onCreate() {
        super.onCreate();
      //  appOpenManager = new AppOpenManager(this);
        this.mPreferenceUtils = PreferenceUtils.getInstance(this);
        this.mPreferenceUtils.setChangeListener(this);
        this.lstBackground = AppConstants.listBackground(this);
        setupActivityListener();
        BackgroundModel backgroundModel = (BackgroundModel) this.lstBackground.get(this.mPreferenceUtils.getThemesPosition());
        if (VERSION.SDK_INT >= 28) {
            String processName = getProcessName(this);
            if (!getPackageName().equals(processName)) {
                WebView.setDataDirectorySuffix(processName);
            }
        }
        Builder newBuilder = PrettyFormatStrategy.newBuilder();
        newBuilder.tag("MP3DOWNLOAD-V3");
        Logger.addLogAdapter(new AndroidLogAdapter(newBuilder.build()));

        NewPipe.init(getDownloader(), new Localization("Vi", "vi"));
        this.sqliteHelper = new OfflineSqliteHelper(this);
        new EqualizerDao(this.sqliteHelper);
        this.favoriteSqliteHelper = new FavoriteSqliteHelper(this);
        this.favoriteDao = new FavoriteDao(this, this.favoriteSqliteHelper);
        this.favoriteDao.insertFavorite("DEFAULT_FAVORITEDOWNLOAD");
        this.userAgent = Util.getUserAgent(this, "ExoPlayerDemo");
        InitCustomMaker initCustomMaker = FileDownloader.setupOnApplicationOnCreate(this);
        Configuration configuration = new Configuration();
        configuration.connectTimeout(15000);
        configuration.readTimeout(15000);
        initCustomMaker.connectionCreator(new Creator(configuration));
        initCustomMaker.commit();

        // TODO OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId("8294380f-26f8-4794-82cb-28fe70349250");
    }

    public final String getProcessName(Context context) {
        if (context == null) {
            return null;
        }
        for (RunningAppProcessInfo runningAppProcessInfo : ((ActivityManager)
                context.getSystemService("activity")).getRunningAppProcesses()) {
            if (runningAppProcessInfo.pid == Process.myPid()) {
                Log.e("List Process", runningAppProcessInfo.processName);
                return runningAppProcessInfo.processName;
            }
        }
        return null;
    }

    public Downloader getDownloader() {
        return DownloaderImpl.init(null);
    }

    public static void notificationDeleteFile(Song song) {
        EventBus.getDefault().postSticky(new NotifyDeleteMusic(song));
    }

    public Factory buildDataSourceFactory() {
        return buildReadOnlyCacheDataSource(new DefaultDataSourceFactory((Context) this,
                (Factory) buildHttpDataSourceFactory()), getDownloadCache());
    }

    public HttpDataSource.Factory buildHttpDataSourceFactory() {
        return new DefaultHttpDataSourceFactory(this.userAgent);
    }

    public final synchronized Cache getDownloadCache() {
        if (this.downloadCache == null) {
            this.downloadCache = new SimpleCache(new File(getDownloadDirectory(), "downloads"), new NoOpCacheEvictor());
        }
        return this.downloadCache;
    }

    public final File getDownloadDirectory() {
        if (this.downloadDirectory == null) {
            this.downloadDirectory = getExternalFilesDir(null);
            if (this.downloadDirectory == null) {
                this.downloadDirectory = getFilesDir();
            }
        }
        return this.downloadDirectory;
    }

    public static CacheDataSourceFactory buildReadOnlyCacheDataSource(DefaultDataSourceFactory defaultDataSourceFactory, Cache cache) {
        CacheDataSourceFactory cacheDataSourceFactory = new CacheDataSourceFactory(cache,
                defaultDataSourceFactory, new FileDataSourceFactory(), null,
                2, null);
        return cacheDataSourceFactory;
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String str) {
        if (str.equals("THEME_POSITION")) {
            Log.e("PreferenceChanged", str);
            BackgroundModel backgroundModel = (BackgroundModel) this.lstBackground.get
                    (this.mPreferenceUtils.getThemesPosition());
        }
    }
    private void setupActivityListener() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);            }
            @Override
            public void onActivityStarted(Activity activity) {
            }
            @Override
            public void onActivityResumed(Activity activity) {

            }
            @Override
            public void onActivityPaused(Activity activity) {

            }
            @Override
            public void onActivityStopped(Activity activity) {
            }
            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }
            @Override
            public void onActivityDestroyed(Activity activity) {
            }
        });
    }



}
