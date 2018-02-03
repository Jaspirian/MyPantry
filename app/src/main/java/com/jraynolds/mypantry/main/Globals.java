package com.jraynolds.mypantry.main;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.jraynolds.mypantry.adapters.Adapter_Expandable;
import com.jraynolds.mypantry.objects.Ingredient;
import com.jraynolds.mypantry.objects.Recipe;
import com.jraynolds.mypantry.tabs.Tab_Ingredients;
import com.jraynolds.mypantry.utilities.JSONreader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Jasper on 1/24/2018.
 */

public class Globals extends Application {

    private static Context context;

    private static ArrayList<Ingredient> ingredients;
    private static ArrayList<Ingredient> defaultIngredients;
    private static ArrayList<Ingredient> localIngredients;
    private static ArrayList<Recipe> globalRecipes;

    public static HashMap<String, Tab_Ingredients> ingredientTabs;


    public void onCreate() {
        super.onCreate();
        context = this;
        ingredients = loadIngredients();
        globalRecipes = loadRecipes();
        ingredientTabs = new HashMap<>();
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
        updateLists(null);
        //push to individual Json file
        JSONreader reader = new JSONreader(context);
        reader.saveToJSON(localIngredients, context.getFilesDir() + "ingredients_local" + ".json");
    }

    public static void addRecipe(String title, String description, String image, ArrayList<String> steps) {
        //push to globalRecipes
        //push to individual Json file
    }

    public static ArrayList<Ingredient> getIngredients(String searchStr, boolean isExact, String category, String location) {

        ArrayList<Ingredient> subIngredients = new ArrayList<>();

        for(int i = 0; i<ingredients.size(); i++) {
            Ingredient ing = ingredients.get(i);
            if(location.equals("all") || (location.equals("pantry") && ing.isInPantry) || (location.equals("shopping") && ing.isOnList)) {
                if(category == null || category.toLowerCase().equals(ing.category.toLowerCase())) {
                    if (searchStr != null) {
                        if (isExact && !ing.title.toLowerCase().equals(searchStr.toLowerCase())) {
                            continue;
                        } else if (!isExact && !ing.title.toLowerCase().contains(searchStr.toLowerCase())) {
                            continue;
                        }
                    }
                    subIngredients.add(ing);
                }
            }
        }

        return subIngredients;
    }

    public static void addTab(String s, Tab_Ingredients tab) {
        ingredientTabs.put(s, tab);
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
        updateLists(null);
    }
}
