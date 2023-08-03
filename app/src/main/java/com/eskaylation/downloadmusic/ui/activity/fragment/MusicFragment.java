package com.eskaylation.downloadmusic.ui.activity.fragment;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Optional;

import com.eskaylation.downloadmusic.adapter.SongAdapter;
import com.eskaylation.downloadmusic.base.BaseActivity;
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
import com.eskaylation.downloadmusic.ui.activity.PlayerActivity;

import com.eskaylation.downloadmusic.ui.activity.song.ActivitySong;
import com.eskaylation.downloadmusic.ui.dialog.DialogFavorite;
import com.eskaylation.downloadmusic.ui.dialog.DialogFavoriteListener;
import com.eskaylation.downloadmusic.ui.dialog.DialogMoreListener;
import com.eskaylation.downloadmusic.ui.dialog.DialogMoreSongUtil;


import com.eskaylation.downloadmusic.utils.ToastUtils;
import com.wang.avi.AVLoadingIndicatorView;
import java.util.ArrayList;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import com.eskaylation.downloadmusic.R;

public class MusicFragment extends Fragment implements OnItemSongClickListener, SongListenner, DialogMoreListener, DialogFavoriteListener {
    private FrameLayout ad_view_container;
    public FrameLayout adView;
    public SongAdapter adapter;
    public ImageButton btnBack;
    public Button btnPlayAll;

    public ServiceConnection connection = new ServiceConnection() {
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MusicFragment.this.musicPlayerService = ((MusicServiceBinder) iBinder).getService();
            MusicFragment.this.musicPlayerService.setAdapterControlFrgSong(MusicFragment.this.adapter, MusicFragment.this.rv_song);
            MusicFragment.this.musicPlayerService.initAdapterControlFrgSong();
            MusicFragment.this.mBound = true;
        }
        public void onServiceDisconnected(ComponentName componentName) {
            MusicFragment.this.mBound = false;
        }
    };

    public CoordinatorLayout coordinator;
    public DialogFavorite dialogFavorite;
    public DialogMoreSongUtil dialogMoreSong;
    public EditText edtSearch;
    public ArrayList<Song> lstMusic;
    public boolean mBound = false;
    public Window mWindow;
    public MusicPlayerService musicPlayerService;
    public AVLoadingIndicatorView progressBar;
    public RecyclerView rvSearch;
    public RecyclerView rv_song;
    public SongAdapter searchAdapter;
    public SearchLoader searchLoader;
    public Thread t;
    public TrackLoader trackLoader;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_song, container, false);
