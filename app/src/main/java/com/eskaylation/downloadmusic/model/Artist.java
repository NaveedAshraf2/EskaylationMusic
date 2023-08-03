package com.eskaylation.downloadmusic.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class Artist implements Parcelable {
    public static final Creator<Artist> CREATOR = new Creator<Artist>() {
        public Artist createFromParcel(Parcel parcel) {
            return new Artist(parcel);
        }

        public Artist[] newArray(int i) {
            return new Artist[i];
        }
    };
    public int albumCount;
    public long id;
    public String name;
    public int trackCount;

    public int describeContents() {
        return 0;
    }

    public Artist(long j, String str, int i, int i2) {
        this.id = j;
        if (str == null) {
            str = "<unknown>";
        }
        this.name = str;
        this.albumCount = i;
        this.trackCount = i2;
    }

    public Artist(Parcel parcel) {
        this.id = parcel.readLong();
        this.name = parcel.readString();
        this.albumCount = parcel.readInt();
        this.trackCount = parcel.readInt();
    }

    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getTrackCount() {
        return this.trackCount;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.id);
        parcel.writeString(this.name);
        parcel.writeInt(this.albumCount);
        parcel.writeInt(this.trackCount);
    }
}
