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
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.models.ExpandableList;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
        categoryTitle.setText(model.category);
    }

//    public ArrayList<ArrayList<Ingredient>> splitCategories(ArrayList<Ingredient> ingredients) {
//        ArrayList<ArrayList<Ingredient>> groupedIngredients = new ArrayList<>();
//
//        //populate categories
//        ArrayList<String> categories = new ArrayList<>();
//        for(int i=0; i<ingredients.size(); i++) if(!categories.contains(ingredients.get(i).category)) categories.add(ingredients.get(i).category);
//        //for each category...
//        for(int i=0; i<categories.size(); i++) {
//            //get all from category
//            ArrayList<Ingredient> inCategory = new ArrayList<>();
//            for(int j=0; j<ingredients.size(); j++) if(ingredients.get(j).category.toLowerCase().equals(categories.get(i).toLowerCase())) inCategory.add(ingredients.get(j));
//            //remove from ingredients
//            ingredients.removeAll(inCategory);
//            //sort
//            Collections.sort(inCategory, new Comparator<Ingredient>() {
//                @Override
//                public int compare(Ingredient t1, Ingredient t2) {
//                    return t1.title.compareToIgnoreCase(t2.title);
//                }
//            });
//            //add to array
//            groupedIngredients.add(inCategory);
//        }
//
//        return groupedIngredients;
//    }
}