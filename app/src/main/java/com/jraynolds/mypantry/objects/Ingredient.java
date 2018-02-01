package com.jraynolds.mypantry.objects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

/**
 * Created by Jasper on 1/20/2018.
 */

public class Ingredient implements Serializable, Parcelable {

    public String title;
    public String description;
    public String imageUrl;
    public String category;
    public boolean isInPantry;
    public boolean isOnList;

    public Ingredient(String title, String description, String imageUrl, String category, boolean isInPantry, boolean isOnList) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.category = category;
        this.isInPantry = isInPantry;
        this.isOnList = isOnList;
    }

    protected Ingredient(Parcel in) {
        title = in.readString();
        description = in.readString();
        imageUrl = in.readString();
        category = in.readString();
        isInPantry = in.readByte() != 0;
        isOnList = in.readByte() != 0;
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(imageUrl);
        parcel.writeString(category);
        parcel.writeByte((byte) (isInPantry ? 1 : 0));
        parcel.writeByte((byte) (isOnList ? 1 : 0));
    }
}
