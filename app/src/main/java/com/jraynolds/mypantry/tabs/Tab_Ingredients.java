package com.jraynolds.mypantry.tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.jraynolds.mypantry.CustomExpandableListAdapter;
import com.jraynolds.mypantry.ExpandableListDataPump;
import com.jraynolds.mypantry.adapters.Adapter_Category;
import com.jraynolds.mypantry.R;
import com.jraynolds.mypantry.adapters.Adapter_Expandable;
import com.jraynolds.mypantry.main.Globals;
import com.jraynolds.mypantry.objects.Category;
import com.jraynolds.mypantry.objects.Ingredient;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Jasper on 1/20/2018.
 */

public class Tab_Ingredients extends Fragment {

    private String searchStr;
    private Adapter_Expandable adapter;

    public void onCreate(Bundle savedInstanceState) {
        Log.d("calling", searchStr + ": tab created!");
        super.onCreate(savedInstanceState);
        searchStr = this.getArguments().getString("searchStr");
    }

//    @Override
//    public void onSaveInstanceState(Bundle outstate) {
//        super.onSaveInstanceState(outstate);
//        adapter.onSaveInstanceState(outstate);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d("calling", searchStr + ": tab view created!");
        View rootView = inflater.inflate(R.layout.fragment_all, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.all_ingredients_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        List<Ingredient> ingredients = Globals.getIngredients(null, false, null, searchStr);
        adapter = new Adapter_Expandable(splitIntoCategories(ingredients), getContext(), searchStr);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


//        ExpandableListView expandableListView = rootView.findViewById(R.id.all_ingredients_listView);
//        final LinkedHashMap<String, List<Ingredient>> categories = ExpandableListDataPump.getData(searchStr);
//        final List<String> categoryTitles = new ArrayList<>(categories.keySet());
//        CustomExpandableListAdapter adapter = new CustomExpandableListAdapter(this.getContext(), categoryTitles, categories);
//        Globals.addTab(searchStr, adapter);
//        expandableListView.setAdapter(adapter);

        return rootView;
    }

    private List<Category> splitIntoCategories(List<Ingredient> ingredients) {
        List<Category> categories = new ArrayList<>();

        //populate categories
        List<String> categoryNames = new ArrayList<>();
        for(int i=0; i<ingredients.size(); i++) if(!categoryNames.contains(ingredients.get(i).category)) categoryNames.add(ingredients.get(i).category);
        //for each category...
        for(int i=0; i<categoryNames.size(); i++) {
            //get all from category
            List<Ingredient> inCategory = new ArrayList<>();
            for(int j=0; j<ingredients.size(); j++) if(ingredients.get(j).category.toLowerCase().equals(categoryNames.get(i).toLowerCase())) inCategory.add(ingredients.get(j));
            //remove from ingredients
            ingredients.removeAll(inCategory);
            //sort
            sortIngredients(inCategory);
            //add to array
            categories.add(new Category(categoryNames.get(i), inCategory));
        }

        return categories;
    }

    private void sortIngredients(List<Ingredient> list) {
        Collections.sort(list, new Comparator<Ingredient>() {
            @Override
            public int compare(Ingredient t1, Ingredient t2) {
                return t1.title.compareToIgnoreCase(t2.title);
            }
        });
    }
}
