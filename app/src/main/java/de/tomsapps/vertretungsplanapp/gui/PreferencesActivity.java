package de.tomsapps.vertretungsplanapp.gui;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import de.tomsapps.vertretungsplanapp.R;
import de.tomsapps.vertretungsplanapp.core.Preferences;
import de.tomsapps.vertretungsplanapp.core.VertretungsplanApp;

public class PreferencesActivity extends FragmentActivity implements AdapterView.OnItemSelectedListener
{
    private Preferences preferences; // hier werden die Einstellungen gespeichert

    private Spinner groupingSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_preferences);

        ActionBar actionBar = this.getActionBar();
        actionBar.setTitle("Einstellungen");
        actionBar.setSubtitle("Martineum Vertretungsplan App");

        preferences = ((VertretungsplanApp)this.getApplication()).preferences;

        groupingSpinner = (Spinner)findViewById(R.id.activity_preferences_grouping_spinner);
        groupingSpinner.setAdapter(ArrayAdapter.createFromResource(this, R.array.activity_preferences_grouping_spinner, android.R.layout.simple_spinner_item));
        groupingSpinner.setOnItemSelectedListener(this);
        groupingSpinner.setSelection(0);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        switch (parent.getId())
        {
            case R.id.activity_preferences_grouping_spinner:
                switch (groupingSpinner.getSelectedItemPosition())
                {
                    case 0: // Klasse
                        preferences.gruppierenNach = Preferences.VertretungsplanSpalte.Klasse;
                        break;
                    case 1: // Lehrer
                        preferences.gruppierenNach = Preferences.VertretungsplanSpalte.Lehrer;
                        break;
                    case 2: // Raum
                        preferences.gruppierenNach = Preferences.VertretungsplanSpalte.Raum;
                        break;
                    case 3: // Fach
                        preferences.gruppierenNach = Preferences.VertretungsplanSpalte.Fach;
                        break;
                    case 4: // Stunde
                        preferences.gruppierenNach = Preferences.VertretungsplanSpalte.Stunde;
                        break;

                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {
        // this is not expected to happen!!!
    }
}
