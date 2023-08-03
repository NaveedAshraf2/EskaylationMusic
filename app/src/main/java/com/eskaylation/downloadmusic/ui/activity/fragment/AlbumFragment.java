package com.eskaylation.downloadmusic.ui.activity.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eskaylation.downloadmusic.R;
import com.eskaylation.downloadmusic.adapter.AlbumAdapter;
import com.eskaylation.downloadmusic.adapter.AlbumAdapter.OnItemAlbumClickListener;
import com.eskaylation.downloadmusic.base.BaseActivity;
import com.eskaylation.downloadmusic.base.BaseActivityLoader;
import com.eskaylation.downloadmusic.base.BaseLoaderFragment;
import com.eskaylation.downloadmusic.bus.NotifyDeleteMusic;
import com.eskaylation.downloadmusic.listener.AlbumListenner;
import com.eskaylation.downloadmusic.loader.AlbumLoader;
import com.eskaylation.downloadmusic.model.AdsManager;
import com.eskaylation.downloadmusic.model.Album;
import com.eskaylation.downloadmusic.model.Song;
import com.eskaylation.downloadmusic.ui.activity.ListSongActivity;
import com.eskaylation.downloadmusic.ui.activity.album.ActivityAlbum;
import com.eskaylation.downloadmusic.utils.PreferenceUtils;
import com.eskaylation.downloadmusic.widget.GridSpacingItemDecoration;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class AlbumFragment extends Fragment implements OnItemAlbumClickListener, AlbumListenner {
    private AlbumLoader albumsLoader;
    private FrameLayout ad_view_container;
    private AlbumAdapter gridAdapter;
    private ArrayList<Album> lstAlbum;
    private long mLastClickTime;
    private ImageButton btnBack;
    private CoordinatorLayout coordinator;
    private RecyclerView rv_album;
    private Thread t;

//    public AlbumFragment() {
//        // Required empty public constructor
//
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_album, container, false);
        btnBack = rootView.findViewById(R.id.btnBack);
        coordinator = rootView.findViewById(R.id.coordinator);
        rv_album = rootView.findViewById(R.id.rv_album);
        ad_view_container = rootView.findViewById(R.id.ad_view_container);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();

        AdsManager.showBanner(requireActivity(),ad_view_container);

//        loadBanner(adView); // You need to get adView reference
        btnBack.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
//                onBackPressed();
            }
        });
    }

    private void init() {
        gridAdapter = new AlbumAdapter(requireContext(), this);
        rv_album.addItemDecoration(new GridSpacingItemDecoration(2, 16, true));
        rv_album.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        rv_album.setHasFixedSize(true);
        rv_album.setAdapter(gridAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        loader();
    }

    private void loader() {
        albumsLoader = new AlbumLoader(requireContext(), this);
        albumsLoader.setSortOrder("album_key");
        albumsLoader.filterartistsong(null, null);
        t = new Thread(new Runnable() {
            public final void run() {
                albumsLoader.loadInBackground();
            }
        });
        t.start();
    }

    @Override
    public void onItemClick(final int i, final Album album) {
        if (SystemClock.elapsedRealtime() - mLastClickTime >= 1000) {
            AdsManager.showNext((Activity) requireContext(), new AdsManager.AdCloseListener() {
                @Override
                public void onAdClosed() {
                    Intent intent = new Intent(requireContext(), ListSongActivity.class);
                    intent.putExtra("album_data", album);
                    intent.putExtra("position_intent", i);
                    startActivity(intent);
                }
            });
            mLastClickTime = SystemClock.elapsedRealtime();
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onDeleteSong(NotifyDeleteMusic notifyDeleteMusic) {
        loader();
    }

    @Override
    public void onAlbumLoadedSuccessful(ArrayList<Album> arrayList) {
        this.lstAlbum = arrayList;

        requireActivity().runOnUiThread(new Runnable() {
            public final void run() {
                onAlbumload2();
            }
        });
    }
    public  void onAlbumload2() {
        this.gridAdapter.setData(this.lstAlbum);
    }

}
