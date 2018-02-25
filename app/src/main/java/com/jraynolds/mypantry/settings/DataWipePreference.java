package com.jraynolds.mypantry.settings;

import android.app.Activity;
import android.content.Context;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceViewHolder;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.jraynolds.mypantry.R;
import com.jraynolds.mypantry.main.Globals;

/**
 * Created by Jasper on 2/24/2018.
 */

public class DataWipePreference extends Preference {

    private Context context;

    public DataWipePreference(Context context) {
        super(context, null);
        this.context = context;
        setLayoutResource(R.layout.danger_button);
    }

    public DataWipePreference(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        this.context = context;
        setLayoutResource(R.layout.danger_button);
    }

    public DataWipePreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr, defStyleAttr);
        this.context = context;
        setLayoutResource(R.layout.danger_button);
    }

    public DataWipePreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        setLayoutResource(R.layout.danger_button);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);

        final Switch enable = (Switch) holder.findViewById(R.id.enable);
        final Button button = (Button) holder.findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Globals.wipeUserMemory();
                Toast.makeText(context, "All user data wiped.", Toast.LENGTH_LONG).show();
                enable.setChecked(false);
            }
        });

        // it's switched off by default.
        enable.setChecked(false);
        enable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked) {
                    button.setVisibility(View.VISIBLE);
                } else {
                    button.setVisibility(View.GONE);
                }
            }
        });
    }
}
