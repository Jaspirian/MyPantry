package com.jraynolds.mypantry.objects;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.ArrayList;

/**
 * Created by Jasper on 1/31/2018.
 */

public class Category extends ExpandableGroup<Ingredient> {

    public Category(String title, ArrayList<Ingredient> ingredients) {
        super(title, ingredients);
    }
}
