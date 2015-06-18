package de.tomsapps.vertretungsplanapp.core;

public class Preferences
{
    public VertretungsplanSpalte gruppierenNach = VertretungsplanSpalte.Klasse;

    public enum VertretungsplanSpalte
    {
        Klasse, Lehrer, Stunde, Fach, Raum
    }

    public Preferences() {}
}
