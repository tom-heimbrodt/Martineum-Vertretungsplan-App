package de.tomsapps.vertretungsplanapp.core;

public class VertretungsplanUnit
// Eine Klasse, die eine Einheit, d.h. eine Zeile, des Vertretungsplanes repräsentiert.
{
    // Speichert Klasse, Stunde, Fach, Lehrer, Raum und zus. Info's als String.
    private String klasse, stunde, fach, lehrer, raum, info;

    public VertretungsplanUnit(String klasse, String stunde, String fach,
                               String lehrer, String raum, String info)
    // Konstruktor, Zuweisung der Variablen.
    {
        this.klasse = klasse;
        this.stunde = stunde;
        this.fach   = fach  ;
        this.lehrer = lehrer;
        this.raum   = raum  ;
        this.info   = info  ;
    }

    public String getKlasse() { return klasse; }
    public String getStunde() { return stunde; }
    public String getFach  () { return fach;   }
    public String getLehrer() { return lehrer; }
    public String getRaum  () { return raum;   }
    public String getInfo  () { return info;   }
}
