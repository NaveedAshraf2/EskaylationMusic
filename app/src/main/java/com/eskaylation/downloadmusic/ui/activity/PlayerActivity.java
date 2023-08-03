package com.eskaylation.downloadmusic.ui.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.provider.MediaStore.Audio.Media;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.airbnb.lottie.LottieAnimationView;
import com.eskaylation.downloadmusic.adapter.PagerAdapter;
import com.eskaylation.downloadmusic.base.BaseActivity;
import com.eskaylation.downloadmusic.base.BaseApplication;
import com.eskaylation.downloadmusic.bus.AddToLoveSong;
import com.eskaylation.downloadmusic.bus.EndSong;
import com.eskaylation.downloadmusic.bus.FinishDownload;
import com.eskaylation.downloadmusic.bus.PaletteColor;
import com.eskaylation.downloadmusic.bus.StopService;
import com.eskaylation.downloadmusic.database.SongListDao;
import com.eskaylation.downloadmusic.database.SongListSqliteHelper;
import com.eskaylation.downloadmusic.model.AdsManager;
import com.eskaylation.downloadmusic.model.Song;
import com.eskaylation.downloadmusic.service.MusicPlayerService;
import com.eskaylation.downloadmusic.service.MusicPlayerService.MusicServiceBinder;
import com.eskaylation.downloadmusic.ui.activity.main.MainActivity;
import com.eskaylation.downloadmusic.ui.activity.nowplaying.NowPlayingActivity;
import com.eskaylation.downloadmusic.ui.dialog.DialogMoreListener;
import com.eskaylation.downloadmusic.ui.dialog.DialogMoreSongUtil;
import com.eskaylation.downloadmusic.ui.fragment.player.FragmentPlayer;


import com.eskaylation.downloadmusic.utils.AppUtils;

import com.eskaylation.downloadmusic.utils.ConfigApp;
import com.eskaylation.downloadmusic.utils.PreferenceUtils;
import com.eskaylation.downloadmusic.utils.RatePreferenceUtils;
import com.eskaylation.downloadmusic.utils.ToastUtils;
import com.eskaylation.downloadmusic.widget.SwipeViewPager;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultRenderersFactory;

