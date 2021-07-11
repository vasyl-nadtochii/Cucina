package com.faint.cucina.classes;

import android.os.Parcel;
import android.os.Parcelable;

public class Announcement implements Parcelable { // made it parcelable to pass list via intent

    private final int type;
    private final String image_url;
    private String title;
    private String desc;
    private String end_date;
    private final String city;

    // these are all types of news (for comfortable usage within project)
    public final static int TYPE_NEW_LOCATION = 0;
    public final static int TYPE_GOOD_NEWS = 1;
    public final static int TYPE_WARNING = 2;
    public final static int TYPE_BAD_NEWS = 3;

    public Announcement(String image_url, int type, String title, String desc, String city, String end_date) {
        this.image_url = image_url;
        this.type = type;
        this.title = title;
        this.desc = desc;
        this.city = city;
        this.end_date = end_date;
    }

    protected Announcement(Parcel in) {
        image_url = in.readString();
        type = in.readInt();
        title = in.readString();
        desc = in.readString();
        city = in.readString();
        end_date = in.readString();
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

    public String getImageUrl() {
        return image_url;
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

    public String getCity() {
        return city;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(image_url);
        parcel.writeInt(type);
        parcel.writeString(title);
        parcel.writeString(desc);
        parcel.writeString(city);
        parcel.writeString(end_date);
    }
}
