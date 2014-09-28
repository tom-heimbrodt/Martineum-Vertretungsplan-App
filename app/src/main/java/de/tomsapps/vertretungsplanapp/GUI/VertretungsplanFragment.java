package de.tomsapps.vertretungsplanapp.GUI;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import de.tomsapps.vertretungsplanapp.R;
import de.tomsapps.vertretungsplanapp.Core.VertretungsplanApp;

public class VertretungsplanFragment extends Fragment
// Stellt das Layout eines Vertretungsplans in Form eines Fragments dar.
{
    int position;
    ExpandableListView listView;
    ProgressBar progressBar;
    TextView ueberschrift;
    VertretungsplanListAdapter listViewAdapter;

    public VertretungsplanFragment()
    {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        position = this.getArguments().getInt("POSITION");
    }

     @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_vertretungsplan, container, false);
        ueberschrift = (TextView) rootView.findViewById(R.id.fragment_vertretungsplan_title);
        ueberschrift.setText(getDayOfWeek(position));
        progressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        listView = (ExpandableListView) rootView.findViewById(R.id.exp_list_view);
        update();
        return rootView;
    }

    private String getDayOfWeek(int index)
    // gibt Wochentag als String zurück
    {
        switch (index)
        {
            case 0: return "Montag";
            case 1: return "Dienstag";
            case 2: return "Mittwoch";
            case 3: return "Donnerstag";
            case 4: return "Freitag";
            default:  return "HÄ?";
        }
    }

    public void update()
    {
        if (VertretungsplanApp.singleton.getVertretungsplan(position) != null && listView != null)
        {
            // Liste mit Hilfe des Adapters aktualisieren
            if (listView.getExpandableListAdapter() == null)
                listView.setAdapter(listViewAdapter = new VertretungsplanListAdapter(position, this.getActivity()));
            // Datum in den Titel schreiben
            ueberschrift.setText(VertretungsplanApp.singleton.getVertretungsplan(position).getDate());
            // Progressbar ausblenden
            listView.setVisibility(View.VISIBLE);
            if (progressBar != null)
                progressBar.setVisibility(View.GONE);
        }
    }
}