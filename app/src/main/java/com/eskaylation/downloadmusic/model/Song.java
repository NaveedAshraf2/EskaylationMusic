package com.eskaylation.downloadmusic.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class Song implements Parcelable {
    public static final Creator<Song> CREATOR = new Creator<Song>() {
        public Song createFromParcel(Parcel parcel) {
            return new Song(parcel);
        }

        public Song[] newArray(int i) {
            return new Song[i];
        }
    };
    public String album;
    public long albumId;
    public String artist;
    public long artistId;
    public String duration;
    public String genre;
    public long id;
    public boolean isSelected = false;
    public String lyrics;
    public String mSongPath;
    public String title;
    public int trackNumber;
    public String year;

    public int describeContents() {
        return 0;
    }

    public Song(Parcel parcel) {
        boolean z = false;
        this.id = parcel.readLong();
        this.title = parcel.readString();
        this.artist = parcel.readString();
        this.album = parcel.readString();
        this.trackNumber = parcel.readInt();
        this.albumId = parcel.readLong();
        this.genre = parcel.readString();
        this.mSongPath = parcel.readString();
        if (parcel.readByte() != 0) {
            z = true;
        }
        this.isSelected = z;
        this.year = parcel.readString();
        this.lyrics = parcel.readString();
        this.artistId = parcel.readLong();
        this.duration = parcel.readString();
    }

    public Song() {
    }

    public Song(long j, String str, String str2, String str3, int i, long j2, String str4, String str5, boolean z, String str6, String str7, long j3, String str8) {
        this.id = j;
        this.title = str;
        this.artist = str2;
        this.album = str3;
        this.trackNumber = i;
        this.albumId = j2;
        this.genre = str4;
        this.mSongPath = str5;
        this.isSelected = z;
        this.year = str6;
        this.lyrics = str7;
        this.artistId = j3;
        this.duration = str8;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long j) {
        this.id = j;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public String getArtist() {
        return this.artist;
    }

    public void setArtist(String str) {
        this.artist = str;
    }

    public void setAlbum(String str) {
        this.album = str;
    }

    public void setTrackNumber(int i) {
        this.trackNumber = i;
    }

    public long getAlbumId() {
        return this.albumId;
    }

    public void setAlbumId(long j) {
        this.albumId = j;
    }

    public String getmSongPath() {
        return this.mSongPath;
    }

    public void setmSongPath(String str) {
        this.mSongPath = str;
    }

    public void setSelected(boolean z) {
        this.isSelected = z;
    }

    public void setYear(String str) {
        this.year = str;
    }

    public void setArtistId(long j) {
        this.artistId = j;
    }

    public void setDuration(String str) {
        this.duration = str;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Song{id=");
        sb.append(this.id);
        sb.append(", title='");
        sb.append(this.title);
        sb.append('\'');
        sb.append(", artist='");
        sb.append(this.artist);
        sb.append('\'');
        sb.append(", album='");
        sb.append(this.album);
        sb.append('\'');
        sb.append(", trackNumber=");
        sb.append(this.trackNumber);
        sb.append(", albumId=");
        sb.append(this.albumId);
        sb.append(", genre='");
        sb.append(this.genre);
        sb.append('\'');
        sb.append(", mSongPath='");
        sb.append(this.mSongPath);
        sb.append('\'');
        sb.append(", isSelected=");
        sb.append(this.isSelected);
        sb.append(", year='");
        sb.append(this.year);
        sb.append('\'');
        sb.append(", lyrics='");
        sb.append(this.lyrics);
        sb.append('\'');
        sb.append(", artistId=");
        sb.append(this.artistId);
        sb.append(", duration='");
        sb.append(this.duration);
        sb.append('\'');
        sb.append('}');
        return sb.toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.id);
        parcel.writeString(this.title);
        parcel.writeString(this.artist);
        parcel.writeString(this.album);
        parcel.writeInt(this.trackNumber);
        parcel.writeLong(this.albumId);
        parcel.writeString(this.genre);
        parcel.writeString(this.mSongPath);
        parcel.writeByte(this.isSelected ? (byte) 1 : 0);
        parcel.writeString(this.year);
        parcel.writeString(this.lyrics);
        parcel.writeLong(this.artistId);
        parcel.writeString(this.duration);
    }

    public boolean equals(Object obj) {
        return (obj instanceof Song) && this.id == ((Song) obj).id;
    }
}
