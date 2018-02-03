package com.jraynolds.mypantry.adapters;

import android.view.View;
import android.widget.TextView;

import com.jraynolds.mypantry.R;
import com.jraynolds.mypantry.objects.Ingredient;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

/**
 * Created by Jasper on 1/29/2018.
 */

public class Adapter_Category extends GroupViewHolder {

    private TextView categoryTitle;

    public Adapter_Category(View itemView) {
        super(itemView);
        categoryTitle = itemView.findViewById(R.id.layout_categoryTitle);
    }

    public void setCategory(ExpandableGroup group) {
        Ingredient model = (Ingredient) group.getItems().get(0);
        categoryTitle.setText(model.category + " (" + group.getItemCount() +")");
    }
}