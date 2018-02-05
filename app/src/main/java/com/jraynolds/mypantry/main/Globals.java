package com.jraynolds.mypantry.main;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.jraynolds.mypantry.CustomExpandableListAdapter;
import com.jraynolds.mypantry.objects.Ingredient;
import com.jraynolds.mypantry.objects.Recipe;
import com.jraynolds.mypantry.utilities.JSONreader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jasper on 1/24/2018.
 */

public class Globals extends Application {

    private static Context context;

    private static ArrayList<Ingredient> defaultIngredients;
    private static ArrayList<Ingredient> localIngredients;

    private static ArrayList<Recipe> globalRecipes;

    public static HashMap<String, CustomExpandableListAdapter> tabAdapters;


    public void onCreate() {
        super.onCreate();
        context = this;
        loadIngredients();

        globalRecipes = loadRecipes();

        tabAdapters = new HashMap<>();
    }

    private void loadIngredients() {
        JSONreader reader = new JSONreader(this);
        //grab from locals
        localIngredients = new ArrayList<>();
        try {
            String ingredientsString = reader.inputToString(context.openFileInput("ingredients_local" + ".json"));
            localIngredients = reader.stringToIngredients(ingredientsString);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //grab from app
        defaultIngredients = new ArrayList<>();
        try {
            String ingredientsString = reader.inputToString(this.getAssets().open("ingredients_default" + ".json"));
            defaultIngredients = reader.stringToIngredients(ingredientsString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Recipe> loadRecipes() {
        ArrayList<Recipe> recipes = new ArrayList<>();

        return recipes;
    }

    public static void addIngredient(Ingredient i, Boolean isInPantry, Boolean isOnList) {
        //push to globalIngredients;
        localIngredients.add(i);
        //update
        updateLists(null);
        //push to individual Json file
        JSONreader reader = new JSONreader(context);
        reader.saveToJSON(localIngredients, context.getFilesDir() + "ingredients_local" + ".json");
    }

    public static void addRecipe(String title, String description, String image, ArrayList<String> steps) {
        //push to globalRecipes
        //push to individual Json file
    }

    public static ArrayList<Ingredient> getIngredients(String title, boolean isExact, String category, String location) {

        HashMap<String, Ingredient> uniqueIngredients = new HashMap<>();

        for(Ingredient i : defaultIngredients) {
            //check locations
            if(location != null) {
                //continue if not in location
                if(location.equals("pantry")) if(!i.isInPantry(context)) continue;
                if(location.equals("shopping")) if(!i.isOnList(context)) continue;
            }
            //check category, continue if not equal
            if(category != null) if(!category.toLowerCase().equals(i.category.toLowerCase())) continue;
            //check title
            if (title != null) {
                if (isExact && !i.title.toLowerCase().equals(title.toLowerCase())) continue;
                else if (!isExact && !i.title.toLowerCase().contains(title.toLowerCase())) continue;
            }
            //if we got this far...
            uniqueIngredients.put(i.title, i);
        }

        for(Ingredient i : localIngredients) {
            //check locations
            if(location != null) {
                //continue if not in location
                if(location.equals("pantry")) if(!i.isInPantry(context)) continue;
                if(location.equals("shopping")) if(!i.isOnList(context)) continue;
            }
            //check category, continue if not equal
            if(category != null) if(!category.toLowerCase().equals(i.category.toLowerCase())) continue;
            //check title
            if (title != null) {
                if (isExact && !i.title.toLowerCase().equals(title.toLowerCase())) continue;
                else if (!isExact && !i.title.toLowerCase().contains(title.toLowerCase())) continue;
            }
            //if we got this far...
            uniqueIngredients.put(i.title, i);
        }

        ArrayList<Ingredient> ingredients = new ArrayList<>();
        ingredients.addAll(uniqueIngredients.values());
        Log.d("ingredients", location + " = " + ingredients.toString());
        for(Ingredient i : ingredients) {
            Log.d("ingredients", Boolean.toString(i.isInPantry(context)) + ", " + Boolean.toString(i.isOnList(context)));
        }

        return ingredients;
    }

    public static void addTab(String s, CustomExpandableListAdapter adapter) {
        tabAdapters.put(s, adapter);
    }

    public static void updateLists(String tab) {
//        if(tab != null) {
//            adapters.get(tab).notifyDataSetChanged();
//        } else {
//            for(int i=0; i<adapters.size(); i++) {
//                Log.d("adapter", adapters.get(i).toString());
//                adapters.get(i).notifyDataSetChanged();
//            }
//        }
    }

    public static void modifyIngredient(Ingredient ingredient) {
        //if in globals, add to local
        //if in local, modify
        int localIndex = -1;
        for(int i=0; i<localIngredients.size(); i++) {
            if(localIngredients.get(i).title.toLowerCase().equals(ingredient.title.toLowerCase())) {
                localIndex = i;
                break;
            }
        }
        Log.d("modifying (local)", Integer.toString(localIndex));

        if(localIndex != -1) localIngredients.remove(localIndex);
        Log.d("modifying", ingredient.description);
        addIngredient(ingredient, null, null);
    }

    public static void removeIngredientByTitle(String title) {
//        int ingredientsIndex = -1;
//        for(int i = 0; i<ingredients.size() && ingredientsIndex < 0; i++) if(ingredients.get(i).title.toLowerCase().equals(title.toLowerCase())) ingredientsIndex = i;
//
//        int localsIndex = -1;
//        for(int i = 0; i<localIngredients.size() && localsIndex < 0; i++) if(localIngredients.get(i).title.toLowerCase().equals(title.toLowerCase())) localsIndex = i;
//
//        Log.d("removing globals", Integer.toString(ingredientsIndex));
//        Log.d("removing locals", Integer.toString(localsIndex));
//
//        if(localsIndex != -1) localIngredients.remove(localsIndex);
//        ingredients.remove(ingredientsIndex);
//        updateLists(null);
    }
}
