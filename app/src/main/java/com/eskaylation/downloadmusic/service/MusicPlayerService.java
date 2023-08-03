package com.eskaylation.downloadmusic.service;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.RemoteControlClient;
import android.media.RemoteControlClient.MetadataEditor;
import android.media.audiofx.BassBoost;
import android.media.audiofx.Equalizer;
import android.media.audiofx.Equalizer.Settings;
import android.media.audiofx.Virtualizer;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.v4.media.MediaBrowserCompat.MediaItem;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.core.app.NotificationCompat.Action;
import androidx.core.app.NotificationCompat.Builder;
import androidx.core.graphics.drawable.IconCompat;
import androidx.media.MediaBrowserServiceCompat;
import androidx.media.app.NotificationCompat;

import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;
import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.eskaylation.downloadmusic.R;
import com.eskaylation.downloadmusic.adapter.NowPlayingAdapter;
import com.eskaylation.downloadmusic.adapter.SongAdapter;
import com.eskaylation.downloadmusic.base.BaseApplication;
import com.eskaylation.downloadmusic.base.GlideApp;
import com.eskaylation.downloadmusic.bus.CloseDialog;
import com.eskaylation.downloadmusic.bus.Control;
import com.eskaylation.downloadmusic.bus.EndSong;
import com.eskaylation.downloadmusic.bus.MusicPlayServiceUrl;
import com.eskaylation.downloadmusic.bus.MusicPlayServiceUrlFailed;
import com.eskaylation.downloadmusic.bus.NotifyDeleteMusic;
import com.eskaylation.downloadmusic.bus.PaletteColor;
import com.eskaylation.downloadmusic.bus.StopService;
import com.eskaylation.downloadmusic.database.EqualizerDao;
import com.eskaylation.downloadmusic.database.OfflineSqliteHelper;
import com.eskaylation.downloadmusic.database.SongListDao;
import com.eskaylation.downloadmusic.database.SongListSqliteHelper;
import com.eskaylation.downloadmusic.listener.GenerateUrlListener;
import com.eskaylation.downloadmusic.listener.RecommendListener;
import com.eskaylation.downloadmusic.model.AudioExtract;
import com.eskaylation.downloadmusic.model.CustomPreset;
import com.eskaylation.downloadmusic.model.Song;
import com.eskaylation.downloadmusic.net.GenerateUrlMusicUtils;
import com.eskaylation.downloadmusic.receiver.BroadcastControl;
import com.eskaylation.downloadmusic.ui.activity.PlayerActivity;
import com.eskaylation.downloadmusic.utils.AppUtils;
import com.eskaylation.downloadmusic.utils.ArtworkUtils;
import com.eskaylation.downloadmusic.utils.BitmapUtils;
import com.eskaylation.downloadmusic.utils.PreferenceUtils;
import com.eskaylation.downloadmusic.utils.ToastUtils;
import com.eskaylation.downloadmusic.widget.PlayerView;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player.EventListener;