//        ButterKnife.bind((Activity) requireContext(), view);
        btnBack = view.findViewById(R.id.btnBack);
        ad_view_container = view.findViewById(R.id.ad_view_container);
        tvEmptySearch = view.findViewById(R.id.tvEmpty);
        rv_song = view.findViewById(R.id.rv_song);
        rvSearch = view.findViewById(R.id.rvSearch);
        progressBar = view.findViewById(R.id.progress_loading);
        edtSearch = view.findViewById(R.id.edtSearch);
        coordinator = view.findViewById(R.id.coordinator);
        ad_view_container = view.findViewById(R.id.ad_view_container);
        btnPlayAll = view.findViewById(R.id.btnPlayAll);


        this.mWindow = requireActivity().getWindow();
        this.mWindow.getDecorView().setSystemUiVisibility(1280);
        init();
        btnPlayAll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onListenAll();
            }
        });
        return view;
    }

    public void onDestroy() {
        super.onDestroy();
        unbindServicePlayMusic();
    }

    public void init() {
//        ButterKnife.bind((Activity) requireContext());
//        setBackgroundThemes(this.coordinator, PreferenceUtils.getInstance(this).getThemesPosition());

        this.btnBack.setOnClickListener(new OnClickListener() {
            public final void onClick(View view) {
                MusicFragment.this.lambda$init$0$ActivitySong(view);
            }
        });
        this.lstMusic = new ArrayList<>();
        this.adapter = new SongAdapter(requireContext(), this.lstMusic, this);
        this.rv_song.setLayoutManager(new LinearLayoutManager(requireContext()));
        this.rv_song.setHasFixedSize(true);
        this.rv_song.setAdapter(this.adapter);
        loader();
        this.dialogMoreSong = new DialogMoreSongUtil(requireContext(), this, false);
        initSearch();
        AdsManager.showBanner(requireActivity(),ad_view_container);
    }


    public void lambda$init$0$ActivitySong(View view) {
        requireActivity().onBackPressed();
    }

    public void onListenAll() {
        ArrayList<Song> arrayList = this.lstMusic;
        if (arrayList == null || arrayList.size() <= 0) {
            ToastUtils.error(requireContext(), getString(R.string.no_song_to_play));
        } else {
            MusicFragment.this.playSong();
    }}

    public void playSong() {
        MusicPlayerService musicPlayerService2 = this.musicPlayerService;
        if (musicPlayerService2 != null) {
            musicPlayerService2.setListMusic(this.lstMusic, 0);
            this.musicPlayerService.setSongPos(0);
            this.musicPlayerService.stopSong();
            Intent intent = new Intent(requireContext(), MusicPlayerService.class);
            intent.setAction("com.eskaylation.downloadmusic.ACTION.SETDATAOFFLINEPLAYER");
            requireContext().startService(intent);
            Intent intent2 = new Intent(requireContext(), PlayerActivity.class);
            intent2.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent2);
        }
    }
    

    public void onResume() {
        super.onResume();
    }
    public void initSearch() {
        this.searchLoader = new SearchLoader(new SearchListenner() {
            public final void onAudioSearchSuccessful(ArrayList arrayList) {
                MusicFragment.this.lambda$initSearch$1$ActivitySong(arrayList);
            }
        }, requireContext());
        this.searchLoader.setSortOrder("title_key");
        this.edtSearch.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (charSequence.length() > 0) {
                    rv_song.setVisibility(GONE);
                    rvSearch.setVisibility(VISIBLE);
                    searchLoader.setSearchName(MusicFragment.this.edtSearch.getText().toString().trim());
                    t = new Thread(new Runnable() {
                        public final void run() {

                            MusicFragment.this.searchLoader.loadInBackground();
                        }

                    });
                    MusicFragment.this.t.start();
                    MusicFragment.this.btnPlayAll.setVisibility(GONE);
                    return;
                }
                MusicFragment.this.rvSearch.setVisibility(INVISIBLE);
                MusicFragment.this.tvEmptySearch.setVisibility(GONE);
                MusicFragment.this.rv_song.setVisibility(VISIBLE);
                MusicFragment.this.btnPlayAll.setVisibility(VISIBLE);
            }


        });
        this.edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                return i == 6;
            }
        });
    }
    
    
    public void lambda$initSearch$1$ActivitySong(final ArrayList<Song> arrayList) {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                if (arrayList.size() > 0) {
                    rvSearch.setVisibility(View.VISIBLE);
                    tvEmptySearch.setVisibility(View.GONE);
                    btnPlayAll.setVisibility(View.VISIBLE);
                    btnPlayAll.setVisibility(View.GONE);
                } else {
                    rvSearch.setVisibility(View.INVISIBLE);
                    tvEmptySearch.setVisibility(View.VISIBLE);
                    btnPlayAll.setVisibility(View.GONE);
                }

                searchAdapter = new SongAdapter(requireContext(), arrayList, new OnItemSongClickListener() {
                    public void onItemSongClick(ArrayList<Song> arrayList, int i) {
                        if (musicPlayerService != null) {
                            musicPlayerService.setListMusic(arrayList, i);
                            musicPlayerService.setSongPos(i);
                            musicPlayerService.stopSong();
                            Intent intent = new Intent(getActivity(), MusicPlayerService.class);
                            intent.setAction("com.eskaylation.downloadmusic.ACTION.SETDATAOFFLINEPLAYER");
                            getActivity().startService(intent);
                            startActivity(new Intent(getActivity(), PlayerActivity.class));
                            getActivity().finish();
                            onCloseSearch();
                        }
                    }

                    public void onMoreClick(Song song, int i) {
                        if (musicPlayerService.getListAudio().isEmpty()) {
                            dialogMoreSong.showDialogMore(song, false, null, false);
                        } else if (song.getmSongPath().equals(musicPlayerService.getItemIndex().getmSongPath())) {
                            dialogMoreSong.showDialogMore(song, true, null, false);
                        } else {
                            dialogMoreSong.showDialogMore(song, false, null, true);
                        }
                    }
                });

                rvSearch.setHasFixedSize(true);
                rvSearch.setLayoutManager(new LinearLayoutManager(getActivity()));
                rvSearch.setAdapter(searchAdapter);
            }
        });
    }

    public void onCloseSearch() {
        if (rvSearch.getVisibility() == View.VISIBLE) {
            hideKeyboard();
            btnPlayAll.setVisibility(View.VISIBLE);
            edtSearch.setText("");
            tvEmptySearch.setVisibility(View.GONE);
            rvSearch.setVisibility(View.INVISIBLE);
            closeKeyboard();
        }
    }

    public void closeKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(edtSearch.getWindowToken(), 0);
        }
    }

    public void loader() {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.show();
        }
        trackLoader = new TrackLoader(this, requireContext());
        trackLoader.setSortOrder("title_key");
        t = new Thread(new Runnable() {
            public void run() {
                lambda$loader$2$ActivitySong();
            }
        });
        t.start();
    }

    public void lambda$loader$2$ActivitySong() {
        trackLoader.loadInBackground();
    }


    public void onAudioLoadedSuccessful(ArrayList<Song> arrayList) {
        this.lstMusic = arrayList;
        Log.i("TAG", "onAudioLoadedSuccessful: "+lstMusic.size());
        requireActivity().runOnUiThread(new Runnable() {
            public void run() {
                lambda$onAudioLoadedSuccessful$3$ActivitySong(lstMusic);
            }
        });
    }

    public void lambda$onAudioLoadedSuccessful$3$ActivitySong(ArrayList<Song> arrayList) {
        Log.i("TAG", "lambda$onAudioLoadedSuccessful$3$ActivitySong: "+arrayList.size());

        lstMusic = arrayList;
        adapter.setListItem(arrayList);
        progressBar.setVisibility(View.GONE);
        progressBar.hide();
        bindService();
        StringBuilder sb = new StringBuilder();
        sb.append(getString(R.string.txt_play_all));
        sb.append(" (");
        sb.append(arrayList.size());
        sb.append(")");
        btnPlayAll.setText(sb.toString());
    }
    public void bindService() {
        requireActivity().bindService(new Intent((Activity)requireContext(), MusicPlayerService.class), connection, Context.BIND_AUTO_CREATE);
    }

    public void unbindServicePlayMusic() {
        if (mBound) {
            try {
                requireActivity().unbindService(connection);
            } catch (Exception unused) {
            }
        }
    }

    public void onItemSongClick(final ArrayList<Song> arrayList, final int i) {
        itemSongPlay(arrayList, i);

    /*
    AdsManager.showNext(getActivity(), new AdsManager.AdCloseListener() {
        @Override
        public void onAdClosed() {

        }
    });

     */

    }

    public void itemSongPlay(ArrayList<Song> arrayList, int i) {
        MusicPlayerService musicPlayerService2 = musicPlayerService;
        if (musicPlayerService2 != null) {
            musicPlayerService2.setListMusic(arrayList, i);
            musicPlayerService.setSongPos(i);
            musicPlayerService.stopSong();
            Intent intent = new Intent(getActivity(), MusicPlayerService.class);
            intent.setAction("com.eskaylation.downloadmusic.ACTION.SETDATAOFFLINEPLAYER");
            getActivity().startService(intent);
            startActivity(new Intent(getActivity(), PlayerActivity.class));
        }
    }

    public void onMoreClick(Song song, int i) {
        MusicPlayerService musicPlayerService2 = musicPlayerService;
        if (musicPlayerService2 != null) {
            if (musicPlayerService2.getListAudio().isEmpty()) {
                dialogMoreSong.showDialogMore(song, false, null, false);
            } else if (song.getmSongPath().equals(musicPlayerService.getItemIndex().getmSongPath())) {
                dialogMoreSong.showDialogMore(song, true, null, false);
            } else {
                dialogMoreSong.showDialogMore(song, false, null, true);
            }
        }
    }

    public void onNeedPermisionWriteSetting(Song song) {
        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
        StringBuilder sb = new StringBuilder();
        sb.append("package:");
        sb.append(requireActivity().getPackageName());
        intent.setData(Uri.parse(sb.toString()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        BaseActivity baseActivity = (BaseActivity) requireContext();
        baseActivity.ringtone = song;

    }

    @SuppressLint("SetTextI18n")
    public void onDeleteSongSuccessFull(Song song) {
        if (lstMusic.contains(song)) {
            adapter.removeAt(adapter.getPositionFromSong(song));
            Button button = btnPlayAll;
            StringBuilder sb = new StringBuilder();
            sb.append(getString(R.string.txt_play_all));
            sb.append(" (");
            sb.append(adapter.getListSong().size());
            sb.append(")");
            button.setText(sb.toString());
        }
    }

    public void onAddToPlayNext(Song song) {
        musicPlayerService.addSongToNext(song);
    }

    public void onCreatePlaylistFromDialog(Song song) {
        dialogFavorite.showDialogAddSong(song, false);
    }
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onDeleteSong(NotifyDeleteMusic notifyDeleteMusic) {
        Song song = notifyDeleteMusic.song;
        for (int i = 0; i < lstMusic.size() - 1; i++) {
            Song song2 = lstMusic.get(i);
            if (song2.getmSongPath().equals(song.getmSongPath())) {
                adapter.removeAt(adapter.getPositionFromSong(song2));
                Button button = btnPlayAll;
                StringBuilder sb = new StringBuilder();
                sb.append(getString(R.string.txt_play_all));
                sb.append(" (");
                sb.append(adapter.getListSong().size());
                sb.append(")");
                button.setText(sb.toString());
            }
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void closeDialog(CloseDialog closeDialog) {
        if (closeDialog.isClose) {
            if (dialogMoreSong != null) {
                dialogMoreSong.closeDialog();
            }
            EventBus.getDefault().postSticky(new CloseDialog(false));
        }
    }
    public void hideKeyboard() {
        View currentFocus = requireActivity().getCurrentFocus();
        if (currentFocus != null) {
            ((InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }
    }
}
