package de.tomsapps.vertretungsplanapp.GUI;

import android.app.ActionBar;
import android.gesture.GestureOverlayView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import de.tomsapps.vertretungsplanapp.R;
import de.tomsapps.vertretungsplanapp.Core.VertretungsplanApp;

public class MainActivity extends FragmentActivity implements  View.OnTouchListener
// Hauptaktivität der Anwendung.
{
    // globale Verweise
    public MainActivityTabManager tabManager;
    private VertretungsplanApp    application;
    private Window                mainWindow;
    private ViewPager             viewPager;
    private RelativeLayout        dropDownMenuView;
    private FrameLayout           mainContent;
    private GestureOverlayView    gestureOverlay;
    // globale Flags
    private boolean isMenuDownFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    // Wird aufgerufen, wenn die Aktivität erstellt wird.
    {
        // Basisklasse initialisieren und benötigte Verweise ertsellen
        super.onCreate(null);
        application = (VertretungsplanApp) this.getApplication();
        mainWindow  = this.getWindow();

        // Anwendung im Vollbild ausführen
        mainWindow.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // ActionBar ausblenden
        ActionBar actionBar = this.getActionBar();
        if (actionBar != null) actionBar.hide();

        // Layout laden und initialisieren, sowie Verweise erstellen
        this.setContentView(R.layout.activity_main);
        dropDownMenuView = (RelativeLayout)     findViewById(R.id.activity_main_drop_down_menu);
        gestureOverlay   = (GestureOverlayView) findViewById(R.id.activity_main_gesture_overlay);
        mainContent      = (FrameLayout)        findViewById(R.id.activity_main_main_content);
        viewPager        = (ViewPager)          findViewById(R.id.view_pager);

        gestureOverlay.setOnTouchListener(this);
        viewPager.setOffscreenPageLimit(5);
        viewPager.setAdapter(tabManager = new MainActivityTabManager(this.getSupportFragmentManager(), application, this));
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu)
    // Wird durch den Menu-Button ausgelöst
    {
        toggleDropDownMenu();
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    // Menu muss erstellt werden, sonst wird onMenuOpened nicht ausgeführt
    {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onBackPressed()
    // Wird durch den Zurück-Button ausgelöst
    {
        if (isMenuDownFlag)
            hideDropDownMenu();
        else
            this.finish();
    }

    public void toggleDropDownMenu()
    {
        if (!isMenuDownFlag)
            showDropDownMenu();
        else
            hideDropDownMenu();
    }
    public void showDropDownMenu()
    {
        viewPager.clearAnimation();
        viewPager.animate().translationY(dropDownMenuView.getHeight()).start();
        isMenuDownFlag = true;
    }
    public void hideDropDownMenu()
    {
        viewPager.clearAnimation();
        viewPager.animate().translationY(0).start();
        isMenuDownFlag = false;
    }

    public void layoutClicked(View view)
    {
        int id = view.getId();
        switch (id)
        {
            case R.id.button01:
                viewPager.setCurrentItem(0, true);
                hideDropDownMenu();
                break;
            case R.id.button02:
                viewPager.setCurrentItem(1, true);
                hideDropDownMenu();
                break;
            case R.id.button03:
                viewPager.setCurrentItem(2, true);
                hideDropDownMenu();
                break;
            case R.id.button04:
                viewPager.setCurrentItem(3, true);
                hideDropDownMenu();
                break;
            case R.id.button05:
                viewPager.setCurrentItem(4, true);
                hideDropDownMenu();
                break;
        }
    }

    private float  dragYStart;
    private float pDragY;
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent)
    // wird ausgeführt, wenn auf ein View-Objekt geklickt wird und diese Activity als onouchListener gesetzt wurde
    {
        switch (motionEvent.getActionMasked())
        {
            case MotionEvent.ACTION_DOWN:
                dragYStart = motionEvent.getY(0);
                pDragY = dragYStart;
                break;
            case MotionEvent.ACTION_MOVE:
                if (dragYStart <= tabManager.getItem(viewPager.getCurrentItem()).getTitleHeight())
                {
                    if (motionEvent.getY() - dragYStart > 10)
                    {
                        float translateYBy = motionEvent.getY() - pDragY;
                        viewPager.setY(translateYBy + viewPager.getY());
                    }
                }
                pDragY = motionEvent.getY();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                float dif = motionEvent.getY() - dragYStart;
                DisplayMetrics displayMetrics = new DisplayMetrics();
                this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int displayHeight = displayMetrics.heightPixels;
                float GESTURE_DROP_DOWN_MENU_MIN_Y_DIFFERENZ =
                        (displayHeight / 2f > dropDownMenuView.getHeight())?
                                dropDownMenuView.getHeight() :
                                displayHeight / 2f;
                if (dif > GESTURE_DROP_DOWN_MENU_MIN_Y_DIFFERENZ && dragYStart <= tabManager.getItem(viewPager.getCurrentItem()).getTitleHeight())
                    showDropDownMenu();
                else
                    hideDropDownMenu();
                break;
        }

        mainContent.dispatchTouchEvent(motionEvent);
        return true;
    }
}