import com.google.android.exoplayer2.source.hls.DefaultHlsExtractorFactory;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadSampleListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import com.wang.avi.AVLoadingIndicatorView;
import java.io.File;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import com.eskaylation.downloadmusic.R;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class PlayerActivity extends BaseActivity implements DialogMoreListener {
    public String ACTION_NOTIFICATION = "none";
    public PagerAdapter adapterPagerControl;
    public BaseDownloadTask baseDownloadTask;
@BindView(R.id.btnNextBig)
public ImageButton btnNextBig;
    @BindView(R.id.btnPriveBig)
    public ImageButton btnPriveBig;
    @BindView(R.id.btnBack)
    public ImageButton btnBack;

    //@BindView(R.id.btn_cancel)
    public View btnCancel;
    @BindView(R.id.btnDownload)
    public LottieAnimationView btnDownload;

    @BindView(R.id.btn_favorite)
    public ImageButton btnFavorite;

    public ImageView btn_NextBig;
    @BindView(R.id.btn_loop)
    public ImageView btn_loop;

    @BindView(R.id.btn_more)
    public ImageView btn_more;
@BindView(R.id.btn_now_Playing)
    public ImageView btn_now_Playing;
    @BindView(R.id.btnPlayPauseBig)
    public ImageView btn_playPause;

    public ImageView btn_priveBig;
    @BindView(R.id.btn_shuffle)
    public ImageView btn_shuffle;
    @BindView(R.id.bufferView)
    public AVLoadingIndicatorView bufferView;
    public ServiceConnection connection = new ServiceConnection() {
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            PlayerActivity.this.musicPlayerService = ((MusicServiceBinder) iBinder).getService();
            PlayerActivity.this.mBound = true;
            MusicPlayerService access$000 = PlayerActivity.this.musicPlayerService;
            PlayerActivity playerActivity = PlayerActivity.this;
            access$000.setUIConTrolPlayerActivity(playerActivity.tv_name, playerActivity.tv_artist, playerActivity.btn_playPause, playerActivity.tv_duration, playerActivity.tv_timePlaying, playerActivity.seekBarSmall, playerActivity.btnFavorite, playerActivity.bufferView);
            PlayerActivity.this.musicPlayerService.initDataPlayerActivity();
            if (PlayerActivity.this.musicPlayerService.isPlayOnline()) {
                PlayerActivity.this.btnFavorite.setVisibility(GONE);
                PlayerActivity.this.btn_more.setVisibility(GONE);
                PlayerActivity.this.btnDownload.setVisibility(VISIBLE);
                return;
            }
            PlayerActivity.this.btnDownload.setVisibility(GONE);
            PlayerActivity.this.btnFavorite.setVisibility(VISIBLE);
            PlayerActivity.this.btn_more.setVisibility(VISIBLE);
            if (PlayerActivity.this.preferenceUtils.getBoolean("com.eskaylation.downloadmusic.SHUFFLE_MUSIC")) {
                PlayerActivity.this.btn_shuffle.setImageResource(R.drawable.ic_shuffle_on);
            } else {
                PlayerActivity.this.btn_shuffle.setImageResource(R.drawable.ic_shuffle);
            }
        }

        public void onServiceDisconnected(ComponentName componentName) {
            PlayerActivity.this.mBound = false;
        }
    };
    @BindView(R.id.coordinator)
    public CoordinatorLayout coordinator;
    public Dialog dialogDownload;
    public DialogMoreSongUtil dialogMoreSongUtil;
    public Dialog dialogRating;
    public String dirpath;
    public String fileNameSave;
    public boolean mBound = false;
    public File mFileOutput;

    public MusicPlayerService musicPlayerService;
    @BindView(R.id.pager_control)
    public SwipeViewPager pagerControl;

    public PreferenceUtils preferenceUtils;

    @BindView(R.id.seekBar_small)
    public AppCompatSeekBar seekBarSmall;
    public SongListDao songListDao;
    public SongListSqliteHelper songListSqliteHelper;
    //@BindView(R.id.tvProgressDownload)
    public TextView tvProgressDownload;

    @BindView(R.id.tv_artist)
    public TextView tv_artist;

    @BindView(R.id.tv_duration)
    public TextView tv_duration;

    @BindView(R.id.tv_name)
    public TextView tv_name;

    @BindView(R.id.tv_time_playing)
    public TextView tv_timePlaying;

    public static  boolean lambda$init$0(View view, MotionEvent motionEvent) {
        return false;
    }

    @SuppressLint({"SetTextI18n"})
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void EndSong(EndSong endSong) {
    }

    public void onDeleteSongFromPlaylist(Song song) {
    }

    public void onDeleteSongSuccessFull(Song song) {
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void paletteColor(PaletteColor paletteColor) {
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void refreshData(FinishDownload finishDownload) {
    }

    public void onStart() {
        super.onStart();
        checkListPermission();
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_player);
        BaseApplication.setContext(this);
        init();
        if (!ConfigApp.getInstance(this).isHideRate()) {
            showRateDialog();
        }
    }

    public void onResume() {
        super.onResume();
        bindService();
    }

    public void onBackPressed() {
        unbindServicePlayMusic();
        if (this.ACTION_NOTIFICATION != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }
        finish();
    }

    public void onDestroy() {
        super.onDestroy();
        BaseApplication.setContext(null);
    }

    public void onStop() {
        unbindServicePlayMusic();
        Dialog dialog = this.dialogRating;
        if (dialog != null && dialog.isShowing()) {
            this.dialogRating.dismiss();
        }
        super.onStop();
    }

    @SuppressLint({"ClickableViewAccessibility"})
    public void init() {
        ButterKnife.bind(this);
        setBackgroundThemes(this.coordinator, PreferenceUtils.getInstance(this).getThemesPosition());
        this.ACTION_NOTIFICATION = getIntent().getStringExtra("notify");
        this.btn_shuffle.setOnTouchListener(PlayerActivity0.INSTANCE);
        this.songListSqliteHelper = new SongListSqliteHelper(this, "DEFAULT_FAVORITEDOWNLOAD");
        this.songListDao = new SongListDao(this.songListSqliteHelper);
        this.dialogMoreSongUtil = new DialogMoreSongUtil(this, this, true);
        this.preferenceUtils = PreferenceUtils.getInstance(this);
        this.tv_name.setSelected(true);
        if (this.preferenceUtils.getBoolean("com.eskaylation.downloadmusic.SHUFFLE_MUSIC")) {
            this.btn_shuffle.setImageResource(R.drawable.ic_shuffle_on);
        } else {
            this.btn_shuffle.setImageResource(R.drawable.ic_shuffle);
        }
        String str = "com.eskaylation.downloadmusic.action.loop_music";
        if (this.preferenceUtils.getInt(str) == 0) {
            this.btn_loop.setImageResource(R.drawable.ic_loop);
        } else if (this.preferenceUtils.getInt(str) == 1) {
            this.btn_loop.setImageResource(R.drawable.ic_loop_one);
        } else if (this.preferenceUtils.getInt(str) == 999) {
            this.btn_loop.setImageResource(R.drawable.ic_loop_all);
        }
        this.seekBarSmall.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (z && PlayerActivity.this.musicPlayerService != null) {
                    PlayerActivity.this.musicPlayerService.seekMusic(i);
                }
            }
        });
        initViewPager();
    }

    public void onAddToFavorite() {
        if (!this.musicPlayerService.checkLoveSong()) {
            this.btnFavorite.setImageResource(R.drawable.favorite_on);
            if (this.songListDao.insertFavoriteSong(this.musicPlayerService.getCurentSong()) != -1) {
                ToastUtils.success((Context) this, getString(R.string.add_to_love_song_done));
            } else {
                ToastUtils.warning((Context) this, getString(R.string.song_exist));
            }
        } else {
            this.btnFavorite.setImageResource(R.drawable.favorite_off);
            this.songListDao.deleteFavoriteSong(this.musicPlayerService.getCurentSong());
            ToastUtils.success((Context) this, getString(R.string.delete_love_song_done));
        }
        this.musicPlayerService.refreshLoveSong();
    }

    public void initViewPager() {
        this.adapterPagerControl = new PagerAdapter(getSupportFragmentManager());
        this.adapterPagerControl.addFragment(new FragmentPlayer(), getString(R.string.tit_song));
        this.pagerControl.setAdapter(this.adapterPagerControl);
        this.pagerControl.setOffscreenPageLimit(this.adapterPagerControl.getCount() - 1);

btn_loop.setOnClickListener(new OnClickListener() {
    @Override
    public void onClick(View view) {
        loopMusic();
    }
});
        btnNextBig.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                PlayerActivity.this.next();
//                AdsManager.showNext(PlayerActivity.this, new AdsManager.AdCloseListener() {
//                    @Override
//                    public void onAdClosed() {
//                        PlayerActivity.this.next();
//                    }
//                });

            }
        });

        btnBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnDownload.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!musicPlayerService.isPlayOnline()) {
                    return;
                }
                if (AppUtils.isMusicDownloaded(PlayerActivity.this, musicPlayerService.getCurrentOnline())) {
                    ToastUtils.success((Context) PlayerActivity.this, getString(R.string.this_song_downloaded));
                } else {
                    download();
                }
            }
        });
        btn_playPause.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                playAudio();
            }
        });

        btnPriveBig.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                PlayerActivity.this.prive();
