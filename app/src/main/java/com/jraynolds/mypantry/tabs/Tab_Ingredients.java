package com.jraynolds.mypantry.tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jraynolds.mypantry.adapters.Adapter_Category;
import com.jraynolds.mypantry.R;
import com.jraynolds.mypantry.adapters.Adapter_Expandable;
import com.jraynolds.mypantry.main.Globals;
import com.jraynolds.mypantry.objects.Category;
import com.jraynolds.mypantry.objects.Ingredient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Jasper on 1/20/2018.
 */

public class Tab_Ingredients extends Fragment {

    private String searchStr;
    private RecyclerView recyclerView;
    private View rootView;
    private Adapter_Expandable expandAdapt;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchStr = this.getArguments().getString("searchStr");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_ingredients, container, false);

        recyclerView = rootView.findViewById(R.id.ingredients_list);

        createList();
        return rootView;
    }

    public void createList() {
        List<Category> categories = splitCategories(Globals.getIngredients(null, false, null, searchStr));
        expandAdapt = new Adapter_Expandable(categories, getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(expandAdapt);
    }

    public List<Category> splitCategories(ArrayList<Ingredient> ingredients) {
        List<Category> groupedIngredients = new ArrayList<>();

        //populate categories
        ArrayList<String> categories = new ArrayList<>();
        for(int i=0; i<ingredients.size(); i++) if(!categories.contains(ingredients.get(i).category)) categories.add(ingredients.get(i).category);
        //for each category...
        for(int i=0; i<categories.size(); i++) {
            //get all from category
            ArrayList<Ingredient> inCategory = new ArrayList<>();
            for(int j=0; j<ingredients.size(); j++) if(ingredients.get(j).category.toLowerCase().equals(categories.get(i).toLowerCase())) inCategory.add(ingredients.get(j));
            //remove from ingredients
            ingredients.removeAll(inCategory);
            //sort
            Collections.sort(inCategory, new Comparator<Ingredient>() {
                @Override
                public int compare(Ingredient t1, Ingredient t2) {
                    return t1.title.compareToIgnoreCase(t2.title);
                }
            });
            //add to array
            groupedIngredients.add(new Category(inCategory.get(0).category, inCategory));
        }

        return groupedIngredients;
    }

}
