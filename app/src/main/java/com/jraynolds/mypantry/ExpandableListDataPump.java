package com.jraynolds.mypantry;

import android.util.Log;

import com.jraynolds.mypantry.main.Globals;
import com.jraynolds.mypantry.objects.Ingredient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by Jasper on 2/3/2018.
 */

public class ExpandableListDataPump {

    public static TreeMap<String, List<Ingredient>> getData(String location) {
        List<Ingredient> ingredients = Globals.getIngredients(null, false, null, location);

        Log.d("ingredientsSearch", location + " = " + ingredients.toString());

        return splitCategories(ingredients);

    }

    public static void sortIngredients(List<Ingredient> list) {
        Collections.sort(list, new Comparator<Ingredient>() {
            @Override
            public int compare(Ingredient t1, Ingredient t2) {
                return t1.title.compareToIgnoreCase(t2.title);
            }
        });
    }

    public static TreeMap<String, List<Ingredient>> splitCategories(List<Ingredient> ingredients) {
        TreeMap<String, List<Ingredient>> categories = new TreeMap<>();

        //populate categories
        List<String> categoryNames = new ArrayList<>();
        for(int i=0; i<ingredients.size(); i++) if(!categoryNames.contains(ingredients.get(i).category)) categoryNames.add(ingredients.get(i).category);
        //for each category...
        for(int i=0; i<categoryNames.size(); i++) {
            //get all from category
            List<Ingredient> inCategory = new ArrayList<>();
            for(int j=0; j<ingredients.size(); j++) if(ingredients.get(j).category.toLowerCase().equals(categoryNames.get(i).toLowerCase())) inCategory.add(ingredients.get(j));
            //remove from ingredients
            ingredients.removeAll(inCategory);
            //sort
            sortIngredients(inCategory);
            //add to array
            categories.put(categoryNames.get(i), inCategory);
        }

        return categories;
    }
}
