package com.eskaylation.downloadmusic.ui.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.eskaylation.downloadmusic.adapter.SongLightAdapter;
import com.eskaylation.downloadmusic.base.BaseActivity;
import com.eskaylation.downloadmusic.base.BaseApplication;
import com.eskaylation.downloadmusic.base.BaseLoaderListSong;
import com.eskaylation.downloadmusic.bus.CloseDialog;
import com.eskaylation.downloadmusic.database.SongListDao;
import com.eskaylation.downloadmusic.database.SongListSqliteHelper;
import com.eskaylation.downloadmusic.listener.FolderTrackListenner;
import com.eskaylation.downloadmusic.listener.OnItemSongClickListener;
import com.eskaylation.downloadmusic.listener.SongListenner;
import com.eskaylation.downloadmusic.loader.GetSongFolder;
import com.eskaylation.downloadmusic.loader.TrackLoader;
import com.eskaylation.downloadmusic.model.AdsManager;
import com.eskaylation.downloadmusic.model.Album;
import com.eskaylation.downloadmusic.model.Artist;
import com.eskaylation.downloadmusic.model.Favorite;
import com.eskaylation.downloadmusic.model.Folder;
import com.eskaylation.downloadmusic.model.Song;
import com.eskaylation.downloadmusic.service.MusicPlayerService;
import com.eskaylation.downloadmusic.service.MusicPlayerService.MusicServiceBinder;
import com.eskaylation.downloadmusic.ui.activity.addsong.ActivityAddSong;
import com.eskaylation.downloadmusic.ui.dialog.DialogMoreListener;
import com.eskaylation.downloadmusic.ui.dialog.DialogMoreSongUtil;



import com.eskaylation.downloadmusic.utils.PreferenceUtils;
import com.eskaylation.downloadmusic.utils.ToastUtils;
import com.google.android.exoplayer2.C;
import java.util.ArrayList;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import com.eskaylation.downloadmusic.R;

public class ListSongActivity extends BaseActivity implements BaseLoaderListSong, SongListenner, OnItemSongClickListener, FolderTrackListenner, DialogMoreListener {
  @BindView(R.id.ad_view_container)

    public FrameLayout adView;
    @BindView(R.id.btnBack)
    public ImageButton btnBack;
    public SongLightAdapter adapter;
    public Album albumShare;
    public Artist artistShare;
    @BindView(R.id.btnAddSong)
    public ImageButton btnAddSong;
    @BindView(R.id.btn_play_all)
    public Button btnPlayAll;
    public ServiceConnection connection = new ServiceConnection() {
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            ListSongActivity.this.musicPlayerService = ((MusicServiceBinder) iBinder).getService();
            ListSongActivity.this.mBound = true;
        }