//                AdsManager.showNext(PlayerActivity.this, new AdsManager.AdCloseListener() {
//                    @Override
//                    public void onAdClosed() {
//
//                    }
//                });

            }
        });

        btn_more.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (musicPlayerService.getCurentSong() != null) {
                    dialogMoreSongUtil.showDialogMore(musicPlayerService.getCurentSong(), true, null, false);
                    return;
                }
            }
        });

        btn_now_Playing.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showListPlaying();
            }
        });
        btnFavorite.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!musicPlayerService.checkLoveSong()) {
                    btnFavorite.setImageResource(R.drawable.favorite_on);
                    if (songListDao.insertFavoriteSong(musicPlayerService.getCurentSong()) != -1) {
                        ToastUtils.success((Context) PlayerActivity.this, getString(R.string.add_to_love_song_done));
                    } else {
                        ToastUtils.warning((Context)PlayerActivity. this, getString(R.string.song_exist));
                    }
                } else {
                    btnFavorite.setImageResource(R.drawable.favorite_off);
                    songListDao.deleteFavoriteSong(musicPlayerService.getCurentSong());
                    ToastUtils.success((Context) PlayerActivity.this, getString(R.string.delete_love_song_done));
                }musicPlayerService.refreshLoveSong();
            }
        });

        btn_shuffle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                shuffleMusic();
            }
        });

    }

    public void onClickControl(View view) {
        switch (view.getId()) {


            default:
                return;
        }
    }

    public final void showListPlaying() {
        startActivity(new Intent(this, NowPlayingActivity.class));
    }

    public final void download() {
        if (!AppUtils.isOnline(this)) {
            ToastUtils.error((Context) this, getString(R.string.txt_check_connection));
        } else if (TextUtils.isEmpty(this.musicPlayerService.urlPlayer)) {
            showMessage(getString(R.string.waiting_txt));
        } else {
            AdsManager.showNext(this, new AdsManager.AdCloseListener() {
                @Override
                public void onAdClosed() {
                    PlayerActivity.this.showDialogDownload();
                    PlayerActivity playerActivity = PlayerActivity.this;
                    playerActivity.startDownload(playerActivity.musicPlayerService.urlPlayer);
                }
            });
        }
    }

    public void showDialogDownload() {
        this.dialogDownload = new Dialog(this);
        this.dialogDownload.requestWindowFeature(1);
        this.dialogDownload.setContentView(R.layout.dialog_download);
        Window window = this.dialogDownload.getWindow();
        LayoutParams attributes = window.getAttributes();
        attributes.gravity = 80;
        this.dialogDownload.setCancelable(false);
        attributes.flags &= -5;
        window.setAttributes(attributes);
        this.dialogDownload.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        this.dialogDownload.getWindow().setBackgroundDrawableResource(17170445);
        this.dialogDownload.getWindow().setLayout(-1, -2);
        this.tvProgressDownload = (TextView) this.dialogDownload.findViewById(R.id.tvProgressDownload);
        this.btnCancel = this.dialogDownload.findViewById(R.id.btnCancelDownload);
        this.btnCancel.setOnClickListener(new OnClickListener() {
            public final void onClick(View view) {
                PlayerActivity.this.lambda$showDialogDownload$1$PlayerActivity(view);
            }
        });
        this.btnCancel.setVisibility(VISIBLE);
        this.dialogDownload.show();
    }

    public  void lambda$showDialogDownload$1$PlayerActivity(View view) {
        new File(this.mFileOutput.getPath()).delete();
        new File(FileDownloadUtils.getTempPath(this.mFileOutput.getPath())).delete();
        FileDownloader.getImpl().clearAllTaskData();
        this.dialogDownload.dismiss();
    }

    public void startDownload(String str) {
        this.dirpath = AppUtils.outputPath(getApplicationContext());
        String str2 = "";
        String str3 = "\\\\";
        this.fileNameSave = this.musicPlayerService.getCurrentOnline().title.replaceAll("\\/", str2).replaceAll(str3, str2).replaceAll("\\*", str2).replaceAll("\\?", str2).replaceAll("\\\"", str2).replaceAll("<", str2).replaceAll("\\(", str2).replaceAll("\\)", str2).replaceAll(">", str2).replaceAll("\\|", str2).replaceAll("|", str2).replaceAll("#", str2).replaceAll("\\#", str2).replaceAll("\\.", str2).replaceAll("\\:", str2);
        StringBuilder sb = new StringBuilder();
        sb.append(this.fileNameSave.replaceAll("[\\\\><\"|*?%:#/]", str2));
        sb.append(".mp3");
        this.fileNameSave = sb.toString();
        StringBuilder sb2 = new StringBuilder();
        sb2.append(this.dirpath);
        sb2.append("/");
        sb2.append(this.fileNameSave);
        this.mFileOutput = new File(sb2.toString());
        String replaceAll = str.replaceAll(str3, str2);
        BaseDownloadTask create = FileDownloader.getImpl().create(replaceAll);
        create.setPath(this.mFileOutput.getPath(), false);
        create.setCallbackProgressTimes(1000);
        create.setMinIntervalUpdateSpeed(1000);
        create.setCallbackProgressMinInterval(1500);
        create.setTag(replaceAll);
        create.setListener(new FileDownloadSampleListener() {
            public void pending(BaseDownloadTask baseDownloadTask, int i, int i2) {
                super.pending(baseDownloadTask, i, i2);
            }

            public void progress(BaseDownloadTask baseDownloadTask, int i, int i2) {
                super.progress(baseDownloadTask, i, i2);
                double round = AppUtils.round((((double) i) / 1024.0d) / 1024.0d, 2);
                double round2 = AppUtils.round((((double) i2) / 1024.0d) / 1024.0d, 2);
                StringBuilder sb = new StringBuilder();
                sb.append(PlayerActivity.this.getString(R.string.download));
                sb.append(": ");
                sb.append(round);
                sb.append("/");
                sb.append(round2);
                sb.append(" MB");
                PlayerActivity.this.tvProgressDownload.setText(sb.toString());
            }

            public void error(BaseDownloadTask baseDownloadTask, Throwable th) {
                super.error(baseDownloadTask, th);
                th.printStackTrace();
            }

            public void connected(BaseDownloadTask baseDownloadTask, String str, boolean z, int i, int i2) {
                super.connected(baseDownloadTask, str, z, i, i2);
            }

            public void paused(BaseDownloadTask baseDownloadTask, int i, int i2) {
                super.paused(baseDownloadTask, i, i2);
            }

            public void completed(BaseDownloadTask baseDownloadTask) {
                super.completed(baseDownloadTask);
                CountDownTimer r0 = new CountDownTimer(DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS, 1000) {
                    @SuppressLint({"SetTextI18n"})
                    public void onTick(long j) {
                        PlayerActivity.this.btnCancel.setVisibility(GONE);
                        TextView textView = PlayerActivity.this.tvProgressDownload;
                        StringBuilder sb = new StringBuilder();
                        sb.append(PlayerActivity.this.getString(R.string.txt_download_done));
                        sb.append(": ");
                        sb.append(j / 1000);
                        sb.append("s");
                        textView.setText(sb.toString());
                    }

                    public void onFinish() {
                        PlayerActivity.this.addSongToMediaStore();
                        PlayerActivity.this.dialogDownload.dismiss();
                        PlayerActivity playerActivity = PlayerActivity.this;
                        playerActivity.showMessage(playerActivity.getString(R.string.txt_download_done));
                    }
                };
                r0.start();
            }

            public void warn(BaseDownloadTask baseDownloadTask) {
                super.warn(baseDownloadTask);
            }
        });
        this.baseDownloadTask = create;
        this.baseDownloadTask.start();
    }

    public void addSongToMediaStore() {
        if (this.mFileOutput.exists()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("title", this.fileNameSave);
            contentValues.put("is_music", Boolean.valueOf(true));
            contentValues.put("date_added", AppUtils.getCurrentMillisecond());
            contentValues.put("_data", this.mFileOutput.getPath());
            contentValues.put("duration", getDuration(this.mFileOutput.getPath()));
            try {
                getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, contentValues);
            } catch (Exception unused) {
            }
        }
    }

    public Long getDuration(String str) {
        File file = new File(str);
        String str2 = "600000";
        if (file.exists()) {
            String path = Uri.fromFile(file).getPath();
            if (!TextUtils.isEmpty(path)) {
                MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                try {
                    mediaMetadataRetriever.setDataSource(path);
                    str2 = mediaMetadataRetriever.extractMetadata(9);
                    mediaMetadataRetriever.release();
                } catch (Exception unused) {
                }
            }
        }
        return Long.valueOf(str2 != null ? Long.valueOf(str2).longValue() : convertDuration());
    }

    public long convertDuration() {
        long j = this.musicPlayerService.getCurrentOnline().duration;
        if (!TextUtils.isEmpty(String.valueOf(j))) {
            return j;
        }
        return 600000;
    }

    public final void shuffleMusic() {
        String str = "com.eskaylation.downloadmusic.SHUFFLE_MUSIC";
        if (this.preferenceUtils.getBoolean(str)) {
            this.preferenceUtils.putBoolean(str, false);
            this.btn_shuffle.setImageResource(R.drawable.ic_shuffle);
            return;
        }
        this.preferenceUtils.putBoolean(str, true);
        this.btn_shuffle.setImageResource(R.drawable.ic_shuffle_on);
    }

    public final void loopMusic() {
        String str = "com.eskaylation.downloadmusic.action.loop_music";
        if (this.preferenceUtils.getInt(str) == 0) {
            this.preferenceUtils.putInt(str, 999);
            this.btn_loop.setImageResource(R.drawable.ic_loop_all);
        } else if (this.preferenceUtils.getInt(str) == 999) {
            this.preferenceUtils.putInt(str, 1);
            this.btn_loop.setImageResource(R.drawable.ic_loop_one);
        } else {
            this.preferenceUtils.putInt(str, 0);
            this.btn_loop.setImageResource(R.drawable.ic_loop);
        }
    }

    public final void bindService() {
        bindService(new Intent(this, MusicPlayerService.class), this.connection, 1);
    }

    public final void unbindServicePlayMusic() {
        if (this.mBound) {
            try {
                unbindService(this.connection);
            } catch (Exception unused) {
            }
        }
    }

    public final void next() {
        Intent intent = new Intent(this, MusicPlayerService.class);
        intent.setAction("com.eskaylation.downloadmusic.action.next");
        startService(intent);
    }

    public final void prive() {
        this.bufferView.setVisibility(VISIBLE);
        this.btn_playPause.setVisibility(INVISIBLE);
        Intent intent = new Intent(this, MusicPlayerService.class);
        intent.setAction("com.eskaylation.downloadmusic.action.prive");
        startService(intent);
    }

    public void playAudio() {
        Intent intent = new Intent(this, MusicPlayerService.class);
        intent.setAction("com.eskaylation.downloadmusic.action.playpause");
        startService(intent);
    }

    public void onNeedPermisionWriteSetting(Song song) {
        Intent intent = new Intent("android.settings.action.MANAGE_WRITE_SETTINGS");
        StringBuilder sb = new StringBuilder();
        sb.append("package:");
        sb.append(getPackageName());
        intent.setData(Uri.parse(sb.toString()));
        intent.addFlags( 0x10000000);
        startActivity(intent);
        this.ringtone = song;
    }

    public void onAddToPlayNext(Song song) {
        this.musicPlayerService.addSongToNext(song);
    }

    public void showRateDialog() {
        if (!RatePreferenceUtils.getInstance(this).getBoolean(RatePreferenceUtils.PREF_DONT_SHOW_RATE)) {
            RatePreferenceUtils.getInstance(this).setCount();
            if (RatePreferenceUtils.getInstance(this).getCount() % 4 == 0) {
                this.dialogRating = new Dialog(this);
                this.dialogRating.requestWindowFeature(1);
                this.dialogRating.setContentView(R.layout.rating_dialog);
                Window window = this.dialogRating.getWindow();
                LayoutParams attributes = window.getAttributes();
                attributes.gravity = 80;
                this.dialogRating.setCancelable(false);
                attributes.flags &= -5;
                window.setAttributes(attributes);
                this.dialogRating.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                this.dialogRating.getWindow().setBackgroundDrawableResource(17170445);
                this.dialogRating.getWindow().setLayout(-1, -2);
                TextView textView = (TextView) this.dialogRating.findViewById(R.id.btn_cancel);
                ((TextView) this.dialogRating.findViewById(R.id.btn_rating)).setOnClickListener(new OnClickListener() {
                    public final void onClick(View view) {
                        PlayerActivity.this.lambda$showRateDialog$2$PlayerActivity(view);
                    }
                });
                textView.setOnClickListener(new OnClickListener() {
                    public final void onClick(View view) {
                        PlayerActivity.this.lambda$showRateDialog$3$PlayerActivity(view);
                    }
                });
                this.dialogRating.show();
            }
        }
    }

    public  void lambda$showRateDialog$2$PlayerActivity(View view) {
        RatePreferenceUtils.getInstance(this).putBoolean(RatePreferenceUtils.PREF_DONT_SHOW_RATE, true);
        rateInStore();
        this.dialogRating.dismiss();
    }

    public  void lambda$showRateDialog$3$PlayerActivity(View view) {
        RatePreferenceUtils.getInstance(this).putTime(RatePreferenceUtils.PREF_TIME_LATTER_4_HOUR, System.currentTimeMillis());
        this.dialogRating.dismiss();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onStopService(StopService stopService) {
        if (stopService.stop) {
            if (!isFinishing()) {
                finish();
            }
            EventBus.getDefault().postSticky(new StopService(false));
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onAddToLoveSong(AddToLoveSong addToLoveSong) {
        MusicPlayerService musicPlayerService2 = this.musicPlayerService;
        if (musicPlayerService2 != null) {
            musicPlayerService2.refreshLoveSong();
            this.musicPlayerService.initDataPlayerActivity();
        }
        if (addToLoveSong.isPlayingAct) {
            ImageButton imageButton = this.btnFavorite;
            if (imageButton != null) {
                imageButton.setImageResource(R.drawable.favorite_on);
                return;
            }
            return;
        }
        ImageButton imageButton2 = this.btnFavorite;
        if (imageButton2 != null) {
            imageButton2.setImageResource(R.drawable.favorite_off);
        }
    }
}
