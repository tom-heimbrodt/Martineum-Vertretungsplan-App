package de.tomsapps.vertretungsplanapp.core;

import android.app.Application;
import android.graphics.Typeface;

import de.tomsapps.vertretungsplanapp.taskmanagement.AsyncTaskManager;
import de.tomsapps.vertretungsplanapp.taskmanagement.Task;

public class VertretungsplanApp extends Application
// Hauptklase, welche die Anwedung an sich repräsentiert.
{
    // erstelle einen TaskManager, der für die Aufgabenverwaltung zuständig ist.
    public AsyncTaskManager taskManager;
    // erstelle ThreadSynchronizer für Threadsicherheit
    private ThreadSynchronizer synchronizer = new ThreadSynchronizer();

    // hier werden alle Einstellungen gespeichert
    public Preferences preferences;

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

    public Vertretungsplan getVertretungsplan(int index)
    { return synchronizer.getVertretungsplan(index); }
    public void setVertretungsplan(int index, Vertretungsplan vertretungsplan)
    { synchronizer.setVertretungsplan(index, vertretungsplan); }
    public String getVertretungsplanRawData(int index)
    { return synchronizer.getVertretungsplanRawData(index); }
    public void setVertretungsplanRawData(int index, String data)
    { synchronizer.setVertretungsplanRawData(index, data); }

    public AsyncTaskManager getApplicationTaskManager()
    { return taskManager; }

    private final class ThreadSynchronizer
    // Klasse die Methoden und Funktionen zum sicheren synchronisierten Zugriff
    // auf von mehreren Threads benutzte Objekte gewährleistet
    {
        // gemeinsam genutzte Objekte
        private String[] vertretungsplaeneRawData = new String[5];
        private Vertretungsplan[] vertretungsplaene = new Vertretungsplan[5];

        public synchronized Vertretungsplan getVertretungsplan(int index)
        // gibt ein gewünschtes Vertretungsplan Objekt zurück
        { return vertretungsplaene[index]; }

        public synchronized void setVertretungsplan(int index, Vertretungsplan vertretungsplan)
        // ermöglicht es ein Vertretungsplan-Objekt zuzuweisen
        {
            if (index >= 0 && index <= 4)
                vertretungsplaene[index] = vertretungsplan;
        }

        public synchronized String getVertretungsplanRawData(int index)
        // gibt den gewünschten Vertretungsplan-HTML-String zurück
        { return vertretungsplaeneRawData[index]; }

        public void setVertretungsplanRawData(int index, String vertretungsplanRawData)
        // ermöglicht es den Vertretungsplan-HTML-Code zuzuweisen
        {
            if (index >= 0 && index <= 4)
                vertretungsplaeneRawData[index] = vertretungsplanRawData;
        }
    }
}
