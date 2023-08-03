package com.eskaylation.downloadmusic.bus;

import com.eskaylation.downloadmusic.model.Song;

public class NotifyDeleteMusic {
    public Song song;

    public NotifyDeleteMusic(Song song2) {
        this.song = song2;
    }
}
