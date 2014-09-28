package de.tomsapps.vertretungsplanapp.StaticSupportAlgorithms;

public final class OtherAlgorithms
{
    private OtherAlgorithms() {}

    public static int getIndexFromDay(String day)
    {
        if (day.contentEquals("Montag"))
        { return 0; }
        else if (day.contentEquals("Dienstag"))
        { return 1; }
        else if (day.contentEquals("Mittwoch"))
        { return 2; }
        else if (day.contentEquals("Donnerstag"))
        { return 3; }
        else if (day.contentEquals("Freitag"))
        { return 4; }
        else return -1;
    }


}
