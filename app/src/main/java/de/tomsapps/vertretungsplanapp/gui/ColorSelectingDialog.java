package de.tomsapps.vertretungsplanapp.gui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import de.tomsapps.vertretungsplanapp.R;

public class ColorSelectingDialog
{
    int r, g, b;
    boolean ok;

    IColorSelectingDialogListener listener;

    public ColorSelectingDialog(int startColor, IColorSelectingDialogListener listener)
    {
        this.listener = listener;
        this.r = Color.red(startColor);
        this.g = Color.green(startColor);
        this.b = Color.blue(startColor);
    }

    public void show(String title, FragmentActivity activity)
    {
        final ColorSelectingDialog the_dialog = this;
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(activity);

        builder.setTitle(title);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ok = true;
                listener.colorDialogClosed(the_dialog, r, g, b, ok);
            }
        });
        builder.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ok = false;
                listener.colorDialogClosed(the_dialog, r, g, b, ok);
            }
        });
        LayoutInflater inflater = activity.getLayoutInflater();
        View rootView = inflater.inflate(R.layout.dialog_color_selecting, null);

        final SeekBar rotBar = (SeekBar)rootView.findViewById(R.id.dialog_color_rot_seek);
        rotBar.setProgress(r);
        final SeekBar gruenBar = (SeekBar)rootView.findViewById(R.id.dialog_color_gruen_seek);
        gruenBar.setProgress(g);
        final SeekBar blauBar = (SeekBar)rootView.findViewById(R.id.dialog_color_blau_seek);
        blauBar.setProgress(b);

        final TextView rotValueText = (TextView) rootView.findViewById(R.id.dialog_color_rot_value);
        rotValueText.setText(String.valueOf(Math.round(r / 2.550)) + "%");
        final TextView gruenValueText = (TextView) rootView.findViewById(R.id.dialog_color_gruen_value);
        gruenValueText.setText(String.valueOf(Math.round(g / 2.550)) + "%");
        final TextView blauValueText = (TextView) rootView.findViewById(R.id.dialog_color_blau_value);
        blauValueText.setText(String.valueOf(Math.round(b / 2.550)) + "%");

        final View vorschauView = rootView.findViewById(R.id.dialog_color_vorschau);
        vorschauView.setBackgroundColor(Color.argb(255, r, g, b));

        SeekBar.OnSeekBarChangeListener changeListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                r = rotBar.getProgress();
                g = gruenBar.getProgress();
                b = blauBar.getProgress();

                rotValueText.setText(String.valueOf(Math.round(r / 2.550)) + "%");
                gruenValueText.setText(String.valueOf(Math.round(g / 2.550)) + "%");
                blauValueText.setText(String.valueOf(Math.round(b / 2.550)) + "%");

                vorschauView.setBackgroundColor(Color.argb(255, r, g, b));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // who cares?
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // nobody does!
            }
        };

        rotBar.setOnSeekBarChangeListener(changeListener);
        gruenBar.setOnSeekBarChangeListener(changeListener);
        blauBar.setOnSeekBarChangeListener(changeListener);

        builder.setView(rootView);
        builder.show();
    }
}
