package com.jraynolds.mypantry.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jraynolds.mypantry.R;
import com.jraynolds.mypantry.main.Globals;
import com.jraynolds.mypantry.objects.Ingredient;

import java.io.IOException;
import java.io.InputStream;

public class IngredientView extends AppCompatActivity {

    private Ingredient i;

    private EditText title, description, category;
    private TextView uniqueTitle;
    private ImageButton image;
    private EditText recipes;
    private CheckBox inPantry, onList;
    private Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        i = (Ingredient) getIntent().getSerializableExtra("Ingredient");

        setContentView(R.layout.activity_ingredient_view);
        initializeVariables();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setReadout(i);
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

    private void initializeVariables() {
        title = findViewById(R.id.text_title);
        uniqueTitle = findViewById(R.id.text_uniqueTitle);
        uniqueTitle.setText("");
        image = findViewById(R.id.imageButton);
        description = findViewById(R.id.text_description);
        category = findViewById(R.id.text_category);
        //recipe
        inPantry = findViewById(R.id.check_inPantry);
        onList = findViewById(R.id.check_onList);
        save = findViewById(R.id.button_save);
    }

    private void setReadout(final Ingredient i) {
        title.setText(i.title);
        description.setText(i.description);
        category.setText(i.category);
        if(i.imageUrl != null) {
            AssetManager am = getApplicationContext().getAssets();
            try {
                InputStream is = am.open("images/" + i.imageUrl);
                image.setImageBitmap(BitmapFactory.decodeStream(is));
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        inPantry.setChecked(i.isInList("pantry", getApplicationContext()));
        onList.setChecked(i.isInList("shopping", getApplicationContext()));

        //recipes

        save.setOnClickListener(new SaveListener(i));
    }

    public class SaveListener implements View.OnClickListener {

        private final Ingredient i;

        public SaveListener(Ingredient i) {
            this.i = i;
        }

        @Override
        public void onClick(View view) {
            Ingredient newIngredient = new Ingredient(title.getText().toString(), description.getText().toString(), null, category.getText().toString());
            if(i.title.equals(newIngredient.title) || Globals.getIngredients(newIngredient.title, true, null,"all").isEmpty()) {
                Globals.addIngredient(newIngredient);
                newIngredient.setIsInList("pantry", inPantry.isChecked(), getApplicationContext());
                newIngredient.setIsInList("shopping", inPantry.isChecked(), getApplicationContext());
                finish();
            } else {
                uniqueTitle.setText(R.string.uniqueTitleWarning);
            }
        }
    }

}
