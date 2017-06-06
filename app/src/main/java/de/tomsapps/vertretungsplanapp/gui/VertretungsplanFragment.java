package de.tomsapps.vertretungsplanapp.gui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import de.tomsapps.vertretungsplanapp.R;
import de.tomsapps.vertretungsplanapp.algorithms.OtherAlgorithms;
import de.tomsapps.vertretungsplanapp.core.Preferences;
import de.tomsapps.vertretungsplanapp.core.Resources;
import de.tomsapps.vertretungsplanapp.core.VertretungsplanApp;

public class VertretungsplanFragment extends Fragment
// Stellt das Layout eines Vertretungsplans in Form eines Fragments dar.
{
    int position;
    ExpandableListView listView;
    ProgressBar progressBar;
    TextView ueberschrift;
    RelativeLayout ueberschriftLayout;
    VertretungsplanListAdapter listViewAdapter;

    WebView webview;

    MainActivity activity;
    MainActivityPageManager tabManager;
    VertretungsplanApp application;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // Verweise erstellen
        activity = (MainActivity) this.getActivity();
        tabManager = activity.tabManager;
        application = (VertretungsplanApp) activity.getApplication();
        // Position aus Argumenten extrahieren
        position = this.getArguments().getInt("POSITION");
    }

    public float getTitleHeight()
    // gibt Höhe des Titels/Überschrift zurück
    {
        if (Build.VERSION.SDK_INT >= 11)
        {
            float ueberschriftY = ueberschriftLayout.getY();
            float ueberschriftHeight = ueberschriftLayout.getHeight();
            return ueberschriftHeight + ueberschriftY;
        }
        else
            return -1;
    }

     @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Erstelle Layout . . .
        View rootView = inflater.inflate(R.layout.fragment_vertretungsplan, container, false); // -> lade Layout aus XML-Datei
        ueberschrift = (TextView) rootView.findViewById(R.id.fragment_vertretungsplan_title);
        ueberschrift.setTypeface(Resources.roboto_light);
        Preferences prefs = application.preferences;
        int color = (Color.red(prefs.primaryColor) + Color.green(prefs.primaryColor) + Color.blue(prefs.primaryColor)) / 3;
        if (color > 128)
            ueberschrift.setTextColor(Color.BLACK);
        else
            ueberschrift.setTextColor(Color.WHITE);
        ueberschrift.setText(OtherAlgorithms.getDayOfWeek(position));
        progressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        listView = (ExpandableListView) rootView.findViewById(R.id.exp_list_view);
        ueberschriftLayout = (RelativeLayout) rootView.findViewById(R.id.fragment_vertretungsplan_title_layout);
        webview = (WebView) rootView.findViewById(R.id.fragment_vertretungsplan_webview);

        update();

        return rootView;
    }

    public void update()
    // aktualisiert das Layout
    {
		if (application.getVertretungsplan(position) != null && !application.showHTML)
		{
			if (listView != null)
			{
				listView.setAdapter(listViewAdapter = new VertretungsplanListAdapter(position, this.getActivity(), application));
				// Datum in den Titel schreiben
				ueberschrift.setText(application.getVertretungsplan(position).getDate());
				// Progressbar ausblenden
				listView.setVisibility(View.VISIBLE);
				if (progressBar != null)
					progressBar.setVisibility(View.GONE);
				if (webview != null)
					webview.setVisibility(View.INVISIBLE);
			}
		}
        else if (application.getVertretungsplanRawData(position) != null && webview != null &&
                (application.showHTML || application.getVertretungsplan(position) == null))
        {
	        if (webview != null)
            {
                try
                {
                    String base64 = android.util.Base64.encodeToString(application.getVertretungsplanRawData(position)
                            .getBytes("iso-8859-1"), android.util.Base64.DEFAULT);
                    webview.loadData(base64, "text/html; charset=iso-8859-1", "base64");
                    webview.setVisibility(View.VISIBLE);
                    if (progressBar != null)
						progressBar.setVisibility(View.GONE);
	                if (listView != null)
						listView.setVisibility(View.INVISIBLE);
                } catch (Exception e) { }
            }
        }

        if (Build.VERSION.SDK_INT >= 16)
			ueberschriftLayout.setBackground(new ColorDrawable(application.preferences.primaryColor));
    }
}