package com.jraynolds.mypantry;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import org.json.JSONArray;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Jasper on 1/24/2018.
 */

public class Globals extends Application {

    private static Context context;

    private static ArrayList<Ingredient> globalIngredients;
    private static ArrayList<Ingredient> localIngredients;
    private static ArrayList<Recipe> globalRecipes;

    private static Tab_Recipes recipesTab;
    private static Tab_Pantry pantryTab;
    private static Tab_Shopping shoppingTab;

    private static Adapter_PantryIngredients pantryAdapter;
    private static Adapter_ShoppingIngredients shoppingAdapter;

    public void onCreate() {
        super.onCreate();
        context = this;
        globalIngredients = loadIngredients();
        globalRecipes = loadRecipes();
    }

    private ArrayList<Ingredient> loadIngredients() {
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
        ArrayList<Ingredient> defaults = new ArrayList<>();
        try {
            String ingredientsString = reader.inputToString(this.getAssets().open("ingredients_default" + ".json"));
            defaults = reader.stringToIngredients(ingredientsString);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //only add copies
        globalIngredients = new ArrayList<>();
        globalIngredients.addAll(localIngredients);
        for(int i=0; i<defaults.size(); i++) {
            Ingredient defaultIngredient = defaults.get(i);
            boolean alreadyContained = false;
            for(int j=0; i<localIngredients.size(); i++) {
                Ingredient comparingIngredient = localIngredients.get(j);
                if(defaultIngredient.title.toLowerCase().equals(comparingIngredient.title.toLowerCase())) {
                    alreadyContained = true;
                    break;
                }
            }
            if(!alreadyContained) globalIngredients.add(defaultIngredient);
        }

        //order by title
        Collections.sort(globalIngredients, new Comparator<Ingredient>() {
            @Override
            public int compare(Ingredient t1, Ingredient t2) {
                return t1.title.compareToIgnoreCase(t2.title);
            }
        });

        return globalIngredients;
    }

    private ArrayList<Recipe> loadRecipes() {
        ArrayList<Recipe> recipes = new ArrayList<>();

        return recipes;
    }

    public static void addIngredient(String title, String description, String image, boolean isInPantry, boolean isOnList) {
        Log.d("ingredient", title);
        Log.d("ingredient", description);
//        Log.d("ingredient", title);
        Log.d("ingredient", Boolean.toString(isInPantry));
        Log.d("ingredient", Boolean.toString(isOnList));
        //push to globalIngredients
        Ingredient newIngredient = new Ingredient(title, description, image, isInPantry, isOnList);
        localIngredients.add(newIngredient);
        globalIngredients.add(newIngredient);
        //update
        updateLists();
        //push to individual Json file
        JSONreader reader = new JSONreader(context);
        reader.saveToJSON(localIngredients, context.getFilesDir() + "ingredients_local" + ".json");
    }

    public static void addRecipe(String title, String description, String image, ArrayList<String> steps) {
        //push to globalRecipes
        //push to individual Json file
    }

    public static ArrayList<Ingredient> getIngredients(String searchStr, boolean isExact, boolean includeNonPantry, boolean includeNonList) {

        ArrayList<Ingredient> subIngredients = new ArrayList<>();

        for(int i = 0; i<globalIngredients.size(); i++) {
            Ingredient ing = globalIngredients.get(i);
            if(ing.isInPantry && ing.isOnList) {
                subIngredients.add(ing);
            } else if(ing.isInPantry) {
                if(includeNonList) subIngredients.add(ing);
            } else if(ing.isOnList) {
                if(includeNonPantry) subIngredients.add(ing);
            } else {
                if(includeNonList && includeNonPantry) subIngredients.add(ing);
            }
        }

        ArrayList<Ingredient> searchedIngredients = new ArrayList<>();
        if(searchStr != null) {
            for(int i=0; i<subIngredients.size(); i++) {
                if(isExact) {
                    if(subIngredients.get(i).title.toLowerCase().equals(searchStr.toLowerCase())) searchedIngredients.add(subIngredients.get(i));
                } else {
                    if(subIngredients.get(i).title.toLowerCase().contains(searchStr.toLowerCase())) searchedIngredients.add(subIngredients.get(i));
                }
            }
        } else {
            searchedIngredients = subIngredients;
        }

        return searchedIngredients;
    }

    public static void setPantryAdapter(Adapter_PantryIngredients adapter) {
        pantryAdapter = adapter;
    }

    public static void setShoppingAdapter(Adapter_ShoppingIngredients adapter) {
        shoppingAdapter = adapter;
    }

    public static void updateLists() {
        pantryAdapter.updateIngredients();
        shoppingAdapter.updateIngredients();
    }
}
