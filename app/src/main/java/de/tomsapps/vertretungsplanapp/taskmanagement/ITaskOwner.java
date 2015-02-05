package de.tomsapps.vertretungsplanapp.taskmanagement;

public interface ITaskOwner
// Kann von einer Klasse implementiert werden, um selbst Aufträge zur Aufgabenverwaltung hinzuzufügen
// Damit kann die entsprechende Klasse benachrichtigt werden, wenn der Auftrag abgeschlossen ist
{
    public void taskFinished(Task task, boolean successfully);
}