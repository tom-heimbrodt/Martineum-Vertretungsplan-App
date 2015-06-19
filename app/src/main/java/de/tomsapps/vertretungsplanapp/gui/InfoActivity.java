package de.tomsapps.vertretungsplanapp.gui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.WindowManager;

import de.tomsapps.vertretungsplanapp.R;
import de.tomsapps.vertretungsplanapp.core.Preferences;
import de.tomsapps.vertretungsplanapp.core.VertretungsplanApp;

public class InfoActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_info);

        if (((VertretungsplanApp)getApplication()).preferences.statusLeisteAuslenden == Preferences.StatusLeisteAuslenden.Immer)
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
