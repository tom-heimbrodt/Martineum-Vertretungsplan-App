package de.tomsapps.vertretungsplanapp.gui;

import android.content.*;
import android.gesture.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.os.*;
import android.support.v4.view.*;
import android.support.v7.app.*;
import android.util.*;
import android.view.*;
import android.view.animation.*;
import android.widget.*;
import de.tomsapps.vertretungsplanapp.*;
import de.tomsapps.vertretungsplanapp.algorithms.*;
import de.tomsapps.vertretungsplanapp.core.*;
import de.tomsapps.vertretungsplanapp.taskmanagement.*;

import java.util.*;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener
// Hauptaktivität der Anwendung.
{
	// globale Verweise
	public  MainActivityPageManager tabManager;
	private VertretungsplanApp      application;
	private Window                  mainWindow;
	private ViewPager               viewPager;
	private RelativeLayout          dropDownMenuView;
	private FrameLayout             mainContent;
	private GestureOverlayView      gestureOverlay;
	private View                    menuActionBar;
	// globale Flags
	private boolean isMenuDownFlag = false;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	// Wird aufgerufen, wenn die Aktivität erstellt wird.
	{
		// Basisklasse initialisieren und benötigte Verweise ertsellen
		super.onCreate(savedInstanceState);
		application = (VertretungsplanApp) this.getApplication();
		mainWindow = this.getWindow();

		// Anwendung im Vollbild ausführen
		if (application.preferences.statusLeisteAuslenden != Preferences.StatusLeisteAuslenden.Nie)
			mainWindow.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams
					.FLAG_FULLSCREEN);

		// ActionBar ausblenden, sofern vorhanden (API >= 11)
		ActionBar actionBar = this.getSupportActionBar();
		if (actionBar != null) actionBar.hide();

		// Layout laden und initialisieren, sowie Verweise erstellen
		this.setContentView(R.layout.activity_main);
		dropDownMenuView = (RelativeLayout) findViewById(R.id.activity_main_drop_down_menu);
		gestureOverlay = (GestureOverlayView) findViewById(R.id.activity_main_gesture_overlay);
		mainContent = (FrameLayout) findViewById(R.id.activity_main_main_content);
		viewPager = (ViewPager) findViewById(R.id.view_pager);
		menuActionBar = findViewById(R.id.menu_icon_bar_shadow_1);

		gestureOverlay.setOnTouchListener(this);

		// viewPager zeigt die Tabs an, die von tabManager verwaltet werden
		viewPager.setOffscreenPageLimit(7);
		viewPager.setAdapter(tabManager = new MainActivityPageManager(this.getSupportFragmentManager(), application,
                this));
		viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
		                                  {
		                                      int position = 1;

			                                  @Override
			                                  public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
			                                  {

			                                  }

			                                  @Override
			                                  public void onPageSelected(int _position)
			                                  {
			                                  	  position = _position;
			                                  }

			                                  @Override
			                                  public void onPageScrollStateChanged(int state)
			                                  {
			                                  	   if (state == ViewPager.SCROLL_STATE_IDLE)
												   {
													   if (position == 0)
													   {
														   viewPager.setCurrentItem(5, false);
													   }

													   // skip fake page (last), go to first page
													   if (position == 6)
													   {
														   viewPager.setCurrentItem(1, false); //notice how this jumps to position 1, and not position 0. Position 0 is the fake page!
													   }

												   }
			                                  }
		                                  });
		viewPager.setCurrentItem(1);

		for (int i = 0; i < 5; i++)
		{
			if (application.getVertretungsplan(i) == null)
			{
				VertretungsplanLoadingTask task = new VertretungsplanLoadingTask(application, this);
				task.execute(i);
			}
		}

		((Button) findViewById(R.id.button01)).setTypeface(Resources.roboto_light);
		((Button) findViewById(R.id.button02)).setTypeface(Resources.roboto_light);
		((Button) findViewById(R.id.button03)).setTypeface(Resources.roboto_light);
		((Button) findViewById(R.id.button04)).setTypeface(Resources.roboto_light);
		((Button) findViewById(R.id.button05)).setTypeface(Resources.roboto_light);
	}

	@Override
	protected void onResume()
	{
		super.onResume();

		hideDropDownMenu();

		if (application.preferences.statusLeisteAuslenden != Preferences.StatusLeisteAuslenden.Nie)
			this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams
                    .FLAG_FULLSCREEN);
		else
			this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

		if (Build.VERSION.SDK_INT >= 21)
		{
			mainWindow.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			mainWindow.setStatusBarColor(application.preferences.secondaryColor);
		}

		if (Build.VERSION.SDK_INT >= 16)
			menuActionBar.setBackground(new ColorDrawable(application.preferences.secondaryColor));

		try
		{
			for (int i = 0; i < tabManager.getCount(); i++)
				tabManager.updateFragment(i);
		}
		catch (Exception e) {}

		Preferences prefs       = application.preferences;
		int         colorMiddle = Color.red(prefs.secondaryColor) + Color.blue(prefs.secondaryColor) + Color.green
                (prefs.secondaryColor);
		colorMiddle = colorMiddle / 3;
		if (colorMiddle > 128)
		{
			findViewById(R.id.button_back_image).setBackgroundResource(R.drawable.up_black);
			findViewById(R.id.button_pref_image).setBackgroundResource(R.drawable.settings_black);
			findViewById(R.id.button_reload_image).setBackgroundResource(R.drawable.refresh_black);
		}
		else
		{
			findViewById(R.id.button_back_image).setBackgroundResource(R.drawable.up_white);
			findViewById(R.id.button_pref_image).setBackgroundResource(R.drawable.settings_white);
			findViewById(R.id.button_reload_image).setBackgroundResource(R.drawable.refresh_white);
		}
	}


	ArrayList<android.support.v7.app.AlertDialog.Builder> preLoadingDialogs = new ArrayList<android.support.v7.app
            .AlertDialog.Builder>();

	@Override
	protected void onPostResume()
	{
		super.onPostResume();

		for (int i = 0; i < preLoadingDialogs.size(); i++)
			preLoadingDialogs.get(i).create().show();
		preLoadingDialogs.clear();
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
			viewPager.animate().translationY(dropDownMenuView.getHeight()).setInterpolator(new
                    AccelerateDecelerateInterpolator()).start();
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
				viewPager.setCurrentItem(1, true);
				hideDropDownMenu();
				break;
			case R.id.button02:
				viewPager.setCurrentItem(2, true);
				hideDropDownMenu();
				break;
			case R.id.button03:
				viewPager.setCurrentItem(3, true);
				hideDropDownMenu();
				break;
			case R.id.button04:
				viewPager.setCurrentItem(4, true);
				hideDropDownMenu();
				break;
			case R.id.button05:
				viewPager.setCurrentItem(5, true);
				hideDropDownMenu();
				break;
			case R.id.button_back:
				hideDropDownMenu();
				break;
			case R.id.button_pref:
				// Einstellungsactivity starten
				Intent intent = new Intent(this, PreferencesActivity.class);
				this.startActivity(intent);
				break;
			case R.id.button_reload:
				for (int i = 0; i < 5; i++)
				{
					VertretungsplanLoadingTask task = new VertretungsplanLoadingTask(application, this);
					task.execute(i);
				}
				showToast("Daten neu geladen.");
				hideDropDownMenu();
				break;
			case R.id.fragment_vertretungsplan_title:
			case R.id.fragment_vertretungsplan_title_layout:
				// do nothing
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

				case MotionEvent.ACTION_MOVE: // -> wird jedesmal ausgeführt, wenn Finger/Stift/WasAuchImmer bewegt
				// wird
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
					if (dif > GESTURE_DROP_DOWN_MENU_MIN_Y_DIFFERENZ && dragYStart <= tabManager.getItem(viewPager
                            .getCurrentItem()).getTitleHeight())
						showDropDownMenu();
					else if (!isMenuDownFlag)
						hideDropDownMenu();
					break;
			}

		mainContent.dispatchTouchEvent(motionEvent);
		// -> damit auch noch unter dem GestureOverlay liegende Objekte Touch Ereignisse empfangen können

		return true;
	}

	public void showToast(String message)
	{
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
	}

	public void showDialog(String title, String message)
	// zeigt einen Informationsdialog an
	{
		android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
		builder.setMessage(message);
		builder.setTitle(title);
		builder.setCancelable(true);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialogInterface, int i)
			{
				dialogInterface.dismiss();
			}
		});

		try
		{
			builder.create().show();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			preLoadingDialogs.add(builder);
		}
	}

	public void taskFinished(int _vplanID, Vertretungsplan _vlpan, VertretungsplanLoadingTask.ErrorCode _errorCode)
	{
		try
		{
			tabManager.updateFragment(_vplanID);
		}
		catch (Exception e)
		{
		}
	}
}