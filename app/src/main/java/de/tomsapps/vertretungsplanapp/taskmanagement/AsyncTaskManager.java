package de.tomsapps.vertretungsplanapp.taskmanagement;

import de.tomsapps.vertretungsplanapp.core.VertretungsplanApp;

public class AsyncTaskManager
// Klasse, die die asynchronen Aufgabenverwaltung verwaltet
{
    ThreadSynchronizer synchronizer     = new ThreadSynchronizer(); // --> s. ThreadSynchronizer.java
    BackgroundThread   backgroundThread = new BackgroundThread();   // --> s. BackgroundThread Klasse
    VertretungsplanApp application;

    public AsyncTaskManager(VertretungsplanApp app)
    {   this.application = app;   }

    public void addTask(Task task)
    // Ermöglicht das Hinzufügen eines Auftrages
    {
        // Aufgabe wird zur Aufgabenliste hinzugefügt . . .
        synchronizer.addTask(task);
        // . . . und falls kein Arbeitsthread läuft diesen [evtl. zuvor erstellen und] starten
        switch (backgroundThread.getStatus())
        {
            case FINISHED:
                backgroundThread = new BackgroundThread();
            case NOT_STARTED:
                backgroundThread.start();
                break;
        }
    }

    private class BackgroundThread extends Thread
    // repräsentiert einen im Hintergrund laufenden Arbeitsthread
    {
        private ThreadStatus status;    // --> Status des Threads, entweder noch-nicht-gestartet (NOT_STARTED),
        public ThreadStatus getStatus() //     arbeitet-grade (RUNNING), oder schon-beendet (FINISHED)
        {   return status;   }

        public BackgroundThread()
        {
            super();
            status = ThreadStatus.NOT_STARTED;
        }

        @Override
        public void start()
        {
            status = ThreadStatus.RUNNING;
            super.start();
        }

        @Override
        public void run()
        {
            while (true)
            // --> kann hier noch keine Abbruchbedingung deklarieren, da bei Erfülltsein dieser
            //     zunächst der ThreadStatus auf beendet gesetzt werden muss
            //     (SOLLTE DIES NICHT GESCHEHEN UND IN DER ZEIT ZWISCHEN BEENDEN DER SCHLEIFE UND
            //      SETZTEN DES BEENDEN FLAGS EIN AUFTRAG DER AUFGABENVERWALTUNG ZUGEFÜHRT WIRD,
            //      WIRD ES ZU UNVORHERSEHBAREN EREIGNISSEN KOMMEN (WAHRSCHEINLICH WÜRDE DER AUF-
            //      TRAG EINFACH NICHT ABGEARBEITET, WAS ABER AUCH SCHON EINEN BUG DARSTELLT.)
            {
              // -- *WIRD ASYNCHRON AUSGEFÜHRT* --

                Task task = synchronizer.getNextTask();
                if (task == null) // --> wenn kein Auftrag mehr vorhanden ist Schleife sicher beenden
                {   status = ThreadStatus.FINISHED; return;   }
                else
                {
                    task.runTaskSync(application); // -> Auftrag abarbeiten
                    synchronizer.removeTask(task);
                }
            }
        }
    }

    private enum ThreadStatus
    // Repräsentiert den Status des Hintergrundthreads
    {
        NOT_STARTED, RUNNING, FINISHED
    }
}