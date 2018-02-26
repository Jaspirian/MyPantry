package com.jraynolds.mypantry.lists;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jraynolds.mypantry.R;
import com.jraynolds.mypantry.main.Globals;
import com.jraynolds.mypantry.main.IngredientView;
import com.jraynolds.mypantry.objects.Ingredient;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by Jasper on 2/3/2018.
 */

public class IngredientExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private TreeMap<String, List<Ingredient>> categories;

    public IngredientExpandableListAdapter(Context context, TreeMap<String, List<Ingredient>> categories) {
        this.context = context;
        this.categories = categories;
    }

    public void sortIngredients(List<Ingredient> list) {
        Collections.sort(list, new Comparator<Ingredient>() {
            @Override
            public int compare(Ingredient t1, Ingredient t2) {
                return t1.title.compareToIgnoreCase(t2.title);
            }
        });
    }

    public void toggleIngredient(Ingredient i, boolean adding) {
        String category = i.category;
        List<Ingredient> list = this.categories.get(category);

        if(adding) {
            //add it
            if (list != null) {
                list.add(i);
                sortIngredients(list);
            } else {
                //add a new list
                list = new ArrayList<>();
                list.add(i);
            }
            categories.put(category, list);
        } else {
            if(list.contains(i)) {
                //remove it
                list.remove(i);
                categories.put(category, list);

                //remove the category if it's empty
                if (list.isEmpty()) {
                    this.categories.remove(category);
                }
            }
        }

        Globals.updateLists();
    }

    @Override
    public int getGroupCount() {
        return this.categories.size();
    }

    @Override
    public int getChildrenCount(int position) {
        return this.categories.get(this.getGroup(position)).size();
    }

    @Override
    public String getGroup(int position) {
        return (String) this.categories.keySet().toArray()[position];
    }

    @Override
    public Ingredient getChild(int position, int expandedPosition) {
        return this.categories.get(this.getGroup(position)).get(expandedPosition);
    }

    @Override
    public long getGroupId(int position) {
        return position;
    }

    @Override
    public long getChildId(int position, int expandedPosition) {
        return expandedPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int position, boolean isExpanded, View view, ViewGroup parent) {
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert layoutInflater != null;
            view = layoutInflater.inflate(R.layout.list_category, null);
        }

        initializeGroupComponents(view, getGroup(position));
        return view;
    }

    private void initializeGroupComponents(View view, String title) {
        TextView listTitleTextView = view.findViewById(R.id.layout_categoryTitle);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(title);
    }

    @Override
    public View getChildView(int position, int expandedPosition, boolean isLastChild, View view, ViewGroup parent) {
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert layoutInflater != null;
            view = layoutInflater.inflate(R.layout.list_ingredient, parent, false);
        }

        initializeChildComponents(view, getChild(position, expandedPosition));
        return view;
    }

    private void initializeChildComponents(View view, final Ingredient ingredient) {
        //onclick for the row
        LinearLayout row = view.findViewById(R.id.row);
        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, IngredientView.class);
                intent.putExtra("Ingredient", (Serializable) ingredient);
                context.startActivity(intent);
            }
        });

        CheckBox checkPantry = view.findViewById(R.id.ingredient_isInPantry);
        CheckBox checkList = view.findViewById(R.id.ingredient_isOnList);
        TextView titleView = view.findViewById(R.id.ingredient_title);
        TextView descriptionView = view.findViewById(R.id.ingredient_description);
        ImageView imageView = view.findViewById(R.id.ingredient_image);

        checkPantry.setChecked(ingredient.isInList("pantry", context));
        checkPantry.setOnClickListener(new checkboxPantryClick(ingredient));
        checkList.setChecked(ingredient.isInList("shopping", context));
        checkList.setOnClickListener(new checkboxListClick(ingredient));
        titleView.setText(ingredient.title);
        descriptionView.setText(ingredient.description);

        //handle image.
        if(ingredient.imageUrl != null) {
            AssetManager am = context.getAssets();
            try {
                InputStream is = am.open("images/" + ingredient.imageUrl);
                Bitmap b = BitmapFactory.decodeStream(is);
                imageView.setImageBitmap(b);
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean isChildSelectable(int position, int expandedPosition) {
        return true;
    }

    public class checkboxPantryClick implements View.OnClickListener {

        private Ingredient i;

        public checkboxPantryClick(Ingredient i) {
            this.i = i;
        }

        @Override
        public void onClick(View view) {
            i.toggleIsInList("pantry", context);
        }
    }

    public class checkboxListClick implements View.OnClickListener {

        private Ingredient i;
        public checkboxListClick(Ingredient i) {
            this.i = i;
        }

        @Override
        public void onClick(View view) {
            i.toggleIsInList("shopping", context);
        }
    }
}
