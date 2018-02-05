package com.jraynolds.mypantry.main;

import android.app.Dialog;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.jraynolds.mypantry.R;
import com.jraynolds.mypantry.tabs.Tab_Ingredients;
import com.jraynolds.mypantry.objects.Ingredient;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private Tab_Ingredients[] ingredientsTabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ingredientsTabs = new Tab_Ingredients[3];
        String[] searches = new String[]{"all", "pantry", "shopping"};
        for(int i=0; i<3; i++) {
            ingredientsTabs[i] = new Tab_Ingredients();
            Bundle bundle = new Bundle();
            bundle.putString("searchStr", searches[i]);
            ingredientsTabs[i].setArguments(bundle);
        }

        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);

        TabLayout tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.dialog_addingredient);
                dialog.setTitle("Add Ingredient");

                final EditText title = dialog.findViewById(R.id.text_title);
                final TextView isAvailable = dialog.findViewById(R.id.text_titleAvailable);

                final EditText description = dialog.findViewById(R.id.text_description);
                final EditText category = dialog.findViewById(R.id.text_category);
                //image
                final CheckBox inPantry = dialog.findViewById(R.id.check_inPantry);
                final CheckBox onList = dialog.findViewById(R.id.check_onList);

                Button cancel = dialog.findViewById(R.id.button_cancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                final Button add = dialog.findViewById(R.id.button_add);
                add.setEnabled(false);
                add.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        Globals.addIngredient(new Ingredient(title.getText().toString(), description.getText().toString(), null, category.getText().toString()), inPantry.isChecked(), onList.isChecked());
                        dialog.dismiss();
                    }
                });

                title.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        ArrayList<Ingredient> matches = Globals.getIngredients(title.getText().toString(), true, null,"all");
                        Log.d("titling", matches.toString());
                        if(!matches.isEmpty()) {
                            Log.d("titling", "already exists!");
                            isAvailable.setText("Already used!");
                            add.setEnabled(false);
                        } else {
                            isAvailable.setText("");
                            add.setEnabled(true);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                dialog.show();
            }
        });

        mViewPager.setCurrentItem(1);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return ingredientsTabs[position];
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch(position) {
                case 0:
                    return "All";
                case 1:
                    return "Pantry";
                case 2:
                    return "Buying";
                default:
                    return "How the heck did you get here?";
            }
        }
    }
}