import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector.Parameters;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector.ParametersBuilder;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DataSource.Factory;
import com.google.android.exoplayer2.util.EventLogger;
import com.google.android.exoplayer2.util.Util;
import com.orhanobut.logger.Logger;
import com.wang.avi.AVLoadingIndicatorView;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class MusicPlayerService extends MediaBrowserServiceCompat implements OnAudioFocusChangeListener, GenerateUrlListener, RecommendListener {
    public static boolean isServiceRunning = false;
    public static final CharSequence name = "com.eskaylation.downloadmusic";
    public boolean IS_PLAYING_ONLINE = false;
    public NowPlayingAdapter adapterNowPlaying;
    public SongAdapter adapterSong;
    public AudioManager audioManager;
    public BassBoost bassBooster;
    public Bitmap bitmapDefault;
    public BroadcastControl broadcastControl;
    public ImageView btn_play_pause_PlayerAct;
    public ImageView btnfavorite;
    public AVLoadingIndicatorView bufferView;
    public PlayerView categoryPlayerView;
    public CountDownTimer countDownTimer;
    public Factory dataSourceFactory;
    public boolean durationSet = false;
    public Equalizer equalizer;
    public EqualizerDao equalizerDao;
    public ImageView frgBigThumb;
    public GenerateUrlMusicUtils generateUrlMusicUtils;
    public boolean headsetConnected = false;
    public BroadcastReceiver headsetListener = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String str = "state";
            if (!intent.hasExtra(str)) {
                return;
            }
            if (MusicPlayerService.this.headsetConnected && intent.getIntExtra(str, 0) == 0) {
                MusicPlayerService.this.headsetConnected = false;
                if (MusicPlayerService.isServiceRunning && MusicPlayerService.this.mExoPlayer != null) {
                    try {
                        if (MusicPlayerService.this.mExoPlayer.getPlayWhenReady()) {
                            MusicPlayerService.this.playSong();
                        }
                    } catch (Exception unused) {
                    }
                }
            } else if (!MusicPlayerService.this.headsetConnected && intent.getIntExtra(str, 0) == 1) {
                MusicPlayerService.this.headsetConnected = true;
            }
        }
    };
    public ArrayList<AudioExtract> listOnLine = new ArrayList<>();
    public ArrayList<AudioExtract> listPreviousOnline = new ArrayList<>();
    public ArrayList<Song> lstLoveSong;
    public ArrayList<Song> lstOffline = new ArrayList<>();
    public ArrayList<CustomPreset> lstPreset;
    public final IBinder mBinder = new MusicServiceBinder();
    public Builder mBuilder;
    public AudioExtract mCurrentOnline;
    public SimpleExoPlayer mExoPlayer;
    public long mInterval = 1000;
    public MediaSessionCompat mMediaSessionCompat;
    public OnAudioFocusChangeListener mOnAudioFocusChangeListener_ = new OnAudioFocusChangeListener() {
        public void onAudioFocusChange(int i) {
            if (i != -3 && i != -2) {
                if (i == -1) {
                    if (MusicPlayerService.isServiceRunning && MusicPlayerService.this.mExoPlayer != null) {
                        try {
                            if (MusicPlayerService.this.mExoPlayer.getPlayWhenReady()) {
                                MusicPlayerService.this.playSong();
                            }
                        } catch (Exception unused) {
                        }
                    }
                }
            }
        }
    };
    public Runnable mProgressRunnerMain = new Runnable() {
        public void run() {
            if (MusicPlayerService.this.mExoPlayer != null && MusicPlayerService.this.mExoPlayer.getPlayWhenReady() && MusicPlayerService.this.progress_main != null) {
                MusicPlayerService.this.progress_main.setVisibility(VISIBLE);
                MusicPlayerService.this.progress_main.postDelayed(MusicPlayerService.this.mProgressRunnerMain, MusicPlayerService.this.mInterval);
                MusicPlayerService.this.progress_main.setProgress((float) MusicPlayerService.this.mExoPlayer.getCurrentPosition());
            }
        }
    };
    public Runnable mProgressSmall = new Runnable() {
        public void run() {
            if (MusicPlayerService.this.mExoPlayer != null) {
                try {
                    if (MusicPlayerService.this.mExoPlayer.getPlayWhenReady() && MusicPlayerService.this.seekBarSmall != null) {
                        if (MusicPlayerService.this.IS_PLAYING_ONLINE) {
                            MusicPlayerService.this.seekBarSmall.setMax((int) MusicPlayerService.this.mExoPlayer.getDuration());
                        } else if (MusicPlayerService.this.mExoPlayer.getDuration() >= 0) {
                            MusicPlayerService.this.seekBarSmall.setMax((int) MusicPlayerService.this.mExoPlayer.getDuration());
                        } else {
                            MusicPlayerService.this.seekBarSmall.setMax(Integer.parseInt(MusicPlayerService.this.getCurentSong().duration));
                        }
                        MusicPlayerService.this.seekBarSmall.setSecondaryProgress((int) MusicPlayerService.this.mExoPlayer.getBufferedPosition());
                        MusicPlayerService.this.seekBarSmall.postDelayed(MusicPlayerService.this.mProgressSmall, MusicPlayerService.this.mInterval);
                        MusicPlayerService.this.seekBarSmall.setProgress((int) MusicPlayerService.this.mExoPlayer.getCurrentPosition());
                        MusicPlayerService.this.tv_time_playing.setText(AppUtils.convertDuration(MusicPlayerService.this.mExoPlayer.getCurrentPosition()));
                    }
                } catch (Exception unused) {
                }
            }
        }
    };
    public PlayerView mainPlayerview;
    public ComponentName mediaControlReceiver;
    public MediaSource mediaSource;
    public NotificationManager notificationManager;
    public Palette palette;
    public boolean parseRunning;
    public PlayerView persionalPlayerview;
    public PreferenceUtils preferenceUtils;
    public RoundCornerProgressBar progress_main;
    public RemoteControlClient remoteControlClient = null;
    public RecyclerView rv_nowPlaying;
    public RecyclerView rv_song;
    public PlayerView searchPlayerView;
    public SeekBar seekBarSmall;
    public SongListDao songListDao;
    public SongListSqliteHelper songListSqliteHelper;
    public int songPos = 0;
    public OfflineSqliteHelper sqliteHelper;
    public int thumb;
    public DefaultTrackSelector trackSelector;
    public Parameters trackSelectorParameters;
    public TextView tv_artist;
    public TextView tv_duration;
    public TextView tv_name;
    public TextView tv_time_playing;
    public TextView tv_timmer;
    public String urlPlayer = "";
    public LinearLayout viewLoadingNowPlaying;
    public Virtualizer virtualizer;

    public class MusicServiceBinder extends Binder {
        public MusicServiceBinder() {
        }

        public MusicPlayerService getService() {
            return MusicPlayerService.this;
        }
    }

    public class PlayerEventListener implements EventListener {
        public  void onLoadingChanged(boolean z) {
           // CC.$default$onLoadingChanged(this, z);
        }

        public  void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
            //CC.$default$onPlaybackParametersChanged(this, playbackParameters);
        }

        public  void onPositionDiscontinuity(int i) {
        //    CC.$default$onPositionDiscontinuity(this, i);
        }

        public  void onRepeatModeChanged(int i) {
          //  CC.$default$onRepeatModeChanged(this, i);
        }

        public  void onSeekProcessed() {
         //   defaultonSeekProcessed(this);
        }

        public  void onShuffleModeEnabledChanged(boolean z) {
           // CC.$default$onShuffleModeEnabledChanged(this, z);
        }

        public  void onTimelineChanged(Timeline timeline, Object obj, int i) {
          //  CC.$default$onTimelineChanged(this, timeline, obj, i);
        }

        public  void onTracksChanged(TrackGroupArray trackGroupArray, TrackSelectionArray trackSelectionArray) {
            //CC.$default$onTracksChanged(this, trackGroupArray, trackSelectionArray);
        }

        public PlayerEventListener() {
        }

        public void onPlayerStateChanged(boolean z, int i) {
            if (i == 1) {
                return;
            }
            if (i != 2) {
                if (i == 3) {
                    if (!MusicPlayerService.this.durationSet) {
                        MusicPlayerService.this.initPrepare();
                        MusicPlayerService.this.durationSet = true;
                    }
                    if (MusicPlayerService.this.bufferView != null) {
                        MusicPlayerService.this.bufferView.setVisibility(INVISIBLE);
                        MusicPlayerService.this.btn_play_pause_PlayerAct.setVisibility(VISIBLE);
                    }
                } else if (i == 4) {
                    MusicPlayerService.this.durationSet = false;
                    EventBus.getDefault().postSticky(new CloseDialog(true));
                    MusicPlayerService.this.updateRemoteControl(2);
                    if (MusicPlayerService.this.persionalPlayerview != null) {
                        MusicPlayerService.this.persionalPlayerview.setStatePlayer(false);
                    }
                    if (MusicPlayerService.this.btn_play_pause_PlayerAct != null) {
                        MusicPlayerService.this.btn_play_pause_PlayerAct.setImageResource(R.drawable.ic_play);
                    }
                    String str = "com.eskaylation.downloadmusic.action.loop_music";
                    if (MusicPlayerService.this.preferenceUtils.getInt(str) == 0) {
                        if (MusicPlayerService.this.preferenceUtils.getBoolean("com.eskaylation.downloadmusic.SHUFFLE_MUSIC")) {
                            if (MusicPlayerService.this.lstOffline.size() > 0) {
                                MusicPlayerService musicPlayerService = MusicPlayerService.this;
                                musicPlayerService.songPos = AppUtils.getRandomNumber(musicPlayerService.lstOffline.size() - 1);
                            }
                            MusicPlayerService.this.next();
                            return;
                        }
                        MusicPlayerService.this.next();
                    } else if (MusicPlayerService.this.preferenceUtils.getInt(str) == 1) {
                        MusicPlayerService.this.mExoPlayer.seekTo(0);
                        MusicPlayerService.this.mExoPlayer.setPlayWhenReady(true);
                        if (MusicPlayerService.this.btn_play_pause_PlayerAct != null) {
                            MusicPlayerService.this.btn_play_pause_PlayerAct.setImageResource(R.drawable.ic_pause);
                        }
                        if (MusicPlayerService.this.persionalPlayerview != null) {
                            MusicPlayerService.this.persionalPlayerview.setStatePlayer(false);
                        }
                    } else if (MusicPlayerService.this.preferenceUtils.getInt(str) != 999) {
                    } else {
                        if (MusicPlayerService.this.songPos == MusicPlayerService.this.lstOffline.size() - 1) {
                            MusicPlayerService.this.restartPlayer();
                        } else {
                            MusicPlayerService.this.next();
                        }
                    }
                }
            } else if (MusicPlayerService.this.IS_PLAYING_ONLINE && MusicPlayerService.this.btn_play_pause_PlayerAct != null) {
                MusicPlayerService.this.bufferView.setVisibility(VISIBLE);
                MusicPlayerService.this.btn_play_pause_PlayerAct.setVisibility(INVISIBLE);
            }
        }

        public void onPlayerError(ExoPlaybackException exoPlaybackException) {
            Log.e("Error", exoPlaybackException.toString());
            if (MusicPlayerService.this.IS_PLAYING_ONLINE) {
                MusicPlayerService.this.stopServiceAndCloseNotification();
                ToastUtils.warning(MusicPlayerService.this.getApplicationContext(), MusicPlayerService.this.getString(R.string.txt_error_again));
            } else if (!new File(MusicPlayerService.this.getCurentSong().mSongPath).exists()) {
                MusicPlayerService.this.next();
            }
        }
    }

    public void onAudioFocusChange(int i) {
    }

    public BrowserRoot onGetRoot(String str, int i, Bundle bundle) {
        return null;
    }

    public void onLoadChildren(String str, Result<List<MediaItem>> result) {
    }

    public boolean isPlayOnline() {
        return this.IS_PLAYING_ONLINE;
    }

    public AudioExtract getCurrentOnline() {
        return this.mCurrentOnline;
    }

    public void onGenerateUrlSongDone(String str, ArrayList<AudioExtract> arrayList) {
        EventBus.getDefault().postSticky(new MusicPlayServiceUrl(this.mCurrentOnline.urlVideo, str, arrayList));
        this.parseRunning = false;
    }

    public void onGenerateUrlSongFailed() {
        EventBus.getDefault().postSticky(new MusicPlayServiceUrlFailed(true));
        this.parseRunning = false;
    }

    public void onCreate() {
        super.onCreate();
        this.sqliteHelper = new OfflineSqliteHelper(this);
        this.equalizerDao = new EqualizerDao(this.sqliteHelper);
        this.preferenceUtils = PreferenceUtils.getInstance(this);
        EventBus.getDefault().register(this);
        this.generateUrlMusicUtils = new GenerateUrlMusicUtils(getApplicationContext(), this);
        this.audioManager = (AudioManager) getApplicationContext().getSystemService("audio");
        this.mediaControlReceiver = new ComponentName(getPackageName(), MediaControlButtonReceiver.class.getName());
        this.audioManager.registerMediaButtonEventReceiver(this.mediaControlReceiver);
        initMediaSession();
        setBroadcast();
        this.lstPreset = this.equalizerDao.getAllPreset();
        if (this.headsetListener != null) {
            registerReceiver(this.headsetListener, new IntentFilter("android.intent.action.HEADSET_PLUG"));
        }
        initDBLoveSong();
    }

    public final void initMediaSession() {
        PendingIntent pendingIntent;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(Intent.ACTION_MEDIA_BUTTON), PendingIntent.FLAG_IMMUTABLE);
        } else {
            pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(Intent.ACTION_MEDIA_BUTTON), PendingIntent.FLAG_UPDATE_CURRENT);
        }
        this.mMediaSessionCompat = new MediaSessionCompat(getApplicationContext(), "Tag", null, pendingIntent);
        this.mMediaSessionCompat.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        this.mMediaSessionCompat.setMetadata(new MediaMetadataCompat.Builder().putLong(MediaMetadataCompat.METADATA_KEY_DURATION, -1).build());
        setSessionToken(this.mMediaSessionCompat.getSessionToken());
    }

    public void initDBLoveSong() {
        this.songListSqliteHelper = new SongListSqliteHelper(getApplicationContext(), "DEFAULT_FAVORITEDOWNLOAD");
        this.songListDao = new SongListDao(this.songListSqliteHelper);
        this.lstLoveSong = this.songListDao.getAllFavoriteSong();
    }

    public void addSongToNext(Song song) {
        if (isServiceRunning && !this.lstOffline.isEmpty()) {
            this.lstOffline.add(getSongPos() + 1, song);
        }
    }

    public void registerRemoteControl() {
        if (this.remoteControlClient == null) {
            try {
                Intent intent = new Intent("android.intent.action.MEDIA_BUTTON");
                intent.setComponent(this.mediaControlReceiver);
                if (VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    this.remoteControlClient = new RemoteControlClient(PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE));
                } else {
                    this.remoteControlClient = new RemoteControlClient(PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));
                }
                this.remoteControlClient.setTransportControlFlags(137);
                this.audioManager.registerRemoteControlClient(this.remoteControlClient);
            } catch (Exception unused) {
            }
        }
    }

    public void unregisterRemoteControl() {
        RemoteControlClient remoteControlClient2 = this.remoteControlClient;
        if (remoteControlClient2 != null) {
            remoteControlClient2.setPlaybackState(1);
            MetadataEditor editMetadata = this.remoteControlClient.editMetadata(true);
            editMetadata.clear();
            editMetadata.apply();
            this.audioManager.unregisterRemoteControlClient(this.remoteControlClient);
            this.remoteControlClient = null;
        }
    }

    @TargetApi(14)
    public void updateRemoteControl(int i) {
        RemoteControlClient remoteControlClient2 = this.remoteControlClient;
        if (remoteControlClient2 == null) {
            return;
        }
        if (!this.IS_PLAYING_ONLINE) {
            remoteControlClient2.setPlaybackState(i);
            this.remoteControlClient.setTransportControlFlags(137);
            this.remoteControlClient.editMetadata(true).putString(2, getCurentSong().artist).putString(1, getCurentSong().album).putString(7, getCurentSong().title).apply();
            return;
        }
        remoteControlClient2.setPlaybackState(i);
        this.remoteControlClient.setTransportControlFlags(137);
        this.remoteControlClient.editMetadata(true).putString(2, "").putString(7, this.mCurrentOnline.title).apply();
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(4:116|117|118|119) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:118:0x0247 */
    public int onStartCommand(Intent intent, int i, int i2) {
        char c;
        Intent intent2 = intent;
        if (!(intent2 == null || intent.getAction() == null)) {
            int intExtra = intent2.getIntExtra("com.eskaylation.downloadmusic.STRENGTH_BASSBOOSTER", 0);
            int intExtra2 = intent2.getIntExtra("com.eskaylation.downloadmusic.STRENGTH_VTUARLIZER", 0);
            int intExtra3 = intent2.getIntExtra("com.eskaylation.downloadmusic.STRENGTH_EQUALIZER", 0);
            String str = "com.eskaylation.downloadmusic.PREF_ENABLE_EQUALIZER";
            boolean booleanExtra = intent2.getBooleanExtra(str, false);
            String str2 = "com.eskaylation.downloadmusic.PREF_ENABLE_BASSBOSSTER";
            boolean booleanExtra2 = intent2.getBooleanExtra(str2, false);
            String str3 = "com.eskaylation.downloadmusic.PREF_ENABLE_VITUARLIZER";
            boolean booleanExtra3 = intent2.getBooleanExtra(str3, false);
            String action = intent.getAction();
            int hashCode = action.hashCode();
            String str4 = "com.eskaylation.downloadmusic.reverb_profile";
            String str5 = "set_timmer";
            String str6 = "com.eskaylation.downloadmusic.ACTION.SETDATAONLINEPLAYER";
            boolean z = booleanExtra;
            switch (hashCode) {
                case -1781377049:
                    if (action.equals(str2)) {
                        c = 8;
                        break;
                    }
                case -1633022058:
                    if (action.equals(str6)) {
                        c = 1;
                        break;
                    }
                case -1183531785:
                    if (action.equals("com.eskaylation.downloadmusic.action.next_nolimited")) {
                        c = 19;
                        break;
                    }
                case -1151144769:
                    if (action.equals(str5)) {
                        c = 18;
                        break;
                    }
                case -610370046:
                    if (action.equals("com.eskaylation.downloadmusic.ACTION.SETDATAOFFLINEPLAYER")) {
                        c = 0;
                        break;
                    }
                case -399999368:
                    if (action.equals("com.eskaylation.downloadmusic.action.playpause")) {
                        c = 4;
                        break;
                    }
                case -376054275:
                    if (action.equals("com.eskaylation.downloadmusic.action.next")) {
                        c = 2;
                        break;
                    }
                case 87061860:
                    if (action.equals(str)) {
                        c = 7;
                        break;
                    }
                case 231986583:
                    if (action.equals(str3)) {
                        c = 9;
                        break;
                    }
                case 299798425:
                    if (action.equals("com.eskaylation.downloadmusic.ACTION.CHANGEPRESET")) {
                        c = 6;
                        break;
                    }
                case 1229439436:
                    if (action.equals("com.eskaylation.downloadmusic.action.prive")) {
                        c = 3;
                        break;
                    }
                case 1371943730:
                    if (action.equals("com.eskaylation.downloadmusic.action.stop_music")) {
                        c = 5;
                        break;
                    }
                case 1554179155:
                    if (action.equals("com.eskaylation.downloadmusic.ACTION.STRENGTH_BASS")) {
                        c = 10;
                        break;
                    }
                case 1778017137:
                    if (action.equals("com.eskaylation.downloadmusic.ACTION.STRENGTH_VITUARLIZER")) {
                        c = 11;
                        break;
                    }
                case 2063435946:
                    if (action.equals(str4)) {
                        c = 17;
                        break;
                    }
                default:
                    switch (hashCode) {
                        case 620133414:
                            if (action.equals("com.eskaylation.downloadmusic.ACTION.SLIDER1")) {
                                c = 12;
                                break;
                            }
                        case 620133415:
                            if (action.equals("com.eskaylation.downloadmusic.ACTION.SLIDER2")) {
                                c = 13;
                                break;
                            }
                        case 620133416:
                            if (action.equals("com.eskaylation.downloadmusic.ACTION.SLIDER3")) {
                                c = 14;
                                break;
                            }
                        case 620133417:
                            if (action.equals("com.eskaylation.downloadmusic.ACTION.SLIDER4")) {
                                c = 15;
                                break;
                            }
                        case 620133418:
                            if (action.equals("com.eskaylation.downloadmusic.ACTION.SLIDER5")) {
                                c = 16;
                                break;
                            }
                    }
                    c = 65535;
                    break;
            }
            switch (c) {
                case 0:
                    this.listOnLine.clear();
                    this.durationSet = false;
                    this.IS_PLAYING_ONLINE = false;
                    setDatasource();
                    showNotification();
                    registerRemoteControl();
                    break;
                case 1:
                    if (this.parseRunning) {
                        ToastUtils.warning((Context) this, getString(R.string.waiting_txt));
                        break;
                    } else {
                        stopPlayer();
                        this.durationSet = false;
                        this.mCurrentOnline = (AudioExtract) intent2.getParcelableExtra(str6);
                        this.listPreviousOnline.add(this.mCurrentOnline);
                        this.IS_PLAYING_ONLINE = true;
                        this.lstOffline.clear();
                        LinearLayout linearLayout = this.viewLoadingNowPlaying;
                        if (linearLayout != null) {
                            linearLayout.setVisibility(VISIBLE);
                            this.rv_nowPlaying.setVisibility(GONE);
                        }
                        this.generateUrlMusicUtils.getUrlAudio(this.mCurrentOnline);
                        this.parseRunning = true;
                        initDataPlayerActivity();
                        showNotification();
                        registerRemoteControl();
                        break;
                    }
                case 2:
                    if (isServiceRunning) {
                        next();
                        break;
                    }
                    break;
                case 3:
                    if (isServiceRunning) {
                        priveSong();
                        break;
                    }
                    break;
                case 4:
                    if (isServiceRunning) {
                        playSong();
                        break;
                    }
                    break;
                case 5:
                    stopServiceAndCloseNotification();
                    break;
                case 6:
                    this.lstPreset = this.equalizerDao.getAllPreset();
                    if (this.preferenceUtils.getBoolean(str)) {
                        if (this.equalizer == null) {
                            enableEqualizer(getPosEqualizer());
                            break;
                        } else {
                            setPresetEqualizer(getPosEqualizer());
                            break;
                        }
                    }
                    break;
                case 7:
                    if (!z) {
                        Equalizer equalizer2 = this.equalizer;
                        if (equalizer2 != null) {
                            equalizer2.release();
                            break;
                        }
                    } else {
                        enableBassBooster();
                        enableVituarLizer();
                        this.equalizer = new Equalizer(1, this.mExoPlayer.getAudioSessionId());
                        this.equalizer.setProperties(new Settings("Equalizer;curPreset=-1;numBands=5;band1Level=0;band2Level=0;band3Level=0;band4Level=0;band5Level=0;"));
                        this.equalizer.setEnabled(true);
                        setPresetEqualizer(this.preferenceUtils.getSpinerPosition());
                        break;
                    }
                    break;
                case 8:
                    if (!booleanExtra2) {
                        BassBoost bassBoost = this.bassBooster;
                        if (bassBoost != null) {
                            bassBoost.release();
                        }
                        Virtualizer virtualizer2 = this.virtualizer;
                        if (virtualizer2 != null) {
                            virtualizer2.release();
                            break;
                        }
                    } else {
                        this.bassBooster = new BassBoost(1, this.mExoPlayer.getAudioSessionId());
                        this.bassBooster.setEnabled(true);
                        this.bassBooster.setStrength((short) this.preferenceUtils.getBBSlider());
                        this.virtualizer = new Virtualizer(1, this.mExoPlayer.getAudioSessionId());
                        this.virtualizer.setEnabled(true);
                        this.virtualizer.setStrength((short) this.preferenceUtils.getVirSlider());
                        break;
                    }
                    break;
                case 9:
                    if (!booleanExtra3) {
                        Virtualizer virtualizer3 = this.virtualizer;
                        if (virtualizer3 != null) {
                            virtualizer3.release();
                            break;
                        }
                    } else {
                        this.virtualizer = new Virtualizer(1, this.mExoPlayer.getAudioSessionId());
                        this.virtualizer.setEnabled(true);
                        this.virtualizer.setStrength((short) this.preferenceUtils.getVirSlider());
                        break;
                    }
                    break;
                case 10:
                    setStrengthBass((short) intExtra);
                    break;
                case 11:
                    setStrengthVir((short) intExtra2);
                    break;
                case 12:
                    if (this.preferenceUtils.getBoolean(str)) {
                        Equalizer equalizer3 = this.equalizer;
                        if (equalizer3 != null) {
                            equalizer3.setBandLevel((short) 0, (short) intExtra3);
                            ((CustomPreset) this.lstPreset.get(this.lstPreset.size() - 1)).setSlider1(intExtra3);
                            break;
                        }
                    }
                    break;
                case 13:
                    if (this.preferenceUtils.getBoolean(str)) {
                        Equalizer equalizer4 = this.equalizer;
                        if (equalizer4 != null) {
                            equalizer4.setBandLevel( (short)1, (short) intExtra3);
                            ((CustomPreset) this.lstPreset.get(this.lstPreset.size() - 1))
                                    .setSlider2(intExtra3);
                            break;
                        }
                    }
                    break;
                case 14:
                    if (this.preferenceUtils.getBoolean(str)) {
                        Equalizer equalizer5 = this.equalizer;
                        if (equalizer5 != null) {
                            equalizer5.setBandLevel( (short)2, (short) intExtra3);
                            ((CustomPreset) this.lstPreset.get(this.lstPreset.size() - 1)).setSlider3(intExtra3);
                            break;
                        }
                    }
                    break;
                case 15:
                    if (this.preferenceUtils.getBoolean(str)) {
                        Equalizer equalizer6 = this.equalizer;
                        if (equalizer6 != null) {
                            equalizer6.setBandLevel( (short)3, (short) intExtra3);
                            ((CustomPreset) this.lstPreset.get(this.lstPreset.size() - 1)).setSlider4(intExtra3);
                            break;
                        }
                    }
                    break;
                case 16:
                    if (this.preferenceUtils.getBoolean(str)) {
                        Equalizer equalizer7 = this.equalizer;
                        if (equalizer7 != null) {
                            try {
                                equalizer7.setBandLevel( (short)4, (short) intExtra3);
                                ((CustomPreset) this.lstPreset.get(this.lstPreset.size() - 1)).setSlider5(intExtra3);
                                break;
                            } catch (Exception unused) {
                                break;
                            }
                        }
                    }
                    break;
                case 17:
                    intent2.getIntExtra(str4, 0);
                    break;
                case 18:
                    startTimer(intent2.getLongExtra(str5, 0));
                    break;
                case 19:
                    nextSongUnLimitted();
                    break;
            }
        }
        return 2;
    }

    public final void enableVituarLizer() {
        if (this.preferenceUtils.getBoolean("com.eskaylation.downloadmusic.PREF_ENABLE_BASSBOSSTER")) {
            Virtualizer virtualizer2 = this.virtualizer;
            if (virtualizer2 != null) {
                virtualizer2.release();
                this.virtualizer = null;
            }
            try {
                this.virtualizer = new Virtualizer(1, this.mExoPlayer.getAudioSessionId());
                this.virtualizer.setEnabled(true);
                this.virtualizer.setStrength((short) this.preferenceUtils.getVirSlider());
            } catch (Exception unused) {
            }
        } else {
            Virtualizer virtualizer3 = this.virtualizer;
            if (virtualizer3 != null) {
                virtualizer3.release();
                this.virtualizer = null;
            }
        }
    }

    public final void setStrengthVir(short s) {
        if (this.preferenceUtils.getBoolean("com.eskaylation.downloadmusic.PREF_ENABLE_BASSBOSSTER")) {
            SimpleExoPlayer simpleExoPlayer = this.mExoPlayer;
            if (simpleExoPlayer != null) {
                Virtualizer virtualizer2 = this.virtualizer;
                if (virtualizer2 == null) {
                    try {
                        this.virtualizer = new Virtualizer(1, simpleExoPlayer.getAudioSessionId());
                        this.virtualizer.setEnabled(true);
                        this.virtualizer.setStrength(s);
                    } catch (Exception unused) {
                    }
                } else {
                    virtualizer2.setStrength(s);
                }
            }
        }
    }

    public final void enableBassBooster() {
        if (this.preferenceUtils.getBoolean("com.eskaylation.downloadmusic.PREF_ENABLE_BASSBOSSTER")) {
            BassBoost bassBoost = this.bassBooster;
            if (bassBoost != null) {
                bassBoost.release();
                this.bassBooster = null;
            }
            try {
                this.bassBooster = new BassBoost(1, this.mExoPlayer.getAudioSessionId());
                this.bassBooster.setEnabled(true);
                this.bassBooster.setStrength((short) this.preferenceUtils.getBBSlider());
            } catch (Exception unused) {
            }
        } else {
            BassBoost bassBoost2 = this.bassBooster;
            if (bassBoost2 != null) {
                bassBoost2.release();
            }
        }
    }

    public void setStrengthBass(short s) {
        if (this.preferenceUtils.getBoolean("com.eskaylation.downloadmusic.PREF_ENABLE_BASSBOSSTER")) {
            BassBoost bassBoost = this.bassBooster;
            if (bassBoost == null) {
                try {
                    this.bassBooster = new BassBoost(1, this.mExoPlayer.getAudioSessionId());
                    this.bassBooster.setEnabled(true);
                    this.bassBooster.setStrength(s);
                } catch (Exception unused) {
                }
            } else {
                bassBoost.setStrength(s);
            }
        }
    }

    public void disableAllEffect() {
        SimpleExoPlayer simpleExoPlayer = this.mExoPlayer;
        if (simpleExoPlayer != null) {
            simpleExoPlayer.clearAuxEffectInfo();
        }
        Virtualizer virtualizer2 = this.virtualizer;
        if (virtualizer2 != null) {
            virtualizer2.release();
            this.virtualizer = null;
        }
        BassBoost bassBoost = this.bassBooster;
        if (bassBoost != null) {
            bassBoost.release();
            this.bassBooster = null;
        }
        Equalizer equalizer2 = this.equalizer;
        if (equalizer2 != null) {
            equalizer2.release();
            this.equalizer = null;
        }
    }

    public int getPosEqualizer() {
        return this.preferenceUtils.getSpinerPosition();
    }

    public final void setPresetEqualizer(int i) {
        if (i < 10) {
            try {
                this.equalizer.usePreset((short) i);
            } catch (Exception unused) {
            }
        } else {
            setCustomPreset(i);
        }
    }

    public final void setCustomPreset(int i) {
        Equalizer equalizer2 = this.equalizer;
        if (equalizer2 != null) {
            equalizer2.release();
        }
        try {
            this.equalizer = new Equalizer(1, this.mExoPlayer.getAudioSessionId());
            CustomPreset customPreset = (CustomPreset) this.lstPreset.get(i);
            this.equalizer.setBandLevel((short)0, (short) customPreset.slider1);
            this.equalizer.setBandLevel((short)1, (short) customPreset.slider2);
            this.equalizer.setBandLevel((short)2, (short) customPreset.slider3);
            this.equalizer.setBandLevel((short)3, (short) customPreset.slider4);
            this.equalizer.setBandLevel((short)4, (short) customPreset.slider5);
            this.equalizer.setEnabled(true);
        } catch (Exception unused) {
        }
    }

    public final void enableEqualizer(int i) {
        if (this.preferenceUtils.getBoolean("com.eskaylation.downloadmusic.PREF_ENABLE_EQUALIZER")) {
            Equalizer equalizer2 = this.equalizer;
            if (equalizer2 != null) {
                equalizer2.release();
                this.equalizer = null;
            }
            try {
                this.equalizer = new Equalizer(1, this.mExoPlayer.getAudioSessionId());
                this.equalizer.setProperties(new Settings("Equalizer;curPreset=-1;numBands=5;band1Level=0;band2Level=0;band3Level=0;band4Level=0;band5Level=0;"));
                setPresetEqualizer(i);
                this.equalizer.setEnabled(true);
            } catch (Exception unused) {
            }
        } else {
            Equalizer equalizer3 = this.equalizer;
            if (equalizer3 != null) {
                equalizer3.release();
            }
        }
    }

    public void enableAllEffect() {
        enableEqualizer(getPosEqualizer());
        enableBassBooster();
        enableVituarLizer();
    }

    public void startTimer(long j) {
        CountDownTimer countDownTimer2 = this.countDownTimer;
        if (countDownTimer2 != null) {
            countDownTimer2.cancel();
        }
        TextView textView = this.tv_timmer;
        if (textView != null) {
            textView.setText("");
        }
        CountDownTimer r2 = new CountDownTimer(j, 1000) {
            public void onTick(long j) {
                StringBuilder sb = new StringBuilder();
                sb.append(MusicPlayerService.this.getString(R.string.turn_offtimmer));
                sb.append(": ");
                sb.append(AppUtils.convertDuration(j));
                String sb2 = sb.toString();
                if (MusicPlayerService.this.tv_timmer != null) {
                    MusicPlayerService.this.tv_timmer.setText(sb2);
                }
            }

            public void onFinish() {
                if (MusicPlayerService.this.tv_timmer != null) {
                    MusicPlayerService.this.tv_timmer.setText("");
                }
                MusicPlayerService.this.preferenceUtils.setIsTimerOff(false);
                MusicPlayerService.this.preferenceUtils.setTimmer(0);
                if (MusicPlayerService.this.mExoPlayer != null && MusicPlayerService.this.mExoPlayer.getPlayWhenReady()) {
                    MusicPlayerService.this.playSong();
                }
            }
        };
        this.countDownTimer = r2;
        this.countDownTimer.start();
    }

    public void stopTimer() {
        CountDownTimer countDownTimer2 = this.countDownTimer;
        if (countDownTimer2 != null) {
            countDownTimer2.cancel();
        }
        TextView textView = this.tv_timmer;
        if (textView != null) {
            textView.setText("");
        }
        this.preferenceUtils.setTimmer(0);
        this.preferenceUtils.setIsTimerOff(false);
    }

    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        isServiceRunning = false;
        unregisterReceiver(this.broadcastControl);
        unregisterReceiver(this.headsetListener);
        super.onDestroy();
    }

    public IBinder onBind(Intent intent) {
        return this.mBinder;
    }

    public void initViewMainActivity(PlayerView playerView, TextView textView) {
        this.mainPlayerview = playerView;
        this.tv_timmer = textView;
    }

    public void initDataMain() {
        if (this.mExoPlayer != null) {
            PlayerView playerView = this.mainPlayerview;
            if (playerView != null) {
                String str = this.IS_PLAYING_ONLINE ? this.mCurrentOnline.title : this.lstOffline.size() > 0 ? ((Song) this.lstOffline.get(getSongPos())).getTitle() : getString(R.string.txt_song);
                playerView.setTitle(str);
                PlayerView playerView2 = this.mainPlayerview;
                String str2 = this.IS_PLAYING_ONLINE ? "Download Music" : this.lstOffline.size() > 0 ? ((Song) this.lstOffline.get(getSongPos())).getArtist() : getString(R.string.txt_artist);
                playerView2.setArtist(str2);
                this.mainPlayerview.setStatePlayer(isPlay());
                PlayerView playerView3 = this.mainPlayerview;
                Context applicationContext = getApplicationContext();
                boolean z = this.IS_PLAYING_ONLINE;
                playerView3.setThumb(applicationContext, z, z ? "x" : getAlbumID());
                if (isServiceRunning) {
                    this.mainPlayerview.setVisibility(VISIBLE);
                } else {
                    this.mainPlayerview.setVisibility(GONE);
                }
            }
        }
    }

    public void setAdapterControlFrgSong(SongAdapter songAdapter, RecyclerView recyclerView) {
        this.adapterSong = songAdapter;
        this.rv_song = recyclerView;
    }

    public void initAdapterControlFrgSong() {
        SongAdapter songAdapter = this.adapterSong;
        if (songAdapter != null && !this.IS_PLAYING_ONLINE) {
            if (isServiceRunning) {
                songAdapter.setItemIndex(getItemIndex());
                this.rv_song.scrollToPosition(getSongPos());
                return;
            }
            songAdapter.setItemIndex(new Song());
        }
    }

    public void setUIConTrolPlayerActivity(TextView textView, TextView textView2, ImageView imageView, TextView textView3, TextView textView4, AppCompatSeekBar appCompatSeekBar, ImageButton imageButton, AVLoadingIndicatorView aVLoadingIndicatorView) {
        this.bufferView = aVLoadingIndicatorView;
        this.tv_name = textView;
        this.tv_artist = textView2;
        this.btn_play_pause_PlayerAct = imageView;
        this.tv_duration = textView3;
        this.tv_time_playing = textView4;
        this.seekBarSmall = appCompatSeekBar;
        this.btnfavorite = imageButton;
    }

    public void setUiNowPlayingActivity(RecyclerView recyclerView, NowPlayingAdapter nowPlayingAdapter, LinearLayout linearLayout) {
        this.adapterNowPlaying = nowPlayingAdapter;
        this.rv_nowPlaying = recyclerView;
        this.viewLoadingNowPlaying = linearLayout;
    }

    public void initDataNowplaying() {
        NowPlayingAdapter nowPlayingAdapter = this.adapterNowPlaying;
        if (nowPlayingAdapter == null) {
            return;
        }
        if (isServiceRunning) {
            if (this.IS_PLAYING_ONLINE) {
                nowPlayingAdapter.setListOnline(this.listOnLine);
                if (this.listOnLine.isEmpty()) {
                    this.viewLoadingNowPlaying.setVisibility(GONE);
                } else {
                    this.viewLoadingNowPlaying.setVisibility(GONE);
                    this.rv_nowPlaying.setVisibility(VISIBLE);
                }
            } else {
                nowPlayingAdapter.setListOffline(this.lstOffline);
                this.adapterNowPlaying.setIndexOffline(getItemIndex());
            }
            this.rv_nowPlaying.scrollToPosition(getSongPos());
            return;
        }
        nowPlayingAdapter.setIndexOffline(new Song());
    }

    public void moveSongItem(int i, int i2) {
        if (this.IS_PLAYING_ONLINE) {
            AudioExtract audioExtract = (AudioExtract) this.listOnLine.get(i);
            this.listOnLine.remove(i);
            this.listOnLine.add(i2, audioExtract);
            this.adapterNowPlaying.setListOnline(this.listOnLine);
            return;
        }
        Song song = (Song) this.lstOffline.get(i);
        Song curentSong = getCurentSong();
        this.lstOffline.remove(i);
        this.lstOffline.add(i2, song);
        this.adapterNowPlaying.setListOffline(this.lstOffline);
        this.songPos = this.lstOffline.indexOf(curentSong);
        StringBuilder sb = new StringBuilder();
        sb.append(this.songPos);
        sb.append(" - to ");
        sb.append(getCurentSong().getTitle());
        Log.e("Move", sb.toString());
    }

    public void initDataPlayerActivity() {
        String str;
        long parseLong = 0;
        TextView textView = this.tv_name;
        if (textView != null) {
            String str2 = this.IS_PLAYING_ONLINE ? this.mCurrentOnline.title :
                    this.lstOffline.size() > 0 ? ((Song) this.lstOffline.get(getSongPos())).getTitle() :
                            getString(R.string.txt_song);
            textView.setText(str2);
            TextView textView2 = this.tv_artist;
            String str3 = this.IS_PLAYING_ONLINE ? "Download Music" : this.lstOffline.size() > 0 ? ((Song) this.lstOffline.get(getSongPos())).getArtist() : getString(R.string.txt_artist);
            textView2.setText(str3);
            this.tv_name.setSelected(true);
            SimpleExoPlayer simpleExoPlayer = this.mExoPlayer;
            if (simpleExoPlayer != null) {
                if (simpleExoPlayer.getPlaybackState() == 2) {
                    this.bufferView.setVisibility(VISIBLE);
                } else {
                    this.bufferView.setVisibility(INVISIBLE);
                }
                TextView textView3 = this.tv_duration;
                String str4 = "00:00";
                if (this.mExoPlayer.getDuration() > 0) {
                    parseLong = this.mExoPlayer.getDuration();
                } else if (!this.IS_PLAYING_ONLINE) {
                    parseLong = Long.parseLong(getCurentSong().duration);
                } else {
                    str = str4;
                    textView3.setText(str);
                    TextView textView4 = this.tv_time_playing;
                    if (this.mExoPlayer.getCurrentPosition() > 0) {
                        str4 = AppUtils.convertDuration(this.mExoPlayer.getCurrentPosition());
                    }
                    textView4.setText(str4);
                    if (!this.IS_PLAYING_ONLINE) {
                        this.seekBarSmall.setMax((int) this.mExoPlayer.getDuration());
                    } else if (this.mExoPlayer.getDuration() >= 0) {
                        this.seekBarSmall.setMax((int) this.mExoPlayer.getDuration());
                    } else {
                        this.seekBarSmall.setMax(Integer.parseInt(getCurentSong() != null ? getCurentSong().duration : "0"));
                    }
                    this.seekBarSmall.setProgress((int) this.mExoPlayer.getCurrentPosition());
                    this.seekBarSmall.postDelayed(this.mProgressSmall, this.mInterval);
                    this.mProgressSmall.run();
                }
                str = AppUtils.convertDuration(parseLong);
                textView3.setText(str);
                TextView textView42 = this.tv_time_playing;
                if (this.mExoPlayer.getCurrentPosition() > 0) {
                }
                textView42.setText(str4);
                if (!this.IS_PLAYING_ONLINE) {
                }
                this.seekBarSmall.setProgress((int) this.mExoPlayer.getCurrentPosition());
                this.seekBarSmall.postDelayed(this.mProgressSmall, this.mInterval);
                this.mProgressSmall.run();
            } else {
                this.bufferView.setVisibility(VISIBLE);
                this.btn_play_pause_PlayerAct.setVisibility(GONE);
            }
            if (this.lstLoveSong.contains(getCurentSong())) {
                this.btnfavorite.setImageResource(R.drawable.favorite_on);
            } else {
                this.btnfavorite.setImageResource(R.drawable.favorite_off);
            }
            if (isPlay()) {
                this.btn_play_pause_PlayerAct.setImageResource(R.drawable.ic_pause);
            } else {
                this.btn_play_pause_PlayerAct.setImageResource(R.drawable.ic_play);
            }
            refreshLoveSong();
        }
    }

    @SuppressLint({"SetTextI18n"})
    public void setUIConTrolFragmentPlayer(ImageView imageView) {
        this.frgBigThumb = imageView;
        setArtWork();
    }

    public void initViewSearch(PlayerView playerView) {
        this.searchPlayerView = playerView;
        setDataSearchPlayerView();
    }

    public void setDataCategoryPlayerView() {
        PlayerView playerView = this.categoryPlayerView;
        if (playerView != null) {
            playerView.setStatePlayer(isPlay());
            PlayerView playerView2 = this.categoryPlayerView;
            String str = this.IS_PLAYING_ONLINE ? this.mCurrentOnline.title : !this.lstOffline.isEmpty() ? ((Song) this.lstOffline.get(getSongPos())).getTitle() : getString(R.string.txt_title);
            playerView2.setTitle(str);
            PlayerView playerView3 = this.categoryPlayerView;
            String str2 = this.IS_PLAYING_ONLINE ? AppUtils.convertDuration(this.mCurrentOnline.duration) : !this.lstOffline.isEmpty() ? ((Song) this.lstOffline.get(getSongPos())).getArtist() : getString(R.string.tit_artist);
            playerView3.setArtist(str2);
            PlayerView playerView4 = this.categoryPlayerView;
            Context applicationContext = getApplicationContext();
            boolean z = this.IS_PLAYING_ONLINE;
            playerView4.setThumb(applicationContext, z, z ? "x" : getAlbumID());
            if (this.mExoPlayer == null) {
                this.categoryPlayerView.setVisibility(GONE);
            } else if (isServiceRunning) {
                this.categoryPlayerView.setVisibility(VISIBLE);
            } else {
                this.categoryPlayerView.setVisibility(GONE);
            }
        }
    }

    public void setDataSearchPlayerView() {
        PlayerView playerView = this.searchPlayerView;
        if (playerView != null) {
            playerView.setStatePlayer(isPlay());
            PlayerView playerView2 = this.searchPlayerView;
            String str = this.IS_PLAYING_ONLINE ? this.mCurrentOnline.title : !this.lstOffline.isEmpty() ? ((Song) this.lstOffline.get(getSongPos())).getTitle() : getString(R.string.txt_title);
            playerView2.setTitle(str);
            PlayerView playerView3 = this.searchPlayerView;
            String str2 = this.IS_PLAYING_ONLINE ? AppUtils.convertDuration(this.mCurrentOnline.duration) : !this.lstOffline.isEmpty() ? ((Song) this.lstOffline.get(getSongPos())).getArtist() : getString(R.string.tit_artist);
            playerView3.setArtist(str2);
            PlayerView playerView4 = this.searchPlayerView;
            Context applicationContext = getApplicationContext();
            boolean z = this.IS_PLAYING_ONLINE;
            playerView4.setThumb(applicationContext, z, z ? "x" : getAlbumID());
            if (this.mExoPlayer == null) {
                this.searchPlayerView.setVisibility(GONE);
            } else if (isServiceRunning) {
                this.searchPlayerView.setVisibility(VISIBLE);
            } else {
                this.searchPlayerView.setVisibility(GONE);
            }
        }
    }

    public String getAlbumID() {
        return !this.lstOffline.isEmpty() ? String.valueOf(((Song) this.lstOffline.get(this.songPos)).getAlbumId()) : "0";
    }

    public boolean checkLoveSong() {
        this.lstLoveSong = this.songListDao.getAllFavoriteSong();
        for (int i = 0; i < this.lstLoveSong.size(); i++) {
            if (((Song) this.lstLoveSong.get(i)).id == getCurentSong().getId()) {
                return true;
            }
        }
        return false;
    }

    public void refreshLoveSong() {
        this.lstLoveSong = this.songListDao.getAllFavoriteSong();
    }

    public final void createNotificationChannel() {
        this.notificationManager = (NotificationManager) getSystemService("notification");
        if (VERSION.SDK_INT >= 26) {
            NotificationChannel notificationChannel = new NotificationChannel("com.eskaylation.downloadmusic", name, 2);
            notificationChannel.enableVibration(false);
            notificationChannel.enableLights(true);
            this.notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    public void showNotification() {
        if (!isServiceRunning) {
            isServiceRunning = true;
            createNotificationChannel();
            createNotification(true);
            startForeground(888888, this.mBuilder.build());
        }
    }

    public void createNotification(boolean z) {
        String str;
        String str2;
        Intent intent = new Intent("com.eskaylation.downloadmusic.action.next");
        Intent intent2 = new Intent("com.eskaylation.downloadmusic.action.prive");
        Intent intent3 = new Intent("com.eskaylation.downloadmusic.action.stop_music");
        PendingIntent broadcast;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            broadcast = PendingIntent.getBroadcast(this, 0, new Intent("com.eskaylation.downloadmusic.action.playpause"), PendingIntent.FLAG_IMMUTABLE);
        } else {
            broadcast = PendingIntent.getBroadcast(this, 0, new Intent("com.eskaylation.downloadmusic.action.playpause"), PendingIntent.FLAG_UPDATE_CURRENT);
        }
        PendingIntent broadcast2;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            broadcast2 = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        } else {
            broadcast2 = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        PendingIntent broadcast3;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            broadcast3 = PendingIntent.getBroadcast(this, 0, intent2, PendingIntent.FLAG_IMMUTABLE);
        } else {
            broadcast3 = PendingIntent.getBroadcast(this, 0, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        PendingIntent broadcast4;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            broadcast4 = PendingIntent.getBroadcast(this, 0, intent3, PendingIntent.FLAG_IMMUTABLE);
        } else {
            broadcast4 = PendingIntent.getBroadcast(this, 0, intent3, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        Intent intent4 = new Intent(getApplicationContext(), PlayerActivity.class);
        intent4.setAction("ACTION_MAIN_OPEN");
        intent4.putExtra("notify", "OPEN_FROM_NOTIFICATION");
        intent4.setFlags(603979776);
        PendingIntent activity;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            activity = PendingIntent.getActivity(this, 0, intent4, PendingIntent.FLAG_IMMUTABLE);
        } else {
            activity = PendingIntent.getActivity(this, 0, intent4, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        if (this.IS_PLAYING_ONLINE) {
            AudioExtract audioExtract = this.mCurrentOnline;
            str2 = audioExtract != null ? audioExtract.title : getString(R.string.txt_songs);
            str = getString(R.string.unknow);
        } else {
            str2 = !this.lstOffline.isEmpty() ? ((Song) this.lstOffline.get(this.songPos)).getTitle() : getString(R.string.txt_songs);
            str = !this.lstOffline.isEmpty() ? ((Song) this.lstOffline.get(this.songPos)).artist
                    : getString(R.string.tit_artist);
        }
        String str3 = "Close";
        String str4 = "next";
        String str5 = "Pause";
        String str6 = "Prive";
        String str7 = "com.eskaylation.downloadmusic";
        if (z) {
            this.mBuilder = new Builder(getApplicationContext(), str7);
            Builder builder = this.mBuilder;
            builder.setSmallIcon(R.drawable.song_not_found);
            builder.setShowWhen(false);
            builder.setVisibility(1);
            builder.setContentTitle(str2);
            builder.setContentText(str);
            builder.setOngoing(true);
            builder.setSubText(getString(R.string.txt_now_playing));
            builder.setPriority(-1);
            builder.setContentIntent(activity);
            builder.addAction(R.drawable.noti_prive, str6, broadcast3);
            builder.addAction(R.drawable.noti_pause, str5, broadcast);
            builder.addAction(R.drawable.noti_next, str4, broadcast2);
            builder.addAction(R.drawable.noti_close, str3, broadcast4);
            NotificationCompat.MediaStyle notificationCompat$MediaStyle = new NotificationCompat.MediaStyle();
            notificationCompat$MediaStyle.setShowActionsInCompactView(0, 1, 2);
            notificationCompat$MediaStyle.setMediaSession(this.mMediaSessionCompat.getSessionToken());
            builder.setStyle(notificationCompat$MediaStyle);
            return;
        }
        this.mBuilder = new Builder(getApplicationContext(), str7);
        Builder builder2 = this.mBuilder;
        builder2.setSmallIcon(R.drawable.song_not_found);
        builder2.setShowWhen(false);
        builder2.setOngoing(false);
        builder2.setVisibility(1);
        builder2.setContentTitle(str2);
        builder2.setContentText(str);
        builder2.setSubText(getString(R.string.txt_now_playing));
        builder2.setPriority(-1);
        builder2.setContentIntent(activity);
        builder2.addAction(R.drawable.noti_prive, str6, broadcast3);
        builder2.addAction(R.drawable.noti_play, str5, broadcast);
        builder2.addAction(R.drawable.noti_next, str4, broadcast2);
        builder2.addAction(R.drawable.noti_close, str3, broadcast4);
        NotificationCompat.MediaStyle notificationCompat$MediaStyle2 = new NotificationCompat.MediaStyle();
        notificationCompat$MediaStyle2.setShowActionsInCompactView(0, 1, 2);
        notificationCompat$MediaStyle2.setMediaSession(this.mMediaSessionCompat.getSessionToken());
        builder2.setStyle(notificationCompat$MediaStyle2);
    }

    @SuppressLint({"RestrictedApi"})
    public void setUpNotificationData() {
        String str;
        String str2;
        if (this.IS_PLAYING_ONLINE) {
            AudioExtract audioExtract = this.mCurrentOnline;
            str2 = audioExtract != null ? audioExtract.title : getString(R.string.txt_songs);
            str = getString(R.string.unknow);
        } else {
            str2 = !this.lstOffline.isEmpty() ? ((Song) this.lstOffline.get(this.songPos)).getTitle() : getString(R.string.txt_songs);
            str = !this.lstOffline.isEmpty() ? ((Song) this.lstOffline.get(this.songPos)).artist : getString(R.string.tit_artist);
        }
        this.mBuilder.setContentTitle(str2);
        this.mBuilder.setContentText(str);
        this.notificationManager = (NotificationManager) getSystemService("notification");
        SimpleExoPlayer simpleExoPlayer = this.mExoPlayer;
        if (simpleExoPlayer == null) {
            return;
        }
        if (simpleExoPlayer.getPlayWhenReady()) {
            if (this.notificationManager != null) {
                this.mBuilder.setOngoing(true);
                if (VERSION.SDK_INT >= 23) {
                    ((IconCompat) Objects.requireNonNull(((Action) this.mBuilder.mActions.get(1)).getIconCompat())).mInt1 = R.drawable.noti_pause;
                } else {
                    ((Action) this.mBuilder.mActions.get(1)).icon = R.drawable.noti_pause;
                }
                startForeground(888888, this.mBuilder.build());
            }
        } else if (this.notificationManager != null) {
            this.mBuilder.setOngoing(false);
            if (VERSION.SDK_INT >= 23) {
                ((IconCompat) Objects.requireNonNull(((Action) this.mBuilder.mActions.get(1)).getIconCompat())).mInt1 = R.drawable.noti_play;
            } else {
                ((Action) this.mBuilder.mActions.get(1)).icon = R.drawable.noti_play;
            }
            stopForeground(false);
            this.notificationManager.notify(888888, this.mBuilder.build());
        }
    }

    @SuppressLint({"CheckResult"})
    public void setArtWork() {
        this.thumb = R.drawable.ic_player_default;
        this.bitmapDefault = BitmapFactory.decodeResource(getResources(), R.drawable.bg_1);
        ImageView imageView = this.frgBigThumb;
        if (imageView == null) {
            return;
        }
        if (this.IS_PLAYING_ONLINE) {
            imageView.setImageResource(this.thumb);
            this.palette = BitmapUtils.createPaletteSync(this.bitmapDefault);
            EventBus.getDefault().postSticky(new PaletteColor(this.palette));
            this.mBuilder.setLargeIcon(this.bitmapDefault);
            NotificationManager notificationManager2 = this.notificationManager;
            if (notificationManager2 != null) {
                notificationManager2.notify(888888, this.mBuilder.build());
            }
        } else if (getCurentSong() != null) {
            Glide.with(this).asBitmap().load(ArtworkUtils.uri(getCurentSong().albumId)).into(new CustomTarget<Bitmap>() {
                public void onLoadCleared(Drawable drawable) {
                }

                public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                    MusicPlayerService.this.frgBigThumb.setImageBitmap(bitmap);
                    MusicPlayerService.this.palette = BitmapUtils.createPaletteSync(bitmap);
                    EventBus.getDefault().postSticky(new PaletteColor(MusicPlayerService.this.palette));
                    MusicPlayerService.this.mBuilder.setLargeIcon(bitmap);
                    if (MusicPlayerService.this.notificationManager != null) {
                        MusicPlayerService.this.notificationManager.notify(888888, MusicPlayerService.this.mBuilder.build());
                    }
                    Log.e("Glide", "true");
                }

                public void onLoadFailed(Drawable drawable) {
                    super.onLoadFailed(drawable);
                    MusicPlayerService.this.frgBigThumb.setImageResource(MusicPlayerService.this.thumb);
                    MusicPlayerService musicPlayerService = MusicPlayerService.this;
                    musicPlayerService.palette = BitmapUtils.createPaletteSync(musicPlayerService.bitmapDefault);
                    EventBus.getDefault().postSticky(new PaletteColor(MusicPlayerService.this.palette));
                    MusicPlayerService.this.mBuilder.setLargeIcon(MusicPlayerService.this.bitmapDefault);
                    if (MusicPlayerService.this.notificationManager != null) {
                        MusicPlayerService.this.notificationManager.notify(888888, MusicPlayerService.this.mBuilder.build());
                    }
                    Log.e("Glide", "Failed");
                }
            });
        }
    }

    public final void setBroadcast() {
        this.broadcastControl = new BroadcastControl();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.eskaylation.downloadmusic.action.next");
        intentFilter.addAction("com.eskaylation.downloadmusic.action.prive");
        intentFilter.addAction("com.eskaylation.downloadmusic.action.playpause");
        intentFilter.addAction("com.eskaylation.downloadmusic.ACTION.STARTSERVICE");
        intentFilter.addAction("com.eskaylation.downloadmusic.action.stop_music");
        registerReceiver(this.broadcastControl, intentFilter);
    }

    public void stopServiceAndCloseNotification() {
        if (isServiceRunning) {
            disableAllEffect();
            unregisterRemoteControl();
            stopPlayer();
            stopTimer();
            this.preferenceUtils.setShowAds(true);
            isServiceRunning = false;
            if (VERSION.SDK_INT >= 26) {
                this.notificationManager.deleteNotificationChannel("com.eskaylation.downloadmusic");
            } else {
                stopForeground(true);
            }
            this.notificationManager.cancel(888888);
            EventBus.getDefault().postSticky(new StopService(true));
        }
        this.listPreviousOnline.clear();
        this.listOnLine.clear();
        this.lstOffline.clear();
        this.urlPlayer = "";
        Logger.e("Stop service", new Object[0]);
    }

    @SuppressLint({"SetTextI18n"})
    public void stopPlayer() {
        this.urlPlayer = "";
        releasePlayer();
        PlayerView playerView = this.persionalPlayerview;
        if (playerView != null) {
            playerView.setStatePlayer(false);
            this.persionalPlayerview.setVisibility(GONE);
        }
        PlayerView playerView2 = this.searchPlayerView;
        if (playerView2 != null) {
            playerView2.setStatePlayer(false);
            this.searchPlayerView.setVisibility(GONE);
        }
        PlayerView playerView3 = this.categoryPlayerView;
        if (playerView3 != null) {
            playerView3.setStatePlayer(false);
            this.categoryPlayerView.setVisibility(GONE);
        }
        ImageView imageView = this.btn_play_pause_PlayerAct;
        if (imageView != null) {
            imageView.setImageResource(R.drawable.ic_play);
        }
        SeekBar seekBar = this.seekBarSmall;
        if (seekBar != null) {
            seekBar.setProgress(0);
            this.seekBarSmall.setSecondaryProgress(0);
            this.seekBarSmall.removeCallbacks(this.mProgressSmall);
        }
        RoundCornerProgressBar roundCornerProgressBar = this.progress_main;
        if (roundCornerProgressBar != null) {
            roundCornerProgressBar.setVisibility(GONE);
            this.progress_main.setProgress(0.0f);
            this.progress_main.removeCallbacks(this.mProgressRunnerMain);
        }
        TextView textView = this.tv_time_playing;
        if (textView != null) {
            textView.setText("00:00");
            this.tv_time_playing.removeCallbacks(this.mProgressSmall);
        }
        PlayerView playerView4 = this.mainPlayerview;
        if (playerView4 != null) {
            playerView4.setStatePlayer(false);
            this.mainPlayerview.setVisibility(GONE);
        }
        SongAdapter songAdapter = this.adapterSong;
        if (songAdapter != null) {
            songAdapter.setItemIndex(new Song());
        }
        NowPlayingAdapter nowPlayingAdapter = this.adapterNowPlaying;
        if (nowPlayingAdapter != null) {
            nowPlayingAdapter.setIndexOffline(new Song());
        }
    }

    public void playSong() {
        SimpleExoPlayer simpleExoPlayer = this.mExoPlayer;
        if (simpleExoPlayer != null) {
            if (simpleExoPlayer.getPlayWhenReady()) {
                ImageView imageView = this.btn_play_pause_PlayerAct;
                if (imageView != null) {
                    imageView.setImageResource(R.drawable.ic_play);
                }
                PlayerView playerView = this.persionalPlayerview;
                if (playerView != null) {
                    playerView.setStatePlayer(false);
                }
                PlayerView playerView2 = this.searchPlayerView;
                if (playerView2 != null) {
                    playerView2.setStatePlayer(false);
                }
                PlayerView playerView3 = this.categoryPlayerView;
                if (playerView3 != null) {
                    playerView3.setStatePlayer(false);
                }
                PlayerView playerView4 = this.mainPlayerview;
                if (playerView4 != null) {
                    playerView4.setStatePlayer(false);
                }
                updateRemoteControl(2);
                this.mExoPlayer.setPlayWhenReady(false);
                this.preferenceUtils.setShowAds(true);
            } else if (this.audioManager.requestAudioFocus(this.mOnAudioFocusChangeListener_, 3, 1) == 1) {
                this.mExoPlayer.setPlayWhenReady(true);
                this.preferenceUtils.setShowAds(false);
                updateRemoteControl(3);
                ImageView imageView2 = this.btn_play_pause_PlayerAct;
                if (imageView2 != null) {
                    imageView2.setImageResource(R.drawable.ic_pause);
                }
                PlayerView playerView5 = this.persionalPlayerview;
                if (playerView5 != null) {
                    playerView5.setStatePlayer(true);
                }
                PlayerView playerView6 = this.searchPlayerView;
                if (playerView6 != null) {
                    playerView6.setStatePlayer(true);
                }
                PlayerView playerView7 = this.categoryPlayerView;
                if (playerView7 != null) {
                    playerView7.setStatePlayer(true);
                }
                PlayerView playerView8 = this.mainPlayerview;
                if (playerView8 != null) {
                    playerView8.setStatePlayer(true);
                }
                if (this.seekBarSmall != null) {
                    this.mProgressSmall.run();
                    if (this.IS_PLAYING_ONLINE) {
                        this.seekBarSmall.setMax((int) this.mExoPlayer.getDuration());
                    } else if (this.mExoPlayer.getDuration() >= 0) {
                        this.seekBarSmall.setMax((int) this.mExoPlayer.getDuration());
                    } else {
                        this.seekBarSmall.setMax(Integer.parseInt(getCurentSong().duration));
                    }
                    this.seekBarSmall.postDelayed(this.mProgressSmall, this.mInterval);
                }
                if (this.progress_main != null) {
                    this.mProgressRunnerMain.run();
                    this.progress_main.setMax((float) ((int) this.mExoPlayer.getDuration()));
                    this.progress_main.postDelayed(this.mProgressRunnerMain, this.mInterval);
                }
            }
            setUpNotificationData();
            return;
        }
        ToastUtils.warning((Context) this, getString(R.string.waiting_txt));
        Logger.e("Exo null", new Object[0]);
    }

    public void next() {
        this.urlPlayer = "";
        this.durationSet = false;
        String str = "com.eskaylation.downloadmusic.action.loop_music";
        if (!this.IS_PLAYING_ONLINE) {
            if (!this.lstOffline.isEmpty()) {
                stopPlayer();
                EventBus.getDefault().postSticky(new CloseDialog(true));
                if (this.preferenceUtils.getBoolean("com.eskaylation.downloadmusic.SHUFFLE_MUSIC")) {
                    if (this.lstOffline.size() > 0) {
                        this.songPos = AppUtils.getRandomNumber(this.lstOffline.size() - 1);
                    }
                    setDatasource();
                } else if (this.preferenceUtils.getInt(str) == 1) {
                    setDatasource();
                } else if (this.preferenceUtils.getInt(str) == 999) {
                    if (this.songPos == this.lstOffline.size() - 1) {
                        this.songPos = 0;
                    } else {
                        this.songPos = getSongPos() + 1;
                    }
                    setDatasource();
                } else if (this.songPos < this.lstOffline.size() - 1) {
                    this.songPos = getSongPos() + 1;
                    setDatasource();
                } else {
                    endList();
                    Intent intent = new Intent(this, MusicPlayerService.class);
                    intent.setAction("com.eskaylation.downloadmusic.action.stop_music");
                    startService(intent);
                }
            }
        } else if (this.listOnLine.isEmpty()) {
        } else {
            if (this.preferenceUtils.getInt(str) == 1) {
                SimpleExoPlayer simpleExoPlayer = this.mExoPlayer;
                if (simpleExoPlayer != null) {
                    simpleExoPlayer.seekTo(0);
                }
            } else if (!this.parseRunning) {
                if (this.mCurrentOnline != null && !this.listPreviousOnline.isEmpty()) {
                    ArrayList<AudioExtract> arrayList = this.listPreviousOnline;
                    if (!((AudioExtract) arrayList.get(arrayList.size() - 1)).urlVideo.equals(this.mCurrentOnline.urlVideo)) {
                        this.listPreviousOnline.add(this.mCurrentOnline);
                    }
                }
                this.mCurrentOnline = (AudioExtract) this.listOnLine.get(0);
                LinearLayout linearLayout = this.viewLoadingNowPlaying;
                if (linearLayout != null) {
                    linearLayout.setVisibility(VISIBLE);
                    this.rv_nowPlaying.setVisibility(GONE);
                }
                initDataPlayerActivity();
                this.bufferView.setVisibility(VISIBLE);
                this.btn_play_pause_PlayerAct.setVisibility(GONE);
                this.generateUrlMusicUtils.getUrlAudio(this.mCurrentOnline);
                this.parseRunning = true;
            } else {
                ToastUtils.warning((Context) this, getString(R.string.waiting_txt));
            }
        }
    }

    public void nextSongUnLimitted() {
        if (!this.parseRunning) {
            if (this.mCurrentOnline != null && !this.listPreviousOnline.isEmpty()) {
                ArrayList<AudioExtract> arrayList = this.listPreviousOnline;
                if (!((AudioExtract) arrayList.get(arrayList.size() - 1)).urlVideo.equals(this.mCurrentOnline.urlVideo)) {
                    this.listPreviousOnline.add(this.mCurrentOnline);
                }
            }
            this.mCurrentOnline = (AudioExtract) this.listOnLine.get(0);
            LinearLayout linearLayout = this.viewLoadingNowPlaying;
            if (linearLayout != null) {
                linearLayout.setVisibility(VISIBLE);
                this.rv_nowPlaying.setVisibility(GONE);
            }
            initDataPlayerActivity();
            this.bufferView.setVisibility(VISIBLE);
            this.btn_play_pause_PlayerAct.setVisibility(GONE);
            this.generateUrlMusicUtils.getUrlAudio(this.mCurrentOnline);
            this.parseRunning = true;
            return;
        }
        ToastUtils.warning((Context) this, getString(R.string.waiting_txt));
    }

    public void priveSong() {
        this.urlPlayer = "";
        EventBus.getDefault().postSticky(new CloseDialog(true));
        String str = "com.eskaylation.downloadmusic.action.loop_music";
        if (!this.IS_PLAYING_ONLINE) {
            stopPlayer();
            this.durationSet = false;
            if (this.preferenceUtils.getBoolean("com.eskaylation.downloadmusic.SHUFFLE_MUSIC")) {
                if (this.lstOffline.size() > 0) {
                    this.songPos = AppUtils.getRandomNumber(this.lstOffline.size() - 1);
                }
                setDatasource();
            } else if (this.preferenceUtils.getInt(str) == 1) {
                setDatasource();
            } else if (this.preferenceUtils.getInt(str) == 999) {
                int i = this.songPos;
                if (i == 0) {
                    this.songPos = this.lstOffline.size() - 1;
                } else {
                    this.songPos = i - 1;
                }
                setDatasource();
            } else if (!this.lstOffline.isEmpty()) {
                int i2 = this.songPos;
                if (i2 != 0) {
                    this.songPos = i2 - 1;
                    setDatasource();
                    return;
                }
                this.songPos = 0;
                setDatasource();
            }
        } else if (this.preferenceUtils.getInt(str) == 1) {
            SimpleExoPlayer simpleExoPlayer = this.mExoPlayer;
            if (simpleExoPlayer != null) {
                simpleExoPlayer.seekTo(0);
                if (!this.mExoPlayer.getPlayWhenReady()) {
                    playSong();
                }
            }
        } else {
            stopPlayer();
            this.durationSet = false;
            if (this.listPreviousOnline.isEmpty()) {
                return;
            }
            if (!this.parseRunning) {
                if (this.listPreviousOnline.size() != 1) {
                    ArrayList<AudioExtract> arrayList = this.listPreviousOnline;
                    this.mCurrentOnline = (AudioExtract) arrayList.get(arrayList.size() - 1);
                    ArrayList<AudioExtract> arrayList2 = this.listPreviousOnline;
                    arrayList2.remove(arrayList2.size() - 1);
                } else {
                    ArrayList<AudioExtract> arrayList3 = this.listPreviousOnline;
                    this.mCurrentOnline = (AudioExtract) arrayList3.get(arrayList3.size() - 1);
                    Toast.makeText(this, getString(R.string.txt_first_song), Toast.LENGTH_SHORT).show();
                }
                LinearLayout linearLayout = this.viewLoadingNowPlaying;
                if (linearLayout != null) {
                    linearLayout.setVisibility(VISIBLE);
                    this.rv_nowPlaying.setVisibility(GONE);
                }
                LinearLayout linearLayout2 = this.viewLoadingNowPlaying;
                if (linearLayout2 != null) {
                    linearLayout2.setVisibility(VISIBLE);
                    this.rv_nowPlaying.setVisibility(GONE);
                }
                initDataPlayerActivity();
                this.bufferView.setVisibility(VISIBLE);
                this.btn_play_pause_PlayerAct.setVisibility(GONE);
                this.generateUrlMusicUtils.getUrlAudio(this.mCurrentOnline);
                this.parseRunning = true;
                return;
            }
            ToastUtils.warning((Context) this, getString(R.string.waiting_txt));
        }
    }

    @SuppressLint({"SetTextI18n"})
    public void endList() {
        PlayerView playerView = this.persionalPlayerview;
        if (playerView != null) {
            playerView.setStatePlayer(false);
            this.progress_main.setProgress(0.0f);
            this.progress_main.setVisibility(GONE);
        }
        PlayerView playerView2 = this.mainPlayerview;
        if (playerView2 != null) {
            playerView2.setStatePlayer(false);
            this.mainPlayerview.setVisibility(GONE);
        }
        PlayerView playerView3 = this.searchPlayerView;
        if (playerView3 != null) {
            playerView3.setStatePlayer(false);
            this.searchPlayerView.setVisibility(GONE);
        }
        PlayerView playerView4 = this.categoryPlayerView;
        if (playerView4 != null) {
            playerView4.setStatePlayer(false);
            this.categoryPlayerView.setVisibility(GONE);
        }
        if (this.tv_time_playing != null) {
            this.seekBarSmall.setProgress(0);
            this.seekBarSmall.setSecondaryProgress(0);
            this.tv_time_playing.setText("00:00");
            this.btn_play_pause_PlayerAct.setImageResource(R.drawable.ic_play);
        }
        unregisterRemoteControl();
        createNotification(false);
    }

    public void restartPlayer() {
        this.songPos = 0;
        setDatasource();
    }

    public void seekMusic(int i) {
        SimpleExoPlayer simpleExoPlayer = this.mExoPlayer;
        if (simpleExoPlayer != null) {
            simpleExoPlayer.seekTo((long) i);
            if (this.songPos == this.lstOffline.size() - 1) {
                this.mExoPlayer.getPlayWhenReady();
            }
            if (!this.mExoPlayer.getPlayWhenReady()) {
                playSong();
            }
        }
    }

    public void stopSong() {
        try {
            releasePlayer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Song> getListAudio() {
        return this.lstOffline;
    }

    public int getSongPos() {
        return this.songPos;
    }

    public void setSongPos(int i) {
        this.songPos = i;
    }

    public Song getCurentSong() {
        if (!this.lstOffline.isEmpty()) {
            return (Song) this.lstOffline.get(getSongPos());
        }
        return null;
    }

    public Song getItemIndex() {
        if (this.IS_PLAYING_ONLINE || this.lstOffline.isEmpty() || getSongPos() > this.lstOffline.size() - 1) {
            return null;
        }
        return (Song) this.lstOffline.get(getSongPos());
    }

    public boolean isPlay() {
        SimpleExoPlayer simpleExoPlayer = this.mExoPlayer;
        return simpleExoPlayer != null && simpleExoPlayer.getPlayWhenReady();
    }

    public void setListMusic(ArrayList<Song> arrayList, int i) {
        this.lstOffline.clear();
        this.lstOffline.addAll(arrayList);
        setSongPos(i);
    }

    public void setDatasource() {
        this.urlPlayer = "";
        if (getCurentSong() != null) {
            String str = ((Song) this.lstOffline.get(getSongPos())).getmSongPath();
            stopPlayer();
            this.dataSourceFactory = buildDataSourceFactory();
            this.trackSelectorParameters = new ParametersBuilder().build();
            AdaptiveTrackSelection.Factory factory = new AdaptiveTrackSelection.Factory();
            DefaultRenderersFactory defaultRenderersFactory = new DefaultRenderersFactory(this);
            this.trackSelector = new DefaultTrackSelector((TrackSelection.Factory) factory);
            this.trackSelector.setParameters(this.trackSelectorParameters);
            this.mExoPlayer = ExoPlayerFactory.newSimpleInstance(getApplicationContext(), (RenderersFactory) defaultRenderersFactory, (TrackSelector) this.trackSelector, (LoadControl) new DefaultLoadControl());
            this.mExoPlayer.addListener(new PlayerEventListener());
            this.mExoPlayer.setAudioAttributes(new AudioAttributes.Builder().setContentType(2).setUsage(1).build());
            this.mExoPlayer.addAnalyticsListener(new EventLogger(this.trackSelector));
            try {
                if (new File(str).exists()) {
                    this.mediaSource = buildMediaSource(Uri.parse(str));
                    if (this.audioManager.requestAudioFocus(this.mOnAudioFocusChangeListener_, 3, 1) == 1) {
                        this.mExoPlayer.setPlayWhenReady(true);
                    }
                    this.mExoPlayer.prepare(this.mediaSource);
                    return;
                }
                next();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            next();
        }
    }

    public void setDatasource(String str) {
        this.durationSet = false;
        stopPlayer();
        this.dataSourceFactory = buildDataSourceFactory();
        this.trackSelectorParameters = new ParametersBuilder().build();
        AdaptiveTrackSelection.Factory factory = new AdaptiveTrackSelection.Factory();
        DefaultRenderersFactory defaultRenderersFactory = new DefaultRenderersFactory(this);
        this.trackSelector = new DefaultTrackSelector((TrackSelection.Factory) factory);
        this.trackSelector.setParameters(this.trackSelectorParameters);
        this.mExoPlayer = ExoPlayerFactory.newSimpleInstance(getApplicationContext(), (RenderersFactory) defaultRenderersFactory, (TrackSelector) this.trackSelector, (LoadControl) new DefaultLoadControl());
        this.mExoPlayer.setAudioAttributes(new AudioAttributes.Builder().setContentType(2).setUsage(1).build());
        this.mExoPlayer.addListener(new PlayerEventListener());
        this.mExoPlayer.addAnalyticsListener(new EventLogger(this.trackSelector));
        try {
            this.mediaSource = buildMediaSource(Uri.parse(str));
            this.urlPlayer = str;
            if (this.audioManager.requestAudioFocus(this.mOnAudioFocusChangeListener_, 3, 1) == 1) {
                this.mExoPlayer.setPlayWhenReady(true);
            }
            this.mExoPlayer.prepare(this.mediaSource);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final void initPrepare() {
        enableAllEffect();
        if (this.IS_PLAYING_ONLINE) {
            EventBus.getDefault().postSticky(new EndSong(true));
        }
        PlayerView playerView = this.persionalPlayerview;
        if (playerView != null) {
            playerView.setStatePlayer(false);
            this.progress_main.setProgress(0.0f);
            this.progress_main.setVisibility(VISIBLE);
        }
        PlayerView playerView2 = this.mainPlayerview;
        if (playerView2 != null) {
            playerView2.setStatePlayer(false);
            this.mainPlayerview.setVisibility(VISIBLE);
        }
        PlayerView playerView3 = this.searchPlayerView;
        if (playerView3 != null) {
            playerView3.setStatePlayer(false);
            this.searchPlayerView.setVisibility(VISIBLE);
        }
        PlayerView playerView4 = this.categoryPlayerView;
        if (playerView4 != null) {
            playerView4.setStatePlayer(false);
            this.categoryPlayerView.setVisibility(VISIBLE);
        }
        updateRemoteControl(3);
        initDataPlayerActivity();
        initDataMain();
        setDataSearchPlayerView();
        setDataCategoryPlayerView();
        initAdapterControlFrgSong();
        setUpNotificationData();
        setArtWork();
        initAdapterControlFrgSong();
        initDataNowplaying();
        if (this.frgBigThumb != null) {
            TextView textView = this.tv_name;
            String str = this.IS_PLAYING_ONLINE ? this.mCurrentOnline.title : this.lstOffline.size() > 0 ? ((Song) this.lstOffline.get(getSongPos())).getTitle() : getString(R.string.txt_song);
            textView.setText(str);
            TextView textView2 = this.tv_artist;
            String str2 = this.IS_PLAYING_ONLINE ? "Download Music" : this.lstOffline.size() > 0 ? ((Song) this.lstOffline.get(getSongPos())).getArtist() : getString(R.string.txt_artist);
            textView2.setText(str2);
            this.tv_name.setSelected(true);
            if (this.lstLoveSong.contains(getCurentSong())) {
                this.btnfavorite.setImageResource(R.drawable.favorite_on);
            } else {
                this.btnfavorite.setImageResource(R.drawable.favorite_off);
            }
        }
    }

    public final MediaSource buildMediaSource(Uri uri) {
        return buildMediaSource(uri, null);
    }

    public final MediaSource buildMediaSource(Uri uri, String str) {
        int inferContentType = Util.inferContentType(uri, str);
        if (inferContentType == 0) {
            return new DashMediaSource.Factory(this.dataSourceFactory).createMediaSource(uri);
        }
        if (inferContentType == 1) {
            return new SsMediaSource.Factory(this.dataSourceFactory).createMediaSource(uri);
        }
        if (inferContentType == 2) {
            return new HlsMediaSource.Factory(this.dataSourceFactory).createMediaSource(uri);
        }
        if (inferContentType == 3) {
            return new ExtractorMediaSource.Factory(this.dataSourceFactory).createMediaSource(uri);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Unsupported type: ");
        sb.append(inferContentType);
        throw new IllegalStateException(sb.toString());
    }

    public final void releasePlayer() {
        SimpleExoPlayer simpleExoPlayer = this.mExoPlayer;
        if (simpleExoPlayer != null) {
            simpleExoPlayer.release();
            this.mExoPlayer = null;
            this.mediaSource = null;
            this.trackSelector = null;
        }
        this.durationSet = false;
    }

    public final Factory buildDataSourceFactory() {
        return ((BaseApplication) getApplication()).buildDataSourceFactory();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onDeleteSong(NotifyDeleteMusic notifyDeleteMusic) {
        if (!this.lstOffline.isEmpty()) {
            Song song = (Song) this.lstOffline.get(getSongPos());
            Song song2 = notifyDeleteMusic.song;
            for (int i = 0; i < this.lstOffline.size(); i++) {
                if (((Song) this.lstOffline.get(i)).getmSongPath().equals(song2.getmSongPath())) {
                    this.lstOffline.remove(i);
                    this.songPos = this.lstOffline.indexOf(song);
                }
            }
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void OnMusicPlayServiceUrl(MusicPlayServiceUrl musicPlayServiceUrl) {
        String str = musicPlayServiceUrl.url;
        this.urlPlayer = str;
        setDatasource(str);
        ArrayList<AudioExtract> arrayList = musicPlayServiceUrl.lstRecomend;
        if (!arrayList.isEmpty()) {
            this.listOnLine.clear();
            this.listOnLine.addAll(arrayList);
            NowPlayingAdapter nowPlayingAdapter = this.adapterNowPlaying;
            if (nowPlayingAdapter != null) {
                nowPlayingAdapter.setListOnline(this.listOnLine);
                this.rv_nowPlaying.scrollToPosition(0);
                this.rv_nowPlaying.setVisibility(VISIBLE);
                this.viewLoadingNowPlaying.setVisibility(GONE);
            }
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void OnMusicPlayServiceUrlFailed(MusicPlayServiceUrlFailed musicPlayServiceUrlFailed) {
        this.urlPlayer = "";
        ToastUtils.error((Context) this, getString(R.string.txt_error_again));
        stopServiceAndCloseNotification();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void ConTrolBroadcast(Control control) {
        char c;
        String str = control.event;
        String str2 = "com.eskaylation.downloadmusic.action.stop_music";
        switch (str.hashCode()) {
            case -399999368:
                if (str.equals("com.eskaylation.downloadmusic.action.playpause")) {
                    c = 1;
                    break;
                }
            case -376054275:
                if (str.equals("com.eskaylation.downloadmusic.action.next")) {
                    c = 0;
                    break;
                }
            case 1229439436:
                if (str.equals("com.eskaylation.downloadmusic.action.prive")) {
                    c = 2;
                    break;
                }
            case 1371943730:
                if (str.equals(str2)) {
                    c = 3;
                    break;
                }
            default:
                c = 65535;
                break;
        }
        if (c != 0) {
            if (c != 1) {
                if (c != 2) {
                    if (c == 3) {
                        Intent intent = new Intent(this, MusicPlayerService.class);
                        intent.setAction(str2);
                        startService(intent);
                    }
                } else if (isServiceRunning) {
                    priveSong();
                }
            } else if (isServiceRunning) {
                playSong();
            }
        } else if (isServiceRunning) {
            next();
        }
    }
}
