package com.jraynolds.mypantry.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jraynolds.mypantry.R;
import com.jraynolds.mypantry.objects.Ingredient;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

/**
 * Created by Jasper on 1/31/2018.
 */

public class Adapter_Expandable extends ExpandableRecyclerViewAdapter<Adapter_Category, Adapter_Ingredient> {

    private Context context;

    public Adapter_Expandable(List<? extends ExpandableGroup> categories, Context context) {
        super(categories);
        this.context = context;
    }

    @Override
    public Adapter_Category onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_category, parent, false);
        return new Adapter_Category(view);
    }

    @Override
    public Adapter_Ingredient onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_ingredient, parent, false);
        return new Adapter_Ingredient(view, context);
    }

    @Override
    public void onBindChildViewHolder(Adapter_Ingredient holder, int flatPosition, ExpandableGroup group, int childIndex) {
        final Ingredient i = (Ingredient) group.getItems().get(childIndex);
        holder.setIngredient(i);
    }

    @Override
    public void onBindGroupViewHolder(Adapter_Category holder, int flatPosition, ExpandableGroup group) {
        holder.setCategory(group);
    }
}
