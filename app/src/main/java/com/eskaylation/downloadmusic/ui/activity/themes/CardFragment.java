package com.eskaylation.downloadmusic.ui.activity.themes;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.eskaylation.downloadmusic.model.BackgroundModel;
import com.eskaylation.downloadmusic.utils.AppUtils;
import com.eskaylation.downloadmusic.R;
public class CardFragment extends Fragment {
    public ConstraintLayout rootBg;

    static {
        Class<CardFragment> cls = CardFragment.class;
    }

    public static CardFragment getInstance(BackgroundModel backgroundModel) {
        CardFragment cardFragment = new CardFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("item", backgroundModel);
        cardFragment.setArguments(bundle);
        return cardFragment;
    }

    @SuppressLint({"DefaultLocale", "ResourceType"})
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.sliding_theme_layout, viewGroup, false);
        ImageView imageView = (ImageView) inflate.findViewById(R.id.imgBackground);
        this.rootBg = (ConstraintLayout) inflate.findViewById(R.id.rootBGLayout);
        BackgroundModel backgroundModel = (BackgroundModel) getArguments().getParcelable("item");
        if (backgroundModel != null) {
            int i = backgroundModel.bgRoot;
            if (i != -1) {
                imageView.setImageResource(i);
            } else {
                imageView.setImageDrawable(AppUtils.createDrawable(backgroundModel.startGradient, backgroundModel.endGradient));
            }
        } else {
            Log.e("ITEM", "Null");
        }
        return inflate;
    }

    public ConstraintLayout getRootBG() {
        return this.rootBg;
    }
}
