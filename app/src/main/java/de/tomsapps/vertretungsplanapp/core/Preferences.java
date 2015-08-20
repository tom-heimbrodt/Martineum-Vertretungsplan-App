package de.tomsapps.vertretungsplanapp.core;

import android.graphics.Color;

public class Preferences
{
    public VertretungsplanSpalte gruppierenNach = VertretungsplanSpalte.Klasse;

    public enum VertretungsplanSpalte
    {
        Klasse, Lehrer, Stunde, Fach, Raum
    }

    public StatusLeisteAuslenden statusLeisteAuslenden = StatusLeisteAuslenden.NurVertretungsplan;

    public enum StatusLeisteAuslenden
    {
        Immer, Nie, NurVertretungsplan
    }

    public int primaryColor, secondaryColor;

    public Preferences()
    {
        gruppierenNach = VertretungsplanSpalte.Klasse;
        statusLeisteAuslenden = StatusLeisteAuslenden.NurVertretungsplan;
        primaryColor = Color.rgb(50, 50, 50);
        secondaryColor = Color.rgb(0, 0, 0);
    }
}
