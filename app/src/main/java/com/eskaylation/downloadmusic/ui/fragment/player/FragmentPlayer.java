package com.eskaylation.downloadmusic.ui.fragment.player;

import android.animation.Animator;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.androidanimations.library.YoYo.AnimatorCallback;
import com.eskaylation.downloadmusic.R;
import com.eskaylation.downloadmusic.base.BaseFragment;
import com.eskaylation.downloadmusic.model.AdsManager;
import com.eskaylation.downloadmusic.model.Favorite;
import com.eskaylation.downloadmusic.model.Song;
import com.eskaylation.downloadmusic.service.MusicPlayerService;
import com.eskaylation.downloadmusic.service.MusicPlayerService.MusicServiceBinder;
import com.eskaylation.downloadmusic.ui.activity.main.MainActivity;
import com.eskaylation.downloadmusic.ui.dialog.DialogFavorite;
import com.eskaylation.downloadmusic.ui.dialog.DialogFavoriteListener;

import com.eskaylation.downloadmusic.utils.AppConstants;





import java.util.ArrayList;
import java.util.List;

public class FragmentPlayer extends BaseFragment implements DialogFavoriteListener {
    
    private final String TAG = "NativeAdActivity".getClass().getSimpleName();
    public LinearLayout adView;
    
    @BindView(R.id.img_thumb)
    public ImageView bigThumb;
    public ServiceConnection connection = new ServiceConnection() {
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            FragmentPlayer.this.musicPlayerService = ((MusicServiceBinder) iBinder).getService();
            FragmentPlayer.this.mBound = true;
            FragmentPlayer.this.lstAudioPlay.addAll(FragmentPlayer.this.musicPlayerService.getListAudio());
            FragmentPlayer.this.musicPlayerService.setUIConTrolFragmentPlayer(FragmentPlayer.this.bigThumb);
        }

        public void onServiceDisconnected(ComponentName componentName) {
            FragmentPlayer.this.mBound = false;
        }
    };
    public DialogFavorite dialogFavorite;
    public ArrayList<Song> lstAudioPlay = new ArrayList<>();
    public boolean mBound = false;
    public MusicPlayerService musicPlayerService;
   // @BindView(R.id.nativeAdsView)

    public void deletePlaylistDone(int i) {
    }

    public void onCreateNewPlaylist(Favorite favorite) {
    }

    public void onOpenAddSong(Favorite favorite) {
    }

    public void onUpdatePlaylist(int i, Favorite favorite) {
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_player, viewGroup, false);
        init(inflate);

        loadNativeAd(inflate);
        return inflate;
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
    }

    public void onResume() {
        super.onResume();

        bindService();
    }

    public final void bindService() {
        getContext().bindService(new Intent(getContext(), MusicPlayerService.class), this.connection, 1);
    }

    public final void unbindServicePlayMusic() {
        if (this.mBound) {
            try {
                getActivity().unbindService(this.connection);
            } catch (Exception unused) {
            }
        }
    }

    public void onDetach() {
        super.onDetach();
        unbindServicePlayMusic();
    }

    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public void init(View view) {
        ButterKnife.bind(this, view);
        this.dialogFavorite = new DialogFavorite(getContext(), this);
    }

    public void onCreatePlaylistFromDialog(Song song) {
        this.dialogFavorite.showDialogAddSong(song, true);
    }

    private void loadNativeAd(View view) {

        AdsManager.showNativeDialog(requireActivity(),view);
    }
}
