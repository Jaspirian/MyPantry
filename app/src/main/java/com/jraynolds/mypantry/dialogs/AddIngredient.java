package com.jraynolds.mypantry.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.jraynolds.mypantry.R;
import com.jraynolds.mypantry.main.Globals;
import com.jraynolds.mypantry.objects.Ingredient;

import java.util.ArrayList;

/**
 * Created by Jasper on 2/24/2018.
 */

public class AddIngredient extends Dialog {

    private final Context context;

    public AddIngredient(@NonNull Context context) {
        super(context);
        this.context = context;
        initializeComponents();
    }

    private void initializeComponents() {
        setContentView(R.layout.dialog_addingredient);
        setTitle("Add Ingredient");

        final EditText title = findViewById(R.id.text_title);
        final TextView isAvailable = findViewById(R.id.text_titleAvailable);

        final EditText description = findViewById(R.id.text_description);
        final EditText category = findViewById(R.id.text_category);
        //image
        final CheckBox inPantry = findViewById(R.id.check_inPantry);
        final CheckBox onList = findViewById(R.id.check_onList);

        Button cancel = findViewById(R.id.button_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        final Button add = findViewById(R.id.button_add);
        add.setEnabled(false);
        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Ingredient i = new Ingredient(title.getText().toString(), description.getText().toString(), null, category.getText().toString());
                Globals.addIngredient(i);
                i.setIsInList("pantry", inPantry.isChecked(), context);
                i.setIsInList("shopping", onList.isChecked(), context);
                dismiss();
            }
        });

        title.addTextChangedListener(new TitleWatcher(title, isAvailable, add));

    }

    public class TitleWatcher implements TextWatcher {

        private final TextView title;
        private final TextView isAvailable;
        private final Button add;

        public TitleWatcher(TextView title, TextView isAvailable, Button add) {
            this.title = title;
            this.isAvailable = isAvailable;
            this.add = add;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            ArrayList<Ingredient> matches = Globals.getIngredients(title.getText().toString(), true, null,"all");
            if(!matches.isEmpty()) {
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
    }

}
