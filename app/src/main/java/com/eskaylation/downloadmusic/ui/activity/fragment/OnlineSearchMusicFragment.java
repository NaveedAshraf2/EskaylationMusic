package com.eskaylation.downloadmusic.ui.activity.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.eskaylation.downloadmusic.R;
import com.eskaylation.downloadmusic.model.AdsManager;
import com.eskaylation.downloadmusic.ui.activity.album.ActivityAlbum;
import com.eskaylation.downloadmusic.ui.activity.artist.ActivityArtist;
import com.eskaylation.downloadmusic.ui.activity.main.MainActivity;
import com.eskaylation.downloadmusic.ui.activity.playlist.ActivityPlaylist;
import com.eskaylation.downloadmusic.ui.activity.search.SearchActivity;
import com.eskaylation.downloadmusic.ui.activity.song.ActivitySong;

import butterknife.BindView;

public class OnlineSearchMusicFragment extends Fragment {
    LinearLayout downloadx;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_online_search_music, container, false);
        downloadx=view.findViewById(R.id.downloadx);

        downloadx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdsManager.showNext((Activity) requireContext(), new AdsManager.AdCloseListener() {
                    @Override
                    public void onAdClosed() {
                        startActivity(new Intent(requireContext(), SearchActivity.class));
                    }
                });
            }
        });

        return view;

    }
}