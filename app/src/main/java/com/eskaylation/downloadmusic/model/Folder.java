package com.eskaylation.downloadmusic.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class Folder implements Parcelable {
    public static final Creator<Folder> CREATOR = new Creator<Folder>() {
        public Folder createFromParcel(Parcel parcel) {
            return new Folder(parcel);
        }

        public Folder[] newArray(int i) {
            return new Folder[i];
        }
    };
    public int count;
    public String name;
    public int parentID;
    public String path;

    public int describeContents() {
        return 0;
    }

    public Folder(String str, int i, String str2, int i2) {
        this.name = str;
        this.count = i;
        this.path = str2;
        this.parentID = i2;
    }

    public Folder(Parcel parcel) {
        this.name = parcel.readString();
        this.count = parcel.readInt();
        this.path = parcel.readString();
        this.parentID = parcel.readInt();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.name);
        parcel.writeInt(this.count);
        parcel.writeString(this.path);
        parcel.writeInt(this.parentID);
    }
}
