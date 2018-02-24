package com.jraynolds.mypantry.utilities;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jraynolds.mypantry.objects.Ingredient;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Jasper on 1/24/2018.
 */

public class JSONreader {

    private Context context;

    public JSONreader(Context context) {
        this.context = context;
    }

    public String inputToString(InputStream is) throws IOException {
        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();
        return new String(buffer, "UTF-8");
    }

    public ArrayList<Ingredient> stringToArray(String s) {
        ArrayList<Ingredient> ingredients = null;
        Gson gson = new Gson();
        ingredients = gson.fromJson(s, new TypeToken<ArrayList<Ingredient>>(){}.getType());
        return ingredients;
    }

    public HashMap<String, Ingredient> stringToMap(String s) {
        ArrayList<Ingredient> list = stringToArray(s);
        HashMap<String, Ingredient> map = new HashMap<>();
        for(Ingredient i : list) map.put(i.title, i);
        return map;
    }

    public void saveToJSON(ArrayList<Ingredient> arr, String filename) {

        Gson gson = new Gson();
        String json = gson.toJson(arr);
        try {
            FileWriter writer = new FileWriter(new File(filename));
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
