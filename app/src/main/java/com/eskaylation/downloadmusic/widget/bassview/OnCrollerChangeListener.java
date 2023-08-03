package com.eskaylation.downloadmusic.widget.bassview;

public interface OnCrollerChangeListener {
    void onProgressChanged(Croller croller, int i);

    void onStartTrackingTouch(Croller croller);

    void onStopTrackingTouch(Croller croller);
}
