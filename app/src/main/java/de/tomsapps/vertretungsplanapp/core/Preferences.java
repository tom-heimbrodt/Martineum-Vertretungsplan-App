package de.tomsapps.vertretungsplanapp.core;

import android.graphics.Color;

public class Preferences
{
    public VertretungsplanSpalte gruppierenNach = VertretungsplanSpalte.Klasse;

    public enum VertretungsplanSpalte
    {
        Klasse, Lehrer, Stunde, Fach, Raum
    }

    public StatusLeisteAuslenden statusLeisteAuslenden = StatusLeisteAuslenden.Nie;

    public enum StatusLeisteAuslenden
    {
        Immer, Nie, NurVertretungsplan
    }

    public int primaryColor, secondaryColor;

    public Preferences()
    {
        gruppierenNach = VertretungsplanSpalte.Klasse;
        statusLeisteAuslenden = StatusLeisteAuslenden.Nie;
        primaryColor = Color.rgb(1, 0x47, 0x9B);
        secondaryColor = Color.rgb(0, 0x28, 0x70);
    }
}
