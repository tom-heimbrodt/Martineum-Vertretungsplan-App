package de.tomsapps.vertretungsplanapp.GUI;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import de.tomsapps.vertretungsplanapp.R;
import de.tomsapps.vertretungsplanapp.Core.VertretungsplanApp;

public class MainActivity extends FragmentActivity
// Hauptaktivität der Anwendung.
{
    public static MainActivityTabManager tabManager;
    VertretungsplanApp application;
    ViewPager viewPager;
    View dropDownMenuView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    // Wird aufgerufen, wenn die Aktivität erstellt wird.
    {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // ActionBar initialisieren
        ActionBar actionBar = this.getActionBar();
        if (actionBar != null) actionBar.hide();
        super.onCreate(savedInstanceState);
        // Verweis auf Application Objekt erstellen
        application = (VertretungsplanApp) this.getApplication();
        // Layout aus der Layoutdatei laden.
        this.setContentView(R.layout.activity_main);
        dropDownMenuView = findViewById(R.id.activity_main_drop_down_menu);
        // ViewPager und TabManager initialisieren
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(tabManager = new MainActivityTabManager(this.getSupportFragmentManager(), application, this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        switch (id)
        {
            case R.id.action_montag:
                viewPager.setCurrentItem(0, false);
                return true;
            case R.id.action_dienstag:
                viewPager.setCurrentItem(1, false);
                return true;
            case R.id.action_mittwoch:
                viewPager.setCurrentItem(2, false);
                return true;
            case R.id.action_donnerstag:
                viewPager.setCurrentItem(3, false);
                return true;
            case R.id.action_freitag:
                viewPager.setCurrentItem(4, false);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean isMenuDown = false;
    public void toggleDropDownMenu()
    {
        if (!isMenuDown)
            viewPager.animate().translationY(dropDownMenuView.getHeight());
        else
            viewPager.animate().translationY(0);
        isMenuDown = !isMenuDown;
    }

    public void layoutClicked(View view)
    {
        int id = view.getId();
        switch (id)
        {
            case R.id.fragment_vertretungsplan_title:
                toggleDropDownMenu();
                break;
            case R.id.button01:
                viewPager.setCurrentItem(0, true);
                toggleDropDownMenu();
                break;
            case R.id.button02:
                viewPager.setCurrentItem(1, true);
                toggleDropDownMenu();
                break;
            case R.id.button03:
                viewPager.setCurrentItem(2, true);
                toggleDropDownMenu();
                break;
            case R.id.button04:
                viewPager.setCurrentItem(3, true);
                toggleDropDownMenu();
                break;
            case R.id.button05:
                viewPager.setCurrentItem(4, true);
                toggleDropDownMenu();
                break;
            case R.id.button06:
                break;
        }
    }
}