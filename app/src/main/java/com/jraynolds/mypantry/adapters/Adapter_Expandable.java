package com.jraynolds.mypantry.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jraynolds.mypantry.R;
import com.jraynolds.mypantry.objects.Category;
import com.jraynolds.mypantry.objects.Ingredient;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.models.ExpandableListPosition;

import java.util.List;

/**
 * Created by Jasper on 1/31/2018.
 */

public class Adapter_Expandable extends ExpandableRecyclerViewAdapter<Adapter_Category, Adapter_Ingredient> {

    private Context context;
    private String searchStr;

    public Adapter_Expandable(List<? extends ExpandableGroup> categories, Context context, String searchStr) {
        super(categories);
        this.context = context;
        this.searchStr = searchStr;
        Log.d("calling",  searchStr + ": adapter expandable created!");
    }

    @Override
    public Adapter_Category onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_category, parent, false);
        Log.d("calling", searchStr + ": adapter expandable group view created!");
        return new Adapter_Category(view);
    }

    @Override
    public Adapter_Ingredient onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_ingredient, parent, false);
        Log.d("calling", searchStr + ": adapter expandable child view created!");
        return new Adapter_Ingredient(view, context);
    }

    @Override
    public void onBindChildViewHolder(Adapter_Ingredient holder, int flatPosition, ExpandableGroup group, int childIndex) {
        final Ingredient i = (Ingredient) group.getItems().get(childIndex);
        holder.setIngredient(i);
        Log.d("calling", searchStr + ": adapter expandable child view bound!");
    }

    @Override
    public void onBindGroupViewHolder(Adapter_Category holder, int flatPosition, ExpandableGroup group) {
        holder.setCategory(group);
        Log.d("calling", searchStr + ": adapter expandable group view bound!");
    }
}
