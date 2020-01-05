package com.example.shhilent;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class messageDialog {
    public void showDialog(Activity activity, String title, String beschrijving, String datum, String tijd){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.melding_detail);

        TextView titleText = (TextView) dialog.findViewById(R.id.titleMelding);
        TextView beschrijvingText = (TextView) dialog.findViewById(R.id.beschrijvingMelding);
        TextView datumText = (TextView) dialog.findViewById(R.id.datum);
        TextView tijdText = (TextView) dialog.findViewById(R.id.tijd);
        Button oke = dialog.findViewById(R.id.button);

        titleText.setText(title);
        beschrijvingText.setText(beschrijving);
        datumText.setText(datum);
        tijdText.setText(tijd);

        oke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
