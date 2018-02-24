package com.jraynolds.mypantry.objects;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.jraynolds.mypantry.CustomExpandableListAdapter;
import com.jraynolds.mypantry.main.Globals;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

/**
 * Created by Jasper on 1/20/2018.
 */

public class Ingredient implements Parcelable, Serializable {

    public String title;
    public String description;
    public String imageUrl;
    public String category;

    public Ingredient(String title, String description, String imageUrl, String category) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    protected Ingredient(Parcel in) {
        title = in.readString();
        description = in.readString();
        imageUrl = in.readString();
        category = in.readString();
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

    public boolean isInList(String searchStr, Context context) {
        SharedPreferences prefs = context.getSharedPreferences(searchStr + "Prefs", 0);
        return prefs.getBoolean(this.title, false);
    }

    public void setIsInList(String searchStr, boolean isInList, Context context) {
        SharedPreferences prefs = context.getSharedPreferences(searchStr + "Prefs", 0);
        //if we're describing a different state,
        if(prefs.getBoolean(this.title, false) != isInList) {
            //change it
            putToPreferences(prefs.edit(), isInList);

            //then toggle the adapter
            CustomExpandableListAdapter adapter = Globals.tabAdapters.get(searchStr);
            adapter.toggleIngredient(this, isInList);
        }
    }

    public void toggleIsInList(String searchStr, Context context) {
        //toggle it
        SharedPreferences prefs = context.getSharedPreferences(searchStr + "Prefs", 0);
        boolean isInList = !this.isInList(searchStr, context);
        putToPreferences(prefs.edit(), isInList);
        //then toggle the adapter
        CustomExpandableListAdapter adapter = Globals.tabAdapters.get(searchStr);
        adapter.toggleIngredient(this, isInList);
    }

    public void putToPreferences(SharedPreferences.Editor editor, boolean bool) {
        editor.putBoolean(this.title, bool);
        editor.apply();
    }

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
    }
}
