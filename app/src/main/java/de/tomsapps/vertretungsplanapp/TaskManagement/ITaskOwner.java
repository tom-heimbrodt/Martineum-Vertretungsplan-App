package de.tomsapps.vertretungsplanapp.TaskManagement;

public interface ITaskOwner
{
    public void taskFinished(Task task, boolean successfully);
}