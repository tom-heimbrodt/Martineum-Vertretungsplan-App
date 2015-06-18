package de.tomsapps.vertretungsplanapp.gui;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import de.tomsapps.vertretungsplanapp.R;
import de.tomsapps.vertretungsplanapp.algorithms.OtherAlgorithms;
import de.tomsapps.vertretungsplanapp.core.Preferences;
import de.tomsapps.vertretungsplanapp.core.VertretungsplanApp;
import de.tomsapps.vertretungsplanapp.taskmanagement.Task;

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
        groupingSpinner.setAdapter(ArrayAdapter.createFromResource(this, R.array.activity_preferences_grouping_spinner, android.R.layout.simple_dropdown_item_1line));
        groupingSpinner.setOnItemSelectedListener(this);
        groupingSpinner.setSelection(OtherAlgorithms.getIntFromSpalte(preferences.gruppierenNach));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        switch (parent.getId())
        {
            case R.id.activity_preferences_grouping_spinner:
                preferences.gruppierenNach = OtherAlgorithms.getSpalteFromInt(groupingSpinner.getSelectedItemPosition());
                ((VertretungsplanApp)getApplication()).taskManager.addTask(new Task(null, "SAVE_SETTINGS"));
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {
        // this is not expected to happen!!!
    }

    public void layoutClicked(View view)
    {
        switch (view.getId())
        {
            case R.id.activity_preferences_ueber_button:
                Intent intent = new Intent(this, InfoActivity.class);
                startActivity(intent);
                break;
        }
    }
}
