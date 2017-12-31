package de.tomsapps.vertretungsplanapp.algorithms;

import android.support.annotation.NonNull;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import de.tomsapps.vertretungsplanapp.core.Vertretungsplan;
import de.tomsapps.vertretungsplanapp.core.VertretungsplanUnit;

public class XmlVertretungsplanParser
{
	private final String input;
	private Vertretungsplan vertretungsplan;

	public XmlVertretungsplanParser(String _input)
	{
		input = _input;
	}

	public Vertretungsplan parse() throws Exception
	{
		if (vertretungsplan != null)
			return vertretungsplan;

		XmlPullParser parser = Xml.newPullParser();

		List<VertretungsplanUnit> _units = new ArrayList<>();
		String _datum = "";

		parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
		parser.setInput(new StringReader(input));

		int i;
		while ((i = parser.next()) != XmlPullParser.END_DOCUMENT) {
			if (parser.getEventType() == XmlPullParser.START_TAG) {
				String name = parser.getName();

				switch (name) {
					case "titel":
						_datum = parser.nextText();
						break;
					case "aktion":
						_units.add(parseAktion(parser));
						break;
				}
			}
		}

		vertretungsplan = new Vertretungsplan(_units, _datum);
		return vertretungsplan;
	}

	public Vertretungsplan getVertretungsplan()
	{
		return vertretungsplan;
	}

	private VertretungsplanUnit parseAktion(XmlPullParser _parser) throws Exception
	{
		int _depth = 1;

		String _stunde = "",
				_klasse = "",
				_lehrer = "",
				_fach = "",
				_raum = "",
				_info = "";
		String _tagName = "";

		do
		{
			_parser.next();

			int _eventType = _parser.getEventType();
			if (_eventType == XmlPullParser.START_TAG)
			{
				_depth++;
				_tagName = _parser.getName();
			}

			else if (_eventType == XmlPullParser.TEXT)
			{
				switch (_tagName.toLowerCase())
				{
					case "klasse":
						_klasse = _parser.getText();
						break;
					case "raum":
						_raum = _parser.getText();
						break;
					case "fach":
						_fach = _parser.getText();
						break;
					case "lehrer":
						_lehrer = _parser.getText();
						break;
					case "stunde":
						_stunde = _parser.getText();
						break;
					case "info":
						_info = _parser.getText();
						break;
				}
			}
			else if (_eventType == XmlPullParser.END_TAG || _eventType == XmlPullParser.END_DOCUMENT)
			{
				_depth--;
				_tagName = "";
			}
		}
		while (_depth > 0);

		return new VertretungsplanUnit(_klasse, _stunde, _fach, _lehrer, _raum, _info);
	}
}
