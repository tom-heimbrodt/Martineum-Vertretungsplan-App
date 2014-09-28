package de.tomsapps.vertretungsplanapp.StaticSupportAlgorithms;

import android.content.Context;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import de.tomsapps.vertretungsplanapp.Core.VertretungsplanApp;

public final class EnvironmentInterfaces
// Stellt Methoden zum Interagieren mit Netzwerk und lokalem Speicher dar.
{
    private EnvironmentInterfaces() {}

    public static class Network
    {
        public static String generateURL(int VertretungsplanID)
        // Methode zum generieren der Download - URL aus der ID.
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
                default: return "";
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
            while ((readLength = reader.read(buffer)) > 0)
            {
                stringBuilder.append(buffer, 0, readLength);
            }

            // Stream schließen
            reader.close();
            stream.close();

            // String zurückgeben
            return stringBuilder.toString();
        }
    }

    public static class LokalStorage
    {
        public static void saveData(String name, String data)
        // Speichert Daten unter einem bestimmten eindeutigen Namen ab.
        {
            try
            {
                // Stream aus vorhandener oder neu erstellter Datei erzeugen
                FileOutputStream stream = VertretungsplanApp.singleton.openFileOutput(name, Context.MODE_PRIVATE);
                // in Stream schreiben
                stream.write(data.getBytes("iso-8859-1"));
                // Stream schließen
                stream.close();
            }
            // benötigte catch klauseln
            catch (FileNotFoundException e) { e.printStackTrace(); }
            catch (IOException e)           { e.printStackTrace(); }
        }

        public static String loadData(String name)
        // Liest Daten aus dem loakeln Speicher, die durch den eindeutigen Namen gekennzeichnet werden.
        {
            try
            {
                // Stream aus vorhandener oder neu erstellter Datei erzeugen
                FileInputStream stream = VertretungsplanApp.singleton.openFileInput(name);
                // StringBuilder erstellen (s. Network.downloadData(String))
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
            // benötigte catch klauseln
            catch (FileNotFoundException e) { e.printStackTrace(); }
            catch (IOException e)           { e.printStackTrace(); }

            return null;
        }
    }
}