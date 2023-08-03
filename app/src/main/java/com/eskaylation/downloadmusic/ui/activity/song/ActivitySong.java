package com.eskaylation.downloadmusic.ui.activity.song;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.eskaylation.downloadmusic.adapter.SongAdapter;
import com.eskaylation.downloadmusic.base.BaseActivityLoader;
import com.eskaylation.downloadmusic.bus.CloseDialog;
import com.eskaylation.downloadmusic.bus.NotifyDeleteMusic;
import com.eskaylation.downloadmusic.listener.OnItemSongClickListener;
import com.eskaylation.downloadmusic.listener.SearchListenner;
import com.eskaylation.downloadmusic.listener.SongListenner;
import com.eskaylation.downloadmusic.loader.SearchLoader;
import com.eskaylation.downloadmusic.loader.TrackLoader;
import com.eskaylation.downloadmusic.model.AdsManager;
import com.eskaylation.downloadmusic.model.Favorite;
import com.eskaylation.downloadmusic.model.Song;
import com.eskaylation.downloadmusic.service.MusicPlayerService;
import com.eskaylation.downloadmusic.service.MusicPlayerService.MusicServiceBinder;
import com.eskaylation.downloadmusic.ui.activity.ListSongActivity;
import com.eskaylation.downloadmusic.ui.activity.PlayerActivity;

import com.eskaylation.downloadmusic.ui.dialog.DialogFavorite;
import com.eskaylation.downloadmusic.ui.dialog.DialogFavoriteListener;
import com.eskaylation.downloadmusic.ui.dialog.DialogMoreListener;
import com.eskaylation.downloadmusic.ui.dialog.DialogMoreSongUtil;



import com.eskaylation.downloadmusic.utils.PreferenceUtils;
import com.eskaylation.downloadmusic.utils.ToastUtils;
import com.google.android.exoplayer2.C;
import com.wang.avi.AVLoadingIndicatorView;
import java.util.ArrayList;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import com.eskaylation.downloadmusic.R;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;



public class ActivitySong extends BaseActivityLoader implements OnItemSongClickListener, SongListenner, DialogMoreListener, DialogFavoriteListener {
  @BindView(R.id.ad_view_container)
    public FrameLayout adView;
    public SongAdapter adapter;
    @BindView(R.id.btnBack)
    public ImageButton btnBack;
    @BindView(R.id.btnPlayAll)
    public Button btnPlayAll;

    public ServiceConnection connection = new ServiceConnection() {
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            ActivitySong.this.musicPlayerService = ((MusicServiceBinder) iBinder).getService();
            ActivitySong.this.musicPlayerService.setAdapterControlFrgSong(ActivitySong.this.adapter, ActivitySong.this.rv_song);
            ActivitySong.this.musicPlayerService.initAdapterControlFrgSong();
            ActivitySong.this.mBound = true;
        }

