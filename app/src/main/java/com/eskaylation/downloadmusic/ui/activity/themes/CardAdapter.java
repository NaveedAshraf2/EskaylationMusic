package com.eskaylation.downloadmusic.ui.activity.themes;

import androidx.constraintlayout.widget.ConstraintLayout;

public interface CardAdapter {
    float getBaseElevation();

    ConstraintLayout getCardViewAt(int i);

    int getCount();
}
