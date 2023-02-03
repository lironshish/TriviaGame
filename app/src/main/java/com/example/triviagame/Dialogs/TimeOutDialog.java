package com.example.triviagame.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.triviagame.R;
import com.google.android.material.button.MaterialButton;

public class TimeOutDialog {


    private TextView time_out_TXT;
    private ImageView time_out_IMG;
    private MaterialButton time_out_BTN;

    public void show(Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.time_out_dialog);

        findViews(dialog);

        time_out_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        dialog.show();
    }

    public void findViews(Dialog dialog) {
        time_out_TXT = dialog.findViewById(R.id.time_out_TXT);
        time_out_IMG = dialog.findViewById(R.id.time_out_IMG);
        time_out_BTN = dialog.findViewById(R.id.time_out_BTN);
    }
}
