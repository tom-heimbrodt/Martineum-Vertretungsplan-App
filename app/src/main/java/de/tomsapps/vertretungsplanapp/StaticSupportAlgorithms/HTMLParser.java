package de.tomsapps.vertretungsplanapp.StaticSupportAlgorithms;

import java.util.ArrayList;

import de.tomsapps.vertretungsplanapp.Core.VertretungsplanUnit;

public final class HTMLParser
// Stellt statische Methoden zum entschlüsseln der Vertretungsplan-Daten
// aus dem entsprechenden HTML-Code der Webseite bereit.
{
    private HTMLParser() {}

    private static final String HTML_DATE_START = "<span class=\"vpfuerdatum\">";
    private static final String HTML_DATE_END   = "</span>";

    public static String parseVertretungsplanDatum(String HTMLData, int startSearchAt)
    {
        int datumStartIndex = HTMLData.indexOf(HTML_DATE_START, startSearchAt) + HTML_DATE_START.length();
        int datumEndIndex   = HTMLData.indexOf(HTML_DATE_END, datumStartIndex);
        return HTMLData.substring(datumStartIndex, datumEndIndex);
    }


    private static final String HTML_TABLE_START = "<table class=\"tablekopf\" border=\"2\">";
    private static final String HTML_UNIT_START  = "<tr>";
    private static final String HTML_UNIT_END    = "</tr>";

    public static ArrayList<VertretungsplanUnit> generateVertretungsplanUnits(String HTMLData, int startSearchAt)
    // Vertretungsplan Units aus HTML-Code auslesen
    {
        ArrayList<VertretungsplanUnit> units = new ArrayList<VertretungsplanUnit>();

        // Anfangspunkt suchen
        int startIndex = HTMLData.indexOf(HTML_TABLE_START, startSearchAt);

        int nextIndex = -1;
        while ((nextIndex = HTMLData.indexOf(HTML_UNIT_START, (nextIndex == -1) ? startIndex : nextIndex)) != -1)
        // wird ausgeführt, solange der nächste Index einer Unit gefunden wird (nicht -1 ist)
        {
            nextIndex += HTML_UNIT_START.length();
            int endIndex = HTMLData.indexOf(HTML_UNIT_END, nextIndex);
            // VertretungsplanUnit generieren
            VertretungsplanUnit unit = generateVertretungsplanUnit(HTMLData.substring(nextIndex, endIndex));
            if (unit != null) units.add(unit);
        }

        return units;
    }

    private static final String HTML_INFO_START = "<td class=\"tdaktionen\">";
    private static final String HTML_INFO_START_NEU = "<td class=\"tdaktionenneu\">";
    private static final String HTML_INFO_END = "</td>";

    public static VertretungsplanUnit generateVertretungsplanUnit(String HTMLUnitData)
    // Inhalt einer Vertretungsplan Unit aus dem HTML-Code ausesen
    {
        // Indexes der Start- (index % 2 = 0) und Endpunkte (index % 2 = 1), der auszulesenden Daten
        int[] indexes = new int[12];
        // Punkte suchen
        for (int i = 0; i < 6; i++)
        {
            // Startpunkt einlesen
            int s1 = HTMLUnitData.indexOf(HTML_INFO_START, (i > 0) ? indexes[i * 2 - 1] : 0) + HTML_INFO_START.length();
            int s2 = HTMLUnitData.indexOf(HTML_INFO_START_NEU, (i > 0) ? indexes[i * 2 - 1] : 0) + HTML_INFO_START_NEU.length();
            int s1NotFound = -1 + HTML_INFO_START.length();
            int s2NotFound = -1 + HTML_INFO_START_NEU.length();
            if (s1 == s1NotFound && s2 > s2NotFound)
                indexes[i * 2] = s2;
            else if (s1 > s1NotFound && s2 == s2NotFound)
                indexes[i * 2] = s1;
            else if (s1 > s1NotFound && s2 > s2NotFound)
                indexes[i * 2] = (s1 < s2) ? s1 : s2;
            else if (s1 == s1NotFound && s2 == s2NotFound)
                return null;

            // Endpunkt einlesen
            indexes[i * 2 + 1] = HTMLUnitData.indexOf(HTML_INFO_END, (i > 0) ? indexes[i * 2] : 0);
            if (indexes[i * 2 + 1] == -1) return null;
        }

        // VertretungsplanUnit erstellen
        String klasse = HTMLUnitData.substring(indexes[ 0], indexes[ 1]);
        String stunde = HTMLUnitData.substring(indexes[ 2], indexes[ 3]);
        String fach   = HTMLUnitData.substring(indexes[ 4], indexes[ 5]);
        String lehrer = HTMLUnitData.substring(indexes[ 6], indexes[ 7]);
        String raum   = HTMLUnitData.substring(indexes[ 8], indexes[ 9]);
        String info   = HTMLUnitData.substring(indexes[10], indexes[11]);
        return new VertretungsplanUnit(klasse, stunde, fach, lehrer, raum, info);
    }
}