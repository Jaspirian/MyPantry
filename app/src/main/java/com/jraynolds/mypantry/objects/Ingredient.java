package com.jraynolds.mypantry.objects;

import android.content.Context;
import android.content.SharedPreferences;
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

public class Ingredient {

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

    public boolean isInPantry(Context context) {
        SharedPreferences pantryPrefs = context.getSharedPreferences("pantryPrefs", 0);
        return pantryPrefs.getBoolean(this.title, false);
    }

    public void setIsInPantry(boolean isInPantry, Context context) {
        SharedPreferences pantryPrefs = context.getSharedPreferences("pantryPrefs", 0);
        SharedPreferences.Editor editor = pantryPrefs.edit();
        editor.putBoolean(this.title, isInPantry);
        editor.apply();
    }

    public boolean toggleIsInPantry(Context context) {
        SharedPreferences pantryPrefs = context.getSharedPreferences("pantryPrefs", 0);
        SharedPreferences.Editor editor = pantryPrefs.edit();
        boolean isInPantry = this.isInPantry(context);
        editor.putBoolean(this.title, !isInPantry);
        editor.apply();
        return !isInPantry;
    }

    public boolean isOnList(Context context) {
        SharedPreferences shoppingPrefs = context.getSharedPreferences("shoppingPrefs", 0);
        return shoppingPrefs.getBoolean(this.title, false);
    }

    public void setIsOnList(boolean isOnList, Context context) {
        SharedPreferences shoppingPrefs = context.getSharedPreferences("shoppingPrefs", 0);
        SharedPreferences.Editor editor = shoppingPrefs.edit();
        editor.putBoolean(this.title, isOnList);
        editor.apply();
    }

    public boolean toggleIsOnList(Context context) {
        SharedPreferences shoppingPrefs = context.getSharedPreferences("shoppingPrefs", 0);
        SharedPreferences.Editor editor = shoppingPrefs.edit();
        boolean isOnList = this.isOnList(context);
        editor.putBoolean(this.title, !isOnList);
        editor.apply();
        return !isOnList;
    }
}
