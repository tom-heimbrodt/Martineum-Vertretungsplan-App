package de.tomsapps.vertretungsplanapp.gui;

import android.os.*;
import android.support.v4.app.*;
import android.support.v4.view.*;
import android.view.*;
import de.tomsapps.vertretungsplanapp.core.*;
import de.tomsapps.vertretungsplanapp.taskmanagement.*;

public class MainActivityPageManager extends PagerAdapter
// Verwaltet die Vertretungsplantabs der Aktivität.
{
	// Zwischenspeierung der Fragments, damit diese nicht immer wieder neu erzeugt werden müssen
	VertretungsplanFragment[] fragments = new VertretungsplanFragment[7];
	// Verweis auf Application-Objekt
	VertretungsplanApp application;
	MainActivity       activity;
	FragmentManager    fragmentManager;

	public MainActivityPageManager(FragmentManager fragmentManager, VertretungsplanApp application, MainActivity
			activity)
	// Konstruktor
	{
		// Verweise speichern
		this.application = application;
		this.activity = activity;
		this.fragmentManager = fragmentManager;
	}

	@Override
	public Fragment instantiateItem(ViewGroup container, int position)
	// instanziert Fragment
	{
		Fragment            fragment = getItem(position);
		FragmentTransaction trans    = fragmentManager.beginTransaction();
		trans.add(container.getId(), fragment, "fragment:" + position);
		trans.commit();
		return fragment;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object)
	// zerstört Fragment
	{
		if (0 <= position && position < fragments.length)
		{
			FragmentTransaction trans = fragmentManager.beginTransaction();
			trans.remove(fragments[position]);
			trans.commit();
			fragments[position] = null;
		}
	}

	public VertretungsplanFragment getItem(int i)
	// gibt ein vorhandenes oder neu erstelltes Fragment zurück
	{
		// Fragment zurückgeben
		if (fragments[i] != null)
		{
			return fragments[i];
		}
		else
		{
			fragments[i] = new VertretungsplanFragment();
			Bundle bundle = new Bundle();
			int pos = i - 1;
			if (pos == -1) pos = 4;
			if (pos == 5) pos = 0;
			bundle.putInt("POSITION", pos);
			fragments[i].setArguments(bundle);

			return fragments[i];
		}
	}

	public void updateFragment(int fragmentIndex)
	{
		if (fragmentIndex == 0 && fragments[6] != null)
			fragments[6].update();
		else if (fragmentIndex == 4 && fragments[0] != null)
			fragments[0].update();
		if (fragments[fragmentIndex + 1] != null)
			fragments[fragmentIndex + 1].update();
	}

	@Override
	public int getCount()
	// gibt die Anzahl der Fragments zurück
	{ return 7; }

	@Override
	public boolean isViewFromObject(View view, Object fragment)
	{
		return ((Fragment) fragment).getView() == view;
	}
}