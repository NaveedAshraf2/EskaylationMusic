package com.eskaylation.downloadmusic.ui.activity.artist;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import com.eskaylation.downloadmusic.R;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.eskaylation.downloadmusic.adapter.ArtistAdapter;
import com.eskaylation.downloadmusic.adapter.ArtistAdapter.OnItemArtistClickListener;
import com.eskaylation.downloadmusic.base.BaseActivityLoader;
import com.eskaylation.downloadmusic.bus.NotifyDeleteMusic;
import com.eskaylation.downloadmusic.listener.ArtistListenner;
import com.eskaylation.downloadmusic.loader.ArtistLoader;
import com.eskaylation.downloadmusic.model.AdsManager;
import com.eskaylation.downloadmusic.model.Artist;
import com.eskaylation.downloadmusic.model.Song;
import com.eskaylation.downloadmusic.ui.activity.ListSongActivity;



import com.eskaylation.downloadmusic.utils.PreferenceUtils;
import com.eskaylation.downloadmusic.widget.GridSpacingItemDecoration;
import java.util.ArrayList;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class ActivityArtist extends BaseActivityLoader implements OnItemArtistClickListener, ArtistListenner {
   @BindView(R.id.ad_view_container)
    public FrameLayout adView;
    public ArtistAdapter adapter;
    public ArtistLoader artistLoader;
    @BindView(R.id.coordinator)
    public CoordinatorLayout coordinator;
    public ArrayList<Artist> lstArtist;
    public long mLastClickTime;
    @BindView(R.id.toolbar)
    public Toolbar mToolbar;
    public Window mWindow;
    @BindView(R.id.rv_artist)
    public RecyclerView rv_artist;
    public Thread t;

    public void setRingtone(Song song) {
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().setNavigationBarColor(-16777216);
        setContentView((int) R.layout.fragment_artist);
        this.mWindow = getWindow();
        this.mWindow.getDecorView().setSystemUiVisibility(1280);
        init();
        loadBanner(this,adView);

        this.mToolbar.setNavigationOnClickListener(new OnClickListener() {
            public final void onClick(View view) {
                ActivityArtist.this.ActivityArtist0(view);
            }
        });
    }

    public  void ActivityArtist0(View view) {
        super.onBackPressed();
    }

    public void init() {
        ButterKnife.bind(this);
        setBackgroundThemes(this.coordinator, PreferenceUtils.getInstance(this).getThemesPosition());
        this.artistLoader = new ArtistLoader(this, this);
        this.rv_artist.addItemDecoration(new GridSpacingItemDecoration(2, 16, true));
        this.rv_artist.setLayoutManager(new GridLayoutManager(this, 2));
        this.adapter = new ArtistAdapter(this, this);
        this.rv_artist.setHasFixedSize(true);
        this.rv_artist.setAdapter(this.adapter);
    }

    public void onResume() {
        super.onResume();
        loader();
    }

    public void loader() {
        this.artistLoader.setSortOrder("artist_key");
        this.t = new Thread(new Runnable() {
            public final void run() {
                ActivityArtist.this.ActivityArtist1();
            }
        });
        this.t.start();
    }

    public  void ActivityArtist1() {
        this.artistLoader.loadInBackground();
    }

    public void onArtistClick(int i, final Artist artist, ImageView imageView, TextView textView, TextView textView2) {
        if (SystemClock.elapsedRealtime() - this.mLastClickTime >= 1000) {

            AdsManager.showNext(this, new AdsManager.AdCloseListener() {
                @Override
                public void onAdClosed() {
                    Intent intent = new Intent(ActivityArtist.this, ListSongActivity.class);
                    intent.putExtra("artist_data", artist);
                    ActivityArtist.this.startActivity(intent);
                }
            });

            this.mLastClickTime = SystemClock.elapsedRealtime();
        }
    }

    public void onLoadArtistSuccessful(ArrayList<Artist> arrayList) {
        this.t = null;
        this.lstArtist = arrayList;
        runOnUiThread(new Runnable() {
            public final void run() {
                ActivityArtist.this.ActivityArtist2();
            }
        });
    }

    public  void ActivityArtist2() {
        this.adapter.setData(this.lstArtist);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onDeleteSong(NotifyDeleteMusic notifyDeleteMusic) {
        loader();
    }
}
