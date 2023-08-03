package com.eskaylation.downloadmusic.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class Favorite implements Parcelable {
    public static final Creator<Favorite> CREATOR = new Creator<Favorite>() {
        public Favorite createFromParcel(Parcel parcel) {
            return new Favorite(parcel);
        }

        public Favorite[] newArray(int i) {
            return new Favorite[i];
        }
    };
    public String favorite_id;
    public int id;
    public String name;

    public int describeContents() {
        return 0;
    }

    public Favorite(int i, String str, String str2) {
        this.id = i;
        this.favorite_id = str;
        this.name = str2;
    }

    public String getFavorite_id() {
        return this.favorite_id;
    }

    public void setName(String str) {
        this.name = str;
    }

    public Favorite(Parcel parcel) {
        this.id = parcel.readInt();
        this.favorite_id = parcel.readString();
        this.name = parcel.readString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.id);
        parcel.writeString(this.favorite_id);
        parcel.writeString(this.name);
    }
}
