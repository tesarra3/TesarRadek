package com.radektesar.weather.android.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.radektesar.weather.android.R;


/**
 * Created by Radek on 12. 3. 2015.
 */
    public class AbouttDialog extends DialogFragment {





    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {



        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_about, null);


        final TextView text = (TextView)dialogView.findViewById(R.id.dialogTextTextView);


        text.setText(R.string.dialog_text);

        //Set up dialog with possitive button
        builder.setView(dialogView)
                // Add action buttons
                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...

                        AbouttDialog.this.getDialog().cancel();
       }//set title of dialog
                }).setTitle(R.string.dialog_title);


        return builder.create();
    }
}