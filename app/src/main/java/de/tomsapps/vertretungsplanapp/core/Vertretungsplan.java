package de.tomsapps.vertretungsplanapp.core;

import java.util.ArrayList;

import de.tomsapps.vertretungsplanapp.algorithms.HTMLParser;

public class Vertretungsplan
// Kapselt Felder und Methoden die zum verarbeiten und speichern von Daten
// in Bezug auf den Vertretungsplan genutzt werden können.
{
    // Speicherung einer unbestimmten Anzahl an Units (Zeilen).
    ArrayList<VertretungsplanUnit> units;
    String datum;

    public Vertretungsplan(String HTMLData)
    {
        // initialisiere Daten mithilfe des HTML - Parsers aus dem Algorithmen - Paket
        datum = HTMLParser.parseVertretungsplanDatum(HTMLData, 0);
        units = HTMLParser.generateVertretungsplanUnits(HTMLData, 0);
    }

    public VertretungsplanUnit getUnit     (int index) { return units.get(index); }
    public int                 getUnitsSize()          { return units.size();     }
    public String              getDate     ()          { return datum;            }
}