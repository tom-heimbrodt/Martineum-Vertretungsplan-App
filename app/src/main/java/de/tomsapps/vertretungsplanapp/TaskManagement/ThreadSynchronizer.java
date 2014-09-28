package de.tomsapps.vertretungsplanapp.TaskManagement;

import java.util.ArrayList;

public class ThreadSynchronizer
{
    private ArrayList<Task> taskList = new ArrayList<Task>();

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
