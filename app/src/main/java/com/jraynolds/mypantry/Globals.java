package com.jraynolds.mypantry;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.stream.Stream;

/**
 * Created by Jasper on 1/24/2018.
 */

public class Globals extends Application {

    private static ArrayList<Ingredient> globalIngredients;
    private static ArrayList<Recipe> globalRecipes;

    private static Tab_Recipes recipesTab;
    private static Tab_Pantry pantryTab;
    private static Tab_Shopping shoppingTab;

    private static Adapter_PantryIngredients pantryAdapter;
    private static Adapter_ShoppingIngredients shoppingAdapter;

    public void onCreate() {
        super.onCreate();
        globalIngredients = loadIngredients();
        globalRecipes = loadRecipes();
    }

    public ArrayList<Ingredient> loadIngredients() {
        JSONreader reader = new JSONreader(this);
        ArrayList<Ingredient> ingredients = reader.JSONtoIngredientArrayList("ingredients_local");
        ArrayList<Ingredient> defaultIngredients = reader.JSONtoIngredientArrayList("ingredients_default");
        Log.d("what", ingredients.toString());
        if(ingredients != null || ingredients.isEmpty()) {
            for (int i = 0; i < defaultIngredients.size(); i++) {
                Ingredient defaultIngredient = defaultIngredients.get(i);
                boolean alreadyContained = false;
                for (int j = 0; j < ingredients.size(); j++) {
                    Log.d("comparing", defaultIngredient.title);
                    if (defaultIngredient.title.toLowerCase().equals(ingredients.get(j).title.toLowerCase())) {
                        alreadyContained = true;
                        break;
                    }
                }
                if (!alreadyContained) ingredients.add(defaultIngredient);
            }
        } else {
            ingredients = defaultIngredients;
        }

        return ingredients;
    }

    public ArrayList<Recipe> loadRecipes() {
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
        globalIngredients.add(newIngredient);
        //update
        updateLists();
        //push to individual Json file
//        Gson gson = new Gson();
//        String jsonString = gson.toJson(newIngredient);
//        FileWriter fileWriter = new FileWriter("ing")
    }

    public void addRecipe(String title, String description, String image, ArrayList<String> steps) {
        //push to globalRecipes
        //push to individual Json file
    }

    public static ArrayList<Ingredient> getIngredients(String searchStr, boolean includeNonPantry, boolean includeNonList) {

        ArrayList<Ingredient> subIngredients = new ArrayList<>();

        for(int i = 0; i< globalIngredients.size(); i++) {
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
                if(subIngredients.get(i).title.toLowerCase().contains(searchStr.toLowerCase())) searchedIngredients.add(subIngredients.get(i));
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
