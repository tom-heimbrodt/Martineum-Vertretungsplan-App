package de.tomsapps.vertretungsplanapp.algorithms;

import android.app.Application;
import android.content.Context;
import android.support.v4.content.ContextCompat;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.GregorianCalendar;

import de.tomsapps.vertretungsplanapp.core.Preferences;
import de.tomsapps.vertretungsplanapp.core.VertretungsplanApp;

public final class EnvironmentInterfaces
// Stellt Methoden zum Interagieren mit Netzwerk und lokalem Speicher dar.
{
    private EnvironmentInterfaces() { }

    public static Preferences loadPreferences(Application _app)
    {
        String settingsRaw = EnvironmentInterfaces.LokalStorage.loadData("SETTINGS", _app);
        String[] settings = settingsRaw.split("§%§");

        Preferences _prefs = new Preferences();

        // [0] = gruppieren nach . . .
        _prefs.gruppierenNach = OtherAlgorithms.getSpalteFromInt(Integer.parseInt(settings[0]));

        // [1] = Statusleiste ausblenden
        _prefs.statusLeisteAuslenden = OtherAlgorithms.getStatusLeisteAusblendenFromInt(Integer.parseInt(settings[1]));

        // [2] = primaryColor
        _prefs.primaryColor = Integer.parseInt(settings[2]);

        // [3] = secondaryColor
        _prefs.secondaryColor = Integer.parseInt(settings[3]);

        return _prefs;
    }

    public static void savePreferences(Preferences _prefs, Application _app)
    {
        String rawSettings = "";

        rawSettings += String.valueOf(OtherAlgorithms.getIntFromSpalte(_prefs.gruppierenNach));
        rawSettings += "§%§";
        rawSettings += String.valueOf(OtherAlgorithms.getIntFromStatusLeisteAusblenden(_prefs.statusLeisteAuslenden));
        rawSettings += "§%§";
        rawSettings += String.valueOf(_prefs.primaryColor);
        rawSettings += "§%§";
        rawSettings += String.valueOf(_prefs.secondaryColor);

        EnvironmentInterfaces.LokalStorage.saveData("SETTINGS", rawSettings, _app);
    }


    public static class Network
    {
        public static String generateURL(int VertretungsplanID)
        // Funktion zum generieren der Download - URL aus der ID.
        {
        	Calendar cal = Calendar.getInstance();

            int day_of_week = Calendar.MONDAY;
            switch (VertretungsplanID)
            {
                case 0: day_of_week = Calendar.MONDAY; break;
                case 1: day_of_week = Calendar.TUESDAY; break;
                case 2: day_of_week = Calendar.WEDNESDAY; break;
                case 3: day_of_week = Calendar.THURSDAY; break;
                case 4: day_of_week = Calendar.FRIDAY; break;
            }

            while (cal.get(Calendar.DAY_OF_WEEK) != day_of_week)
                cal.add(Calendar.DATE, 1);

            String id = String.valueOf(cal.get(Calendar.YEAR));

            int month = cal.get(Calendar.MONTH) + 1;
            if (month < 10) id += '0';
            id += month;

            int day = cal.get(Calendar.DAY_OF_MONTH);
            if (day < 10) id += '0';
            id += day;

            return "http://www.martineum-halberstadt.de/vplan/vdaten/VplanKl" + id + ".xml";
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
            InputStreamReader reader = new InputStreamReader(stream, "UTF-8");

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
                String _charset = "UTF-8";
                if (name.contentEquals("SETTINGS")) _charset = "iso-8859-1";
                stream.write(data.getBytes(_charset));
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
                String _charset = "UTF-8";
                if (name.contentEquals("SETTINGS")) _charset = "iso-8859-1";
                InputStreamReader reader = new InputStreamReader(stream, _charset);

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