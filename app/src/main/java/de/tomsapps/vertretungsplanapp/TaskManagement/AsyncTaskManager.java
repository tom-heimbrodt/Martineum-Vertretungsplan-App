package de.tomsapps.vertretungsplanapp.TaskManagement;


import de.tomsapps.vertretungsplanapp.Core.VertretungsplanApp;

public class AsyncTaskManager
{
    ThreadSynchronizer synchronizer     = new ThreadSynchronizer();
    BackgroundThread   backgroundThread = new BackgroundThread();
    VertretungsplanApp application;

    public AsyncTaskManager(VertretungsplanApp app)
    {
        this.application = app;
    }

    public void addTask(Task task)
    {
        synchronizer.addTask(task);
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
        private ThreadStatus status;
        public ThreadStatus getStatus() { return status; }

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
            {
                // wird asynchron aufgeführt
                // Task abarbeiten
                Task task = synchronizer.getNextTask();
                if (task == null) { status = ThreadStatus.FINISHED; return; }
                else
                {
                    task.runTaskSync(application);
                    synchronizer.removeTask(task);
                }
            }
        }
    }

    private enum ThreadStatus
    {
        NOT_STARTED, RUNNING, FINISHED
    }
}