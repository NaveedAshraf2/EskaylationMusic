package com.eskaylation.downloadmusic.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class BackgroundModel implements Parcelable {
    public static final Creator<BackgroundModel> CREATOR = new Creator<BackgroundModel>() {
        public BackgroundModel createFromParcel(Parcel parcel) {
            return new BackgroundModel(parcel);
        }

        public BackgroundModel[] newArray(int i) {
            return new BackgroundModel[i];
        }
    };
    public int bgNavigation;
    public int bgRoot;
    public int endGradient;
    public int startGradient;

    public int describeContents() {
        return 0;
    }

    public BackgroundModel(int i, int i2, int i3, int i4) {
        this.bgRoot = i;
        this.bgNavigation = i2;
        this.startGradient = i3;
        this.endGradient = i4;
    }

    public BackgroundModel(Parcel parcel) {
        this.bgRoot = parcel.readInt();
        this.bgNavigation = parcel.readInt();
        this.startGradient = parcel.readInt();
        this.endGradient = parcel.readInt();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.bgRoot);
        parcel.writeInt(this.bgNavigation);
        parcel.writeInt(this.startGradient);
        parcel.writeInt(this.endGradient);
    }
}
