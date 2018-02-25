package com.jraynolds.mypantry.main;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.jraynolds.mypantry.CustomExpandableListAdapter;
import com.jraynolds.mypantry.R;
import com.jraynolds.mypantry.objects.Ingredient;
import com.jraynolds.mypantry.objects.Recipe;
import com.jraynolds.mypantry.utilities.JSONreader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by Jasper on 1/24/2018.
 */

public class Globals extends Application {

    private static Context context;

    private static HashMap<String, Ingredient> globalIngredients;
    private static HashMap<String, Ingredient> localIngredients;
    private static HashMap<String, Ingredient> ingredients;

    private static ArrayList<Recipe> recipes;

    public static HashMap<String, CustomExpandableListAdapter> tabAdapters;


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
        ingredients = reader.stringToMap(globalString);

        //grab from user-generated
        String localString = reader.inputToString(new FileInputStream(new File(context.getFilesDir() + getString(R.string.userIngredientsFile) + ".json")));
        ArrayList<Ingredient> locals = reader.stringToArray(localString);
        localIngredients = reader.stringToMap(localString);
        for(Ingredient i : locals) ingredients.put(i.title, i);
        Log.d("locals", String.valueOf(localIngredients.size()));
    }

//    private ArrayList<Recipe> loadRecipes() {
//        ArrayList<Recipe> recipes = new ArrayList<>();
//
//        return recipes;
//    }

    public static void addIngredient(Ingredient i) {
        //push to locals, globals
        localIngredients.put(i.title, i);
        ingredients.put(i.title, i);

        //update
        updateLists();

        //push to individual Json file
        JSONreader reader = new JSONreader(context);
        reader.saveToJSON(new ArrayList<>(localIngredients.values()), context.getFilesDir() + "ingredients_added" + ".json");
    }

//    public static void addRecipe(String title, String description, String image, ArrayList<String> steps) {
//        //push to globalRecipes
//        //push to individual Json file
//    }

    public static ArrayList<Ingredient> getIngredients(String title, boolean isExact, String category, String location) {

        HashMap<String, Ingredient> matches = new HashMap<>();

        for(Ingredient i : ingredients.values()) {
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

    public static void addTab(String s, CustomExpandableListAdapter adapter) {
        tabAdapters.put(s, adapter);
    }

    public static void updateLists() {
        for(Map.Entry<String, CustomExpandableListAdapter> entry : tabAdapters.entrySet()) {
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
}
