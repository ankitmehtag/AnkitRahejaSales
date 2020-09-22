package com.model;

import android.os.Parcel;
import android.os.Parcelable;

public class AsmHistoryModel implements Parcelable {
    private String time;
    private String description;
    private String recording;

    public String getTime() {
        return time;

        }

    public String getDescription() {
        return description;
    }

    public String getRecording() {
        return recording;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.time);
        dest.writeString(this.description);
        dest.writeString(this.recording);
    }

    protected AsmHistoryModel(Parcel in) {

        this.time = in.readString();
        this.description = in.readString();
        this.recording = in.readString();

         }

    public static final Creator<AsmHistoryModel> CREATOR = new Creator<AsmHistoryModel>() {
        @Override
        public AsmHistoryModel createFromParcel(Parcel source) {

            return new AsmHistoryModel(source);

        }

        @Override
        public AsmHistoryModel[] newArray(int size) {
            return new AsmHistoryModel[size];
        }
    };
}
