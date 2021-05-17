package com.faint.cucina.classes;

import android.os.Parcel;
import android.os.Parcelable;

public class Announcement implements Parcelable { // made it parcelable to pass list via intent

    private int image, type;
    private String title, desc;

    // these are all types of news (for comfortable usage within project)
    public final static int TYPE_NEW_LOCATION = 0;
    public final static int TYPE_GOOD_NEWS = 1;
    public final static int TYPE_WARNING = 2;
    public final static int TYPE_BAD_NEWS = 3;

    public Announcement(int image, int type, String title, String desc) {
        this.image = image;
        this.type = type;
        this.title = title;
        this.desc = desc;
    }

    protected Announcement(Parcel in) {
        image = in.readInt();
        type = in.readInt();
        title = in.readString();
        desc = in.readString();
    }

    public static final Creator<Announcement> CREATOR = new Creator<Announcement>() {
        @Override
        public Announcement createFromParcel(Parcel in) {
            return new Announcement(in);
        }

        @Override
        public Announcement[] newArray(int size) {
            return new Announcement[size];
        }
    };

    public int getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public int getType() {
        return type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(image);
        parcel.writeInt(type);
        parcel.writeString(title);
        parcel.writeString(desc);
    }
}
