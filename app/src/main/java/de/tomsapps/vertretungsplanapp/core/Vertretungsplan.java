package de.tomsapps.vertretungsplanapp.core;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import de.tomsapps.vertretungsplanapp.algorithms.XmlVertretungsplanParser;

public class Vertretungsplan
// Kapselt Felder und Methoden die zum verarbeiten und speichern von Daten
// in Bezug auf den Vertretungsplan genutzt werden können.
{
    // Speicherung einer unbestimmten Anzahl an Units (Zeilen).
    private final List<VertretungsplanUnit> units;
    private final String datum;

    public Vertretungsplan(List<VertretungsplanUnit> _units, String _datum)
    {
    	units = _units;
        datum = _datum;
    }

    public VertretungsplanUnit getUnit     (int index) { return units.get(index); }
    public int                 getUnitsSize()          { return units.size();     }
    public String              getDate     ()          { return datum;            }
}