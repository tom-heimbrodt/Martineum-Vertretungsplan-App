package de.tomsapps.vertretungsplanapp.taskmanagement;

import de.tomsapps.vertretungsplanapp.core.Vertretungsplan;
import de.tomsapps.vertretungsplanapp.core.VertretungsplanApp;
import de.tomsapps.vertretungsplanapp.algorithms.EnvironmentInterfaces;
import de.tomsapps.vertretungsplanapp.algorithms.OtherAlgorithms;

public class Task
// Repräsentiert einen Auftrag (den es abzuarbeiten gilt)
{
    private String[] args;        // --> dem Auftrag werden Argumente hinzugefügt, damit er weiß, was er zu tun hat
                                  //     dadurch kann man die Aufgabenverwaltung leichter erweitern
    private ITaskOwner owner;     // --> der Auftragtraggeber
    private boolean errorOccured; // --> falls ein Fehler beim Bearbeiten auftritt

    public Task(ITaskOwner sender, String... args)
    {
        this.args         = args;
        this.owner        = sender;
        this.errorOccured = false;
    }

    public String[] getArgs() { return args; }

    public void runTaskSync(VertretungsplanApp app)
    // führt die Aufgabe synchron aus, d. h. der entsprechende Thread wird für die dauer der Ausführung blockiert
    {
        try
        {
            if (args[0].contentEquals("DOWNLOAD"))
            // Schlüsselwort um Datein von Martineum Server herunterzuladen
            {
                int index = OtherAlgorithms.getIndexFromDay(args[1]);
                try
                {
                    // Zuerst versuchen aus dem Internet zu laden . . .
                    app.setVertretungsplanRawData(index, EnvironmentInterfaces.Network.downloadData(EnvironmentInterfaces.Network.generateURL(index)));
                }
                catch (Exception e)
                {
                    try
                    {
                        // . . . dann aus dem lokalen Speicher . . .
                        app.setVertretungsplanRawData(index, EnvironmentInterfaces.LokalStorage.loadData(args[1].toUpperCase() + "_HTML_DATA", app));
                    }
                    catch (Exception e2)
                    {
                        // . . . und dann ist auch schon Schluss!
                        errorOccured = true;
                    }
                }
            }

            else if (args[0].contentEquals("ANALYSE"))
            // Schlüsselwort um heruntergeladene Daten zu analysieren
            {
                int index = OtherAlgorithms.getIndexFromDay(args[1]);
                app.setVertretungsplan(index, new Vertretungsplan(app.getVertretungsplanRawData(index)));
            }

            else if (args[0].contentEquals("SAVE_LOCAL"))
            // Schlüsselwort um Daten lokal zu speichern
            {
                int index = OtherAlgorithms.getIndexFromDay(args[1]);
                if (app.getVertretungsplanRawData(index) != null && !app.getVertretungsplanRawData(index).isEmpty())
                {
                    EnvironmentInterfaces.LokalStorage.saveData(args[1].toUpperCase() + "_HTML_DATA", app.getVertretungsplanRawData(index), app);
                    app.setVertretungsplanRawData(index, null);
                }
            }
        }
        catch (Exception e)
        {
            errorOccured = true;
        }

        taskFinished();
    }

    private void taskFinished()
    // wird beim beenden des Auftrags ausgefüht
    {
        this.owner.taskFinished(this, !errorOccured); // --> Auftraggeber benachrichtigen
    }
}
