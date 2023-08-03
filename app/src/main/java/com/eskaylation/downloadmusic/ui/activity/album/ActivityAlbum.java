package com.eskaylation.downloadmusic.ui.activity.album;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import com.eskaylation.downloadmusic.R;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.eskaylation.downloadmusic.adapter.AlbumAdapter;
import com.eskaylation.downloadmusic.adapter.AlbumAdapter.OnItemAlbumClickListener;
import com.eskaylation.downloadmusic.base.BaseActivityLoader;
import com.eskaylation.downloadmusic.base.BaseLoaderFragment;
import com.eskaylation.downloadmusic.bus.NotifyDeleteMusic;
import com.eskaylation.downloadmusic.listener.AlbumListenner;
import com.eskaylation.downloadmusic.loader.AlbumLoader;
import com.eskaylation.downloadmusic.model.AdsManager;
import com.eskaylation.downloadmusic.model.Album;
import com.eskaylation.downloadmusic.model.Song;
import com.eskaylation.downloadmusic.ui.activity.ListSongActivity;



import com.eskaylation.downloadmusic.utils.PreferenceUtils;
import com.eskaylation.downloadmusic.widget.GridSpacingItemDecoration;
import java.util.ArrayList;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class ActivityAlbum extends BaseActivityLoader implements OnItemAlbumClickListener, AlbumListenner, BaseLoaderFragment {
    @BindView(R.id.ad_view_container)
    public FrameLayout adView;
    public AlbumLoader albumsLoader;
    @BindView(R.id.btnBack)
    public ImageButton btnBack;
    @BindView(R.id.coordinator)
    public CoordinatorLayout coordinator;
    public AlbumAdapter gridAdapter;
    public ArrayList<Album> lstAlbum;
    public long mLastClickTime;
    public Window mWindow;
    @BindView(R.id.rv_album)
    public RecyclerView rv_album;
    public Thread t;

    public void setRingtone(Song song) {
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.fragment_album);
        this.mWindow = getWindow();
        this.mWindow.getDecorView().setSystemUiVisibility(1280);
        init();
        loadBanner(this,adView);
        this.btnBack.setOnClickListener(new OnClickListener() {
            public final void onClick(View view) {
                ActivityAlbum.this.lambda$onCreate$0$ActivityAlbum(view);
            }
        });
    }

    public  void lambda$onCreate$0$ActivityAlbum(View view) {
        onBackPressed();
    }

    public void init() {
        ButterKnife.bind(this);
        setBackgroundThemes(this.coordinator, PreferenceUtils.getInstance(this).getThemesPosition());
        this.gridAdapter = new AlbumAdapter(this, this);
        this.rv_album.addItemDecoration(new GridSpacingItemDecoration(2, 16, true));
        this.rv_album.setLayoutManager(new GridLayoutManager(this, 2));
        this.rv_album.setHasFixedSize(true);
        this.rv_album.setAdapter(this.gridAdapter);
    }

    public void onResume() {
        super.onResume();
        loader();
    }

    public void onItemClick(final int i, final Album album) {
        if (SystemClock.elapsedRealtime() - this.mLastClickTime >= 1000) {

            AdsManager.showNext(this, new AdsManager.AdCloseListener() {
                @Override
                public void onAdClosed() {
                    Intent intent = new Intent(ActivityAlbum.this, ListSongActivity.class);
                    intent.putExtra("album_data", album);
                    intent.putExtra("position_intent", i);
                    ActivityAlbum.this.startActivity(intent);
                }
            });
            


            this.mLastClickTime = SystemClock.elapsedRealtime();
        }
    }

    public void loader() {
        this.albumsLoader = new AlbumLoader(this, this);
        this.albumsLoader.setSortOrder("album_key");
        this.albumsLoader.filterartistsong(null, null);
        this.t = new Thread(new Runnable() {
            public final void run() {
                ActivityAlbum.this.lambda$loader$1$ActivityAlbum();
            }
        });
        this.t.start();
    }

    public  void lambda$loader$1$ActivityAlbum() {
        this.albumsLoader.loadInBackground();
    }

    @SuppressLint({"SetTextI18n"})
    public void onAlbumLoadedSuccessful(ArrayList<Album> arrayList) {
        this.t = null;
        this.lstAlbum = arrayList;
        runOnUiThread(new Runnable() {
            public final void run() {
                ActivityAlbum.this.onAlbumload2();
            }
        });
    }

    public  void onAlbumload2() {
        this.gridAdapter.setData(this.lstAlbum);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onDeleteSong(NotifyDeleteMusic notifyDeleteMusic) {
        loader();
    }
}
