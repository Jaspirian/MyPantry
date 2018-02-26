package com.jraynolds.mypantry.main;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.jraynolds.mypantry.lists.IngredientExpandableListAdapter;
import com.jraynolds.mypantry.R;
import com.jraynolds.mypantry.objects.Ingredient;
import com.jraynolds.mypantry.objects.Recipe;
import com.jraynolds.mypantry.utilities.JSONreader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Jasper on 1/24/2018.
 */

public class Globals extends Application {

    private static Context context;

    private static HashMap<String, Ingredient> globalIngredients;
    private static HashMap<String, Ingredient> localIngredients;
    private static HashMap<String, Ingredient> ingredients;

    public static HashMap<String, IngredientExpandableListAdapter> tabAdapters;

    public void onCreate() {
        super.onCreate();
        context = this;

        globalIngredients = new HashMap<>();
        localIngredients = new HashMap<>();
        ingredients = new HashMap<>();
        try {
            loadIngredients();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        try {
//            recipes = loadRecipes();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        tabAdapters = new HashMap<>();
    }

    private void loadIngredients() throws IOException {
        JSONreader reader = new JSONreader(this);

        String globalString = reader.inputToString(this.getAssets().open(getString(R.string.defaultIngredientsFile) + ".json"));
        globalIngredients = reader.stringToMap(globalString);
        //prune blocked
        Iterator<Ingredient> iter = globalIngredients.values().iterator();
        while(iter.hasNext()) if(iter.next().isInList("block", context)) iter.remove();
        ingredients = new HashMap<>(globalIngredients);

        //grab from user-generated
        String localString = reader.inputToString(new FileInputStream(new File(context.getFilesDir() + getString(R.string.userIngredientsFile) + ".json")));
        ArrayList<Ingredient> locals = reader.stringToArray(localString);
        localIngredients = reader.stringToMap(localString);
        for(Ingredient i : locals) {
            i.isUserCreated = true;
            ingredients.put(i.title, i);
        }
    }

    public static void addIngredient(Ingredient i) {
        //push to locals, globals
        i.isUserCreated = true;
        localIngredients.put(i.title, i);
        ingredients.put(i.title, i);

        //update
        updateLists();

        //push to individual Json file
        JSONreader reader = new JSONreader(context);
        reader.saveToJSON(new ArrayList<>(localIngredients.values()), context.getFilesDir() + "ingredients_added" + ".json");
    }

    public static void deleteIngredient(Ingredient i) {
        //remove from globals
        ingredients.remove(i);

        if(localIngredients.containsKey(i)) {
            localIngredients.remove(i.title);
            i.delete(context);
            //remove block
            i.setIsInList("block", false, context);
        } else {
            i.delete(context);
            //add block
            i.setIsInList("block", true, context);
        }

        updateLists();
    }

    public static ArrayList<Ingredient> getIngredients(String title, boolean isExact, String category, String location) {

        HashMap<String, Ingredient> matches = new HashMap<>();

        for(Ingredient i : ingredients.values()) {
            if(i.isInList("block", context)) continue;
            //check locations
            if(location != null) {
                //continue if not in location
                if(location.equals("pantry")) if(!i.isInList("pantry", context)) continue;
                if(location.equals("shopping")) if(!i.isInList("shopping", context)) continue;
            }
            //check category, continue if not equal
            if(category != null) if(!category.toLowerCase().equals(i.category.toLowerCase())) continue;
            //check title
            if (title != null) {
                if (isExact && !i.title.toLowerCase().equals(title.toLowerCase())) continue;
                else if (!isExact && !i.title.toLowerCase().contains(title.toLowerCase())) continue;
            }
            //if we got this far...
            matches.put(i.title, i);
        }

        return new ArrayList<>(matches.values());
    }

    public static ArrayList<String> getIngredientCategories() {
        ArrayList<String> categories = new ArrayList<>();

        for(Ingredient i : ingredients.values()) if(!categories.contains(i.category)) categories.add(i.category);

        return categories;
    }

    public static void addTab(String s, IngredientExpandableListAdapter adapter) {
        tabAdapters.put(s, adapter);
    }

    public static void updateLists() {
        for(Map.Entry<String, IngredientExpandableListAdapter> entry : tabAdapters.entrySet()) {
            entry.getValue().notifyDataSetChanged();
        }
    }

    public static void wipeUserMemory() {
        // remove all from shared
        for(Map.Entry<String, Ingredient> entry : localIngredients.entrySet()) {
            entry.getValue().delete(context);
            ingredients.remove(entry.getKey());
        }
        // empty
        localIngredients.clear();

        //set all to not in lists
        for(Ingredient i : ingredients.values()) {
            i.setIsInList("pantry", false, context);
            i.setIsInList("shopping", false, context);
        }

        //push to individual Json file
        JSONreader reader = new JSONreader(context);
        reader.saveToJSON(new ArrayList<>(localIngredients.values()), context.getFilesDir() + "ingredients_added" + ".json");

        for(String title : localIngredients.keySet()) Log.d("locals", title);

        updateLists();
    }

    public static void wipeListMemory() {
        //set all to not in lists
        for(Ingredient i : ingredients.values()) {
            i.setIsInList("pantry", false, context);
            i.setIsInList("shopping", false, context);
        }

        updateLists();
    }
}
