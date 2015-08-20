package de.tomsapps.vertretungsplanapp.gui;

public interface IColorSelectingDialogListener
{
    void colorDialogClosed(ColorSelectingDialog sender, int r, int g, int b, boolean succesfully);
}
