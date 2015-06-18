package de.tomsapps.vertretungsplanapp.gui;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import de.tomsapps.vertretungsplanapp.R;

public class PreferencesActivity extends FragmentActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_preferences);

        ActionBar actionBar = this.getActionBar();
        actionBar.setTitle("Einstellungen");
        actionBar.setSubtitle("Martineum Vertretungsplan App");
    }
}
