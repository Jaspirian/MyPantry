package com.jraynolds.mypantry.tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.jraynolds.mypantry.lists.IngredientExpandableListAdapter;
import com.jraynolds.mypantry.lists.IngredientExpandableListDataPump;
import com.jraynolds.mypantry.R;
import com.jraynolds.mypantry.main.Globals;
import com.jraynolds.mypantry.objects.Ingredient;

import java.util.List;
import java.util.TreeMap;

/**
 * Created by Jasper on 1/20/2018.
 */

public class Tab_Ingredients extends Fragment {

    private String searchStr;

    public void onCreate(Bundle savedInstanceState) {
        Log.d("calling", searchStr + ": tab created!");
        super.onCreate(savedInstanceState);
        searchStr = this.getArguments().getString("searchStr");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d("calling", searchStr + ": tab view created!");
        View rootView = inflater.inflate(R.layout.fragment_all, container, false);

        ExpandableListView expandableListView = rootView.findViewById(R.id.layout_listview);
        ViewGroup footer = (ViewGroup) inflater.inflate(R.layout.list_footer, expandableListView, false);
        expandableListView.addFooterView(footer);
        final TreeMap<String, List<Ingredient>> categories = IngredientExpandableListDataPump.getData(searchStr);
        IngredientExpandableListAdapter adapter = new IngredientExpandableListAdapter(this.getContext(), categories);
        Globals.addTab(searchStr, adapter);
        expandableListView.setAdapter(adapter);

        return rootView;
    }


}
