package com.jraynolds.mypantry.tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.jraynolds.mypantry.CustomExpandableListAdapter;
import com.jraynolds.mypantry.ExpandableListDataPump;
import com.jraynolds.mypantry.adapters.Adapter_Category;
import com.jraynolds.mypantry.R;
import com.jraynolds.mypantry.adapters.Adapter_Expandable;
import com.jraynolds.mypantry.main.Globals;
import com.jraynolds.mypantry.objects.Category;
import com.jraynolds.mypantry.objects.Ingredient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Jasper on 1/20/2018.
 */

public class Tab_Ingredients extends Fragment {

    private String searchStr;
//    private RecyclerView recyclerView;
//    private View rootView;
    private Adapter_Expandable expandAdapt;
    public List<Category> categories;

    public void onCreate(Bundle savedInstanceState) {
        Log.d("calling", searchStr + ": tab created!");
        super.onCreate(savedInstanceState);
        searchStr = this.getArguments().getString("searchStr");
        Globals.addTab(searchStr, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d("calling", searchStr + ": tab view created!");
        View rootView = inflater.inflate(R.layout.fragment_all, container, false);

        ExpandableListView expandableListView = (ExpandableListView) rootView.findViewById(R.id.all_ingredients_listView);
        final HashMap<String, List<Ingredient>> expandableListDetail = ExpandableListDataPump.getData(searchStr);
        final List<String> expandableListTitle = new ArrayList<>(expandableListDetail.keySet());
        ExpandableListAdapter expandableListAdapter = new CustomExpandableListAdapter(this.getContext(), expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getContext(),
                        expandableListTitle.get(groupPosition) + " List Expanded.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getContext(),
                        expandableListTitle.get(groupPosition) + " List Collapsed.",
                        Toast.LENGTH_SHORT).show();

            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Toast.makeText(
                        getContext(),
                        expandableListTitle.get(groupPosition)
                                + " -> "
                                + expandableListDetail.get(
                                expandableListTitle.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT
                ).show();
                return false;
            }
        });

        return rootView;
    }

//    public Adapter_Expandable getAdapter(RecyclerView recyclerView) {
//        expandAdapt = new Adapter_Expandable(categories, getContext(), searchStr);
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
//        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//
//        return expandAdapt;
//    }

//    public void addIngredient(Ingredient add) {
//        Log.d("aaaa", "help1");
//        boolean added = false;
//        for(int i=0; i<categories.size(); i++) {
//            if(categories.get(i).getTitle().equals(add.title)) {
//                List<Ingredient> items = categories.get(i).getItems();
//                items.add(add);
//                sortIngredients(items);
//                categories.set(i, new Category(categories.get(i).getTitle(), items));
//                added = true;
//                break;
//            }
//        }
//        if(!added) categories.add(new Category(add.title, Arrays.asList(add)));
//
//        Log.d("aaaa", "help2");
//        expandAdapt.notifyDataSetChanged();
//    }
//
//    public void removeIngredient(Ingredient remove) {
//        Log.d("aaaa", "help1");
//        for(int i=0; i<categories.size(); i++) {
//            for(int j=0; j<categories.get(i).getItemCount(); j++) {
//                if(categories.get(i).getItems().get(j) == remove) {
//                    List<Ingredient> items = categories.get(i).getItems();
//                    if(items.size() <= 1) {
//                        List<Category> newList = new ArrayList<>();
//                        for(int k=0; k<categories.size(); k++) {
//                            if(i != k) newList.add(categories.get(k));
//                        }
//                        categories = newList;
//                    } else {
//                        items.remove(remove);
//                        categories.set(i, new Category(categories.get(i).getTitle(), items));
//                    }
//                    break;
//                }
//            }
//        }
//
//        Log.d("aaaa", "help2");
//        expandAdapt.notifyDataSetChanged();
//    }

//    public void sortIngredients(List<Ingredient> list) {
//        Collections.sort(list, new Comparator<Ingredient>() {
//            @Override
//            public int compare(Ingredient t1, Ingredient t2) {
//                return t1.title.compareToIgnoreCase(t2.title);
//            }
//        });
//    }


}
