package com.eskaylation.downloadmusic.ui.activity.playlist;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.eskaylation.downloadmusic.adapter.PlaylistAdapter;
import com.eskaylation.downloadmusic.base.BaseActivity;
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



import com.eskaylation.downloadmusic.utils.PreferenceUtils;
import java.util.ArrayList;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import com.eskaylation.downloadmusic.R;
public class ActivityPlaylist extends BaseActivity implements DialogFavoriteListener, OnFavoriteClickListener {
   @BindView(R.id.ad_view_container)
    public FrameLayout adView;
    public PlaylistAdapter adapter;
    @BindView(R.id.btn_add_new_playlist)
    public ImageButton btn_add_new_playlist;
    @BindView(R.id.coordinator)
    public CoordinatorLayout coordinator;
    public DialogFavorite dialogFavorite;
    public FavoriteDao favoriteDao;
    public LinearLayoutManager llManager;
    public ArrayList<Favorite> lstPlaylistName;
    public long mLastClickTime;
    @BindView(R.id.toolbar)
    public Toolbar mToolbar;
    public Window mWindow;
    @BindView(R.id.rv_favorite)
    public RecyclerView rv_favorite;
    public SongListDao songListDao;
    public SongListSqliteHelper songListSqliteHelper;
    public FavoriteSqliteHelper sqliteHelper;

    public void setRingtone(Song song) {
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.fragment_playlist);
        this.mWindow = getWindow();
        this.mWindow.getDecorView().setSystemUiVisibility(1280);
        init();
        loadBanner(this,adView);
        this.mToolbar.setNavigationOnClickListener(new OnClickListener() {
            public final void onClick(View view) {
                ActivityPlaylist.this.lambda$onCreate$0$ActivityPlaylist(view);
            }

        });

        btn_add_new_playlist.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick1();
            }
        });
    }

    public /* synthetic */ void lambda$onCreate$0$ActivityPlaylist(View view) {
        super.onBackPressed();
    }

    public void onClick1() {
        ActivityPlaylist.this.dialogFavorite.showDialogCreateFavorite(0, null);

    }
    @Override
    public void onCreateNewPlaylist(Favorite favorite) {

    }

    public void init() {
        ButterKnife.bind(this);
        setBackgroundThemes(this.coordinator, PreferenceUtils.getInstance(this).getThemesPosition());
        querryDb();
    }

    public void onResume() {
        super.onResume();
    }

    public final void querryDb() {
        this.sqliteHelper = new FavoriteSqliteHelper(this);
        this.favoriteDao = new FavoriteDao(this, this.sqliteHelper);
        this.dialogFavorite = new DialogFavorite(this, this);
        this.favoriteDao.insertFavorite("DEFAULT_FAVORITEDOWNLOAD");
        this.lstPlaylistName = this.favoriteDao.getAllFavorite();
        this.adapter = new PlaylistAdapter(this, this.lstPlaylistName, this);
        this.llManager = new LinearLayoutManager(this);
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
        startActivity(new Intent(this, ActivityAddSong.class).putExtra("Favorite_data", favorite));
    }

    public void onItemFavoriteClick(int i, final Favorite favorite, ImageView imageView, TextView textView) {
        if (SystemClock.elapsedRealtime() - this.mLastClickTime >= 1000) {
            AdsManager.showNext(this, new AdsManager.AdCloseListener() {
                @Override
                public void onAdClosed() {
                    Intent intent = new Intent(ActivityPlaylist.this, ListSongActivity.class);
                    intent.putExtra("playlist_name", favorite);
                    ActivityPlaylist.this.startActivity(intent);
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
            this.songListSqliteHelper = new SongListSqliteHelper(this, ((Favorite) this.lstPlaylistName.get(i)).getFavorite_id());
            this.songListDao = new SongListDao(this.songListSqliteHelper);
            this.songListDao.deleteFavoriteSong(song);
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onCreatePlaylist(CreatePlaylist createPlaylist) {
        querryDb();
    }

}
