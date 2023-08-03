package com.eskaylation.downloadmusic.ui.activity.addsong;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.eskaylation.downloadmusic.R;
import com.eskaylation.downloadmusic.adapter.AddSongAdapter;
import com.eskaylation.downloadmusic.base.BaseActivityLoader;
import com.eskaylation.downloadmusic.database.SongListDao;
import com.eskaylation.downloadmusic.database.SongListSqliteHelper;
import com.eskaylation.downloadmusic.listener.SongListenner;
import com.eskaylation.downloadmusic.loader.TrackLoader;
import com.eskaylation.downloadmusic.model.Favorite;
import com.eskaylation.downloadmusic.model.Song;
import com.eskaylation.downloadmusic.utils.PreferenceUtils;
import com.wang.avi.AVLoadingIndicatorView;
import java.util.ArrayList;

public class ActivityAddSong extends BaseActivityLoader {
    @BindView(R.id.btnDone)
    public ImageButton btnDone;

    @BindView(R.id.btnBack)
    public ImageButton btnBack;
    @BindView(R.id.coordinator)
    public CoordinatorLayout coordinator;
    @BindView(R.id.emptyView)
    public TextView emptyView;
    public Favorite favorite;
    public ArrayList<Song> lstFavorite = new ArrayList<>();
    public ArrayList<Song> lstSong = new ArrayList<>();
    public AddSongAdapter mAddSongAdapter;
    public Window mWindow;

    @BindView(R.id.progress_bar)
    public AVLoadingIndicatorView progressBar;
    @BindView(R.id.rv_song)
    public RecyclerView rvSong;
    public SongListDao songListDao;
    public SongListSqliteHelper sqliteHelper;
    public Thread t;
    public TrackLoader trackLoader;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_add_song);
        this.mWindow = getWindow();
        this.mWindow.getDecorView().setSystemUiVisibility(1280);
        init();
    }

    public void init() {
        ButterKnife.bind(this);
        setBackgroundThemes(this.coordinator, PreferenceUtils.getInstance(this).getThemesPosition());
        this.favorite = (Favorite) getIntent().getParcelableExtra("Favorite_data");
        this.sqliteHelper = new SongListSqliteHelper(this, this.favorite.favorite_id);
        this.songListDao = new SongListDao(this.sqliteHelper);
        this.lstFavorite = this.songListDao.getAllFavoriteSong();
        initAdapter();
        loader();
        this.btnDone.setOnClickListener(new OnClickListener() {
            public final void onClick(View view) {
                ActivityAddSong.this.lambda$init$0$ActivityAddSong(view);
            }
        });
        btnBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickBack();
            }
        });
    }

    public  void lambda$init$0$ActivityAddSong(View view) {
        ArrayList arrayList = new ArrayList();
        arrayList.clear();
        arrayList.addAll(this.mAddSongAdapter.getListSelect());
        this.songListDao.insertListFavoriteSong(arrayList);
        finish();
    }

    public void onClickBack() {
        onBackPressed();
    }

    public void loader() {
        AVLoadingIndicatorView aVLoadingIndicatorView = this.progressBar;
        if (aVLoadingIndicatorView != null) {
            aVLoadingIndicatorView.setVisibility(View.VISIBLE);
            this.progressBar.show();
        }
        this.trackLoader = new TrackLoader(new SongListenner() {
            public final void onAudioLoadedSuccessful(ArrayList arrayList) {
                ActivityAddSong.this.lambda$loader$2$ActivityAddSong(arrayList);
            }
        }, this);
        this.trackLoader.setSortOrder("title_key");
        this.t = new Thread(new Runnable() {
            public final void run() {
                ActivityAddSong.this.lambda$loader$3$ActivityAddSong();
            }
        });
        this.t.start();
    }

    public  void lambda$loader$2$ActivityAddSong(ArrayList arrayList) {
        this.t = null;
        runOnUiThread(new Runnable() {


            public final void run() {
                ActivityAddSong.this.ActivityAddSong1(arrayList);
            }
        });
    }

    public  void ActivityAddSong1(ArrayList arrayList) {
        this.lstSong.clear();
        this.lstSong.addAll(arrayList);
        this.mAddSongAdapter.setData(this.lstSong);
        this.mAddSongAdapter.initFavoriteData(this.lstFavorite);
        this.progressBar.setVisibility(View.GONE);
        if (arrayList.size() > 0) {
            this.emptyView.setVisibility(View.GONE);
        } else {
            this.emptyView.setVisibility(View.VISIBLE);
        }
    }

    public  void lambda$loader$3$ActivityAddSong() {
        this.trackLoader.loadInBackground();
    }

    public void initAdapter() {
        this.mAddSongAdapter = new AddSongAdapter(this);
        this.rvSong.setLayoutManager(new LinearLayoutManager(this));
        this.rvSong.setHasFixedSize(true);
        this.rvSong.setAdapter(this.mAddSongAdapter);
    }
}
