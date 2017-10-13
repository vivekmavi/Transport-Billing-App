package com.example.paras.transportmanagement;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

/**
 * Created by Paras on 07/10/2017.
 */

public class GeneratedBillDialog extends DialogFragment
    {
        double totalKm;
        double ratePerKm;
        double totalAmount;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            totalKm = getArguments().getDouble("totalKM",0.0);
            ratePerKm = getArguments().getDouble("ratePerKm",0.0);
            totalAmount = getArguments().getDouble("totalAmount",0.0);
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

            dialogBuilder.setTitle("Bill :");
            dialogBuilder.setMessage("Total Distance = "+String.valueOf(totalKm)
                                +"\nRate per K.M = "+String.valueOf(ratePerKm)
                                +"\nTotal Amount = "+String.valueOf(totalAmount));
            dialogBuilder.setCancelable(false);
            dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    dismiss();
                }
            });
            Dialog dialog = dialogBuilder.create();
            return dialog;
        }
}
