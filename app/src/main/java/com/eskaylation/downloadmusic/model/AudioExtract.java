package com.eskaylation.downloadmusic.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class AudioExtract implements Parcelable {
    public static final Creator<AudioExtract> CREATOR = new Creator<AudioExtract>() {
        public AudioExtract createFromParcel(Parcel parcel) {
            return new AudioExtract(parcel);
        }

        public AudioExtract[] newArray(int i) {
            return new AudioExtract[i];
        }
    };
    public int dbID;
    public long duration;
    public int resourceThumb;
    public String thumbUrl;
    public String title;
    public String urlVideo;

    public int describeContents() {
        return 0;
    }

    public AudioExtract(int i, String str, String str2, long j, int i2, String str3) {
        this.dbID = i;
        this.urlVideo = str;
        this.title = str2;
        this.duration = j;
        this.resourceThumb = i2;
        this.thumbUrl = str3;
    }

    public AudioExtract(Parcel parcel) {
        this.dbID = parcel.readInt();
        this.urlVideo = parcel.readString();
        this.title = parcel.readString();
        this.duration = parcel.readLong();
        this.resourceThumb = parcel.readInt();
        this.thumbUrl = parcel.readString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.dbID);
        parcel.writeString(this.urlVideo);
        parcel.writeString(this.title);
        parcel.writeLong(this.duration);
        parcel.writeInt(this.resourceThumb);
        parcel.writeString(this.thumbUrl);
    }
}
