package de.tomsapps.vertretungsplanapp.taskmanagement;

import java.util.ArrayList;

public class ThreadSynchronizer
// Threadsynchronizer ermöglicht es von mehrern Thread auf Objekte zuzugreifen,
// ohne dass die Integrität dieser Objekte gefährdet würde
{
    // gemeinsam genutzte Objekte
    private ArrayList<Task> taskList = new ArrayList<Task>();

    // Methoden um Objekte zu lesen und manipulieren
    // --> diese Methoden sind synchronisiert, d. h. wird eine Methode aufgerufen
    //     werden bis zur Fertigstellung der Ausführung aller Methoden blockiert
    // --> http://docs.oracle.com/javase/tutorial/essential/concurrency/sync.html

    public synchronized Task getNextTask()
    {
        return (taskList.size() > 0)? taskList.get(0) : null;
    }

    public synchronized void addTask(Task task)
    {
        taskList.add(task);
    }

    public synchronized void removeTask(Task task)
    {
        taskList.remove(task);
    }
}
