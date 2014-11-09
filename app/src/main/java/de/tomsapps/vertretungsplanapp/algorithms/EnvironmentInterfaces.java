package de.tomsapps.vertretungsplanapp.algorithms;

import android.app.Application;
import android.content.Context;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import de.tomsapps.vertretungsplanapp.core.VertretungsplanApp;

public final class EnvironmentInterfaces
// Stellt Methoden zum Interagieren mit Netzwerk und lokalem Speicher dar.
{
    // privater Konstuktor schützt vor versehentlichem Instanzieren der Klasse
    private EnvironmentInterfaces() {}

    public static class Network
    {
        public static String generateURL(int VertretungsplanID)
        // Funktion zum generieren der Download - URL aus der ID.
        {
            switch (VertretungsplanID)
            // 0 -> Montag
            // 1 -> Dienstag
            // 2 -> Mittwoch
            // 3 -> Donnerstag
            // 4 -> Freitag
            {
                case 0:  return "http://www.martineum-halberstadt.de/wb/media/plaene/vpmo.html";
                case 1:  return "http://www.martineum-halberstadt.de/wb/media/plaene/vpdi.html";
                case 2:  return "http://www.martineum-halberstadt.de/wb/media/plaene/vpmi.html";
                case 3:  return "http://www.martineum-halberstadt.de/wb/media/plaene/vpdo.html";
                case 4:  return "http://www.martineum-halberstadt.de/wb/media/plaene/vpfr.html";
                default: throw new IllegalArgumentException("ID außerhalb des Definitionsbereiches.");
            }
        }

        public static String downloadData(String fileURL) throws Exception
        // lädt eine Datei von der angegebenen Adresse herrunter.
        {
            // StringBuilder (s. http://schabby.de/stringbuilder-stringbuffer/)
            StringBuilder stringBuilder = new StringBuilder();
            // URL Objekt erstellen
            URL url = new URL(fileURL);
            // Verbindung aufbauen
            URLConnection connection = url.openConnection();
            connection.connect();
            // Stream initialisieren und auslesen vorbereiten
            InputStream stream = connection.getInputStream();
            InputStreamReader reader = new InputStreamReader(stream, "iso-8859-1");

            // Buffer initialisieren
            char[] buffer = new char[512];
            int readLength;

            // Daten auslesen und in den StringBuilder kopieren
            // --> readLength Anzahl an Zeichen werden in den Buffer kopiert (max. 512),
            //     bis keine Zeichen mehr übrig sind
            while ((readLength = reader.read(buffer)) > 0)
            {
                stringBuilder.append(buffer, 0, readLength);
            }

            // Stream schließen
            reader.close();
            stream.close();

            // String erstellen und zurückgeben
            return stringBuilder.toString();
        }
    }

    public static class LokalStorage
    {
        public static void saveData(String name, String data, Application application)
        // Speichert Daten unter einem bestimmten eindeutigen Namen ab.
        {
            try
            {
                // Stream aus vorhandener oder neu erstellter Datei erzeugen
                FileOutputStream stream = application.openFileOutput(name, Context.MODE_PRIVATE);
                // in Stream schreiben
                // --> Daten werden wieder unter iso-8859-1 abgespeichert, die Kodierung ist
                //     eigentlich ziemlich egal, hauptsache die Datei wird mit derselben wieder
                //     gelesen
                stream.write(data.getBytes("iso-8859-1"));
                // Stream schließen
                stream.close();
            }
            // benötigte catch - Klauseln
            // --> Fehler werden in den Fehlerstream der Anwendung geschrieben
            catch (FileNotFoundException e) { e.printStackTrace(); }
            catch (IOException e)           { e.printStackTrace(); }
        }

        public static String loadData(String name, Application application)
        // Liest Daten aus dem loakeln Speicher, die durch einen eindeutigen Namen gekennzeichnet werden.
        {
            try
            {
                // Stream aus vorhandener oder neu erstellter Datei erzeugen
                FileInputStream stream = application.openFileInput(name);
                // StringBuilder erstellen um Buffer zusammenzufügen
                StringBuilder stringBuilder = new StringBuilder();
                InputStreamReader reader = new InputStreamReader(stream, "iso-8859-1");

                // Buffer initialisieren
                char[] buffer = new char[512];
                int readLength;

                // Daten auslesen und in den StringBuilder kopieren
                while ((readLength = reader.read(buffer)) > 0) {
                    stringBuilder.append(buffer, 0, readLength);
                }
                // Stream schließen
                reader.close();
                stream.close();

                // String erstellen und zurückgeben
                return stringBuilder.toString();
            }
            // benötigte catch - Klauseln
            catch (FileNotFoundException e) { e.printStackTrace(); }
            catch (IOException e)           { e.printStackTrace(); }

            throw new RuntimeException("Beim laden von Dateien aus dem lokalen Speicher " +
                    "ist ein Fehler aufgetreten.");
        }
    }
}