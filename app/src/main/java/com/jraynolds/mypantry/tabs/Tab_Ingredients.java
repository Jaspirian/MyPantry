package com.jraynolds.mypantry.tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jraynolds.mypantry.adapters.Adapter_Category;
import com.jraynolds.mypantry.R;

/**
 * Created by Jasper on 1/20/2018.
 */

public class Tab_Ingredients extends Fragment {

    private String searchStr;
    private RecyclerView recyclerView;
    private View rootView;
    private Adapter_Category catAdapt;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchStr = this.getArguments().getString("searchStr");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_ingredients, container, false);

        recyclerView = rootView.findViewById(R.id.ingredients_list);

        createList();
        return rootView;
    }

    public void createList() {
        catAdapt = new Adapter_Category(rootView, searchStr);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(catAdapt);
    }

    public void update() {
        catAdapt.update();
    }

}
