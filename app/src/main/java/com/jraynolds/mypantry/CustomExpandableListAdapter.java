package com.jraynolds.mypantry;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.Image;
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
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Jasper on 2/3/2018.
 */

public class CustomExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> expandableListTitle;
    private LinkedHashMap<String, List<Ingredient>> expandableListDetail;

    public CustomExpandableListAdapter(Context context, List<String> expandableListTitle, LinkedHashMap<String, List<Ingredient>> expandableListDetail) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
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
        String category = i.category;
        List<Ingredient> list = this.expandableListDetail.get(category);
        if(list != null) {
            list.add(i);
            sortIngredients(list);
        } else {
            list = new ArrayList<>();
            list.add(i);
        }
        expandableListDetail.put(category, list);

        for(int j=0; j<expandableListDetail.size(); j++) {
            for(int k=0; k<expandableListDetail.get(j).size(); k++) {
                Log.d("Adding", "Category " + expandableListDetail.get(j) + ", Ingredient " + expandableListDetail.get(j).get(k).title);
            }
        }

        this.notifyDataSetChanged();
    }

    public void removeIngredient(Ingredient i) {
        String category = i.category;
        List<Ingredient> list = this.expandableListDetail.get(category);
        list.remove(i);
        expandableListDetail.put(category, list);
        if(list.isEmpty()) {
            this.expandableListDetail.remove(category);
        }

        for(int j=0; j<expandableListDetail.size(); j++) {
            for(int k=0; k<expandableListDetail.get(j).size(); k++) {
                Log.d("Removing", "Category " + expandableListDetail.get(j) + ", Ingredient " + expandableListDetail.get(j).get(k).title);
            }
        }

        this.notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public int getChildrenCount(int position) {
        return this.expandableListDetail.get(this.expandableListTitle.get(position)).size();
    }

    @Override
    public String getGroup(int position) {
        return this.expandableListTitle.get(position);
    }

    @Override
    public Ingredient getChild(int position, int expandedPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(position)).get(expandedPosition);
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
    public View getGroupView(int position, boolean b, View view, ViewGroup viewGroup) {
        String listTitle = (String) getGroup(position);
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.list_category, null);
        }
        TextView listTitleTextView = view.findViewById(R.id.layout_categoryTitle);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);
        return view;
    }

    @Override
    public View getChildView(int position, int expandedPosition, boolean bool, View view, ViewGroup viewGroup) {
        final Ingredient ingredient = (Ingredient) getChild(position, expandedPosition);
        final String ingString = ingredient.title;
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.list_ingredient, null);
        }
        CheckBox checkPantry = view.findViewById(R.id.ingredient_isInPantry);
        CheckBox checkList = view.findViewById(R.id.ingredient_isOnList);
        TextView titleView = view.findViewById(R.id.ingredient_title);
        TextView descriptionView = view.findViewById(R.id.ingredient_description);
        ImageView imageView = view.findViewById(R.id.ingredient_image);

        checkPantry.setChecked(ingredient.isInPantry);
        checkPantry.setOnClickListener(new checkboxPantryClick(ingredient));
        checkList.setChecked(ingredient.isOnList);
        checkList.setOnClickListener(new checkboxListClick(ingredient));
        titleView.setText(ingredient.title);
        descriptionView.setText(ingredient.description);
        if(ingredient.imageUrl != null) {
            AssetManager am = context.getAssets();
            try {
                InputStream is = am.open("images/" + ingredient.imageUrl);
                Bitmap b = BitmapFactory.decodeStream(is);
                Log.d("image", ingredient.title);
                imageView.setImageBitmap(b);
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return view;
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
            i.isInPantry = !i.isInPantry;
            CustomExpandableListAdapter adapter = (CustomExpandableListAdapter) Globals.tabAdapters.get("pantry");
            if(i.isInPantry) {
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
            i.isOnList = !i.isOnList;
        }
    }
}
