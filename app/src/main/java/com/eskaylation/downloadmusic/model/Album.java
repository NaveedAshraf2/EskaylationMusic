package com.eskaylation.downloadmusic.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class Album implements Parcelable {
    public static final Creator<Album> CREATOR = new Creator<Album>() {
        public Album createFromParcel(Parcel parcel) {
            return new Album(parcel);
        }

        public Album[] newArray(int i) {
            return new Album[i];
        }
    };
    public String albumName;
    public Uri albumThumb;
    public String artistName;
    public long id;
    public int trackCount;
    public int year;

    public int describeContents() {
        return 0;
    }

    public Album() {
    }

    public Album(Parcel parcel) {
        this.id = parcel.readLong();
        this.albumName = parcel.readString();
        this.artistName = parcel.readString();
        this.year = parcel.readInt();
        this.trackCount = parcel.readInt();
        this.albumThumb = (Uri) parcel.readParcelable(Uri.class.getClassLoader());
    }

    public long getId() {
        return this.id;
    }

    public void setId(long j) {
        this.id = j;
    }

    public String getAlbumName() {
        return this.albumName;
    }

    public void setAlbumName(String str) {
        this.albumName = str;
    }

    public void setArtistName(String str) {
        this.artistName = str;
    }

    public void setYear(int i) {
        this.year = i;
    }

    public void setTrackCount(int i) {
        this.trackCount = i;
    }

    public void setAlbumThumb(Uri uri) {
        this.albumThumb = uri;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.id);
        parcel.writeString(this.albumName);
        parcel.writeString(this.artistName);
        parcel.writeInt(this.year);
        parcel.writeInt(this.trackCount);
        parcel.writeParcelable(this.albumThumb, i);
    }
}
