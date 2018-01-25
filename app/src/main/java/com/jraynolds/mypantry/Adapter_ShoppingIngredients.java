package com.jraynolds.mypantry;

/**
 * Created by Jasper on 1/24/2018.
 */

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Jasper on 1/23/2018.
 */

public class Adapter_ShoppingIngredients extends RecyclerView.Adapter<Adapter_ShoppingIngredients.MyViewHolder> {

    private ArrayList<Ingredient> ingredients = new ArrayList<>();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CheckBox checkPantry, checkList;
        public TextView titleView, descriptionView;
        public ImageView imageView;

        public MyViewHolder(View view) {
            super(view);
            checkPantry = view.findViewById(R.id.ingredient_isInPantry);
            checkList = view.findViewById(R.id.ingredient_isOnList);
            titleView = view.findViewById(R.id.ingredient_title);
            descriptionView = view.findViewById(R.id.ingredient_description);
            imageView = view.findViewById(R.id.ingredient_image);
        }
    }

    public Adapter_ShoppingIngredients() {
        updateIngredients();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.template_pantry_ingredient, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Ingredient i = ingredients.get(position);
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
        ingredients = Globals.getIngredients(null, true,false);
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
