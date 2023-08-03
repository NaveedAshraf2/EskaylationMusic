package com.eskaylation.downloadmusic.ui.dialog;

import com.eskaylation.downloadmusic.model.Song;

public interface DialogMoreListener {
    void onAddToPlayNext(Song song);

    void onDeleteSongFromPlaylist(Song song);

    void onDeleteSongSuccessFull(Song song);

    void onNeedPermisionWriteSetting(Song song);
}
