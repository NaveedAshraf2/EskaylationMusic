package com.eskaylation.downloadmusic.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class Category implements Parcelable {
    public static final Creator<Category> CREATOR = new Creator<Category>() {
        public Category createFromParcel(Parcel parcel) {
            return new Category(parcel);
        }

        public Category[] newArray(int i) {
            return new Category[i];
        }
    };
    public String keyword;
    public int resource;
    public String title;

    public int describeContents() {
        return 0;
    }

    public Category(Parcel parcel) {
        this.title = parcel.readString();
        this.keyword = parcel.readString();
        this.resource = parcel.readInt();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.title);
        parcel.writeString(this.keyword);
        parcel.writeInt(this.resource);
    }
}
