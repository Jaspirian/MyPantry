package com.jraynolds.mypantry;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Jasper on 1/24/2018.
 */

public class JSONreader {

    private Context context;

    public JSONreader(Context context) {
        this.context = context;
    }

    private String JSONtoString(String filename) {
        try {
            Log.d("JSON", context.toString());
            Log.d("JSON", context.getAssets().toString());
            Log.d("JSON", filename + ".json");
            InputStream is = context.getAssets().open(filename + ".json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public ArrayList<Ingredient> JSONtoIngredientArrayList(String filename) {
        ArrayList<Ingredient> ingredients = null;
        String json = JSONtoString(filename);
        Gson gson = new Gson();
        ingredients = gson.fromJson(json, new TypeToken<ArrayList<Ingredient>>(){}.getType());
        return ingredients;
    }

    public ArrayList<Recipe> JSONtoRecipeArrayList(String filename) {
        ArrayList<Recipe> recipes = null;
        String json = JSONtoString(filename);
        Gson gson = new Gson();
        recipes = gson.fromJson(json, new TypeToken<ArrayList<Recipe>>(){}.getType());
        return recipes;
    }
}
