package com.eskaylation.downloadmusic.ui.dialog;

import com.eskaylation.downloadmusic.model.Favorite;
import com.eskaylation.downloadmusic.model.Song;

public interface DialogFavoriteListener {
    void deletePlaylistDone(int i);

    void onCreateNewPlaylist(Favorite favorite);

    void onCreatePlaylistFromDialog(Song song);

    void onOpenAddSong(Favorite favorite);

    void onUpdatePlaylist(int i, Favorite favorite);
}
