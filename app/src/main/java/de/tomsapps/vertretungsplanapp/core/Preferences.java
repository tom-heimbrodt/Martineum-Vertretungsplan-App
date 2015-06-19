package de.tomsapps.vertretungsplanapp.core;

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

    public Preferences() {}
}
