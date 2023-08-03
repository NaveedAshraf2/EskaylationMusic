package com.eskaylation.downloadmusic.ui.activity.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.eskaylation.downloadmusic.R;
import com.eskaylation.downloadmusic.adapter.PlaylistAdapter;
import com.eskaylation.downloadmusic.bus.CreatePlaylist;
import com.eskaylation.downloadmusic.bus.NotifyDeleteMusic;
import com.eskaylation.downloadmusic.database.FavoriteDao;
import com.eskaylation.downloadmusic.database.FavoriteSqliteHelper;
import com.eskaylation.downloadmusic.database.SongListDao;
import com.eskaylation.downloadmusic.database.SongListSqliteHelper;
import com.eskaylation.downloadmusic.listener.OnFavoriteClickListener;
import com.eskaylation.downloadmusic.model.AdsManager;
import com.eskaylation.downloadmusic.model.Favorite;
import com.eskaylation.downloadmusic.model.Song;
import com.eskaylation.downloadmusic.ui.activity.ListSongActivity;
import com.eskaylation.downloadmusic.ui.activity.addsong.ActivityAddSong;
import com.eskaylation.downloadmusic.ui.dialog.DialogFavorite;
import com.eskaylation.downloadmusic.ui.dialog.DialogFavoriteListener;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.util.ArrayList;

public class PlaylistFragment extends Fragment implements DialogFavoriteListener, OnFavoriteClickListener {
    public FrameLayout adView;
    public PlaylistAdapter adapter;
    public ImageButton btn_add_new_playlist;
    public CoordinatorLayout coordinator;
    public DialogFavorite dialogFavorite;
    public FavoriteDao favoriteDao;
    public LinearLayoutManager llManager;
    public ArrayList<Favorite> lstPlaylistName;
    public long mLastClickTime;
    public Window mWindow;
    public RecyclerView rv_favorite;
    public SongListDao songListDao;
    public SongListSqliteHelper songListSqliteHelper;
    public FavoriteSqliteHelper sqliteHelper;

    public void setRingtone(Song song) {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_playlist, container, false);
        adView=view.findViewById(R.id.ad_view_container);
        btn_add_new_playlist=view.findViewById(R.id.btn_add_new_playlist);
        coordinator=view.findViewById(R.id.coordinator);
        rv_favorite=view.findViewById(R.id.rv_favorite);
        init();
        AdsManager.showBanner(requireActivity(),adView);
        btn_add_new_playlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick1();
            }
        });
        return view;
    }

    public void onClick1() {
        this.dialogFavorite.showDialogCreateFavorite(0, null);
        this.adapter.notifyDataSetChanged();

    }
    @Override
    public void onCreateNewPlaylist(Favorite favorite) {

        this.lstPlaylistName = this.favoriteDao.getAllFavorite();
        this.adapter = new PlaylistAdapter(requireContext(), this.lstPlaylistName, this);
        this.llManager = new LinearLayoutManager(requireContext());
        this.rv_favorite.setLayoutManager(this.llManager);
        this.rv_favorite.setHasFixedSize(true);
        this.rv_favorite.setAdapter(this.adapter);

        this.adapter.notifyDataSetChanged();
    }

    public void init() {
        querryDb();
    }

    public void onResume() {
        super.onResume();
    }

    public final void querryDb() {

        this.sqliteHelper = new FavoriteSqliteHelper(requireContext());
        this.favoriteDao = new FavoriteDao(requireContext(), this.sqliteHelper);
        this.dialogFavorite = new DialogFavorite(requireContext(), this);
        this.favoriteDao.insertFavorite("DEFAULT_FAVORITEDOWNLOAD");

        this.lstPlaylistName = this.favoriteDao.getAllFavorite();
        this.adapter = new PlaylistAdapter(requireContext(), this.lstPlaylistName, this);
        this.llManager = new LinearLayoutManager(requireContext());
        this.rv_favorite.setLayoutManager(this.llManager);
        this.rv_favorite.setHasFixedSize(true);
        this.rv_favorite.setAdapter(this.adapter);

    }

    public void onCreatePlaylistFromDialog(Song song) {
        this.dialogFavorite.showDialogAddSong(song, false);
    }

    public void onUpdatePlaylist(int i, Favorite favorite) {
        this.adapter.updateItem(i, favorite);
    }

    public void deletePlaylistDone(int i) {
        this.adapter.deleteItem(i);
    }

    public void onOpenAddSong(Favorite favorite) {
        startActivity(new Intent(requireContext(), ActivityAddSong.class).putExtra("Favorite_data", favorite));
    }

    public void onItemFavoriteClick(int i, final Favorite favorite, ImageView imageView, TextView textView) {
        if (SystemClock.elapsedRealtime() - this.mLastClickTime >= 1000) {
            AdsManager.showNext((Activity) requireContext(), new AdsManager.AdCloseListener() {
                @Override
                public void onAdClosed() {
                    Intent intent = new Intent(requireContext(), ListSongActivity.class);
                    intent.putExtra("playlist_name", favorite);
                    requireContext().startActivity(intent);
                }
            });
            this.mLastClickTime = SystemClock.elapsedRealtime();
        }
    }


    public void onMoreClick(int i, Favorite favorite) {
        this.dialogFavorite.showDialogMore(i, favorite);
    }


    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onDeleteSong(NotifyDeleteMusic notifyDeleteMusic) {
        Song song = notifyDeleteMusic.song;
        for (int i = 0; i < this.lstPlaylistName.size(); i++) {
            this.songListSqliteHelper = new SongListSqliteHelper(requireContext(), ((Favorite) this.lstPlaylistName.get(i)).getFavorite_id());
            this.songListDao = new SongListDao(this.songListSqliteHelper);
            this.songListDao.deleteFavoriteSong(song);
        }
    }

//    onCreateNewPlaylist

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onCreatePlaylist(CreatePlaylist createPlaylist) {
        querryDb();

        this.lstPlaylistName = this.favoriteDao.getAllFavorite();
        this.adapter = new PlaylistAdapter(requireContext(), this.lstPlaylistName, this);
        this.llManager = new LinearLayoutManager(requireContext());
        this.rv_favorite.setLayoutManager(this.llManager);
        this.rv_favorite.setHasFixedSize(true);
        this.rv_favorite.setAdapter(this.adapter);
        this.adapter.notifyDataSetChanged();
    }



}