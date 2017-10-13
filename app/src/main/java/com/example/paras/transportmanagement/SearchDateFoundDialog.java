package com.example.paras.transportmanagement;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

/**
 * Created by Paras on 03/10/2017.
 */

public class SearchDateFoundDialog extends DialogFragment
{
    static Context context2 = null;
    Communicator2 communicator2;

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        context2=getActivity();
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        setCommunicator((Communicator2) context);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

        dialogBuilder.setTitle("Entry found");
        dialogBuilder.setMessage("Do you want to delete the enrty ?");
        dialogBuilder.setCancelable(false);
        dialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {

                Toast.makeText(context2, "you canceled the operation", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
        dialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {

                communicator2.respond();
                dismiss();
            }
        });
        Dialog dialog = dialogBuilder.create();
        return dialog;

    }

    public void setCommunicator(Communicator2 communicator)
    {
        communicator2 = communicator;
    }

    interface Communicator2
    {
        // to avoid direct communication b/w activity and fragmen
        public void respond();
    }
}
