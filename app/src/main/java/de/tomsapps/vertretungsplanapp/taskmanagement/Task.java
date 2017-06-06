package de.tomsapps.vertretungsplanapp.taskmanagement;

import android.graphics.Color;

import de.tomsapps.vertretungsplanapp.core.Preferences;
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
        try {
            if (args[0].contentEquals("DOWNLOAD"))
            // Schlüsselwort um Datein von Martineum Server herunterzuladen
            {
                int index = OtherAlgorithms.getIndexFromDay(args[1]);
                app.setVertretungsplanRawData(index, EnvironmentInterfaces.Network.downloadData(EnvironmentInterfaces.Network.generateURL(index)));
            }
            else if (args[0].contentEquals("LOAD_LOCAL"))
            // Schlüsselwort, um Daten aus dem lokalem Speicher zu laden
            {
                int index = OtherAlgorithms.getIndexFromDay(args[1]);
                app.setVertretungsplanRawData(index, EnvironmentInterfaces.LokalStorage.loadData(args[1].toUpperCase() + "_HTML_DATA", app));
            } else if (args[0].contentEquals("ANALYZE"))
            // Schlüsselwort um heruntergeladene Daten zu analysieren
            {
                int index = OtherAlgorithms.getIndexFromDay(args[1]);
                app.setVertretungsplan(index, new Vertretungsplan(app.getVertretungsplanRawData(index)));
            }
            else if (args[0].contentEquals("SAVE_LOCAL"))
            // Schlüsselwort um Daten lokal zu speichern
            {
                int index = OtherAlgorithms.getIndexFromDay(args[1]);
                if (app.getVertretungsplanRawData(index) != null && !app.getVertretungsplanRawData(index).isEmpty()) {
                    EnvironmentInterfaces.LokalStorage.saveData(args[1].toUpperCase() + "_HTML_DATA", app.getVertretungsplanRawData(index), app);
                    //app.setVertretungsplanRawData(index, null);
                }
            }
            else if (args[0].contentEquals("LOAD_SETTINGS"))
            {
                String settingsRaw = EnvironmentInterfaces.LokalStorage.loadData("SETTINGS", app);
                String[] settings = settingsRaw.split("§%§");

                Preferences prefs = new Preferences();

                // [0] = gruppieren nach . . .
                prefs.gruppierenNach = OtherAlgorithms.getSpalteFromInt(Integer.parseInt(settings[0]));

                // [1] = Statusleiste ausblenden
                prefs.statusLeisteAuslenden = OtherAlgorithms.getStatusLeisteAusblendenFromInt(Integer.parseInt(settings[1]));

                // [2] = primaryColor
                prefs.primaryColor = Integer.parseInt(settings[2]);

                // [3] = secondaryColor
                prefs.secondaryColor = Integer.parseInt(settings[3]);

                app.preferences = prefs;
            }
            else if (args[0].contentEquals("SAVE_SETTINGS"))
            {
                String rawSettings = "";

                Preferences prefs = app.preferences;

                rawSettings += String.valueOf(OtherAlgorithms.getIntFromSpalte(prefs.gruppierenNach));
                rawSettings += "§%§";
                rawSettings += String.valueOf(OtherAlgorithms.getIntFromStatusLeisteAusblenden(prefs.statusLeisteAuslenden));
                rawSettings += "§%§";
                rawSettings += String.valueOf(prefs.primaryColor);
                rawSettings += "§%§";
                rawSettings += String.valueOf(prefs.secondaryColor);

                EnvironmentInterfaces.LokalStorage.saveData("SETTINGS", rawSettings, app);
            }
        } catch (Exception e) {
            errorOccured = true;
        }

        taskFinished();
    }

    private void taskFinished()
    // wird beim beenden des Auftrags ausgefüht
    {
        if (this.owner != null)
            this.owner.taskFinished(this, !errorOccured); // --> Auftraggeber benachrichtigen
    }
}
