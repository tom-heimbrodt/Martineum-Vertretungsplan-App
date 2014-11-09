package de.tomsapps.vertretungsplanapp.algorithms;

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
}
