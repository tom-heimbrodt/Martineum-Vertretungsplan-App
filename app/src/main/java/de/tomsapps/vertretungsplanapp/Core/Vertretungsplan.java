package de.tomsapps.vertretungsplanapp.Core;

import java.util.ArrayList;

import de.tomsapps.vertretungsplanapp.StaticSupportAlgorithms.HTMLParser;

public class Vertretungsplan
// Kapselt Felder, Methoden & Funktionen die zum verarbeiten und speichern von Daten
// in Bezug auf den Vertretungsplan genutzt werden können.
{
    // Speicherung einer unbestimmten Anzahl an Units (Zeilen).
    ArrayList<VertretungsplanUnit> units;
    String datum;

    public Vertretungsplan(String HTMLData)
    // Konstruktor
    {
        datum = HTMLParser.parseVertretungsplanDatum(HTMLData, 0);
        units = HTMLParser.generateVertretungsplanUnits(HTMLData, 0);
    }

    public VertretungsplanUnit getUnit     (int index) { return units.get(index); }
    public int                 getUnitsSize()          { return units.size();     }
    public String              getDate     ()          { return datum;            }
}