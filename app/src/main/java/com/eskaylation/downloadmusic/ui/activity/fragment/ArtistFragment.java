package com.eskaylation.downloadmusic.ui.activity.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eskaylation.downloadmusic.R;
import com.eskaylation.downloadmusic.adapter.ArtistAdapter;
import com.eskaylation.downloadmusic.base.BaseActivity;
import com.eskaylation.downloadmusic.bus.NotifyDeleteMusic;
import com.eskaylation.downloadmusic.listener.ArtistListenner;
import com.eskaylation.downloadmusic.loader.ArtistLoader;
import com.eskaylation.downloadmusic.model.AdsManager;
import com.eskaylation.downloadmusic.model.Artist;
import com.eskaylation.downloadmusic.model.Song;
import com.eskaylation.downloadmusic.ui.activity.ListSongActivity;
import com.eskaylation.downloadmusic.ui.activity.artist.ActivityArtist;
import com.eskaylation.downloadmusic.ui.activity.main.MainActivity;
import com.eskaylation.downloadmusic.utils.PreferenceUtils;
import com.eskaylation.downloadmusic.widget.GridSpacingItemDecoration;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ArtistFragment extends Fragment implements ArtistAdapter.OnItemArtistClickListener, ArtistListenner {

    public FrameLayout adView;
    public ArtistAdapter adapter;
    public ArtistLoader artistLoader;
    public CoordinatorLayout coordinator;
    public ArrayList<Artist> lstArtist;
    public long mLastClickTime;
    public Toolbar mToolbar;
    public Window mWindow;
    public RecyclerView rv_artist;
    public Thread t;
    public void setRingtone(Song song) {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_artist, container, false);
        adView=view.findViewById(R.id.ad_view_container);
        coordinator=view.findViewById(R.id.coordinator);
        mToolbar=view.findViewById(R.id.toolbar);
        rv_artist=view.findViewById(R.id.rv_artist);
        init();
//        BaseActivity baseActivity = (BaseActivity) requireContext();
//        baseActivity.ringtone = song;
        AdsManager.showBanner(requireActivity(),adView);

        return view;
    }
    public void init() {
//        ButterKnife.bind(this);
        this.artistLoader = new ArtistLoader(requireContext(), this);
        this.rv_artist.addItemDecoration(new GridSpacingItemDecoration(2, 16, true));
        this.rv_artist.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        this.adapter = new ArtistAdapter(requireContext(), this);
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
                ActivityArtist1();
            }
        });
        this.t.start();
    }
    public  void ActivityArtist1() {
        this.artistLoader.loadInBackground();
    }
    public void onArtistClick(int i, final Artist artist, ImageView imageView, TextView textView, TextView textView2) {
        if (SystemClock.elapsedRealtime() - this.mLastClickTime >= 1000) {
            AdsManager.showNext((Activity) requireContext(), new AdsManager.AdCloseListener() {
                @Override
                public void onAdClosed() {
                    Intent intent = new Intent(requireContext(), ListSongActivity.class);
                    intent.putExtra("artist_data", artist);
                    requireContext().startActivity(intent);
                }
            });

            this.mLastClickTime = SystemClock.elapsedRealtime();
        }
    }

    public void onLoadArtistSuccessful(ArrayList<Artist> arrayList) {
        this.t = null;
        this.lstArtist = arrayList;
        requireActivity().runOnUiThread(new Runnable() {
            public final void run() {
                ActivityArtist2();
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