package de.tomsapps.vertretungsplanapp.core;

import android.app.Application;
import android.graphics.Typeface;

import de.tomsapps.vertretungsplanapp.taskmanagement.AsyncTaskManager;
import de.tomsapps.vertretungsplanapp.taskmanagement.Task;

public class VertretungsplanApp extends Application
// Hauptklasse, welche die Anwendung an sich repräsentiert.
{
    // erstelle einen TaskManager, der für die Aufgabenverwaltung zuständig ist.
    public AsyncTaskManager taskManager;

    // hier werden alle Einstellungen gespeichert
    public Preferences preferences;

    // Daten der Vertretungspläne
    private String[] vertretungsplaeneRawData = new String[5];
    private Vertretungsplan[] vertretungsplaene = new Vertretungsplan[5];

    public boolean showHTML = false;

    @Override
    public void onCreate()
    // Haupteinstiegspunkt der Anwedung.
    {
        super.onCreate();

        preferences = new Preferences();

        // Aufgabenverwaltung initialisieren.
        taskManager = new AsyncTaskManager(this);
        taskManager.addTask(new Task(null, "LOAD_SETTINGS"));

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
    public String getVertretungsplanRawData(int index)
    {
        return vertretungsplaeneRawData[index];
    }
    public void setVertretungsplanRawData(int index, String data)
    {
        vertretungsplaeneRawData[index] = data;
    }

    public AsyncTaskManager getApplicationTaskManager()
    {
        return taskManager;
    }
}
