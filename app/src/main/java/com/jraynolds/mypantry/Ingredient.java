package com.jraynolds.mypantry;

/**
 * Created by Jasper on 1/20/2018.
 */

public class Ingredient {

    public String title;
    public String description;
    public String imageUrl;
    public boolean isInPantry;
    public boolean isOnList;

    public Ingredient(String title, String description, String imageUrl, boolean isInPantry, boolean isOnList) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.isInPantry = isInPantry;
        this.isOnList = isOnList;
    }

}
