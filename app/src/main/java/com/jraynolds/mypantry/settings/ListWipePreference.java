package com.jraynolds.mypantry.settings;

import android.content.Context;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceViewHolder;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.jraynolds.mypantry.R;
import com.jraynolds.mypantry.main.Globals;

/**
 * Created by Jasper on 2/25/2018.
 */

public class ListWipePreference extends Preference {

    public ListWipePreference(Context context) {
        super(context, null);
        setLayoutResource(R.layout.danger_button);
    }

    public ListWipePreference(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        setLayoutResource(R.layout.danger_button);
    }

    public ListWipePreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr, defStyleAttr);
        setLayoutResource(R.layout.danger_button);
    }

    public ListWipePreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setLayoutResource(R.layout.danger_button);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);

        final TextView title = (TextView) holder.findViewById(R.id.title);
        final TextView summary = (TextView) holder.findViewById(R.id.summary);

        final Switch enable = (Switch) holder.findViewById(R.id.enable);
        final Button button = (Button) holder.findViewById(R.id.button);

        title.setText(getContext().getString(R.string.settings_listwipe_title));
        summary.setText(getContext().getString(R.string.settings_listwipe_summary));
        button.setText(getContext().getString(R.string.settings_listwipe_button));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Globals.wipeListMemory();
                Toast.makeText(getContext(), getContext().getString(R.string.settings_listwipe_toast), Toast.LENGTH_LONG).show();
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
