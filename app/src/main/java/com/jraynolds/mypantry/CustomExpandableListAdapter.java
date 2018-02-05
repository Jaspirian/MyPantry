package com.jraynolds.mypantry;

import android.content.Context;
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
import android.widget.TextView;

import com.jraynolds.mypantry.main.Globals;
import com.jraynolds.mypantry.objects.Ingredient;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Jasper on 2/3/2018.
 */

public class CustomExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> categoryTitles;
    private TreeMap<String, List<Ingredient>> categories;

    public CustomExpandableListAdapter(Context context, List<String> categoryTitles, TreeMap<String, List<Ingredient>> categories) {
        this.context = context;
        this.categoryTitles = categoryTitles;
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

    public void addIngredient(Ingredient i) {
        Log.d("adding", "adding now: " + i.title);
        Log.d("itemset", Integer.toString(categories.size()));

        Log.d("itemset", categoryTitles.toString());
        Log.d("itemset", categories.toString());

        String category = i.category;
        List<Ingredient> list = this.categories.get(category);
        if(list != null) {
            list.add(i);
            sortIngredients(list);
        } else {
            list = new ArrayList<>();
            list.add(i);
            categoryTitles.add(category);
        }
        categories.put(category, list);

        Log.d("itemset", Integer.toString(categories.size()));

        Iterator it = Globals.tabAdapters.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            CustomExpandableListAdapter adapter = (CustomExpandableListAdapter) pair.getValue();
            adapter.notifyDataSetChanged();
        }
    }

    public void removeIngredient(Ingredient i) {
        Log.d("removing", "adding now: " + i.title);
        Log.d("itemset", Integer.toString(categories.size()));
        String category = i.category;
        List<Ingredient> list = this.categories.get(category);
        list.remove(i);
        categories.put(category, list);
        if(list.isEmpty()) {
            this.categories.remove(category);
            this.categoryTitles.remove(category);
        }

        Log.d("itemset", categoryTitles.toString());
        Log.d("itemset", categories.toString());

        Log.d("itemset", Integer.toString(categories.size()));

        Iterator it = Globals.tabAdapters.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            CustomExpandableListAdapter adapter = (CustomExpandableListAdapter) pair.getValue();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public int getGroupCount() {
        return this.categoryTitles.size();
    }

    @Override
    public int getChildrenCount(int position) {
        Log.d("counting", String.valueOf(this.categories.get(this.categoryTitles.get(position)).size()));
        return this.categories.get(this.categoryTitles.get(position)).size();
    }

    @Override
    public String getGroup(int position) {
        return this.categoryTitles.get(position);
    }

    @Override
    public Ingredient getChild(int position, int expandedPosition) {
        return this.categories.get(this.categoryTitles.get(position)).get(expandedPosition);
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

    private void initializeChildComponents(View view, Ingredient ingredient) {
        CheckBox checkPantry = view.findViewById(R.id.ingredient_isInPantry);
        CheckBox checkList = view.findViewById(R.id.ingredient_isOnList);
        TextView titleView = view.findViewById(R.id.ingredient_title);
        TextView descriptionView = view.findViewById(R.id.ingredient_description);
        ImageView imageView = view.findViewById(R.id.ingredient_image);

        checkPantry.setChecked(ingredient.isInPantry(context));
        checkPantry.setOnClickListener(new checkboxPantryClick(ingredient));
        checkList.setChecked(ingredient.isOnList(context));
        checkList.setOnClickListener(new checkboxListClick(ingredient));
        titleView.setText(ingredient.title);
        descriptionView.setText(ingredient.description);
        if(ingredient.imageUrl != null) {
            AssetManager am = context.getAssets();
            try {
                InputStream is = am.open("images/" + ingredient.imageUrl);
                Bitmap b = BitmapFactory.decodeStream(is);
//                Log.d("image", ingredient.title);
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
            CustomExpandableListAdapter adapter = Globals.tabAdapters.get("pantry");

            boolean nowInPantry = i.toggleIsInPantry(context);
            if(nowInPantry) {
                adapter.addIngredient(i);
            } else {
                adapter.removeIngredient(i);
            }
        }
    }

    public class checkboxListClick implements View.OnClickListener {

        private Ingredient i;
        public checkboxListClick(Ingredient i) {
            this.i = i;
        }

        @Override
        public void onClick(View view) {
            CustomExpandableListAdapter adapter = Globals.tabAdapters.get("shopping");

            boolean nowOnList = i.toggleIsOnList(context);
            if(nowOnList) {
                adapter.addIngredient(i);
            } else {
                adapter.removeIngredient(i);
            }
        }
    }
}
