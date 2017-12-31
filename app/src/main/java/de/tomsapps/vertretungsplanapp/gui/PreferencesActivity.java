package de.tomsapps.vertretungsplanapp.gui;

import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import de.tomsapps.vertretungsplanapp.R;
import de.tomsapps.vertretungsplanapp.algorithms.EnvironmentInterfaces;
import de.tomsapps.vertretungsplanapp.algorithms.OtherAlgorithms;
import de.tomsapps.vertretungsplanapp.core.Preferences;
import de.tomsapps.vertretungsplanapp.core.Resources;
import de.tomsapps.vertretungsplanapp.core.VertretungsplanApp;

public class PreferencesActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{
    private Preferences preferences; // hier werden die Einstellungen gespeichert

    private Spinner groupingSpinner, fullscreenSpinner;

    private TextView[] textViews;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_preferences);

        preferences = ((VertretungsplanApp)this.getApplication()).preferences;

        groupingSpinner = (Spinner)findViewById(R.id.activity_preferences_grouping_spinner);
        groupingSpinner.setAdapter(ArrayAdapter.createFromResource(this, R.array.activity_preferences_grouping_spinner, android.R.layout.simple_dropdown_item_1line));
        groupingSpinner.setOnItemSelectedListener(this);
        groupingSpinner.setSelection(OtherAlgorithms.getIntFromSpalte(preferences.gruppierenNach));

        fullscreenSpinner = (Spinner)findViewById(R.id.activity_preferences_fullscreen_spinner);
        fullscreenSpinner.setAdapter(ArrayAdapter.createFromResource(this, R.array.activity_preferences_fullscreen_spinner, android.R.layout.simple_dropdown_item_1line));
        fullscreenSpinner.setOnItemSelectedListener(this);
        fullscreenSpinner.setSelection(OtherAlgorithms.getIntFromStatusLeisteAusblenden(preferences.statusLeisteAuslenden));

        textViews = new TextView[4];
        textViews[0] = (TextView) findViewById(R.id.activity_preferences_grouping_text);
        textViews[0].setTypeface(Resources.roboto_light);
        textViews[1] = (TextView) findViewById(R.id.activity_preferences_fullscreen_text);
        textViews[1].setTypeface(Resources.roboto_light);
        textViews[2] = (TextView) findViewById(R.id.activity_preferences_design_text);
        textViews[2].setTypeface(Resources.roboto_light);
        textViews[3] = (TextView) findViewById(R.id.activity_preferences_ueber_text);
        textViews[3].setTypeface(Resources.roboto_light);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        if (Build.VERSION.SDK_INT >= 11)
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(preferences.secondaryColor));

        if (((VertretungsplanApp)getApplication()).preferences.statusLeisteAuslenden == Preferences.StatusLeisteAuslenden.Immer)
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        else
        {
            Window mainWindow = this.getWindow();

            if (Build.VERSION.SDK_INT >= 21)
            {
                mainWindow.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                mainWindow.setStatusBarColor(((VertretungsplanApp)getApplication()).preferences.secondaryColor);
            }
        }

        if (Build.VERSION.SDK_INT >= 16)
            for (int i = 0; i < textViews.length; i++)
                textViews[i].setBackground(new ColorDrawable(preferences.primaryColor));

        Preferences prefs = ((VertretungsplanApp)getApplication()).preferences;
        int colorMiddle = Color.red(prefs.primaryColor) + Color.blue(prefs.primaryColor) + Color.green(prefs.primaryColor);
        colorMiddle = colorMiddle / 3;
        ActionBar actionBar = this.getSupportActionBar();
        if (colorMiddle > 128)
        {
            for (int i = 0; i < textViews.length; i++)
                textViews[i].setTextColor(Color.BLACK);
        }
        else
        {
            for (int i = 0; i < textViews.length; i++)
                textViews[i].setTextColor(Color.WHITE);
        }
        colorMiddle = Color.red(prefs.secondaryColor) + Color.blue(prefs.secondaryColor) + Color.green(prefs.secondaryColor);
        colorMiddle = colorMiddle / 3;
        if (colorMiddle > 128)
        {
            actionBar.setTitle(Html.fromHtml("<font color=\"black\">Einstellungen</font>"));
            actionBar.setSubtitle(Html.fromHtml("<font color=\"black\">Martineum Vertretungsplan App</font>"));
        }
        else
        {
            actionBar.setTitle(Html.fromHtml("<font color=\"white\">Einstellungen</font>"));
            actionBar.setSubtitle(Html.fromHtml("<font color=\"white\">Martineum Vertretungsplan App</font>"));
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        switch (parent.getId())
        {
            case R.id.activity_preferences_grouping_spinner:
                preferences.gruppierenNach = OtherAlgorithms.getSpalteFromInt(groupingSpinner.getSelectedItemPosition());
                EnvironmentInterfaces.savePreferences(preferences, getApplication());
                break;
            case R.id.activity_preferences_fullscreen_spinner:
                preferences.statusLeisteAuslenden = OtherAlgorithms.getStatusLeisteAusblendenFromInt(fullscreenSpinner.getSelectedItemPosition());
                EnvironmentInterfaces.savePreferences(preferences, getApplication());
                break;
        }

        if (((VertretungsplanApp)getApplication()).preferences.statusLeisteAuslenden == Preferences.StatusLeisteAuslenden.Immer)
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        else
            this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
            case R.id.activity_preferences_design_color1b:
                ColorSelectingDialog dialog = new ColorSelectingDialog(preferences.primaryColor, new IColorSelectingDialogListener() {
                    @Override
                    public void colorDialogClosed(ColorSelectingDialog sender, int r, int g, int b, boolean succesfully) {
                        if (succesfully) preferences.primaryColor = Color.rgb(r, g, b);
                        onResume();
                    }
                });
                dialog.show("Hauptfarbe", this);
                break;
            case R.id.activity_preferences_design_color2b:
                ColorSelectingDialog dialog2 = new ColorSelectingDialog(preferences.secondaryColor, new IColorSelectingDialogListener() {
                    @Override
                    public void colorDialogClosed(ColorSelectingDialog sender, int r, int g, int b, boolean succesfully) {
                        if (succesfully) preferences.secondaryColor = Color.rgb(r, g, b);
                        onResume();
                    }
                });
                dialog2.show("Akzentfarbe", this);
                break;
        }
    }
}
