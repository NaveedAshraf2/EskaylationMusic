package com.eskaylation.downloadmusic.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.bumptech.glide.Glide;
import com.eskaylation.downloadmusic.base.GlideApp;
import com.eskaylation.downloadmusic.utils.AppConstants;
import com.eskaylation.downloadmusic.utils.ArtworkUtils;
import com.eskaylation.downloadmusic.R;
public class PlayerView extends LinearLayout {
    @BindView(R.id.btnNextSmall)
    public ImageView btnNext;

    @BindView(R.id.btnPlayPauseSmall)
    public ImageView btnPlay;

    public ImageView btnPrive;
    @BindView(R.id.img_thumb)
    public ImageView imgThumb;
    public OnViewPlayerListener listener;
    @BindView(R.id.tv_time_artist)
    public TextView tvArtist;
    @BindView(R.id.tv_name_small)
    public TextView tvTitle;
    @BindView(R.id.view_player)
    public LinearLayout viewPlayer;

    public interface OnViewPlayerListener {
        void onClickPlayer();

        void onNext();

        void onPlayPause();

        void onPrive();
    }

    public PlayerView(Context context) {
        super(context);
        initView();
    }

    public PlayerView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView();
    }

    public PlayerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public final void initView() {
        View inflate = LinearLayout.inflate(getContext(), R.layout.view_player, null);
        inflate.setLayoutParams(new LayoutParams(-1, -2));
        addView(inflate);
        ButterKnife.bind(this, inflate);
        btnPlay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                OnViewPlayerListener onViewPlayerListener2 = listener;
                if (onViewPlayerListener2 != null) {
                    onViewPlayerListener2.onPlayPause();
                    return;
                }
            }
        });

        viewPlayer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                OnViewPlayerListener onViewPlayerListener4 = listener;
                if (onViewPlayerListener4 != null) {
                    onViewPlayerListener4.onClickPlayer();
                    return;
                }
            }
        });


        btnNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                OnViewPlayerListener onViewPlayerListener = listener;
                if (onViewPlayerListener != null) {
                    onViewPlayerListener.onNext();
                    return;
                }
            }
        });
      //  this.tvTitle.setSelected(true);
        //this.tvArtist.setSelected(true);
    }

    public void setStatePlayer(boolean z) {

        if (z) {
            this.btnPlay.setImageResource(R.drawable.ic_pause);
        } else {
            this.btnPlay.setImageResource(R.drawable.ic_play);
        }
    }

    public void setThumb(Context context, boolean z, String str) {
        if (z) {
            Glide.with(context).load(Integer.valueOf(AppConstants.randomThumb())).into(this.imgThumb);
        } else {
            Glide.with(context).load(ArtworkUtils.uri(Long.valueOf(str).longValue())).into(this.imgThumb);
        }
    }

    public void setTitle(String str) {
        if (str != null) {
            this.tvTitle.setText(str);
        }
    }

    public void setArtist(String str) {
        if (str != null) {
            this.tvArtist.setText(str);
        }
    }

    public void setPlayerListener(OnViewPlayerListener onViewPlayerListener) {
        this.listener = onViewPlayerListener;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnNextSmall /*2131361917*/:

                return;
            case R.id.btnPlayPauseSmall /*2131361921*/:

                return;
            case R.id.btnPriveSmall /*2131361923*/:
                OnViewPlayerListener onViewPlayerListener3 = this.listener;
                if (onViewPlayerListener3 != null) {
                    onViewPlayerListener3.onPrive();
                    return;
                }
                return;
            case R.id.view_player /*2131364392*/:

                return;
            default:
                return;
        }
    }
}
