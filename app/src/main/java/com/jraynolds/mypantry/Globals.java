package com.jraynolds.mypantry;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Jasper on 1/24/2018.
 */

public class Globals extends Application {

    private static Context context;

    private static ArrayList<Ingredient> ingredients;
    private static ArrayList<Ingredient> defaultIngredients;
    private static ArrayList<Ingredient> localIngredients;
    private static ArrayList<Recipe> globalRecipes;

    private static Tab_All recipesTab;
    private static Tab_Pantry pantryTab;
    private static Tab_Shopping shoppingTab;

    private static Adapter_Ingredients allAdapter;
    private static Adapter_Ingredients pantryAdapter;
    private static Adapter_Ingredients shoppingAdapter;


    public void onCreate() {
        super.onCreate();
        context = this;
        ingredients = loadIngredients();
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
        try {
            String ingredientsString = reader.inputToString(this.getAssets().open("ingredients_default" + ".json"));
            defaultIngredients = reader.stringToIngredients(ingredientsString);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //only add copies
        ingredients = new ArrayList<>();
        ingredients.addAll(localIngredients);
        for(int i=0; i<defaultIngredients.size(); i++) {
            Ingredient defaultIngredient = defaultIngredients.get(i);
            boolean alreadyContained = false;
            for(int j=0; i<localIngredients.size(); i++) {
                Ingredient comparingIngredient = localIngredients.get(j);
                if(defaultIngredient.title.toLowerCase().equals(comparingIngredient.title.toLowerCase())) {
                    alreadyContained = true;
                    break;
                }
            }
            if(!alreadyContained) ingredients.add(defaultIngredient);
        }

        return ingredients;
    }

    private ArrayList<Recipe> loadRecipes() {
        ArrayList<Recipe> recipes = new ArrayList<>();

        return recipes;
    }

    public static void addIngredient(Ingredient i) {
        Log.d("ingredient", i.title);
        Log.d("ingredient", i.description);
        Log.d("ingredient", Boolean.toString(i.isInPantry));
        Log.d("ingredient", Boolean.toString(i.isOnList));
        //push to globalIngredients;
        localIngredients.add(i);
        ingredients.add(i);
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

    public static ArrayList<Ingredient> getIngredients(String searchStr, boolean isExact, String include) {

        ArrayList<Ingredient> subIngredients = new ArrayList<>();

        for(int i = 0; i<ingredients.size(); i++) {
            Ingredient ing = ingredients.get(i);
            if(include.equals("all") || (include.equals("pantry") && ing.isInPantry) || (include.equals("shopping") && ing.isOnList)) {
                if(searchStr != null) {
                    if(isExact && !ing.title.toLowerCase().equals(searchStr.toLowerCase())) {
                        continue;
                    } else if(!isExact && !ing.title.toLowerCase().contains(searchStr.toLowerCase())) {
                        continue;
                    }
                }
                subIngredients.add(ing);
            }
        }

        return subIngredients;
    }

    public static void setPantryAdapter(Adapter_Ingredients adapter) {
        pantryAdapter = adapter;
    }

    public static void setShoppingAdapter(Adapter_Ingredients adapter) {
        shoppingAdapter = adapter;
    }

    public static void setAllAdapter(Adapter_Ingredients adapter) {
        allAdapter = adapter;
    }

    public static void updateLists() {
        allAdapter.updateIngredients();
        pantryAdapter.updateIngredients();
        shoppingAdapter.updateIngredients();
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

        int index = -1;
        for(int i=0; i<ingredients.size(); i++) {
            if(ingredients.get(i).title.toLowerCase().equals(ingredient.title.toLowerCase())) {
                index = i;
                break;
            }
        }
        Log.d("modifying (all)", Integer.toString(index));

        if(localIndex != -1) localIngredients.remove(localIndex);
        Log.d("modifying", ingredient.description);
        ingredients.remove(index);
        addIngredient(ingredient);
    }

    public static void removeIngredientByTitle(String title) {
        int ingredientsIndex = -1;
        for(int i = 0; i<ingredients.size() && ingredientsIndex < 0; i++) if(ingredients.get(i).title.toLowerCase().equals(title.toLowerCase())) ingredientsIndex = i;

        int localsIndex = -1;
        for(int i = 0; i<localIngredients.size() && localsIndex < 0; i++) if(localIngredients.get(i).title.toLowerCase().equals(title.toLowerCase())) localsIndex = i;

        Log.d("removing globals", Integer.toString(ingredientsIndex));
        Log.d("removing locals", Integer.toString(localsIndex));

        if(localsIndex != -1) localIngredients.remove(localsIndex);
        ingredients.remove(ingredientsIndex);
        updateLists();
    }
}
