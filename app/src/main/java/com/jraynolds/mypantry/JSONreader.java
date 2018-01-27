package com.jraynolds.mypantry;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Created by Jasper on 1/24/2018.
 */

public class JSONreader {

    private Context context;

    public JSONreader(Context context) {
        this.context = context;
    }

    public String inputToString(InputStream is) throws IOException {
        Log.d("filejson 34", "file found");
        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();
        Log.d("filejson 39", new String(buffer, "UTF-8"));
        return new String(buffer, "UTF-8");
    }

    public ArrayList<Ingredient> stringToIngredients(String s) {
        ArrayList<Ingredient> ingredients = null;
        Gson gson = new Gson();
        ingredients = gson.fromJson(s, new TypeToken<ArrayList<Ingredient>>(){}.getType());
        return ingredients;
    }

    public void saveToJSON(ArrayList<Ingredient> arr, String filename) {

        Log.d("filejson 52", arr.toString());
        Gson gson = new Gson();
        String json = gson.toJson(arr);
        Log.d("filejson 55", "saving");
        try {
            FileWriter writer = new FileWriter(new File(filename));
            writer.write(json);
            writer.close();
            Log.d("filejson 60", "saved");
        } catch (IOException e) {
            Log.d("filejson 62", "error");
            e.printStackTrace();
        }

        try {
            Log.d("filejson 67", inputToString(context.openFileInput("ingredients_local" + ".json")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
