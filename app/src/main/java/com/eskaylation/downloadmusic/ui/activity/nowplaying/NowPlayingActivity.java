package com.eskaylation.downloadmusic.ui.activity.nowplaying;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.eskaylation.downloadmusic.R;
import com.eskaylation.downloadmusic.adapter.NowPlayingAdapter;
import com.eskaylation.downloadmusic.base.BaseActivity;
import com.eskaylation.downloadmusic.listener.OnItemNowPlayingOnlineClick;
import com.eskaylation.downloadmusic.listener.OnItemSongClickListener;
import com.eskaylation.downloadmusic.model.AdsManager;
import com.eskaylation.downloadmusic.model.AudioExtract;
import com.eskaylation.downloadmusic.model.Song;
import com.eskaylation.downloadmusic.service.MusicPlayerService;
import com.eskaylation.downloadmusic.service.MusicPlayerService.MusicServiceBinder;
import com.eskaylation.downloadmusic.ui.activity.PlayerActivity;



import com.eskaylation.downloadmusic.utils.PreferenceUtils;
import com.eskaylation.downloadmusic.widget.DragSortRecycler;
import com.eskaylation.downloadmusic.widget.DragSortRecycler.OnItemMovedListener;
import java.util.ArrayList;

public class NowPlayingActivity extends BaseActivity {
    @BindView(R.id.ad_view_container)
    public FrameLayout adView;
    public NowPlayingAdapter adapter;
    public ServiceConnection connection = new ServiceConnection() {
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            NowPlayingActivity.this.musicPlayerService = ((MusicServiceBinder) iBinder).getService();
            NowPlayingActivity.this.mBound = true;
            MusicPlayerService access$000 = NowPlayingActivity.this.musicPlayerService;
            NowPlayingActivity nowPlayingActivity = NowPlayingActivity.this;
            access$000.setUiNowPlayingActivity(nowPlayingActivity.rvNowPlaying, nowPlayingActivity.adapter, NowPlayingActivity.this.viewLoading);
            NowPlayingActivity.this.musicPlayerService.initDataNowplaying();
        }

        public void onServiceDisconnected(ComponentName componentName) {
            NowPlayingActivity.this.mBound = false;
        }
    };
    @BindView(R.id.btnBack)
    ImageButton btnBack;
    @BindView(R.id.coordinator)
    public CoordinatorLayout coordinator;
    public LinearLayoutManager llManager;
    public boolean mBound = false;
    public MusicPlayerService musicPlayerService;
    @BindView(R.id.rv_nowPlaying)
    public RecyclerView rvNowPlaying;
    @BindView(R.id.viewLoading)
    public LinearLayout viewLoading;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_now_playing);
        init();
        loadBanner(NowPlayingActivity.this,adView);
    }

    public void init() {
        ButterKnife.bind(this);
        setBackgroundThemes(this.coordinator, PreferenceUtils.getInstance(this).getThemesPosition());
        setDataBind();
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick1();
            }
        });
    }

    public void onResume() {
        super.onResume();
        bindService();
    }

    public void onStop() {
        super.onStop();
        unbindServicePlayMusic();
    }

    public void onClick1() {


        AdsManager.showNext(this, new AdsManager.AdCloseListener() {
            @Override
            public void onAdClosed() {
                onBackPressed();
            }
        });

    }

    public void setDataBind() {
        this.adapter = new NowPlayingAdapter(this, new OnItemSongClickListener() {
            public void onMoreClick(Song song, int i) {
            }

            public void onItemSongClick(ArrayList<Song> arrayList, int i) {
                if (NowPlayingActivity.this.musicPlayerService.isPlayOnline()) {
                    Intent intent = new Intent(NowPlayingActivity.this, MusicPlayerService.class);
                    String str = "com.eskaylation.downloadmusic.ACTION.SETDATAONLINEPLAYER";
                    intent.setAction(str);
                    intent.putExtra(str, (Parcelable) arrayList.get(i));
                    NowPlayingActivity.this.startService(intent);
                    NowPlayingActivity nowPlayingActivity = NowPlayingActivity.this;
                    nowPlayingActivity.startActivity(new Intent(nowPlayingActivity, PlayerActivity.class));
                    return;
                }
                NowPlayingActivity.this.musicPlayerService.setListMusic(arrayList, i);
                NowPlayingActivity.this.musicPlayerService.stopSong();
                Intent intent2 = new Intent(NowPlayingActivity.this, MusicPlayerService.class);
                intent2.setAction("com.eskaylation.downloadmusic.ACTION.SETDATAOFFLINEPLAYER");
                NowPlayingActivity.this.startService(intent2);
            }
        }, new OnItemNowPlayingOnlineClick() {
            public final void onItemNowPlayingOnlineClick(AudioExtract audioExtract) {
                NowPlayingActivity.this.NowPlayingActivity0(audioExtract);
            }
        });
        this.llManager = new LinearLayoutManager(this);
        this.rvNowPlaying.setLayoutManager(this.llManager);
        this.rvNowPlaying.setHasFixedSize(true);
        this.rvNowPlaying.setAdapter(this.adapter);
        this.rvNowPlaying.setItemAnimator(null);
        DragSortRecycler dragSortRecycler = new DragSortRecycler();
        dragSortRecycler.setViewHandleId(R.id.btnDrag);
        dragSortRecycler.setOnItemMovedListener(new OnItemMovedListener() {
            public final void onItemMoved(int i, int i2) {
                NowPlayingActivity.this.NowPlayingActivity1(i, i2);
            }
        });
        this.rvNowPlaying.addItemDecoration(dragSortRecycler);
        this.rvNowPlaying.addOnItemTouchListener(dragSortRecycler);
        this.rvNowPlaying.setOnScrollListener(dragSortRecycler.getScrollListener());
    }

    public  void NowPlayingActivity0(AudioExtract audioExtract) {
        Intent intent = new Intent(this, MusicPlayerService.class);
        String str = "com.eskaylation.downloadmusic.ACTION.SETDATAONLINEPLAYER";
        intent.setAction(str);
        intent.putExtra(str, audioExtract);
        startService(intent);
    }

    public  void NowPlayingActivity1(int i, int i2) {
        this.musicPlayerService.moveSongItem(i, i2);
    }

    public final void bindService() {
        bindService(new Intent(this, MusicPlayerService.class), this.connection, Context.BIND_AUTO_CREATE);
    }

    public final void unbindServicePlayMusic() {
        if (this.mBound) {
            try {
                unbindService(this.connection);
            } catch (Exception unused) {
            }
        }
    }
}
