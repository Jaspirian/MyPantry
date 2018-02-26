package com.jraynolds.mypantry.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jraynolds.mypantry.R;
import com.jraynolds.mypantry.objects.Ingredient;
import com.jraynolds.mypantry.settings.SettingsActivity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class IngredientView extends AppCompatActivity {

    private Ingredient ingredient;

    private EditText title, description;
    private AutoCompleteTextView category;
    private TextView uniqueTitle;
    private ImageButton image;
    private EditText recipes;
    private CheckBox inPantry, onList;
    private Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ingredient = (Ingredient) getIntent().getSerializableExtra("Ingredient");

        setContentView(R.layout.activity_ingredient_view);
        initializeVariables();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setReadout();
        addListeners();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ingredient, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
////            return true;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle("Delete Ingredient")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Globals.deleteIngredient(ingredient);
                        finish();
                    }
                });
        if(ingredient.isUserCreated) {
            builder.setMessage(getString(R.string.itemview_usermade_deleteprompt));
        } else {
            builder.setMessage(getString(R.string.itemview_default_deleteprompt));
        }
        builder.show();
//        }

        return super.onOptionsItemSelected(item);
    }

    private void initializeVariables() {
        title = this.findViewById(R.id.title);
        uniqueTitle = this.findViewById(R.id.uniqueTitle);
        uniqueTitle.setText("");
        image = this.findViewById(R.id.imageButton);
        description = this.findViewById(R.id.description);
        category = this.findViewById(R.id.category);
        //recipe
        inPantry = this.findViewById(R.id.inPantry);
        onList = this.findViewById(R.id.onList);
        save = this.findViewById(R.id.save);
    }

    private void setReadout() {
        title.setText(ingredient.title);
        description.setText(ingredient.description);
        category.setText(ingredient.category);

        final ArrayList<String> CATEGORIES = Globals.getIngredientCategories();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, CATEGORIES);
        category.setAdapter(adapter);

        if(ingredient.imageUrl != null) {
            AssetManager am = getApplicationContext().getAssets();
            try {
                InputStream is = am.open("images/" + ingredient.imageUrl);
                image.setImageBitmap(BitmapFactory.decodeStream(is));
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        inPantry.setChecked(ingredient.isInList("pantry", getApplicationContext()));
        onList.setChecked(ingredient.isInList("shopping", getApplicationContext()));

        //recipes
    }

    private void addListeners() {

        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int s, int s1, int s2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int s, int s1, int s2) {
                save.setEnabled(true);
                uniqueTitle.setText("");
                String titleText = String.valueOf(title.getText());
                if(!titleText.equals(ingredient.title)) {
                    if(!Globals.getIngredients(titleText, true, null, null).isEmpty()) {
                        save.setEnabled(false);
                        uniqueTitle.setText("Please choose a unique title.");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Ingredient newIngredient = new Ingredient(title.getText().toString(), description.getText().toString(), null, category.getText().toString());
                Globals.addIngredient(newIngredient);
                newIngredient.setIsInList("pantry", inPantry.isChecked(), getApplicationContext());
                newIngredient.setIsInList("shopping", inPantry.isChecked(), getApplicationContext());
                finish();
            }
        });
    }

}
