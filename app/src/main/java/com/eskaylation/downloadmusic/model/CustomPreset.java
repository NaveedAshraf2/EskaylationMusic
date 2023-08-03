package com.eskaylation.downloadmusic.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class CustomPreset implements Parcelable {
    public static final Creator<CustomPreset> CREATOR = new Creator<CustomPreset>() {
        public CustomPreset createFromParcel(Parcel parcel) {
            return new CustomPreset(parcel);
        }

        public CustomPreset[] newArray(int i) {
            return new CustomPreset[i];
        }
    };
    public int id;
    public String presetName;
    public int slider1;
    public int slider2;
    public int slider3;
    public int slider4;
    public int slider5;

    public int describeContents() {
        return 0;
    }

    public CustomPreset() {
    }

    public CustomPreset(String str, int i, int i2, int i3, int i4, int i5) {
        this.presetName = str;
        this.slider1 = i;
        this.slider2 = i2;
        this.slider3 = i3;
        this.slider4 = i4;
        this.slider5 = i5;
    }

    public CustomPreset(Parcel parcel) {
        this.id = parcel.readInt();
        this.presetName = parcel.readString();
        this.slider1 = parcel.readInt();
        this.slider2 = parcel.readInt();
        this.slider3 = parcel.readInt();
        this.slider4 = parcel.readInt();
        this.slider5 = parcel.readInt();
    }

    public void setSlider1(int i) {
        this.slider1 = i;
    }

    public void setSlider2(int i) {
        this.slider2 = i;
    }

    public void setSlider3(int i) {
        this.slider3 = i;
    }

    public void setSlider4(int i) {
        this.slider4 = i;
    }

    public void setSlider5(int i) {
        this.slider5 = i;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CustomPreset{id=");
        sb.append(this.id);
        sb.append(", presetName='");
        sb.append(this.presetName);
        sb.append('\'');
        sb.append(", slider1=");
        sb.append(this.slider1);
        sb.append(", slider2=");
        sb.append(this.slider2);
        sb.append(", slider3=");
        sb.append(this.slider3);
        sb.append(", slider4=");
        sb.append(this.slider4);
        sb.append(", slider5=");
        sb.append(this.slider5);
        sb.append('}');
        return sb.toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.id);
        parcel.writeString(this.presetName);
        parcel.writeInt(this.slider1);
        parcel.writeInt(this.slider2);
        parcel.writeInt(this.slider3);
        parcel.writeInt(this.slider4);
        parcel.writeInt(this.slider5);
    }
}
