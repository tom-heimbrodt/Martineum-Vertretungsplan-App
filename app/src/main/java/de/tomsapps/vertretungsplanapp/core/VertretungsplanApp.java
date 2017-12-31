package de.tomsapps.vertretungsplanapp.core;

import android.app.Application;
import android.graphics.Typeface;

import de.tomsapps.vertretungsplanapp.algorithms.EnvironmentInterfaces;
import de.tomsapps.vertretungsplanapp.algorithms.OtherAlgorithms;

public class VertretungsplanApp extends Application
// Hauptklasse, welche die Anwendung an sich repräsentiert.
{
    private static VertretungsplanApp singleton;

    public static VertretungsplanApp getInstance()
    {
        return singleton;
    }

    // hier werden alle Einstellungen gespeichert
    public Preferences preferences;

    // Daten der Vertretungspläne
    private volatile Vertretungsplan[] vertretungsplaene = new Vertretungsplan[5];
    private volatile VertretungsplanState[] state = new VertretungsplanState[5];

    public boolean firstRun = false;

    public enum VertretungsplanState
    {
        Pending,
        Loaded,
        Error
    }

    @Override
    public void onCreate()
    // Haupteinstiegspunkt der Anwedung.
    {
        super.onCreate();

        try
        {
            this.preferences = EnvironmentInterfaces.loadPreferences(this);
        }
        catch (Exception e)
        {
            firstRun = true;
            this.preferences = new Preferences();
        }

        for (int i = 0; i < state.length; i++)
            state[i] = VertretungsplanState.Pending;

        // Aufgabenverwaltung initialisieren.
        Resources.init(getAssets());
    }

    // Zugriffsmethoden auf Vertretungsplan - Daten
    // Müssen nicht synchronisiert werden,
    // da Verweiszuweisungen atomare Operationen sind.
    public Vertretungsplan getVertretungsplan(int index)
    { 
    	return vertretungsplaene[index];
    }
    public void setVertretungsplan(int index, Vertretungsplan vertretungsplan)
    {
        vertretungsplaene[index] = vertretungsplan;
    }

    public VertretungsplanState getVertretungsplanState(int index)
    {
    	return state[index];
    }
    public void setVertretungsplanState(int index, VertretungsplanState value)
    {
    	state[index] = value;
    }
}
