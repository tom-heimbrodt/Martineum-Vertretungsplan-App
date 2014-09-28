package de.tomsapps.vertretungsplanapp.TaskManagement;

import de.tomsapps.vertretungsplanapp.Core.Vertretungsplan;
import de.tomsapps.vertretungsplanapp.Core.VertretungsplanApp;
import de.tomsapps.vertretungsplanapp.StaticSupportAlgorithms.EnvironmentInterfaces;
import de.tomsapps.vertretungsplanapp.StaticSupportAlgorithms.OtherAlgorithms;

public class Task
{
    private String[] args;
    private ITaskOwner owner;
    private boolean errorOccured;

    public Task(ITaskOwner sender, String... args)
    {
        this.args         = args;
        this.owner        = sender;
        this.errorOccured = false;
    }

    public String[] getArgs() { return args; }

    public void runTaskSync(VertretungsplanApp app)
    {
        try
        {
            if (args[0].contentEquals("DOWNLOAD"))
            {
                int index = OtherAlgorithms.getIndexFromDay(args[1]);
                try
                {
                    app.setVertretungsplanRawData(index, EnvironmentInterfaces.Network.downloadData(EnvironmentInterfaces.Network.generateURL(index)));
                }
                catch (Exception e)
                {
                    app.setVertretungsplanRawData(index, EnvironmentInterfaces.LokalStorage.loadData(args[1].toUpperCase() + "_HTML_DATA"));
                }
            }

            else if (args[0].contentEquals("ANALYSE"))
            {
                int index = OtherAlgorithms.getIndexFromDay(args[1]);
                app.setVertretungsplan(index, new Vertretungsplan(app.getVertretungsplanRawData(index)));
            }

            else if (args[0].contentEquals("SAVE_LOCAL"))
            {
                int index = OtherAlgorithms.getIndexFromDay(args[1]);
                EnvironmentInterfaces.LokalStorage.saveData(args[1].toUpperCase() + "_HTML_DATA", app.getVertretungsplanRawData(index));
                app.setVertretungsplanRawData(index, null);
            }
        }
        catch (Exception e)
        {
            errorOccured = true;
        }

        taskFinished();
    }

    private void taskFinished()
    {
        this.owner.taskFinished(this, !errorOccured);
    }
}