        public void onServiceDisconnected(ComponentName componentName) {
            ActivitySong.this.mBound = false;
        }
    };

    @BindView(R.id.coordinator)
    public CoordinatorLayout coordinator;
    public DialogFavorite dialogFavorite;
    public DialogMoreSongUtil dialogMoreSong;
    @BindView(R.id.edtSearch)
    public EditText edtSearch;
    public ArrayList<Song> lstMusic;
    public boolean mBound = false;
    public Window mWindow;
    public MusicPlayerService musicPlayerService;
    @BindView(R.id.progress_loading)
    public AVLoadingIndicatorView progressBar;
    @BindView(R.id.rvSearch)
    public RecyclerView rvSearch;
    @BindView(R.id.rv_song)
    public RecyclerView rv_song;
    public SongAdapter searchAdapter;
    public SearchLoader searchLoader;
    public Thread t;
    public TrackLoader trackLoader;
    @BindView(R.id.tvEmpty)
    public TextView tvEmptySearch;

    public void deletePlaylistDone(int i) {
    }

    public void onCreateNewPlaylist(Favorite favorite) {
    }

    public void onDeleteSongFromPlaylist(Song song) {
    }

    public void onOpenAddSong(Favorite favorite) {
    }

    public void onUpdatePlaylist(int i, Favorite favorite) {
    }

    public void setRingtone(Song song) {
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.fragment_song);
        this.mWindow = getWindow();
        this.mWindow.getDecorView().setSystemUiVisibility(1280);
        init();

        btnPlayAll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onListenAll();
            }
        });
    }

    public void onDestroy() {
        super.onDestroy();
        unbindServicePlayMusic();
    }

    public void init() {
        ButterKnife.bind(this);
        setBackgroundThemes(this.coordinator, PreferenceUtils.getInstance(this).getThemesPosition());
        this.btnBack.setOnClickListener(new OnClickListener() {
            public final void onClick(View view) {
                ActivitySong.this.lambda$init$0$ActivitySong(view);
            }
        });
        this.lstMusic = new ArrayList<>();
        this.adapter = new SongAdapter(this, this.lstMusic, this);
        this.rv_song.setLayoutManager(new LinearLayoutManager(this));
        this.rv_song.setHasFixedSize(true);
        this.rv_song.setAdapter(this.adapter);
        loader();
        this.dialogMoreSong = new DialogMoreSongUtil(this, this, false);
        initSearch();
        loadBanner(this,adView);
    }

    public  void lambda$init$0$ActivitySong(View view) {
        onBackPressed();
    }

    public void onListenAll() {
        ArrayList<Song> arrayList = this.lstMusic;
        if (arrayList == null || arrayList.size() <= 0) {
            ToastUtils.error((Context) this, getString(R.string.no_song_to_play));
        } else {
            ActivitySong.this.playSong();

           /*
            AdsManager.showNext(this, new AdsManager.AdCloseListener() {
                @Override
                public void onAdClosed() {

                }
            });

            */
            

    }}

    public void playSong() {
        MusicPlayerService musicPlayerService2 = this.musicPlayerService;
        if (musicPlayerService2 != null) {
            musicPlayerService2.setListMusic(this.lstMusic, 0);
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

    public void onResume() {
        super.onResume();
    }

    public void initSearch() {
        this.searchLoader = new SearchLoader(new SearchListenner() {
            public final void onAudioSearchSuccessful(ArrayList arrayList) {
                ActivitySong.this.lambda$initSearch$1$ActivitySong(arrayList);
            }
        }, this);
        this.searchLoader.setSortOrder("title_key");
        this.edtSearch.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (charSequence.length() > 0) {
                    ActivitySong.this.rv_song.setVisibility(GONE);
                    ActivitySong.this.rvSearch.setVisibility(VISIBLE);
                    ActivitySong.this.searchLoader.setSearchName(ActivitySong.this.edtSearch.getText().toString().trim());
                    ActivitySong.this.t = new Thread(new Runnable() {
                        public final void run() {

                            ActivitySong.this.searchLoader.loadInBackground();
                        }

                    });
                    ActivitySong.this.t.start();
                    ActivitySong.this.btnPlayAll.setVisibility(GONE);
                    return;
                }
                ActivitySong.this.rvSearch.setVisibility(INVISIBLE);
                ActivitySong.this.tvEmptySearch.setVisibility(GONE);
                ActivitySong.this.rv_song.setVisibility(VISIBLE);
                ActivitySong.this.btnPlayAll.setVisibility(VISIBLE);
            }


        });
        this.edtSearch.setOnEditorActionListener(new OnEditorActionListener() {
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                return i == 6;
            }
        });
    }


    public void lambda$initSearch$1$ActivitySong(final ArrayList<Song> arrayList) {
        runOnUiThread(new Runnable() {
            public void run() {
                if (arrayList.size() > 0) {
                    ActivitySong.this.rvSearch.setVisibility(VISIBLE);
                    ActivitySong.this.tvEmptySearch.setVisibility(GONE);
                    ActivitySong.this.btnPlayAll.setVisibility(VISIBLE);
                    ActivitySong.this.btnPlayAll.setVisibility(GONE);
                } else {
                    ActivitySong.this.rvSearch.setVisibility(INVISIBLE);
                    ActivitySong.this.tvEmptySearch.setVisibility(VISIBLE);
                    ActivitySong.this.btnPlayAll.setVisibility(GONE);
                }
                ActivitySong activitySong = ActivitySong.this;
                activitySong.searchAdapter = new SongAdapter(activitySong, arrayList, new OnItemSongClickListener() {
                    public void onItemSongClick(ArrayList<Song> arrayList, int i) {
                        if (ActivitySong.this.musicPlayerService != null) {
                            ActivitySong.this.musicPlayerService.setListMusic(arrayList, i);
                            ActivitySong.this.musicPlayerService.setSongPos(i);
                            ActivitySong.this.musicPlayerService.stopSong();
                            Intent intent = new Intent(ActivitySong.this, MusicPlayerService.class);
                            intent.setAction("com.eskaylation.downloadmusic.ACTION.SETDATAOFFLINEPLAYER");
                            ActivitySong.this.startService(intent);
                            ActivitySong activitySong = ActivitySong.this;
                            activitySong.startActivity(new Intent(activitySong, PlayerActivity.class));
                            ActivitySong.this.finish();
                            ActivitySong.this.onCloseSearch();
                        }
                    }

                    public void onMoreClick(Song song, int i) {
                        if (ActivitySong.this.musicPlayerService.getListAudio().isEmpty()) {
                            ActivitySong.this.dialogMoreSong.showDialogMore(song, false, null, false);
                        } else if (song.getmSongPath().equals(ActivitySong.this.musicPlayerService.getItemIndex().getmSongPath())) {
                            ActivitySong.this.dialogMoreSong.showDialogMore(song, true, null, false);
                        } else {
                            ActivitySong.this.dialogMoreSong.showDialogMore(song, false, null, true);
                        }
                    }
                });
                ActivitySong.this.rvSearch.setHasFixedSize(true);
                ActivitySong activitySong2 = ActivitySong.this;
                activitySong2.rvSearch.setLayoutManager(new LinearLayoutManager(activitySong2));
                ActivitySong activitySong3 = ActivitySong.this;
                activitySong3.rvSearch.setAdapter(activitySong3.searchAdapter);
            }
        });
    }


    public void onCloseSearch() {
        if (this.rvSearch.getVisibility() == VISIBLE) {
            hideKeyboard();
            this.btnPlayAll.setVisibility(VISIBLE);
            this.edtSearch.setText("");
            this.tvEmptySearch.setVisibility(GONE);
            this.rvSearch.setVisibility(INVISIBLE);
            closeKeyboard();
        }
    }

    public void closeKeyboard() {
        ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(this.edtSearch.getWindowToken(), 0);
    }





    public void loader() {
        AVLoadingIndicatorView aVLoadingIndicatorView = this.progressBar;
        if (aVLoadingIndicatorView != null) {
            aVLoadingIndicatorView.setVisibility(VISIBLE);
            this.progressBar.show();
        }
        this.trackLoader = new TrackLoader(this, this);
        this.trackLoader.setSortOrder("title_key");
        this.t = new Thread(new Runnable() {
            public final void run() {
                ActivitySong.this.lambda$loader$2$ActivitySong();
            }
        });
        this.t.start();
    }
    public  void lambda$loader$2$ActivitySong() {
        this.trackLoader.loadInBackground();
    }
    public void onAudioLoadedSuccessful(ArrayList<Song> arrayList) {
        this.t = null;
        runOnUiThread(new Runnable() {
        

            public final void run() {
                ActivitySong.this.lambda$onAudioLoadedSuccessful$3$ActivitySong(arrayList);
            }
        });
    }
    public  void lambda$onAudioLoadedSuccessful$3$ActivitySong(ArrayList arrayList) {
        this.lstMusic = arrayList;
        this.adapter.setListItem(arrayList);
        this.progressBar.setVisibility(GONE);
        this.progressBar.hide();
        bindService();
        Button button = this.btnPlayAll;
        StringBuilder sb = new StringBuilder();
        sb.append(getString(R.string.txt_play_all));
        sb.append(" (");
        sb.append(arrayList.size());
        sb.append(")");
        button.setText(sb.toString());
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
    public void onItemSongClick(final ArrayList<Song> arrayList, final int i) {
        itemSongPlay(arrayList, i);

        /*
        AdsManager.showNext(this, new AdsManager.AdCloseListener() {
            @Override
            public void onAdClosed() {

            }
        });

         */

    }
    public void itemSongPlay(ArrayList<Song> arrayList, int i) {
        MusicPlayerService musicPlayerService2 = this.musicPlayerService;
        if (musicPlayerService2 != null) {
            musicPlayerService2.setListMusic(arrayList, i);
            this.musicPlayerService.setSongPos(i);
            this.musicPlayerService.stopSong();
            Intent intent = new Intent(this, MusicPlayerService.class);
            intent.setAction("com.eskaylation.downloadmusic.ACTION.SETDATAOFFLINEPLAYER");
            startService(intent);
            startActivity(new Intent(this, PlayerActivity.class));
        }
    }





    public void onMoreClick(Song song, int i) {
        MusicPlayerService musicPlayerService2 = this.musicPlayerService;
        if (musicPlayerService2 != null) {
            if (musicPlayerService2.getListAudio().isEmpty()) {
                this.dialogMoreSong.showDialogMore(song, false, null, false);
            } else if (song.getmSongPath().equals(this.musicPlayerService.getItemIndex().getmSongPath())) {
                this.dialogMoreSong.showDialogMore(song, true, null, false);
            } else {
                this.dialogMoreSong.showDialogMore(song, false, null, true);
            }
        }
    }
    public void onNeedPermisionWriteSetting(Song song) {
        Intent intent = new Intent("android.settings.action.MANAGE_WRITE_SETTINGS");
        StringBuilder sb = new StringBuilder();
        sb.append("package:");
        sb.append(getPackageName());
        intent.setData(Uri.parse(sb.toString()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        this.ringtone = song;

    }
    @SuppressLint({"SetTextI18n"})
    public void onDeleteSongSuccessFull(Song song) {
        if (this.lstMusic.contains(song)) {
            SongAdapter songAdapter = this.adapter;
            songAdapter.removeAt(songAdapter.getPositionFromSong(song));
            Button button = this.btnPlayAll;
            StringBuilder sb = new StringBuilder();
            sb.append(getString(R.string.txt_play_all));
            sb.append(" (");
            sb.append(this.adapter.getListSong().size());
            sb.append(")");
            button.setText(sb.toString());
        }
    }
    public void onAddToPlayNext(Song song) {
        this.musicPlayerService.addSongToNext(song);
    }
    public void onCreatePlaylistFromDialog(Song song) {
        this.dialogFavorite.showDialogAddSong(song, false);
    }





    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onDeleteSong(NotifyDeleteMusic notifyDeleteMusic) {
        Song song = notifyDeleteMusic.song;
        for (int i = 0; i < this.lstMusic.size() - 1; i++) {
            Song song2 = (Song) this.lstMusic.get(i);
            if (song2.getmSongPath().equals(song.getmSongPath())) {
                SongAdapter songAdapter = this.adapter;
                songAdapter.removeAt(songAdapter.getPositionFromSong(song2));
                Button button = this.btnPlayAll;
                StringBuilder sb = new StringBuilder();
                sb.append(getString(R.string.txt_play_all));
                sb.append(" (");
                sb.append(this.adapter.getListSong().size());
                sb.append(")");
                button.setText(sb.toString());
            }
        }
    }


    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void closeDialog(CloseDialog closeDialog) {
        if (closeDialog.isClose) {
            DialogMoreSongUtil dialogMoreSongUtil = this.dialogMoreSong;
            if (dialogMoreSongUtil != null) {
                dialogMoreSongUtil.closeDialog();
            }
            EventBus.getDefault().postSticky(new CloseDialog(false));
        }
    }

}
