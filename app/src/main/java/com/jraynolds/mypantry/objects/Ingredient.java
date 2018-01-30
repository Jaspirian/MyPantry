package com.jraynolds.mypantry.objects;

import java.io.Serializable;

/**
 * Created by Jasper on 1/20/2018.
 */

public class Ingredient implements Serializable {

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

}
