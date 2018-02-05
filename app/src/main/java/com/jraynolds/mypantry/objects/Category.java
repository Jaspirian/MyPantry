package com.jraynolds.mypantry.objects;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.models.ExpandableListPosition;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jasper on 1/31/2018.
 */



public class Category extends ExpandableGroup<Ingredient> {

    private String title;
    private List<Ingredient> ingredients;

    public Category(String title, List<Ingredient> ingredients) {
        super(title, ingredients);
        this.title = title;
        this.ingredients = ingredients;
    }

    public void addIngredient(Ingredient i) {

    }

    public void removeIngredient(int position) {
        ingredients.remove(position);
    }
}
