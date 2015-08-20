package de.tomsapps.vertretungsplanapp.gui;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import de.tomsapps.vertretungsplanapp.core.Preferences;
import de.tomsapps.vertretungsplanapp.core.Resources;
import de.tomsapps.vertretungsplanapp.core.Vertretungsplan;
import de.tomsapps.vertretungsplanapp.core.VertretungsplanApp;
import de.tomsapps.vertretungsplanapp.R;
import de.tomsapps.vertretungsplanapp.core.VertretungsplanUnit;

public class VertretungsplanListAdapter extends BaseExpandableListAdapter
// Klasse, welcher für die initialisierung des ExpandableListView's verantwortlich ist.
{
    // Speichert Tag.
    int vertretungsplanID;
    // Speicherung der Gruppen.
    ArrayList<ListGroup> groups = new ArrayList<ListGroup>();
    int groupSize;
    Context context;

    public VertretungsplanListAdapter(int vertretungsplanID, Context context, VertretungsplanApp app)
    // Konstruktor.
    {
        // Wertzuweisungen.
        this.vertretungsplanID = vertretungsplanID;
        this.context = context;

        // Gruppen initialisieren
        initialize(app);
    }

    public void initialize(final VertretungsplanApp application)
    // Gruppen (re-)initialisieren.
    {
        // Referenz auf den Vertretungsplan erstellen.
        Vertretungsplan vp = application.getVertretungsplan(vertretungsplanID);
        // In jeder Zeile nach Gruppen suchen, welche nicht schon vorhanden sind.
        for (int i = 0; i < vp.getUnitsSize(); i++) {
            VertretungsplanUnit unit = vp.getUnit(i);

            String zelle = "";

            switch (application.preferences.gruppierenNach) {
                case Klasse:
                    zelle = unit.getKlasse();
                    break;
                case Lehrer:
                    zelle = unit.getLehrer().replace("(", "");
                    zelle = zelle.replace(")", "");
                    break;
                case Fach:
                    zelle = unit.getFach();
                    break;
                case Raum:
                    zelle = unit.getRaum();
                    break;
                case Stunde:
                    zelle = unit.getStunde();
                    break;
            }

            if (Build.VERSION.SDK_INT >= 9)
                if (zelle.isEmpty()) zelle = "keine Angabe";
            else
                if (zelle.contentEquals("")) zelle = "keine Angabe";

            ArrayList<String> unitGroupNames = new ArrayList<String>();
            // generate UnitGroupNames (durch ',' getrennte Klassen und weglassen der Kurse nach '/'
            int index = -1, pIndex = 0;
            int endIndex = (application.preferences.gruppierenNach == Preferences.VertretungsplanSpalte.Klasse) ? zelle.indexOf("/") : zelle.length();
            if (endIndex == -1) endIndex = zelle.length();
            do {
                pIndex = index + 1;
                index = zelle.indexOf(",", pIndex + 1);

                unitGroupNames.add(zelle.substring(pIndex, (index != -1) ? index : endIndex));
            }
            while (index != -1);

            for (String unitName : unitGroupNames) {
                boolean groupAlreadyExists = false;
                for (ListGroup group : groups) {
                    // Wenn die Gruppe der Unit schon vorhanden ist, die Unit hinzufügen, . . .
                    if (unitName.contentEquals(group.name)) {
                        group.addUnit(unit);
                        groupAlreadyExists = true;
                        break;
                    }
                }
                if (!groupAlreadyExists) {
                    // . . . wenn nicht neue Gruppe erstellen und Unit hinzufügen.
                    ListGroup group = new ListGroup(unitName);
                    group.addUnit(unit);
                    groups.add(group);
                }
            }
        }

        // Anzahl der Gruppen wird gespeichert, damit nicht immer die Funktion aufgerufen werden muss (-> Geschwindigkeit)
        groupSize = groups.size();

        // Gruppen sortieren
        Collections.sort(groups, new Comparator<ListGroup>() {
            @Override
            public int compare(ListGroup lhs, ListGroup rhs) {
                boolean differenceFound = false;

                // Klasse ist etwas aufwändiger, weil zuerst 5. und dann 10. Klasse kommen muss!!!!
                if (application.preferences.gruppierenNach == Preferences.VertretungsplanSpalte.Klasse) {
                    int number1 = -1, number1IndexEndValue = -1;
                    for (int i = 0; i < lhs.name.length(); i++) {
                        if (lhs.name.charAt(i) >= '0' && lhs.name.charAt(i) <= '9')
                            number1IndexEndValue = i;
                    }
                    if (number1IndexEndValue != -1)
                        number1 = Integer.parseInt(lhs.name.substring(0, number1IndexEndValue + 1));

                    int number2 = -1, number2IndexEndValue = -1;
                    for (int i = 0; i < rhs.name.length(); i++) {
                        if (rhs.name.charAt(i) >= '0' && rhs.name.charAt(i) <= '9')
                            number2IndexEndValue = i;
                    }
                    if (number2IndexEndValue != -1)
                        number2 = Integer.parseInt(rhs.name.substring(0, number2IndexEndValue + 1));

                    if (number1 < number2) return -1;
                    else if (number2 < number1) return 1;
                }


                // Ansonsten alphabetisch ...
                for (int index = 0; !differenceFound && index < lhs.name.length() && index < rhs.name.length(); index++) {
                    if (lhs.name.charAt(index) == rhs.name.charAt(index))
                        continue;
                    else if (lhs.name.charAt(index) < rhs.name.charAt(index))
                        return -1;
                    else
                        return 1;
                }


                // ... oder kürzestes zuerst
                if (lhs.name.length() < rhs.name.length())
                    return -1;
                else if (lhs.name.length() > rhs.name.length())
                    return 1;
                else
                    return 0;
            }
        });
    }

    // region überschriebene Methoden der Basis-Klasse

    @Override
    public int getGroupCount()
    // muss die Anzahl der anzuzeigenden Gruppen zurückgeben
    {
        return groupSize;
    }

    @Override
    public int getChildrenCount(int groupPosition)
    // muss die Anzahl der anzuzeigenden Items in der Gruppe an der Position groupPosition zurückgeben
    {
        return groups.get(groupPosition).unitsSize;
    }

    @Override
    public Object getGroup(int groupPosition) // -> wird von Anwendung nicht benötigt
    {   return null;   }

    @Override
    public Object getChild(int groupPosition, int childPosition) // -> wird von Anwendung nicht benötigt
    {   return null;   }

    @Override
    public long getGroupId(int groupPosition)
    // muss unter Gruppen eineindeutige ID zugeordnet werden
    {
        // ID = Position der Gruppe
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition)
    // muss eine unter allen Items der Gruppe eineindeutige ID zurückgeben
    {
        // ID = Position des Items
        return childPosition;
    }

    @Override
    public boolean hasStableIds()
    {
        return true;
    }

    class GroupLayout
    {
        TextView classView;
        TextView aenderungenView;
        public GroupLayout(TextView classView, TextView aenderungenView)
        {
            this.classView = classView;
            this.aenderungenView = aenderungenView;
        }
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
    {
        // Gibt neu erstelltes oder schon vorhandenes anzuzeigendes Layout zurück
        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.exp_list_view_group, null, false);
        }
        GroupLayout groupLayout = (GroupLayout)convertView.getTag();
        if (groupLayout == null)
        {
            TextView textView1 = (TextView) convertView.findViewById(R.id.exp_list_view_group_text1);
            TextView textView2 = (TextView) convertView.findViewById(R.id.exp_list_view_group_text2);
            groupLayout = new GroupLayout(textView1, textView2);
            convertView.setTag(groupLayout);
        }

        groupLayout.classView.setText(groups.get(groupPosition).name);
        groupLayout.classView.setTypeface(Resources.roboto_light);

        int anzahlAenderungen = groups.get(groupPosition).unitsSize;
        groupLayout.aenderungenView.setText(anzahlAenderungen + ((anzahlAenderungen == 1) ? " Änderung" : " Änderungen"));
        groupLayout.aenderungenView.setTypeface(Resources.roboto_light_italic);
        return convertView;
    }

    class ItemLayout
    {
        TextView[] textViews;
        public ItemLayout(TextView[] textViews)
        {
           this.textViews = textViews;
        }
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {
        // Gibt neu erstelltes oder schon vorhandenes anzuzeigendes Layout zurück
        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.exp_list_view_item, null, false);
        }
        ItemLayout itemLayout = (ItemLayout)convertView.getTag();
        if (itemLayout == null)
        {
            TextView[] textViews = new TextView[6];
            textViews[0] = (TextView) convertView.findViewById(R.id.exp_list_view_item_text1);
            textViews[0].setTypeface(Resources.roboto_light);
            textViews[1] = (TextView) convertView.findViewById(R.id.exp_list_view_item_text2);
            textViews[1].setTypeface(Resources.roboto_light);
            textViews[2] = (TextView) convertView.findViewById(R.id.exp_list_view_item_text3);
            textViews[2].setTypeface(Resources.roboto_light);
            textViews[3] = (TextView) convertView.findViewById(R.id.exp_list_view_item_text4);
            textViews[3].setTypeface(Resources.roboto_light);
            textViews[4] = (TextView) convertView.findViewById(R.id.exp_list_view_item_text5);
            textViews[4].setTypeface(Resources.roboto_light);
            textViews[5] = (TextView) convertView.findViewById(R.id.exp_list_view_item_text6);
            textViews[5].setTypeface(Resources.roboto_light_italic);
            itemLayout = new ItemLayout(textViews);
            convertView.setTag(itemLayout);
        }
        ListGroup group = groups.get(groupPosition);

        itemLayout.textViews[0].setText(group.units.get(childPosition).getKlasse());
        itemLayout.textViews[1].setText(group.units.get(childPosition).getStunde());
        itemLayout.textViews[2].setText(group.units.get(childPosition).getFach());
        itemLayout.textViews[3].setText(group.units.get(childPosition).getLehrer());
        itemLayout.textViews[4].setText(group.units.get(childPosition).getRaum());
        itemLayout.textViews[5].setText(group.units.get(childPosition).getInfo());

        if (childPosition == 0)
            convertView.findViewById(R.id.exp_list_view_item_shadow_top).setVisibility(View.VISIBLE);
            else
            convertView.findViewById(R.id.exp_list_view_item_shadow_top).setVisibility(View.GONE);

        if (isLastChild)
            convertView.findViewById(R.id.exp_list_view_item_shadow_bottom).setVisibility(View.VISIBLE);
        else
            convertView.findViewById(R.id.exp_list_view_item_shadow_bottom).setVisibility(View.GONE);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        return false;
    }

    // endregion

    // region ListGroup - Klasse

    class ListGroup
            // Stellt die Daten einer Gruppe des ExpandableListView's dar.
    {
        // Der Name der Gruppe.
        String name;
        // Ein Array, das die Unit - ID des Inhaltes enthält.
        ArrayList<VertretungsplanUnit> units = new ArrayList<VertretungsplanUnit>();
        int unitsSize = 0;

        public ListGroup(String name)
        {
            this.name = name;
        }

        public void addUnit(VertretungsplanUnit unit)
        {
            units.add(unit);
            unitsSize++;
        }
    }

    // endregion
}
