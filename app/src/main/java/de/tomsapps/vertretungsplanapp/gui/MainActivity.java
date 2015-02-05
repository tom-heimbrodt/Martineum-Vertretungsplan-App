package de.tomsapps.vertretungsplanapp.gui;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.gesture.GestureOverlayView;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import de.tomsapps.vertretungsplanapp.R;
import de.tomsapps.vertretungsplanapp.algorithms.OtherAlgorithms;
import de.tomsapps.vertretungsplanapp.core.VertretungsplanApp;
import de.tomsapps.vertretungsplanapp.taskmanagement.AsyncTaskManager;
import de.tomsapps.vertretungsplanapp.taskmanagement.ITaskOwner;
import de.tomsapps.vertretungsplanapp.taskmanagement.Task;

public class MainActivity extends FragmentActivity implements  View.OnTouchListener, ITaskOwner
// Hauptaktivität der Anwendung.
{
    // globale Verweise
    public MainActivityPageManager tabManager;
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
        super.onCreate(savedInstanceState);
        application = (VertretungsplanApp) this.getApplication();
        mainWindow  = this.getWindow();

        // Anwendung im Vollbild ausführen
        mainWindow.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // ActionBar ausblenden, sofern vorhanden (API >= 11)
        if (Build.VERSION.SDK_INT > 11)
        {
            ActionBar actionBar = this.getActionBar();
            if (actionBar != null) actionBar.hide();
        }

        // Layout laden und initialisieren, sowie Verweise erstellen
        this.setContentView(R.layout.activity_main);
        dropDownMenuView = (RelativeLayout)     findViewById(R.id.activity_main_drop_down_menu);
        gestureOverlay   = (GestureOverlayView) findViewById(R.id.activity_main_gesture_overlay);
        mainContent      = (FrameLayout)        findViewById(R.id.activity_main_main_content);
        viewPager        = (ViewPager)          findViewById(R.id.view_pager);

        gestureOverlay.setOnTouchListener(this);

        // viewPager zeigt die Tabs an, die von tabManager verwaltet werden
        viewPager.setOffscreenPageLimit(5);
        viewPager.setAdapter(tabManager = new MainActivityPageManager(this.getSupportFragmentManager(), application, this));

        // VertretungsplÃ¤ne asynchron aktualisieren
        AsyncTaskManager taskManager = application.getApplicationTaskManager();
        taskManager.addTask(new Task(this, "DOWNLOAD", "Montag"));

    }

   @Override
   public boolean dispatchKeyEvent(KeyEvent event)
   // Wird ausgelöst, wenn eine Taste gedrückt wird.
   // Gibt true zurück, wenn Ereignis nicht länger behandelt werden soll, ansonsten false
   {
       if (event.getAction() == KeyEvent.ACTION_UP)
       {
           int keyCode = event.getKeyCode();
           switch (keyCode)
           {
               case KeyEvent.KEYCODE_BACK:
                   if (isMenuDownFlag)
                       hideDropDownMenu();
                   else
                       this.finish();
                   return true;
               case KeyEvent.KEYCODE_MENU:
                   toggleDropDownMenu();
                   return true;
               default:
                   return false;
           }
       }
       else
           return true; // --> andere Aktionen, wie ACTION_DOWN werden einfach nicht beachtet
   }

    // Prozeduren um das Drop-Down-Menu anzuzeigen bzw. zu verstecken
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
        // ViewPager nach untern bewegen, wenn möglich mit Animation
        if (Build.VERSION.SDK_INT >= 14)
            viewPager.animate().translationY(dropDownMenuView.getHeight()).setInterpolator(new AccelerateDecelerateInterpolator()).start();
        else if (Build.VERSION.SDK_INT >= 11)
            viewPager.setY(dropDownMenuView.getHeight());

        isMenuDownFlag = true;
    }
    public void hideDropDownMenu()
    {
        viewPager.clearAnimation();
        // ViewPager nach oben bewegen, wenn möglich mit Animation
        if (Build.VERSION.SDK_INT >= 14)
            viewPager.animate().translationY(0).setInterpolator(new AccelerateDecelerateInterpolator()).start();
        else if (Build.VERSION.SDK_INT >= 11)
            viewPager.setY(dropDownMenuView.getHeight());

        isMenuDownFlag = false;
    }

    public void layoutClicked(View view)
    // wird ausgeführt, wenn auf ein Layout (-> Button) geklickt wurde
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

    private float dragYStart;
    private float pDragY;
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent)
    // wird ausgeführt, wenn auf ein View-Objekt geklickt wird und diese Activity als onTouchListener gesetzt wurde
    {
        if (Build.VERSION.SDK_INT >= 11)
            switch (motionEvent.getActionMasked())
            {
                case MotionEvent.ACTION_DOWN: // -> wird einmal zu Beginn ausgeführt, Variablen werden initialisiert
                    dragYStart = motionEvent.getY(0);
                    pDragY = dragYStart;
                    break;

                case MotionEvent.ACTION_MOVE: // -> wird jedesmal ausgeführt, wenn Finger/Stift/WasAuchImmer bewegt wird
                    //    -> Aktualisierung der Herunterziehanimation
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

                case MotionEvent.ACTION_UP:     //  -> wird ausgeführt, wenn die Aktion beendet wurde
                case MotionEvent.ACTION_CANCEL: //     -> prüfen ob das Menu unten ist oder nicht
                    float dif = motionEvent.getY() - dragYStart;
                    DisplayMetrics displayMetrics = new DisplayMetrics();
                    this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                    int displayHeight = displayMetrics.heightPixels;
                    float GESTURE_DROP_DOWN_MENU_MIN_Y_DIFFERENZ =
                            (displayHeight / 2f > dropDownMenuView.getHeight()) ?
                                    dropDownMenuView.getHeight() :
                                    displayHeight / 2f;
                    if (dif > GESTURE_DROP_DOWN_MENU_MIN_Y_DIFFERENZ && dragYStart <= tabManager.getItem(viewPager.getCurrentItem()).getTitleHeight())
                        showDropDownMenu();
                    else
                        hideDropDownMenu();
                    break;
            }

        mainContent.dispatchTouchEvent(motionEvent);
        // -> damit auch noch unter dem GestureOverlay liegende Objekte Touch Ereignisse empfangen können

        return true;
    }

    public void showErrorDialog(String title, String message)
    // zeigt einen Informationsdialog an
    {
        try
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(message);
            builder.setTitle(title);
            builder.setCancelable(true);
            final FragmentActivity activity = this;
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    dialogInterface.dismiss();
                    activity.finish();
                }
            });

            // Dialog anzeigen
            builder.create().show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void taskFinished(Task task, boolean successfully)
    {
        String[] args = task.getArgs();
        if (args[0].contentEquals("DOWNLOAD"))
        {
            int index = OtherAlgorithms.getIndexFromDay(args[1]);
            if (successfully)
            {
                if (index <= 3)
                    // nÃ¤chsten Plan herunterladen
                    application.getApplicationTaskManager().addTask(new Task(this, "DOWNLOAD", OtherAlgorithms.getDayOfWeek(index + 1)));
                else
                    application.getApplicationTaskManager().addTask(new Task(this, "ANALYZE", OtherAlgorithms.getDayOfWeek(0)));
            }
            else
            {
                application.getApplicationTaskManager().addTask(new Task(this, "LOAD_LOCAL", args[1]));
            }
        }
        else if (args[0].contentEquals("LOAD_LOCAL"))
        {
            int index = OtherAlgorithms.getIndexFromDay(args[1]);
            if (index <= 3 && successfully)
                application.getApplicationTaskManager().addTask(new Task(this, "DOWNLOAD", OtherAlgorithms.getDayOfWeek(index + 1)));
            else if (successfully)
                application.getApplicationTaskManager().addTask(new Task(this, "ANALYZE", OtherAlgorithms.getDayOfWeek(0)));
            else
                showErrorDialog("Keine Datenquelle gefunden.", "Es konnten keine Daten geladen werden.\r\nÜberprüfe deine Internetverbindung.");
        }
        else if (args[0].contentEquals("ANALYZE"))
        {
            final int index = OtherAlgorithms.getIndexFromDay(args[1]);
            if (successfully)
            {
                this.runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        try {
                            tabManager.updateFragment(index);
                        } catch(Exception e) { e.printStackTrace(); }
                    }
                });
            }

            if (index <= 3)
                application.getApplicationTaskManager().addTask(new Task(this, "ANALYZE", OtherAlgorithms.getDayOfWeek(index + 1)));
            else
                application.getApplicationTaskManager().addTask(new Task(this, "SAVE_LOCAL", OtherAlgorithms.getDayOfWeek(0)));
        }
        else if (args[0].contentEquals("SAVE_LOCAL"))
        {
            int index = OtherAlgorithms.getIndexFromDay(args[1]);
            if (index <= 3)
                application.getApplicationTaskManager().addTask(new Task(this, "SAVE_LOCAL", OtherAlgorithms.getDayOfWeek(index + 1)));
        }
    }
}