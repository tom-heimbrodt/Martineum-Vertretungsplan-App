package de.tomsapps.vertretungsplanapp.GUI;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import de.tomsapps.vertretungsplanapp.StaticSupportAlgorithms.OtherAlgorithms;
import de.tomsapps.vertretungsplanapp.TaskManagement.AsyncTaskManager;
import de.tomsapps.vertretungsplanapp.TaskManagement.ITaskOwner;
import de.tomsapps.vertretungsplanapp.TaskManagement.Task;
import de.tomsapps.vertretungsplanapp.Core.VertretungsplanApp;

public class MainActivityTabManager extends PagerAdapter implements ITaskOwner
// Verwaltet die Vertretungsplantabs der Aktivität.
{
    // Zwischenspeierung der Fragments, damit diese nicht immer wieder neu erzeugt werden müssen
    VertretungsplanFragment[] fragments = new VertretungsplanFragment[5];
    // Verweis auf Application-Objekt
    VertretungsplanApp application;
    AsyncTaskManager taskManager;
    MainActivity activity;
    FragmentManager fragmentManager;

    public MainActivityTabManager(FragmentManager fragmentManager, VertretungsplanApp application, MainActivity activity)
    // Konstruktor
    {
        // Verweise speichern
        this.application = application;
        this.taskManager = application.getApplicationTaskManager();
        this.activity = activity;
        this.fragmentManager = fragmentManager;
        // Vertretungspläne asynchron aktualisieren
        taskManager.addTask(new Task(this, "DOWNLOAD", "Montag"));
        taskManager.addTask(new Task(this, "DOWNLOAD", "Dienstag"));
        taskManager.addTask(new Task(this, "DOWNLOAD", "Mittwoch"));
        taskManager.addTask(new Task(this, "DOWNLOAD", "Donnerstag"));
        taskManager.addTask(new Task(this, "DOWNLOAD", "Freitag"));
        taskManager.addTask(new Task(this, "ANALYSE", "Montag"));
        taskManager.addTask(new Task(this, "ANALYSE", "Dienstag"));
        taskManager.addTask(new Task(this, "ANALYSE", "Mittwoch"));
        taskManager.addTask(new Task(this, "ANALYSE", "Donnerstag"));
        taskManager.addTask(new Task(this, "ANALYSE", "Freitag"));
        taskManager.addTask(new Task(this, "SAVE_LOCAL", "Montag"));
        taskManager.addTask(new Task(this, "SAVE_LOCAL", "Dienstag"));
        taskManager.addTask(new Task(this, "SAVE_LOCAL", "Mittwoch"));
        taskManager.addTask(new Task(this, "SAVE_LOCAL", "Donnerstag"));
        taskManager.addTask(new Task(this, "SAVE_LOCAL", "Freitag"));
    }

    @Override
    public Fragment instantiateItem(ViewGroup container, int position)
    {
        Fragment fragment = getItem(position);
        FragmentTransaction trans = fragmentManager.beginTransaction();
        trans.add(container.getId(),fragment,"fragment:"+position);
        trans.commit();
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
        if (0 <= position && position < fragments.length)
        {
            FragmentTransaction trans = fragmentManager.beginTransaction();
            trans.remove(fragments[position]);
            trans.commit();
            fragments[position] = null;
        }
    }

    public VertretungsplanFragment getItem(int i)
    // gibt ein vorhandenes oder neu erstelltes Fragment zurück
    {
        // Fragment zurückgeben
        if (fragments[i] != null)
        {
            return fragments[i];
        }
        else
        {
            fragments[i] = new VertretungsplanFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("POSITION", i);
            fragments[i].setArguments(bundle);

            return fragments[i];
        }
    }

    @Override
    public int getCount()
    // gibt die Anzahl der Fragments zurück
    { return 5; }

    @Override
    public boolean isViewFromObject(View view, Object fragment)
    {
        return ((Fragment) fragment).getView() == view;
    }

    @Override
    public void taskFinished(final Task task, boolean successfully)
    {
        if (task.getArgs()[0].contentEquals("ANALYSE"))
        {
            activity.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        fragments[OtherAlgorithms.getIndexFromDay(task.getArgs()[1])].update();
                    }
                    catch (Exception e) { }
                }
            });
        }
    }
}