package de.tomsapps.vertretungsplanapp.Core;

import android.app.Application;

import de.tomsapps.vertretungsplanapp.TaskManagement.AsyncTaskManager;

public class VertretungsplanApp extends Application
// Hauptklase, welche die Anwedung an sich repräsentiert.
{
    // erstelle einen TaskManager, der für die Aufgabenverwaltung zuständig ist.
    public AsyncTaskManager taskManager;
    // erstelle ThreadSynchronizer für Threadsicherheit
    private ThreadSynchronizer synchronizer = new ThreadSynchronizer();
    // ein Singleton - Verweis auf die aktuelle Instanz.
    public static VertretungsplanApp singleton;

    @Override
    public void onCreate()
    // Haupteinstiegspunkt der Anwedung.
    {
        super.onCreate();
        singleton = this;

        // Aufgabenverwaltung initialisieren.
        taskManager = new AsyncTaskManager(this);
    }

    public void dispose()
    {
        taskManager = null;
        synchronizer = null;
        singleton = null;

        System.exit(0);
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
        { return (index >= 0 && index <= 4)? vertretungsplaene[index] : null; }

        public synchronized void setVertretungsplan(int index, Vertretungsplan vertretungsplan)
        // ermöglicht es ein Vertretungsplan-Objekt zuzuweisen
        {
            if (index >= 0 && index <= 4)
                vertretungsplaene[index] = vertretungsplan;
        }

        public synchronized String getVertretungsplanRawData(int index)
        // gibt den gewünschten Vertretungsplan-HTML-String zurück
        { return (index >= 0 && index <= 4)? vertretungsplaeneRawData[index] : null; }

        public void setVertretungsplanRawData(int index, String vertretungsplanRawData)
        // ermöglicht es den Vertretungsplan-HTML-Code zuzuweisen
        {
            if (index >= 0 && index <= 4)
                vertretungsplaeneRawData[index] = vertretungsplanRawData;
        }
    }
}