        public void onServiceDisconnected(ComponentName componentName) {
            ListSongActivity.this.mBound = false;
        }
    };
    @BindView(R.id.coordinator)
    public CoordinatorLayout coordinator;
    public DialogMoreSongUtil dialogMore;
    public Folder folder;
    public boolean isLoveSong = false;
    public LinearLayoutManager llManager;
    public ArrayList<Song> lstAudio = new ArrayList<>();
    public boolean mBound = false;
    public GetSongFolder mGetSongFolder;
    public long mLastClickTime;
    public Window mWindow;
    public MusicPlayerService musicPlayerService;
    public Favorite playlist;
    public PreferenceUtils pref;
    @BindView(R.id.rv_listSong)
    public RecyclerView rvListSong;
    public SongListDao songListDao;
    public SongListSqliteHelper sqliteHelper;
    public TrackLoader trackLoader;
    @BindView(R.id.tv_title)
    public TextView tv_title;

    public String filterAlbum() {
        return "album_id = ?";
    }

    public String filterArtist() {
        return "artist_id = ?";
    }

    public String sortOder() {
        return "title_key ASC";
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_list_song);
        this.mWindow = getWindow();
        this.mWindow.getDecorView().setSystemUiVisibility(1280);
        BaseApplication.setContext(this);
        this.pref = PreferenceUtils.getInstance(this);
        init();
        loadBanner(this,adView);
        btnPlayAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onListenAll();
            }
        });
    }

    public void init() {
        ButterKnife.bind(this);
        setBackgroundThemes(this.coordinator, PreferenceUtils.getInstance(this).getThemesPosition());
        Intent intent = getIntent();
        this.albumShare = (Album) intent.getParcelableExtra("album_data");
        this.artistShare = (Artist) intent.getParcelableExtra("artist_data");
        intent.getStringExtra("folder_name");
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        this.folder = (Folder) intent.getParcelableExtra("folder_extra");
        this.playlist = (Favorite) intent.getParcelableExtra("playlist_name");
        this.dialogMore = new DialogMoreSongUtil(this, this, false);
        bindService();
        this.adapter = new SongLightAdapter(this, this);
        this.llManager = new LinearLayoutManager(this);
        this.rvListSong.setLayoutManager(this.llManager);
        this.rvListSong.setHasFixedSize(true);
        this.rvListSong.setAdapter(this.adapter);
        btnAddSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addSong();
            }
        });
    }

    public void onResume() {
        super.onResume();
        setDataIntent();
    }

    public void onDestroy() {
        super.onDestroy();
        unbindServicePlayMusic();
        BaseApplication.setContext(null);
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

    public void OnClickBack() {
        onBackPressed();
    }

    public void addSong() {

        ListSongActivity listSongActivity = ListSongActivity.this;
        listSongActivity.startActivity(new Intent(listSongActivity, ActivityAddSong.class).putExtra("Favorite_data", ListSongActivity.this.playlist));

        /*
        AdsManager.showNext(ListSongActivity.this, new AdsManager.AdCloseListener() {
            @Override
            public void onAdClosed() {

            }
        });


         */
    }

    public void onListenAll() {
        ArrayList<Song> arrayList = this.lstAudio;
        if (arrayList == null || arrayList.size() <= 0) {
            ToastUtils.error((Context) this, getString(R.string.no_song_to_play));
        } else {
            AdsManager.showNext(ListSongActivity.this, new AdsManager.AdCloseListener() {
                @Override
                public void onAdClosed() {
                    ListSongActivity.this.playAll();
                }
            });

        }
    }

    public void playAll() {
        MusicPlayerService musicPlayerService2 = this.musicPlayerService;
        if (musicPlayerService2 != null) {
            musicPlayerService2.setListMusic(this.lstAudio, 0);
            this.musicPlayerService.setSongPos(0);
            this.musicPlayerService.stopSong();
            Intent intent = new Intent(this, MusicPlayerService.class);
            intent.setAction("com.eskaylation.downloadmusic.ACTION.SETDATAOFFLINEPLAYER");
            startService(intent);
            Intent intent2 = new Intent(this, PlayerActivity.class);
            intent2.addFlags( 0x10000000);
            startActivity(intent2);
        }
    }

    public void setDataIntent() {
        Album album = this.albumShare;
        if (album != null) {
            this.tv_title.setText(album.getAlbumName());
            this.trackLoader = new TrackLoader(this, this);
            this.trackLoader.setSortOrder(sortOder());
            this.trackLoader.filteralbumsong(filterAlbum(), argument(this.albumShare.getId()));
            this.trackLoader.loadInBackground();
            return;
        }
        Artist artist = this.artistShare;
        if (artist != null) {
            this.tv_title.setText(artist.getName());
            this.trackLoader = new TrackLoader(this, this);
            this.trackLoader.setSortOrder(sortOder());
            this.trackLoader.filteralbumsong(filterArtist(), argument(this.artistShare.getId()));
            this.trackLoader.loadInBackground();
            return;
        }
        String str = ")";
        String str2 = " (";
        if (this.folder != null) {
            this.mGetSongFolder = new GetSongFolder(this);
            this.mGetSongFolder.setSort(sortOder());
            this.tv_title.setText(this.folder.name);
            this.lstAudio.clear();
            this.lstAudio.addAll(this.mGetSongFolder.getSongsByParentId(this.folder.parentID));
            this.adapter.setData(this.lstAudio);
            this.lstAudio.size();
            Button button = this.btnPlayAll;
            StringBuilder sb = new StringBuilder();
            sb.append(getString(R.string.txt_play_all));
            sb.append(str2);
            sb.append(this.lstAudio.size());
            sb.append(str);
            button.setText(sb.toString());
        } else if (this.playlist != null) {
            this.btnAddSong.setVisibility(View.VISIBLE);
            this.sqliteHelper = new SongListSqliteHelper(this, this.playlist.favorite_id);
            this.songListDao = new SongListDao(this.sqliteHelper);
            if (this.playlist.name.equals("DEFAULT_FAVORITEDOWNLOAD")) {
                this.tv_title.setText(getString(R.string.favorite_song));
                this.isLoveSong = true;
            } else {
                this.tv_title.setText(this.playlist.name);
            }
            this.lstAudio = this.songListDao.getAllFavoriteSong(getSortPlaylist());
            this.lstAudio.size();
            Button button2 = this.btnPlayAll;
            StringBuilder sb2 = new StringBuilder();
            sb2.append(getString(R.string.txt_play_all));
            sb2.append(str2);
            sb2.append(this.lstAudio.size());
            sb2.append(str);
            button2.setText(sb2.toString());
            this.adapter.setData(this.lstAudio);
        }
    }

    public void onAudioLoadedSuccessful(ArrayList<Song> arrayList) {
        runOnUiThread(new Runnable() {
        

            public final void run() {
                ListSongActivity.this.onAudioLoadedSuccessful0(arrayList);
            }
        });
    }

    public  void onAudioLoadedSuccessful0(ArrayList arrayList) {
        this.lstAudio.clear();
        this.lstAudio.addAll(arrayList);
        this.adapter.setData(this.lstAudio);
        this.lstAudio.size();
        Button button = this.btnPlayAll;
        StringBuilder sb = new StringBuilder();
        sb.append(getString(R.string.txt_play_all));
        sb.append(" (");
        sb.append(this.lstAudio.size());
        sb.append(")");
        button.setText(sb.toString());
    }

    public void onItemSongClick(ArrayList<Song> arrayList, int i) {
        if (this.musicPlayerService != null && SystemClock.elapsedRealtime() - this.mLastClickTime >= 1000) {
            this.musicPlayerService.setListMusic(this.lstAudio, i);
            this.musicPlayerService.setSongPos(i);
            this.musicPlayerService.stopSong();
            Intent intent = new Intent(this, MusicPlayerService.class);
            intent.setAction("com.eskaylation.downloadmusic.ACTION.SETDATAOFFLINEPLAYER");
            startService(intent);
            startActivity(new Intent(this, PlayerActivity.class).setFlags( 0x10000000));
            this.mLastClickTime = SystemClock.elapsedRealtime();
        }
    }

    public void onMoreClick(Song song, int i) {
        if (this.musicPlayerService.getListAudio().isEmpty()) {
            Favorite favorite = this.playlist;
            if (favorite != null) {
                this.dialogMore.showDialogMore(song, false, favorite, false);
            } else {
                this.dialogMore.showDialogMore(song, false, null, false);
            }
        } else if (song.getmSongPath().equals(this.musicPlayerService.getItemIndex().getmSongPath())) {
            Favorite favorite2 = this.playlist;
            if (favorite2 != null) {
                this.dialogMore.showDialogMore(song, true, favorite2, false);
            } else {
                this.dialogMore.showDialogMore(song, true, null, false);
            }
        } else {
            Favorite favorite3 = this.playlist;
            if (favorite3 != null) {
                this.dialogMore.showDialogMore(song, false, favorite3, true);
            } else {
                this.dialogMore.showDialogMore(song, false, null, true);
            }
        }
    }

    public String[] argument(long j) {
        return new String[]{String.valueOf(j)};
    }

    public String getSortPlaylist() {
        int i = this.pref.getInt("SORT_TYPE", 999);
        int i2 = this.pref.getInt("SORT_ODER", 222);
        if (i == 1 && i2 == 111) {
            return "YEAR DESC";
        }
        if (i == 1 && i2 == 222) {
            return "YEAR ASC";
        }
        return (i == 999 && i2 == 111) ? "TITLE DESC" : "TITLE ASC";
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

    public void onDeleteSongSuccessFull(Song song) {
        if (this.lstAudio.contains(song)) {
            SongListDao songListDao2 = this.songListDao;
            if (songListDao2 != null) {
                songListDao2.deleteFavoriteSong(song);
            }
            setDataIntent();
        }
    }

    public void onDeleteSongFromPlaylist(Song song) {
        this.lstAudio.remove(song);
        SongLightAdapter songLightAdapter = this.adapter;
        songLightAdapter.removeAt(songLightAdapter.getPositionFromName(song));
        if (this.isLoveSong) {
            this.musicPlayerService.refreshLoveSong();
        }
        this.lstAudio.size();
        Button button = this.btnPlayAll;
        StringBuilder sb = new StringBuilder();
        sb.append(getString(R.string.txt_play_all));
        sb.append(" (");
        sb.append(this.lstAudio.size());
        sb.append(")");
        button.setText(sb.toString());
    }

    public void onAddToPlayNext(Song song) {
        this.musicPlayerService.addSongToNext(song);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void closeDialog(CloseDialog closeDialog) {
        if (closeDialog.isClose) {
            DialogMoreSongUtil dialogMoreSongUtil = this.dialogMore;
            if (dialogMoreSongUtil != null) {
                dialogMoreSongUtil.closeDialog();
            }
            EventBus.getDefault().postSticky(new CloseDialog(false));
        }
    }
}
