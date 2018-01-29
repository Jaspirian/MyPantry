package com.jraynolds.mypantry;

/**
 * Created by Jasper on 1/24/2018.
 */

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Adapter_Ingredients extends RecyclerView.Adapter<Adapter_Ingredients.MyViewHolder> {

    private ArrayList<Ingredient> ingredients = new ArrayList<>();
    private final String searchString;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public final LinearLayout row;
        public final CheckBox checkPantry, checkList;
        public final TextView titleView, descriptionView;
        public final ImageView imageView;

        public MyViewHolder(View view) {
            super(view);
            row = view.findViewById(R.id.row);
            checkPantry = view.findViewById(R.id.ingredient_isInPantry);
            checkList = view.findViewById(R.id.ingredient_isOnList);
            titleView = view.findViewById(R.id.ingredient_title);
            descriptionView = view.findViewById(R.id.ingredient_description);
            imageView = view.findViewById(R.id.ingredient_image);
        }
    }

    public Adapter_Ingredients(String searchString) {
        this.searchString = searchString;
        updateIngredients();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.template_ingredient, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Ingredient i = ingredients.get(position);

        holder.row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("switchActivity", "clicked!");
                Intent intent = new Intent(holder.row.getContext(), IngredientView.class);
                intent.putExtra("Ingredient", i);
                holder.row.getContext().startActivity(intent);
            }
        });
        holder.checkPantry.setChecked(i.isInPantry);
        holder.checkList.setChecked(i.isOnList);

        holder.titleView.setText(i.title);
        holder.descriptionView.setText(i.description);

        holder.checkPantry.setOnClickListener(new checkboxPantryClick(i));
        holder.checkList.setOnClickListener(new checkboxListClick(i));

        //image tho
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public void updateIngredients() {
        ingredients = Globals.getIngredients(null, false, searchString);
        Collections.sort(ingredients, new Comparator<Ingredient>() {
            @Override
            public int compare(Ingredient t1, Ingredient t2) {
                return t1.title.compareToIgnoreCase(t2.title);
            }
        });
        notifyDataSetChanged();
    }

    public class checkboxPantryClick implements View.OnClickListener {

        private Ingredient i;
        public checkboxPantryClick(Ingredient i) {
            this.i = i;
        }

        @Override
        public void onClick(View view) {
            i.isInPantry = !i.isInPantry;
            Globals.updateLists();
        }
    }

    public class checkboxListClick implements View.OnClickListener {

        private Ingredient i;
        public checkboxListClick(Ingredient i) {
            this.i = i;
        }

        @Override
        public void onClick(View view) {
            i.isOnList = !i.isOnList;
            Globals.updateLists();
        }
    }

}
