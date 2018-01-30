package com.jraynolds.mypantry.adapters;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jraynolds.mypantry.R;
import com.jraynolds.mypantry.main.Globals;
import com.jraynolds.mypantry.objects.Ingredient;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Jasper on 1/29/2018.
 */

public class Adapter_Category extends RecyclerView.Adapter<Adapter_Category.MyViewHolder> {

    private ArrayList<ArrayList<Ingredient>> categories = new ArrayList<>();
    private final View rootView;
    private Adapter_Ingredient ingAdapt;
    private final String searchStr;

    public Adapter_Category(View rootView, String searchStr) {
        this.rootView = rootView;
        this.searchStr = searchStr;
        categories = splitCategories(Globals.getIngredients(null, false, null, searchStr));
    }

    public void update() {
        categories = splitCategories(Globals.getIngredients(null, false, null, searchStr));
        this.notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public final TextView categoryTitle;
        public final ExpandableLayout expandable;
        public final RecyclerView list;

        public MyViewHolder(View view) {
            super(view);
            categoryTitle = view.findViewById(R.id.layout_categoryTitle);
            expandable = view.findViewById(R.id.layout_expandable);
            list = view.findViewById(R.id.layout_ingredientList);
        }
    }

    public ArrayList<ArrayList<Ingredient>> splitCategories(ArrayList<Ingredient> ingredients) {
        ArrayList<ArrayList<Ingredient>> groupedIngredients = new ArrayList<>();

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
            groupedIngredients.add(inCategory);
        }

        return groupedIngredients;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_category, parent, false);

        return new Adapter_Category.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        ArrayList<Ingredient> category = categories.get(position);
        //organize
        Collections.sort(category, new Comparator<Ingredient>() {
            @Override
            public int compare(Ingredient t1, Ingredient t2) {
                return t1.title.compareToIgnoreCase(t2.title);
            }
        });
        Ingredient modelIngredient = category.get(0);

        RecyclerView recyclerView = holder.list;
        Log.d("category", holder.categoryTitle.toString());
        Log.d("category", modelIngredient.category);
        //noinspection AndroidLintSetTextI18n
        holder.categoryTitle.setText(modelIngredient.category + " (" + category.size() + ")");
        holder.categoryTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.expandable.toggle();
            }
        });

        ingAdapt = new Adapter_Ingredient(category);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(rootView.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(ingAdapt);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}