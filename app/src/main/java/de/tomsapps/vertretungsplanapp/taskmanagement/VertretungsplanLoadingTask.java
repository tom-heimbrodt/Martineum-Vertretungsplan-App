package de.tomsapps.vertretungsplanapp.taskmanagement;

import android.os.AsyncTask;

import de.tomsapps.vertretungsplanapp.algorithms.EnvironmentInterfaces;
import de.tomsapps.vertretungsplanapp.algorithms.OtherAlgorithms;
import de.tomsapps.vertretungsplanapp.algorithms.XmlVertretungsplanParser;
import de.tomsapps.vertretungsplanapp.core.Vertretungsplan;
import de.tomsapps.vertretungsplanapp.core.VertretungsplanApp;
import de.tomsapps.vertretungsplanapp.gui.MainActivity;

public class VertretungsplanLoadingTask extends AsyncTask<Integer, Double, Vertretungsplan>
{
    private VertretungsplanApp app;
    private MainActivity mainActivity;
    private int vplanID;

    public enum ErrorCode
    {
        SUCCESS,
        NO_DATA,
        INVALID_DATA
    }
    public ErrorCode error;

    public VertretungsplanLoadingTask(VertretungsplanApp _application, MainActivity _activity)
    {
        app = _application;
        mainActivity = _activity;
    }

    @Override
    protected void onProgressUpdate(Double... progress)
    {
    }

    @Override
    protected void onPostExecute(Vertretungsplan vplan)
    {
        if (error == ErrorCode.SUCCESS)
        {
            app.setVertretungsplan(vplanID, vplan);
            app.setVertretungsplanState(vplanID, VertretungsplanApp.VertretungsplanState.Loaded);
        }
        else
            app.setVertretungsplanState(vplanID, VertretungsplanApp.VertretungsplanState.Error);

        mainActivity.taskFinished(vplanID, vplan, error);
    }

    @Override
    protected Vertretungsplan doInBackground(Integer... vplanID)
    {
        this.vplanID = vplanID[0];
        // try download data
        String raw_data;
        try
        {
            raw_data = EnvironmentInterfaces.Network.downloadData(EnvironmentInterfaces.Network.generateURL(vplanID[0]));
            if (raw_data == null || raw_data.isEmpty())
            {
                error = ErrorCode.NO_DATA;
                return null;
            }

            try
            {
                // we were able to successfully load vp-data -> cache in filesystem for offline usage
                EnvironmentInterfaces.LokalStorage.saveData(OtherAlgorithms.getDayOfWeek(vplanID[0]).toUpperCase() + "_XML_DATA", raw_data, app);
            }
            catch (Exception e2)
            {
                e2.printStackTrace();
            }
        }
        catch (Exception e)
        {
            try
            {
                raw_data = EnvironmentInterfaces.LokalStorage.loadData(OtherAlgorithms.getDayOfWeek(vplanID[0]).toUpperCase() + "_XML_DATA", app);
                if (raw_data == null || raw_data.isEmpty())
                {
                    error = ErrorCode.NO_DATA;
                    return null;
                }
            }
            catch (Exception e2)
            {
                // no data available
                error = ErrorCode.NO_DATA;
                return null;
            }
        }

        // parse data
        XmlVertretungsplanParser _parser = new XmlVertretungsplanParser(raw_data);
        try
        {
            Vertretungsplan vplan = _parser.parse();
            if (vplan == null)
                error = ErrorCode.INVALID_DATA;
            else
                error = ErrorCode.SUCCESS;
            return vplan;
        }
        catch (Exception e)
        {
            error = ErrorCode.INVALID_DATA;
            return null;
        }
    }
}
