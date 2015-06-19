package de.tomsapps.vertretungsplanapp.algorithms;

import de.tomsapps.vertretungsplanapp.core.Preferences;

public final class OtherAlgorithms
// Andere Algorithmen, die nicht einer adneren Klasse zugeordnet werden können
{
    private OtherAlgorithms() {}

    public static int getIndexFromDay(String day)
    // Funktion, die den Index eines Tages, ausgehend von seinem Namen zurückgibt
    {
        if (day.contentEquals("Montag"))
            return 0;
        else if (day.contentEquals("Dienstag"))
            return 1;
        else if (day.contentEquals("Mittwoch"))
            return 2;
        else if (day.contentEquals("Donnerstag"))
            return 3;
        else if (day.contentEquals("Freitag"))
            return 4;

        else throw new IllegalArgumentException("Unbekannten Wochentag angegeben.");
    }

    public static String getDayOfWeek(int index)
    // gibt Wochentag als String zurück
    {
        switch (index)
        {
            case 0: return "Montag";
            case 1: return "Dienstag";
            case 2: return "Mittwoch";
            case 3: return "Donnerstag";
            case 4: return "Freitag";
            default:  throw new IllegalArgumentException("Unbakannten Wochentag angegeben.");
        }
    }

    public static Preferences.VertretungsplanSpalte getSpalteFromInt(int id)
    {
        switch (id)
        {
            case 0: return Preferences.VertretungsplanSpalte.Klasse;
            case 1: return Preferences.VertretungsplanSpalte.Lehrer;
            case 2: return Preferences.VertretungsplanSpalte.Stunde;
            case 3: return Preferences.VertretungsplanSpalte.Fach;
            case 4: return Preferences.VertretungsplanSpalte.Raum;
            default: throw new IllegalArgumentException("Die Vertretungsplan-Spalte muss eine Zahl zwischen 0 und 4 sein.");
        }
    }

    public static int getIntFromSpalte(Preferences.VertretungsplanSpalte spalte)
    {
        switch (spalte)
        {
            case Klasse: return 0;
            case Lehrer: return 1;
            case Stunde: return 2;
            case Fach: return 3;
            case Raum: return 4;
            default: throw new RuntimeException("Das diese Exception gewurfen wird ist logisch absolut unmöglich!");
        }
    }

    public static Preferences.StatusLeisteAuslenden getStatusLeisteAusblendenFromInt(int id)
    {
        switch (id)
        {
            case 0: return Preferences.StatusLeisteAuslenden.Immer;
            case 1: return Preferences.StatusLeisteAuslenden.NurVertretungsplan;
            case 2: return Preferences.StatusLeisteAuslenden.Nie;
            default: throw new IllegalArgumentException();
        }
    }

    public static int getIntFromStatusLeisteAusblenden(Preferences.StatusLeisteAuslenden sla)
    {
        switch (sla)
        {
            case Immer: return 0;
            case NurVertretungsplan: return 1;
            default: return 2;
        }
    }
}
