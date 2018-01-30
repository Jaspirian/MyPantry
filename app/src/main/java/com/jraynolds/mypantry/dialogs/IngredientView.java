package com.jraynolds.mypantry.dialogs;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jraynolds.mypantry.R;
import com.jraynolds.mypantry.main.Globals;
import com.jraynolds.mypantry.objects.Ingredient;

public class IngredientView extends AppCompatActivity {

    private Ingredient i;

    private EditText title, description, category;
    private TextView uniqueTitle;
    private ImageButton image;
//    private EditText recipes;
    private CheckBox inPantry, onList;
    private Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.i = (Ingredient) getIntent().getExtras().get("Ingredient");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_view);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title = findViewById(R.id.text_title);
        uniqueTitle = findViewById(R.id.text_uniqueTitle);
        uniqueTitle.setText("");
        image = findViewById(R.id.image_picker);
        description = findViewById(R.id.text_description);
        category = findViewById(R.id.text_category);
        //recipe
        inPantry = findViewById(R.id.check_inPantry);
        onList = findViewById(R.id.check_onList);
        save = findViewById(R.id.button_save);

        title.setText(i.title);
        //image
        description.setText(i.description);
        category.setText(i.category);
        inPantry.setChecked(i.isInPantry);
        onList.setChecked(i.isOnList);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Ingredient newIngredient = new Ingredient(title.getText().toString(), description.getText().toString(), null, category.getText().toString(), inPantry.isChecked(), onList.isChecked());
                Log.d("saving", newIngredient.description);
                if(i.title.toLowerCase().equals(title.getText().toString().toLowerCase())) {
                    Log.d("saving", "same title.");
                    Globals.modifyIngredient(newIngredient);
                    finish();
                } else if(Globals.getIngredients(newIngredient.title, true, null,"all").isEmpty()) {
                    Log.d("saving", "new title.");
                    Globals.removeIngredientByTitle(i.title);
                    Globals.addIngredient(newIngredient);
                    finish();
                } else {
                    Log.d("saving", "unacceptable title.");
                    uniqueTitle.setText("Please pick a unique title.");
                }
            }
        });
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

}
