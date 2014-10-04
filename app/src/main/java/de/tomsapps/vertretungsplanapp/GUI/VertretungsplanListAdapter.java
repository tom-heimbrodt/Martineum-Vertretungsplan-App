package de.tomsapps.vertretungsplanapp.GUI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import de.tomsapps.vertretungsplanapp.Core.Vertretungsplan;
import de.tomsapps.vertretungsplanapp.Core.VertretungsplanApp;
import de.tomsapps.vertretungsplanapp.R;
import de.tomsapps.vertretungsplanapp.Core.VertretungsplanUnit;

public class VertretungsplanListAdapter extends BaseExpandableListAdapter
// Klasse, welcher für die initialisierung des ExpandableListView's verantwortlich ist.
{
    // Speichert Tag.
    int vertretungsplanID;
    // Speicherung der Gruppen.
    ArrayList<ListGroup> groups = new ArrayList<ListGroup>();
    int groupSize;
    Context context;

    public VertretungsplanListAdapter(int vertretungsplanID, Context context)
    // Konstruktor.
    {
        // Wertzuweisungen.
        this.vertretungsplanID = vertretungsplanID;
        this.context = context;

        // Gruppen initialisieren
        initialize();
    }

    public void initialize()
    // Gruppen (re-)initialisieren.
    {
        // Referenz auf den Vertretungsplan erstellen.
        Vertretungsplan vp = VertretungsplanApp.singleton.getVertretungsplan(vertretungsplanID);
        // In jeder Zeile nach Gruppen suchen, welche nicht schon vorhanden sind.
        for (int i = 0; i < vp.getUnitsSize(); i++)
        {
            VertretungsplanUnit unit = vp.getUnit(i);
            ArrayList<String> unitGroupNames = new ArrayList<String>();
            // generate UnitGroupNames (durch ',' getrennte Klassen und weglassen der Kurse nach '/'
            int index = -1, pIndex = 0;
            int endIndex = unit.klasse.indexOf("/");
            if (endIndex == -1) endIndex = unit.klasse.length();
            do
            {
                pIndex = index + 1; index = unit.klasse.indexOf(",", pIndex + 1);

                unitGroupNames.add(unit.klasse.substring(pIndex, (index != -1)? index : endIndex));
            }
            while (index != -1);

            for (String unitName : unitGroupNames)
            {
                boolean groupAlreadyExists = false;
                for (ListGroup group : groups)
                {
                    // Wenn die Gruppe der Unit schon vorhanden ist, die Unit hinzufügen, . . .
                    if (unitName.contentEquals(group.name))
                    {
                        group.addUnit(unit);
                        groupAlreadyExists = true;
                        break;
                    }
                }
                if (!groupAlreadyExists)
                {
                    // . . . wenn nicht neue Gruppe erstellen und Unit hinzufügen.
                    ListGroup group = new ListGroup(unitName);
                    group.addUnit(unit);
                    groups.add(group);
                }
            }
        }

        // Anzahl der Gruppen wird gespeichert, damit nicht immer die Funktion aufgerufen werden muss (-> Geschwindigkeit)
        groupSize = groups.size();
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
    public Object getGroup(int groupPosition)
    {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        return null;
    }

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

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.exp_list_view_group, null, false);
        }
        TextView textView1 = (TextView) convertView.findViewById(R.id.exp_list_view_group_text1);
        textView1.setText(groups.get(groupPosition).name);
        TextView textView2 = (TextView) convertView.findViewById(R.id.exp_list_view_group_text2);
        int anzahlAenderungen = groups.get(groupPosition).unitsSize;
        textView2.setText(anzahlAenderungen + ((anzahlAenderungen == 1)? " Änderung" : " Änderungen"));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.exp_list_view_item, null, false);
        }
        ListGroup group = groups.get(groupPosition);
        TextView textView1 = (TextView) convertView.findViewById(R.id.exp_list_view_item_text1);
        textView1.setText(group.units.get(childPosition).klasse);
        TextView textView2 = (TextView) convertView.findViewById(R.id.exp_list_view_item_text2);
        textView2.setText(group.units.get(childPosition).stunde);
        TextView textView3 = (TextView) convertView.findViewById(R.id.exp_list_view_item_text3);
        textView3.setText(group.units.get(childPosition).fach);
        TextView textView4 = (TextView) convertView.findViewById(R.id.exp_list_view_item_text4);
        textView4.setText(group.units.get(childPosition).lehrer);
        TextView textView5 = (TextView) convertView.findViewById(R.id.exp_list_view_item_text5);
        textView5.setText(group.units.get(childPosition).raum);
        TextView textView6 = (TextView) convertView.findViewById(R.id.exp_list_view_item_text6);
        textView6.setText(group.units.get(childPosition).info);
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
