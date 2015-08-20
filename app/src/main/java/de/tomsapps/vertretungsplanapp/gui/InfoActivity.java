package de.tomsapps.vertretungsplanapp.gui;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import de.tomsapps.vertretungsplanapp.R;
import de.tomsapps.vertretungsplanapp.core.Preferences;
import de.tomsapps.vertretungsplanapp.core.Resources;
import de.tomsapps.vertretungsplanapp.core.VertretungsplanApp;

public class InfoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Preferences prefs = ((VertretungsplanApp) getApplication()).preferences;

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(prefs.secondaryColor));


        this.setContentView(R.layout.activity_info);

        ((TextView)findViewById(R.id.activity_info_info_text)).setTypeface(Resources.roboto_light);
        ((TextView)findViewById(R.id.activity_info_headline)).setTypeface(Resources.roboto_light);

        if (((VertretungsplanApp)getApplication()).preferences.statusLeisteAuslenden == Preferences.StatusLeisteAuslenden.Immer)
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        else
        {
            Window mainWindow = getWindow();

            if (Build.VERSION.SDK_INT >= 21)
            {
                mainWindow.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                mainWindow.setStatusBarColor(((VertretungsplanApp)getApplication()).preferences.secondaryColor);
            }
        }

        View view = findViewById(R.id.activity_info_headline_background);
        if (Build.VERSION.SDK_INT >= 16)
            view.setBackground(new ColorDrawable(((VertretungsplanApp)getApplication()).preferences.primaryColor));

        int colorMiddle = Color.red(prefs.secondaryColor) + Color.blue(prefs.secondaryColor) + Color.green(prefs.secondaryColor);
        colorMiddle = colorMiddle / 3;
        ActionBar actionBar = this.getSupportActionBar();
        if (colorMiddle > 128)
        {
            // apply creepy action bar text color hack
            actionBar.setTitle(Html.fromHtml("<font color=\"black\">App - Info</font>"));
            actionBar.setSubtitle(Html.fromHtml("<font color=\"black\">Martineum Vertretungsplan App</font>"));
            actionBar.setIcon(R.drawable.back_black);
            // TODO: switch to dark back icon
        }
        else
        {
            actionBar.setTitle(Html.fromHtml("<font color=\"white\">App - Info</font>"));
            actionBar.setSubtitle(Html.fromHtml("<font color=\"white\">Martineum Vertretungsplan App</font>"));
        }


        colorMiddle = Color.red(prefs.primaryColor) + Color.blue(prefs.primaryColor) + Color.green(prefs.primaryColor);
        colorMiddle = colorMiddle / 3;
        if (colorMiddle > 128)
        {
            ((TextView)findViewById(R.id.activity_info_headline)).setTextColor(Color.BLACK);
        }
        else
        {
            ((TextView)findViewById(R.id.activity_info_headline)).setTextColor(Color.WHITE);
        }
    }
}
