package de.tomsapps.vertretungsplanapp.core;


import android.content.res.AssetManager;
import android.graphics.Typeface;

public class Resources
{
    public static Typeface roboto_light;
    public static Typeface roboto_thin;
    public static Typeface roboto_thin_italic;
    public static Typeface roboto_light_italic;

    public static void init(AssetManager assets)
    {
        Resources.roboto_light = Typeface.createFromAsset(assets, "Roboto-Light.ttf");
        Resources.roboto_thin = Typeface.createFromAsset(assets, "Roboto-Thin.ttf");
        Resources.roboto_thin_italic = Typeface.createFromAsset(assets, "Roboto-ThinItalic.ttf");
        Resources.roboto_light_italic = Typeface.createFromAsset(assets, "Roboto-LightItalic.ttf");
    }
}
